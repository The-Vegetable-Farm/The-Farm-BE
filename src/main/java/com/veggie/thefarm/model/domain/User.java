package com.veggie.thefarm.model.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Bohyun on 2021.04.16
 */
@Entity
@Table(name = "USER")
@Data
@ToString(exclude = "role")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ACCOUNT")
    private String account;                 // 아이디

    @Column(name = "PASSWORD")
    private String password;                // 비밀번호

    @Column(name = "NICKNAME")
    private String nickname;                // 닉네임

    @Column(name = "EMAIL")
    private String email;                   // 이메일

    @Column(name = "CHARACTER_CNT")
    private int characterCnt;               // 보유 캐릭터 수

    @Column(name = "IS_DORMANT")
    private boolean isDormant;              // 휴면계정 여부

    @Column(name = "REG_TIME")
    private Timestamp regTime;              // 최초가입 시간

    @Column(name = "REG_DT")
    private Date regDt;                     // 최초가입일

    @Column(name = "LAST_LOGIN_TIME")
    private Timestamp lastLoginTime;        // 최근 로그인 시간

    @Column(name = "MOD_TIME")
    private Timestamp modTime;              // 수정시간

    @Column(name = "MOD_DT")
    private Date modDt;                     // 수정일

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLE",
            joinColumns        = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID",  referencedColumnName = "ID")}
    )
    private Role role;

}
