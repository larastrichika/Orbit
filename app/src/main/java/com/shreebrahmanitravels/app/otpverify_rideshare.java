package com.shreebrahmanitravels.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class otpverify_rideshare extends AppCompatActivity {
    Button next_btn;
    TextView OTP_entered_byuser;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify_rideshare);

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

                Toast.makeText(otpverify_rideshare.this, "Verifikasi Rideshare Berhasil!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}