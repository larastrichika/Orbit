package com.shreebrahmanitravels.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shreebrahmanitravels.app.data.AppDatabase;
import com.shreebrahmanitravels.app.data.BookingEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class verifyotp_guestbooking extends AppCompatActivity {
    Button next_btn;
    TextView OTP_entered_byuser;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifyotp_guestbooking);

        progressBar = findViewById(R.id.pgbar);
        if (progressBar != null) progressBar.setVisibility(View.INVISIBLE);
        next_btn = findViewById(R.id.nxt_btn);
        OTP_entered_byuser = findViewById(R.id.number_otp_txt);

        // Pre-fill demo OTP code
        OTP_entered_byuser.setText("123456");

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = OTP_entered_byuser.getText().toString().trim();
                if (code.isEmpty()) {
                    OTP_entered_byuser.setError("Masukkan kode OTP");
                    return;
                }

                if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                Date tomorrow = calendar.getTime();
                final String tomorrowDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(tomorrow);
                final String busRoute = getIntent().getStringExtra("busroute") != null ? getIntent().getStringExtra("busroute") : "SB1";
                final String guestUser = getIntent().getStringExtra("username") != null ? getIntent().getStringExtra("username") : "Guest User";

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase db = AppDatabase.getInstance(verifyotp_guestbooking.this);
                        BookingEntity newBooking = new BookingEntity(guestUser, tomorrowDate, busRoute, "L11", true);
                        db.bookingDao().insertBooking(newBooking);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (progressBar != null) progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(verifyotp_guestbooking.this, "Verifikasi Berhasil! Kursi telah dipesan.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(verifyotp_guestbooking.this, bookedsuccesfully.class);
                                intent.putExtra("id", "L11");
                                intent.putExtra("bus route", busRoute);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                }).start();
            }
        });
    }
}