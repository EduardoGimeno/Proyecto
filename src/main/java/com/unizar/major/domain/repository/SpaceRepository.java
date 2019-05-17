package com.unizar.major.domain.repository;

import com.unizar.major.domain.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpaceRepository extends JpaRepository<Space, String> {
    List<Space> findAll();
    Optional<Space> findById(String id);
    Optional<Space> findByGid(int id);

    @Query(value = "SELECT * FROM espacios e WHERE ST_contains(e.geom, ST_MakePoint(:X, :Y)) AND e.planta = :floor", nativeQuery = true)
    Optional<Space> findByCoords(@Param("floor") int floor, @Param("X") double X, @Param("Y") double Y);

    @Query(value = "SELECT * FROM espacios e WHERE e.chairs >= :chairs", nativeQuery = true)
    List<Space> findByChairs(@Param("chairs") int chairs);

    @Query(value = "SELECT * FROM espacios e WHERE e.area >= :area", nativeQuery = true)
    List<Space> findByArea(@Param("area") double area);

    @Query(value = "SELECT * FROM espacios e WHERE e.area >= :area AND e.chairs >= :chairs", nativeQuery = true)
    List<Space> findByChairsAndArea(@Param("chairs") int chairs, @Param("area") double area);



}