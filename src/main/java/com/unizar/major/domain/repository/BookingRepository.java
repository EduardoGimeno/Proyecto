package com.unizar.major.domain.repository;

import com.unizar.major.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    @Query(value = "SELECT * FROM booking WHERE active", nativeQuery = true)
    List<Booking> findAll();

    @Query(value = "SELECT * FROM booking WHERE id_booking = :id AND active", nativeQuery = true)
    Optional<Booking> findById(@Param("id") Long id);

    List<Booking> findByState(String state);


}