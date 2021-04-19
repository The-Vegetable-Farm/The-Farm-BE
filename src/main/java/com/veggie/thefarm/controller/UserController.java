package com.veggie.thefarm.controller;

import com.veggie.thefarm.constants.ConstValue;
import com.veggie.thefarm.service.UserService;
import com.veggie.thefarm.utils.JSONUtils;
import com.veggie.thefarm.utils.LoggerUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Bohyun on 2021.04.16
 */
@RestController
@RequestMapping(value = "/user")
public class UserController extends LoggerUtils {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 아이디 중복 체크
    @GetMapping(value = "/check/account/{account}")
    public ResponseEntity<?> checkDuplicateAccount(@PathVariable String account) {
        logger.info("checkDuplicateAccount request : account = " + account);
        boolean result = false;

        try {
            result = userService.isDuplicateAccount(account);
            logger.info("checkDuplicateAccount response : account = " + account + ", isDuplicate = " + result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("checkDuplicateAccount response : account = " + account + ", isDuplicate = " + result + ", error = " + e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 이메일 중복 체크
    @GetMapping(value = "/check/email/{email}")
    public ResponseEntity<?> checkDuplicateEmail(@PathVariable String email) {
        logger.info("checkDuplicateEmail request : email = " + email);
        boolean result = false;

        try {
            result = userService.isDuplicateEmail(email);
            logger.info("checkDuplicateEmail response : email = " + email + ", isDuplicate = " + result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("checkDuplicateEmail response : email = " + email + ", isDuplicate = " + result + ", error = " + e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 닉네임 중복 체크
    @GetMapping(value = "/check/nickname/{nickname}")
    public ResponseEntity<?> checkDuplicateNickname(@PathVariable String nickname) {
        logger.info("checkDuplicateNickname request : nickname = " + nickname);
        boolean result = false;

        try {
            result = userService.isDuplicateNickname(nickname);
            logger.info("checkDuplicateNickname response : nickname = " + nickname + ", isDuplicate = " + result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("checkDuplicateNickname response : nickname = " + nickname + ", isDuplicate = " + result + ", error = " + e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 이메일 인증번호 전송
    @GetMapping(value = "/mail/certnum/{email}")
    public ResponseEntity<?> sendCertNumEmail(@PathVariable String email) {
        logger.info("sendCertNumEmail request : email = " + email);
        boolean result = false;

        try {
            result = userService.sendCertNum(email);
            logger.info("sendCertNumEmail response : email = " + email + ", result = " + result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("sendCertNumEmail response : email = " + email + ", result = " + result + ", error = " + e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 이메일 인증번호 확인
    @GetMapping(value = "/check/certnum/{email}/{certNum}")
    public ResponseEntity<?> checkCertNum(@PathVariable String email, @PathVariable String certNum) {
        logger.info("checkCertNum request : email = " + email + ", certNum = " + certNum);
        boolean result = false;

        try {
            result = userService.checkCertNum(email, certNum);
            logger.info("checkCertNum response : email = " + email + ", certNum = " + certNum + ", result = " + result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("checkCertNum response : email = " + email + ", certNum = " + certNum + ", result = " + result + ", error = " + e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 회원가입
    @PostMapping(value = "/signup")
    public ResponseEntity<?> signUp(@RequestBody String payload) {
        logger.info("signUp request : " + payload);

        JSONObject requestJson = JSONUtils.getJsonObject(payload);

        try {
            JSONObject responseJson = userService.signUp(requestJson);
            logger.info("signUp response : " + responseJson.toJSONString());
            return new ResponseEntity<>(responseJson, HttpStatus.OK);
        } catch (Exception e) {
            JSONObject responseJson = new JSONObject();
            responseJson.put("result", ConstValue.RESULT_N);
            responseJson.put("message", "내부작업 오류");
            logger.info("signUp response : " + responseJson.toJSONString() + ", error = " + e.getMessage());
            return new ResponseEntity<>(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 로그인
    @PostMapping(value = "/signin")
    public ResponseEntity<?> signIn(@RequestBody String payload) {
        logger.info("signIn request : " + payload);

        JSONObject requestJson = JSONUtils.getJsonObject(payload);
        String account = JSONUtils.getJsonValue(requestJson, "account", "");
        String password = JSONUtils.getJsonValue(requestJson, "password", "");

        try {
            boolean result = userService.checkSignIn(account, password);
            logger.info("signIn response : " + result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("signIn response : " + false + ", error = " + e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 최근 접속 시간 저장
    @PatchMapping(value = "/last/{account}")
    public void updateLastLoginTime(@PathVariable String account) {
        logger.info("updateLastLoginTime request : account = " + account);

        try {
            userService.updateLastLoginTime(account);
        } catch (Exception e) {
            logger.info("updateLastLoginTime error : " + e.getMessage());
        }
    }

    // 회원정보 조회
    @GetMapping(value = "/{account}")
    public ResponseEntity<?> getUserInfo(@PathVariable String account) {
        logger.info("getUserInfo request : account = " + account);

        try {
            JSONObject responseJson = userService.getUserInfo(account);
            logger.info("getUserInfo response : " + responseJson.toJSONString());
            return new ResponseEntity<>(responseJson, HttpStatus.OK);
        } catch (Exception e) {
            JSONObject responseJson = new JSONObject();
            responseJson.put("result", ConstValue.RESULT_N);
            responseJson.put("message", "내부작업 오류");
            logger.info("getUserInfo response : account = " + account + ", " + responseJson.toJSONString() + ", error = " + e.getMessage());
            return new ResponseEntity<>(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 계정 휴면처리
    @PatchMapping(value = "/dormant/{account}")
    public ResponseEntity<?> setDormant(@PathVariable String account) {
        logger.info("setDormant request : account = " + account);
        boolean result = false;

        try {
            result = userService.setDormantUser(account);
            logger.info("setDormant response : account = " + account + ", result = " + result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("setDormant response : account = " + account + ", result = " + result + ", error = " + e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 회원정보 수정
    @PatchMapping
    public ResponseEntity<?> updateUserInfo(@RequestBody String payload) {
        logger.info("updateUserInfo request : " + payload);

        JSONObject requestJson = JSONUtils.getJsonObject(payload);

        try {
            JSONObject responseJson = userService.updateUserInfo(requestJson);
            logger.info("updateUserInfo response : " + responseJson.toJSONString());
            return new ResponseEntity<>(responseJson, HttpStatus.OK);
        } catch (Exception e) {
            JSONObject responseJson = new JSONObject();
            responseJson.put("result", ConstValue.RESULT_N);
            responseJson.put("message", "내부작업 오류");
            logger.info("updateUserInfo response : " + responseJson.toJSONString() + ", error = " + e.getMessage());
            return new ResponseEntity<>(responseJson, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 회원탈퇴
    @DeleteMapping(value = "/{account}")
    public ResponseEntity<?> withdrawal(@PathVariable String account) {
        logger.info("withdrawal request : account = " + account);
        boolean result = false;

        try {
            result = userService.withdrawal(account);
            logger.info("withdrawal response : account = " + account + ", result = " + result);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.info("withdrawal response : account = " + account + ", result = " + result + ", error = " + e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
