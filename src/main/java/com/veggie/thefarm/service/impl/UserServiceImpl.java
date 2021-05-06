package com.veggie.thefarm.service.impl;

import com.veggie.thefarm.constants.ConstValue;
import com.veggie.thefarm.model.domain.Role;
import com.veggie.thefarm.model.domain.User;
import com.veggie.thefarm.model.repository.UserRepository;
import com.veggie.thefarm.service.SendMailService;
import com.veggie.thefarm.service.UserService;
import com.veggie.thefarm.utils.JSONUtils;
import com.veggie.thefarm.utils.LoggerUtils;
import com.veggie.thefarm.utils.SecurityUtils;
import com.veggie.thefarm.utils.TimeUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Bohyun on 2021.04.16
 */
@Service
public class UserServiceImpl extends LoggerUtils implements UserService {

    private UserRepository userRepository;
    private SendMailService sendMailService;
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, SendMailService sendMailService, RedisTemplate<String, String> redisTemplate) {
        this.userRepository = userRepository;
        this.sendMailService = sendMailService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isDuplicateAccount(String account) {
        return userRepository.existsByAccount(account);
    }

    @Override
    public boolean isDuplicateEmail(String email) {
        return userRepository.existsByEmail(SecurityUtils.encryptAES(email));
    }

    @Override
    public boolean isDuplicateNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Override
    public boolean sendCertNum(String email) {
        String title = "[THE FARM] 이메일 인증번호 안내";
        String certNum = RandomStringUtils.randomNumeric(6);

        redisTemplate.opsForValue().set(email, certNum);
        // 3분 후 삭제
        redisTemplate.expire(email, 3, TimeUnit.MINUTES);

        return sendMailService.sendMail(email, title, "인증번호 : " + certNum);
    }

    @Override
    public boolean checkCertNum(String email, String certNum) {
        String checkCertNum = redisTemplate.opsForValue().get(email);

        if (!StringUtils.isEmpty(checkCertNum)) {
            return checkCertNum.equals(certNum);
        } else {
            logger.info("인증번호 입력 시간 초과 : email = " + email + ", certNum = " + certNum);
            return false;
        }
    }

    @Override
    public JSONObject signUp(JSONObject requestJson) {
        String account = JSONUtils.getJsonValue(requestJson, "account", "");
        String password = JSONUtils.getJsonValue(requestJson, "password", "");
        String nickname = JSONUtils.getJsonValue(requestJson, "nickname", "");
        String email = JSONUtils.getJsonValue(requestJson, "email", "");

        if (StringUtils.isEmpty(account)) {
            requestJson.put("result", ConstValue.RESULT_N);
            requestJson.put("message", "account is empty");
            return requestJson;
        }

        if (StringUtils.isEmpty(password) || password.length() < 8 || password.length() > 15) {
            // 비밀번호 8~14자리
            requestJson.put("result", ConstValue.RESULT_N);
            requestJson.put("message", "password error : length = 8~14");
            return requestJson;
        }

        if (StringUtils.isEmpty(nickname)) {
            requestJson.put("result", ConstValue.RESULT_N);
            requestJson.put("message", "nickname is empty");
            return requestJson;
        }

        if (StringUtils.isEmpty(email)) {
            requestJson.put("result", ConstValue.RESULT_N);
            requestJson.put("message", "email is empty");
            return requestJson;
        }

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

        requestJson.put("result", ConstValue.RESULT_Y);
        requestJson.put("role", user.getRole().getRoleName());
        requestJson.remove("password");
        return requestJson;
    }

    private Role role() {
        Role role = new Role();
        role.setRoleName(ConstValue.ROLE_USER);
        return role;
    }

    @Override
    public boolean checkSignIn(String account, String password) {
        User user = userRepository.findByAccount(account);

        if (user != null && SecurityUtils.matchesBCryptPassword(password, user.getPassword())) {
            logger.info("로그인 성공 : " + account);
            user.setLastLoginTime(new Timestamp(new Date().getTime()));
            userRepository.save(user);
            return true;
        }

        return false;
    }

    @Override
    public void updateLastLoginTime(String account) {
        User user = userRepository.findByAccount(account);

        if (user != null) {
            user.setLastLoginTime(new Timestamp(new Date().getTime()));
            userRepository.save(user);
        }
    }

    @Override
    public JSONObject getUserInfo(String account) {
        JSONObject responseJson = new JSONObject();

        User user = userRepository.findByAccount(account);

        if (user != null) {
            responseJson.put("result", ConstValue.RESULT_Y);
            responseJson.put("id", user.getId());
            responseJson.put("account", user.getAccount());
            responseJson.put("nickname", user.getNickname());
            responseJson.put("regTime", TimeUtils.getFormatTimeStr("yyyy-MM-dd HH:mm:ss", user.getRegTime()));
            responseJson.put("role", user.getRole().getRoleName());
            responseJson.put("characterCnt", user.getCharacterCnt());
            return responseJson;
        } else {
            responseJson.put("result", ConstValue.RESULT_N);
            responseJson.put("message", "등록되지 않은 아이디 입니다.");
        }

        return responseJson;
    }

    @Override
    public boolean setDormantUser(String account) {
        User user = userRepository.findByAccount(account);

        if (user != null) {
            user.setDormant(!user.isDormant());
            Date date = new Date();
            user.setModTime(new Timestamp(date.getTime()));
            user.setModDt(date);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public JSONObject updateUserInfo(JSONObject requestJson) {
        String account = JSONUtils.getJsonValue(requestJson, "account", "");
        String password = JSONUtils.getJsonValue(requestJson, "password", "");
        String nickname = JSONUtils.getJsonValue(requestJson, "nickname", "");
        String email = JSONUtils.getJsonValue(requestJson, "email", "");

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

            requestJson.put("result", ConstValue.RESULT_Y);
        } else {
            requestJson.put("result", ConstValue.RESULT_N);
            requestJson.put("message", "등록되지 않은 아이디 입니다.");
        }

        return requestJson;
    }

    @Override
    public boolean withdrawal(String account) {
        if (userRepository.existsByAccount(account)) {
            try {
                Long id = userRepository.findIdByAccount(account);
                userRepository.deleteById(id);
                return true;
            } catch (Exception e) {
                logger.error("withdrawal error : account = " + account + ", error = " + e.getMessage());
            }
        } else {
            logger.error("withdrawal error : account = " + account + ", error = 등록되지 않은 아이디 입니다.");
        }

        return false;
    }
}
