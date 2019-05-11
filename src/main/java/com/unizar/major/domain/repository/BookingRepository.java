package com.unizar.major.domain.repository;

import com.unizar.major.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findAll();
    Optional<Booking> findById(Long id);
    List<Booking> findByState(String state);


}