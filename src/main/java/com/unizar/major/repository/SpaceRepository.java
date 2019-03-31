package com.unizar.major.repository;

import com.unizar.major.domain.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceRepository extends JpaRepository<Space, String> {
    List<Space> findAll();
    Space findById(Long id);

}