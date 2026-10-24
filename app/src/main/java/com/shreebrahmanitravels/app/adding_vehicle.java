package com.shreebrahmanitravels.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.shreebrahmanitravels.app.data.AppDatabase;
import com.shreebrahmanitravels.app.data.UserEntity;

public class adding_vehicle extends AppCompatActivity {
    
    TextInputLayout entered_name, from, to, vehicle_model, vehicle_number, cost_per_passanger, entered_phone_no;
    TextView vehicle_type;
    Button add_vehicle;
    ProgressBar progressBar;
    String data_name, data_phone_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_vehicle);

        entered_name = findViewById(R.id.name_ridesharetext);
        from = findViewById(R.id.from_ridesharetext);
        to = findViewById(R.id.to_ridesharetext);
        vehicle_model = findViewById(R.id.vehicle_model_rideshare);
        vehicle_number = findViewById(R.id.vehiclenumber_rideshare);
        cost_per_passanger = findViewById(R.id.cost_ridesharetext);
        entered_phone_no = findViewById(R.id.phonenumber_rideshare);
        vehicle_type = findViewById(R.id.vehicletypedropdown);
        add_vehicle = findViewById(R.id.addvehicle_rideshare);
        progressBar = findViewById(R.id.progressbar_rideshare);
        if (progressBar != null) progressBar.setVisibility(View.GONE);
        
        final PopupMenu popupMenu = new PopupMenu(adding_vehicle.this, vehicle_type);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.dropdown_vehicletype, popupMenu.getMenu());

        vehicle_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
            }
        });

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                vehicle_type.setText(menuItem.getTitle());
                return true;
            }
        });

        fetch_user_data();

        add_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (getCurrentFocus() != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                } catch (Exception ignored) {
                }

                if (!validateName() | !validphonenumber() | !valid_from() | !valid_to() | !valid_vehiclemodel() | !valid_vehiclenumber() | !validcost()) {
                    return;
                }

                Toast.makeText(adding_vehicle.this, "Kendaraan berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void fetch_user_data() {
        final String username = new sessionmanager(getBaseContext()).getuserdetail();
        if (username == null) return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(adding_vehicle.this);
                final UserEntity user = db.userDao().getUserByUsername(username);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (user != null) {
                            data_name = user.getName();
                            data_phone_no = user.getPhoneNo();
                            if (entered_name.getEditText() != null && data_name != null) {
                                entered_name.getEditText().setText(data_name);
                            }
                            if (entered_phone_no.getEditText() != null && data_phone_no != null) {
                                entered_phone_no.getEditText().setText(data_phone_no);
                            }
                        }
                    }
                });
            }
        }).start();
    }

    private Boolean validateName() {
        String val = entered_name.getEditText().getText().toString();
        if (val.isEmpty()) {
            entered_name.setError("Nama tidak boleh kosong");
            return false;
        } else {
            entered_name.setError(null);
            entered_name.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean valid_from() {
        String val = from.getEditText().getText().toString();
        if (val.isEmpty()) {
            from.setError("Asal tidak boleh kosong");
            return false;
        } else {
            from.setError(null);
            from.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean valid_to() {
        String val = to.getEditText().getText().toString();
        if (val.isEmpty()) {
            to.setError("Tujuan tidak boleh kosong");
            return false;
        } else {
            to.setError(null);
            to.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean valid_vehiclemodel() {
        String val = vehicle_model.getEditText().getText().toString();
        if (val.isEmpty()) {
            vehicle_model.setError("Model kendaraan tidak boleh kosong");
            return false;
        } else {
            vehicle_model.setError(null);
            vehicle_model.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean valid_vehiclenumber() {
        String val = vehicle_number.getEditText().getText().toString();
        if (val.isEmpty()) {
            vehicle_number.setError("Nomor polisi tidak boleh kosong");
            return false;
        } else {
            vehicle_number.setError(null);
            vehicle_number.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validcost() {
        String val = cost_per_passanger.getEditText().getText().toString();
        if (val.isEmpty()) {
            cost_per_passanger.setError("Tarif tidak boleh kosong");
            return false;
        } else {
            cost_per_passanger.setError(null);
            cost_per_passanger.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validphonenumber() {
        String val = entered_phone_no.getEditText().getText().toString();
        if (val.isEmpty()) {
            entered_phone_no.setError("Masukkan nomor telepon");
            return false;
        } else {
            entered_phone_no.setError(null);
            entered_phone_no.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}