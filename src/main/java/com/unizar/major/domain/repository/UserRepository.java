package com.unizar.major.domain.repository;

import com.unizar.major.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT * FROM personeina WHERE active", nativeQuery = true)
    List<User> findAll();

    @Query(value = "SELECT * FROM personeina WHERE iduser = :id AND active", nativeQuery = true)
    Optional<User> findById(@Param("id") Long id);

    @Query(value = "SELECT * FROM personeina WHERE nombreusuario = :nameUser AND active", nativeQuery = true)
    Optional<User> findByUserName(@Param("nameUser") String nameUser);

    @Query(value = "SELECT * FROM personeina WHERE email = :email AND active", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

}
