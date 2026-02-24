package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long>  {
    @Query(""" 
        select u from User u
        join fetch u.roles
        where u.username = :username
    """)
    Optional<User> findByUsernameWithRoles(@Param("username") String username);
}
