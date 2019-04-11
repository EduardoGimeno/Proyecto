package com.unizar.major.domain.repository;

import com.unizar.major.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findAll();
    Optional<Booking> findById(Long id);


    @Modifying
    @Query("update Booking b set b.isPeriodic =?1, b.reason=?2, b.period.endDate=?3, b.period.startDate=?4 where b.id=?5")
    void setBookingInfoById(Boolean isPeriodic, String reason, String endDate,String startDate, Long id);

}