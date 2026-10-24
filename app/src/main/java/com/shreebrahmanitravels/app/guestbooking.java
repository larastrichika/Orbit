package com.shreebrahmanitravels.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.shreebrahmanitravels.app.data.AppDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class guestbooking extends AppCompatActivity {
    TextInputLayout fullname, username, phonenumber;
    TextView busroute, vacantseat;
    Button reqticket;
    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guestbooking);

        fullname = findViewById(R.id.nameguest);
        username = findViewById(R.id.Usernameguest);
        phonenumber = findViewById(R.id.phonenumberguest);
        reqticket = findViewById(R.id.confirmticketguest);
        busroute = findViewById(R.id.busrouteeditt);
        vacantseat = findViewById(R.id.vacantedit);
        backbutton = findViewById(R.id.backarrrowonboard);

        final String routeVal = getIntent().getStringExtra("busroute") != null ? getIntent().getStringExtra("busroute") : "SB1";
        busroute.setText(": " + routeVal);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        final String tomorrowDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(tomorrow);

        // Fetch vacant seats from Room DB
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(guestbooking.this);
                int booked = db.bookingDao().getBookedSeatCount(tomorrowDate, routeVal);
                final int vacant = Math.max(0, 8 - booked);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vacantseat.setText(": " + vacant + " kursi tersedia");
                    }
                });
            }
        }).start();

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        reqticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validphonenumber() | !validateName())
                    return;

                Intent intent = new Intent(guestbooking.this, verifyotp_guestbooking.class);
                intent.putExtra("fromguest", "true");
                intent.putExtra("fullname", fullname.getEditText().getText().toString());
                intent.putExtra("username", username.getEditText().getText().toString());
                intent.putExtra("phone_no", phonenumber.getEditText().getText().toString());
                intent.putExtra("busroute", routeVal);
                startActivity(intent);
            }
        });
    }

    private Boolean validateName() {
        String val = fullname.getEditText().getText().toString();
        if (val.isEmpty()) {
            fullname.setError("Nama tidak boleh kosong");
            return false;
        } else {
            fullname.setError(null);
            fullname.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = username.getEditText().getText().toString();
        if (val.isEmpty()) {
            username.setError("Username tidak boleh kosong");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validphonenumber() {
        String val = phonenumber.getEditText().getText().toString();
        if (val.isEmpty() || val.length() < 8) {
            phonenumber.setError("Nomor telepon tidak valid");
            return false;
        } else {
            phonenumber.setError(null);
            phonenumber.setErrorEnabled(false);
            return true;
        }
    }
}