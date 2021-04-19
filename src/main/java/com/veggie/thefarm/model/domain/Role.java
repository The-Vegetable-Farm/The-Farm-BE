package com.veggie.thefarm.model.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Bohyun on 2021.04.16
 */
@Entity
@Table(name = "ROLE")
@Data
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ROLE_NAME")
    private String roleName;            // 권한 이름

    @Override
    public String toString() {
        return String.format("ROLE(id = %d, roleName = %s)", id, roleName);
    }

}
