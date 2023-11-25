package com.abdul.SpringSecurityLogin.repository;

import com.abdul.SpringSecurityLogin.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
