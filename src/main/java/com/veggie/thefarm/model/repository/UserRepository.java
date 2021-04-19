package com.veggie.thefarm.model.repository;

import com.veggie.thefarm.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * Created by Bohyun on 2021.04.16
 */
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByAccount(String account);

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    User findByAccount(String account);

    @Query("select u.id from User u where u.account = ?1")
    Long findIdByAccount(String account);

}
