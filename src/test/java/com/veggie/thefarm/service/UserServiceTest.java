package com.veggie.thefarm.service;

import com.veggie.thefarm.constants.ConstValue;
import com.veggie.thefarm.model.domain.Role;
import com.veggie.thefarm.model.domain.User;
import com.veggie.thefarm.model.repository.UserRepository;
import com.veggie.thefarm.service.SendMailService;
import com.veggie.thefarm.utils.SecurityUtils;
import com.veggie.thefarm.utils.TimeUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Bohyun on 2021.04.16
 */
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SendMailService sendMailService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void isDuplicateAccount() {
        String account = "test";
        System.out.println(userRepository.existsByAccount(account));
    }

    @Test
    void isDuplicateEmail() {
        String email = "test@naver.com";
        System.out.println(userRepository.existsByEmail(SecurityUtils.encryptAES(email)));
    }

    @Test
    void isDuplicateNickname() {
        String nickname = "테스트";
        System.out.println(userRepository.existsByNickname(nickname));
    }

    @Test
    void sendCertNum() {
        String receiver = "96bohyun@naver.com";
        String title = "[THE FARM] 이메일 인증번호 안내";
        String certNum = RandomStringUtils.randomNumeric(6);
        System.out.println(sendMailService.sendMail(receiver, title, "인증번호 : " + certNum));
        redisTemplate.opsForValue().set(receiver, certNum);
        // 3분 후 삭제
        redisTemplate.expire(receiver, 3, TimeUnit.MINUTES);
    }

    @Test
    void checkCertNum() {
        String receiver = "96bohyun@naver.com";
        String certNum = "781087";

        String checkCertNum = redisTemplate.opsForValue().get(receiver);

        if (!StringUtils.isEmpty(checkCertNum)) {
            System.out.println(checkCertNum.equals(certNum));
        } else {
            System.out.println("시간초과");
        }
    }

    @Test
    void signUp() {
        String account = "bh";
        String password = "test1234!";
        String nickname = "보보킴";
        String email = "96bohyun@naver.com";

        User user = new User();
        user.setAccount(account);
        user.setPassword(SecurityUtils.encryptBCryptPassword(password));
        user.setNickname(nickname);
        user.setEmail(SecurityUtils.encryptAES(email));
        user.setRole(role());
        Date date = new Date();
        user.setRegDt(date);
        user.setRegTime(new Timestamp(date.getTime()));
        userRepository.save(user);
    }

    private Role role() {
        Role role = new Role();
        role.setRoleName(ConstValue.ROLE_USER);
        return role;
    }

    @Test
    void checkSignIn() {
        String account = "bh";
        String password = "bh1234!";

        User user = userRepository.findByAccount(account);

        if (user != null && SecurityUtils.matchesBCryptPassword(password, user.getPassword())) {
            System.out.println("로그인 성공");
            user.setLastLoginTime(new Timestamp(new Date().getTime()));
            userRepository.save(user);
        } else {
            System.out.println("아이디 또는 비밀번호를 확인해주세요.");
        }
    }

    @Test
    void updateLastLoginTime() {
        String account = "bh";

        User user = userRepository.findByAccount(account);

        if (user != null) {
            user.setLastLoginTime(new Timestamp(new Date().getTime()));
            userRepository.save(user);
        }
    }

    @Test
    void getUserInfo() {
        String account = "bh";

        User user = userRepository.findByAccount(account);

        if (user != null) {
            JSONObject userJson = new JSONObject();
            userJson.put("id", user.getId());
            userJson.put("account", user.getAccount());
            userJson.put("nickname", user.getNickname());
            userJson.put("regTime", TimeUtils.getFormatTimeStr("yyyy-MM-dd HH:mm:ss", user.getRegTime()));
            userJson.put("role", user.getRole().getRoleName());
            userJson.put("characterCnt", user.getCharacterCnt());

            System.out.println(userJson.toJSONString());
        }
    }

    @Test
    void setDormantUser() {
        String account = "bh";

        User user = userRepository.findByAccount(account);

        if (user != null) {
            user.setDormant(!user.isDormant());
            Date date = new Date();
            user.setModTime(new Timestamp(date.getTime()));
            user.setModDt(date);
            userRepository.save(user);
        }
    }

    @Test
    void updateUserInfo() {
        String account = "bh";
        String nickname = "테스트";
        String password = "bh1234!";
        String email = "";

        User user = userRepository.findByAccount(account);

        if (user != null) {
            if (!StringUtils.isEmpty(nickname) && !user.getNickname().equals(nickname)) {
                user.setNickname(nickname);
            }

            if (!StringUtils.isEmpty(password) && !SecurityUtils.matchesBCryptPassword(password, user.getPassword())) {
                user.setPassword(SecurityUtils.encryptBCryptPassword(password));
            }

            if (!StringUtils.isEmpty(email) && !SecurityUtils.decryptAES(user.getEmail()).equals(email)) {
                user.setEmail(SecurityUtils.encryptAES(email));
            }

            Date date = new Date();
            user.setModTime(new Timestamp(date.getTime()));
            user.setModDt(date);

            userRepository.save(user);
        }
    }

    @Test
    void withdrawal() {
        String account = "bh";

        if (userRepository.existsByAccount(account)) {
            try {
                Long id = userRepository.findIdByAccount(account);
                userRepository.deleteById(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}