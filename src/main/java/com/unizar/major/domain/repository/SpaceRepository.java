package com.unizar.major.domain.repository;

import com.unizar.major.domain.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpaceRepository extends JpaRepository<Space, String> {
    List<Space> findAll();
    Optional<Space> findById(String id);
    Optional<Space> findByGid(int id);

}