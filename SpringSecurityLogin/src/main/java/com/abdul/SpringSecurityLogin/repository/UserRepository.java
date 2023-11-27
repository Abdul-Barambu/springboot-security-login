package com.abdul.SpringSecurityLogin.repository;

import com.abdul.SpringSecurityLogin.entities.Role;
import com.abdul.SpringSecurityLogin.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

//    12.       create findByEmail
    Optional<User> findByEmail(String username);

//    create findByRole to use it in your main class and find if the admin is created and if not then create an admin in the database
    User findByRole(Role role);


}
