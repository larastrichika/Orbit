package com.shreebrahmanitravels.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.shreebrahmanitravels.app.data.AppDatabase;
import com.shreebrahmanitravels.app.data.UserEntity;

public class seatManagement extends AppCompatActivity {
    TextInputEditText updateusername, fullname, branch, stand, year, pendingfees, busroute;
    TextView fetchdata;
    ImageView backbtn;
    ProgressBar progressBar;
    Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_management);

        updateusername = findViewById(R.id.updateusername);
        fullname = findViewById(R.id.nameupdate);
        branch = findViewById(R.id.updatebranch);
        stand = findViewById(R.id.updatestand);
        year = findViewById(R.id.updateyear);
        pendingfees = findViewById(R.id.updatependingfees);
        busroute = findViewById(R.id.updateroute);
        fetchdata = findViewById(R.id.fetchdata);
        backbtn = findViewById(R.id.backarrow);
        progressBar = findViewById(R.id.progressbarseatbooking);
        if (progressBar != null) progressBar.setVisibility(View.INVISIBLE);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fetchdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (getCurrentFocus() != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                } catch (Exception ignored) {
                }

                final String username = updateusername.getText().toString().trim();
                if (username.isEmpty()) {
                    updateusername.setError("Masukkan username");
                    return;
                }

                if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase db = AppDatabase.getInstance(seatManagement.this);
                        final UserEntity user = db.userDao().getUserByUsername(username);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (progressBar != null) progressBar.setVisibility(View.INVISIBLE);
                                if (user != null) {
                                    fullname.setText(user.getName());
                                    branch.setText(user.getBranch());
                                    stand.setText(user.getStand());
                                    year.setText(user.getCurrentyear());
                                    pendingfees.setText(user.getPendingfees());
                                    busroute.setText(user.getBus_no());
                                    Toast.makeText(seatManagement.this, "Data ditemukan", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(seatManagement.this, "Pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }
}