package com.unizar.major.repository;

import com.unizar.major.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByFirstName (String FirstName);
    List<User> findAll();
    List<User> findByRol (String rol);
    User findById(Long id);

    @Transactional
    @Modifying
    @Query("update User u set u.firstName =?1, u.lastName=?2, u.rol=?3, u.nombreUsuario=?4 where u.id=?5")
    void setUserInfoById(String firstName,String lastName, String rol, String nombreUsuario, Long id);
}
