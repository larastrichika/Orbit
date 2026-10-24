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
import com.shreebrahmanitravels.app.data.UserEntity;

public class otp_verify extends AppCompatActivity {
    Button next_btn;
    TextView OTP_entered_byuser;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);

        progressBar = findViewById(R.id.pgbar);
        if (progressBar != null) progressBar.setVisibility(View.INVISIBLE);
        next_btn = findViewById(R.id.nxt_btn);
        OTP_entered_byuser = findViewById(R.id.number_otp_txt);

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

                final String name = getIntent().getStringExtra("name");
                final String username = getIntent().getStringExtra("username");
                final String password = getIntent().getStringExtra("password");
                final String phoneNo = getIntent().getStringExtra("phone_no");
                final String bus_no = getIntent().getStringExtra("bus_no");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase db = AppDatabase.getInstance(otp_verify.this);
                        UserEntity user = new UserEntity(
                                username != null ? username : "User",
                                name != null ? name : "Nama",
                                password != null ? password : "password",
                                phoneNo != null ? phoneNo : "08123456789",
                                bus_no != null ? bus_no : "SB1",
                                "Umum",
                                "2026",
                                "Terminal Utama",
                                "0"
                        );
                        db.userDao().insertUser(user);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (progressBar != null) progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(otp_verify.this, "Akun berhasil dibuat!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(otp_verify.this, login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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