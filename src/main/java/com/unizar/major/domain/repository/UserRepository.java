package com.unizar.major.domain.repository;

import com.unizar.major.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    List<User> findAll();
    Optional<User> findById(Long id);


    @Modifying
    @Query("update User u set u.firstName =?1, u.lastName=?2, u.rol=?3, u.nombreUsuario=?4 where u.id=?5")
    void setUserInfoById(String firstName,String lastName, String rol, String nombreUsuario, Long id);
}
