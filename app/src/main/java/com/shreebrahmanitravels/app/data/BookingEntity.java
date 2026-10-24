package com.shreebrahmanitravels.app.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookings")
public class BookingEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String username;

    @NonNull
    private String date;

    private String busRoute;
    private String seatId;
    private boolean isBooked;

    public BookingEntity(@NonNull String username, @NonNull String date, String busRoute, String seatId, boolean isBooked) {
        this.username = username;
        this.date = date;
        this.busRoute = busRoute;
        this.seatId = seatId;
        this.isBooked = isBooked;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getUsername() { return username; }
    public void setUsername(@NonNull String username) { this.username = username; }

    @NonNull
    public String getDate() { return date; }
    public void setDate(@NonNull String date) { this.date = date; }

    public String getBusRoute() { return busRoute; }
    public void setBusRoute(String busRoute) { this.busRoute = busRoute; }

    public String getSeatId() { return seatId; }
    public void setSeatId(String seatId) { this.seatId = seatId; }

    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean booked) { isBooked = booked; }
}
