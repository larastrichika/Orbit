package com.shreebrahmanitravels.app.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBooking(BookingEntity booking);

    @Query("SELECT * FROM bookings WHERE username = :username AND date = :date LIMIT 1")
    BookingEntity getBookingByUserAndDate(String username, String date);

    @Query("SELECT * FROM bookings WHERE date = :date AND busRoute = :busRoute AND seatId = :seatId LIMIT 1")
    BookingEntity getSeatBooking(String date, String busRoute, String seatId);

    @Query("SELECT * FROM bookings WHERE date = :date AND busRoute = :busRoute AND isBooked = 1")
    List<BookingEntity> getBookedSeats(String date, String busRoute);

    @Query("SELECT * FROM bookings WHERE username = :username ORDER BY id DESC LIMIT 1")
    BookingEntity getLastBooking(String username);

    @Query("SELECT COUNT(*) FROM bookings WHERE date = :date AND busRoute = :busRoute AND isBooked = 1")
    int getBookedSeatCount(String date, String busRoute);

    @Query("UPDATE bookings SET isBooked = 1 WHERE date = :date AND busRoute = :busRoute AND seatId = :seatId")
    void markSeatBooked(String date, String busRoute, String seatId);
}
