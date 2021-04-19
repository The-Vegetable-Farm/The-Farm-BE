package com.veggie.thefarm.model.repository;

import com.veggie.thefarm.model.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Bohyun on 2021.04.16
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
}
