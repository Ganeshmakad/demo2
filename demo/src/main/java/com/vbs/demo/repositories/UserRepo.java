package com.vbs.demo.repositories;

import com.vbs.demo.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//majdoor jo actually me database me data save and lane ka kaam karta hai *crud*
//jpa repositories help for mysql queries
//it demand two thing table name,primary key in <>s bas itna kiya to hogaya
@Repository // @Repository: Spring ko batata hai ki ye file Database operations handle karegi.
public interface UserRepo extends JpaRepository<User,Integer> {

    User findByUsername(String username);

    User findByEmail(String value);

    List<User> findAllByRole(String customer, Sort sort);

    List<User> findByUsernameContainingIgnoreCaseAndRole(String keyword, String customer);
}
