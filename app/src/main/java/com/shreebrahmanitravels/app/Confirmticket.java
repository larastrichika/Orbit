package com.shreebrahmanitravels.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shreebrahmanitravels.app.data.AppDatabase;
import com.shreebrahmanitravels.app.data.BookingEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Confirmticket extends AppCompatActivity {
    Button confirmticket;
    ImageView backbtn;
    ImageView L11, infoicon;
    TextView selectedSeatLabel;
    String busroute;
    int recentviewID = 0;
    String selectedseatID = null;
    AlertDialog dialog = null;
    String tomorrowDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmticket);

        L11 = findViewById(R.id.L11);
        infoicon = findViewById(R.id.infoicon);
        backbtn = findViewById(R.id.backarrow);
        confirmticket = findViewById(R.id.confirmticket);
        selectedSeatLabel = findViewById(R.id.selectedSeatLabel);

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflaterdialoge = this.getLayoutInflater();
        alert.setView(inflaterdialoge.inflate(R.layout.seathintdialoge, null));
        dialog = alert.create();
        dialog.setCanceledOnTouchOutside(true);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
        }

        busroute = getIntent().getStringExtra("busnumber");
        if (busroute == null || busroute.isEmpty()) {
            busroute = "SB1";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        tomorrowDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(tomorrow);

        // Load booked seats from Room DB
        loadBookedSeatsFromRoom();

        infoicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialog != null) {
                    dialog.show();
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        confirmticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedseatID != null) {
                    confirmBookingInRoom(selectedseatID, busroute);
                } else {
                    Toast.makeText(Confirmticket.this, "Silakan pilih salah satu kursi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadBookedSeatsFromRoom() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(Confirmticket.this);
                final List<BookingEntity> bookedSeats = db.bookingDao().getBookedSeats(tomorrowDate, busroute);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bookedSeats != null) {
                            for (BookingEntity booking : bookedSeats) {
                                String seatId = booking.getSeatId();
                                int resId = getResources().getIdentifier(seatId, "id", getPackageName());
                                if (resId != 0) {
                                    ImageView seatView = findViewById(resId);
                                    if (seatView != null) {
                                        seatView.setTag("reserved");
                                        seatView.setImageResource(R.drawable.ic_seat_reserved);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }).start();
    }

    private void confirmBookingInRoom(final String seatId, final String route) {
        final String username = new sessionmanager(Confirmticket.this).getuserdetail();
        final String effectiveUser = (username != null && !username.isEmpty()) ? username : "Guest user";

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(Confirmticket.this);
                BookingEntity existingBooking = db.bookingDao().getSeatBooking(tomorrowDate, route, seatId);

                if (existingBooking != null && existingBooking.isBooked()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Confirmticket.this, "Kursi " + seatId + " sudah dipesan orang lain", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    BookingEntity newBooking = new BookingEntity(effectiveUser, tomorrowDate, route, seatId, true);
                    db.bookingDao().insertBooking(newBooking);

                    if (!effectiveUser.equals("Guest user")) {
                        db.userDao().updateLastBookedDate(effectiveUser, tomorrowDate);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Confirmticket.this, "Tiket berhasil dipesan!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Confirmticket.this, bookedsuccesfully.class);
                            intent.putExtra("id", seatId);
                            intent.putExtra("bus route", route);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        }).start();
    }

    public void Onselectedseat(View view) {
        if ("reserved".equals(view.getTag())) {
            Toast.makeText(this, "Kursi ini sudah dipesan", Toast.LENGTH_SHORT).show();
            return;
        }

        ImageView img = (ImageView) view;
        ImageView recentelectedImage = null;

        if (recentviewID != 0) {
            recentelectedImage = findViewById(recentviewID);
        }

        if ("true".equals(view.getTag())) {
            img.setImageResource(R.drawable.ic_seat_available);
            view.setTag("false");
            selectedseatID = null;
            if (selectedSeatLabel != null) {
                selectedSeatLabel.setText("Pilih 1 kursi di atas");
            }
        } else {
            img.setImageResource(R.drawable.ic_seat_selected);
            view.setTag("true");
            selectedseatID = getResources().getResourceEntryName(view.getId()).trim();

            if (selectedSeatLabel != null) {
                selectedSeatLabel.setText("Kursi dipilih: " + getSeatDisplayName(selectedseatID));
            }

            if (recentviewID != 0 && recentviewID != view.getId() && recentelectedImage != null) {
                recentelectedImage.setImageResource(R.drawable.ic_seat_available);
                recentelectedImage.setTag("false");
            }
            recentviewID = view.getId();
        }
    }

    private String getSeatDisplayName(String id) {
        if (id == null) return "";
        switch (id) {
            case "L11": return "Kursi 1 (Depan)";
            case "L21": return "Kursi 2 (Tengah Kiri)";
            case "R21": return "Kursi 3 (Tengah Kanan)";
            case "L31": return "Kursi 4 (Kiri)";
            case "R31": return "Kursi 5 (Kanan)";
            case "L41": return "Kursi 6 (Belakang Kiri)";
            case "L42": return "Kursi 7 (Belakang Tengah)";
            case "R41": return "Kursi 8 (Belakang Kanan)";
            default: return id;
        }
    }
}