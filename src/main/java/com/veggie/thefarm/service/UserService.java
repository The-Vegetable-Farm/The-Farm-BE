package com.veggie.thefarm.service;

import org.json.simple.JSONObject;

/**
 * Created by Bohyun on 2021.04.16
 */
public interface UserService {

    // 아이디 중복 체크
    boolean isDuplicateAccount(String account);

    // 이메일 중복 체크
    boolean isDuplicateEmail(String email);

    // 닉네임 중복 체크
    boolean isDuplicateNickname(String nickname);

    // 이메일 인증번호 전송
    boolean sendCertNum(String email);

    // 이메일 인증번호 확인
    boolean checkCertNum(String email, String certNum);

    // 회원가입
    JSONObject signUp(JSONObject requestJson);

    // 로그인
    boolean checkSignIn(String account, String password);

    // 최근 접속 시간 저장
    void updateLastLoginTime(String account);

    // 회원정보 조회
    JSONObject getUserInfo(String account);

    // 계정 휴면처리
    boolean setDormantUser(String account);

    // 회원정보 수정
    JSONObject updateUserInfo(JSONObject requestJson);

    // 회원탈퇴
    boolean withdrawal(String account);

}
