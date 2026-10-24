package com.shreebrahmanitravels.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class bookedsuccesfully extends AppCompatActivity {
    Button gohome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookedsuccesfully);

        gohome = findViewById(R.id.gohome_succesbook);
        TextView sucesstext = findViewById(R.id.seatsucessmsg);

        String seatId = getIntent().getStringExtra("id");
        String busRoute = getIntent().getStringExtra("bus route");

        sessionmanager session = new sessionmanager(this);
        String username = session.getuserdetail();

        if (username != null && username.equals("Guest user")) {
            sucesstext.setText("Permintaan pemesanan Anda berhasil dikirim!\nKami akan mengonfirmasi pemesanan kursi melalui nomor telepon Anda.");
        } else {
            String msg = "Tiket Berhasil Dipesan!\n";
            if (seatId != null && busRoute != null) {
                msg += "Nomor Kursi: " + seatId + " | Rute: " + busRoute;
            }
            sucesstext.setText(msg);
        }

        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(bookedsuccesfully.this, welcomePage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(bookedsuccesfully.this, welcomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}