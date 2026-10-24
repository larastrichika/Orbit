package com.shreebrahmanitravels.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.shreebrahmanitravels.app.data.AppDatabase;
import com.shreebrahmanitravels.app.data.UserEntity;

public class Forgetpassword extends AppCompatActivity {
    ImageView backbtn;
    TextInputLayout phonenumber;
    Button next_Button_fp_screen;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);

        phonenumber = findViewById(R.id.phonenumberonforgetpassword);
        next_Button_fp_screen = findViewById(R.id.nextbuttononforgetpasswordscreen);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);
        backbtn = findViewById(R.id.backarrow);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Forgetpassword.this, login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        next_Button_fp_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (getCurrentFocus() != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                } catch (Exception ignored) {
                }

                if (!validphonenumber()) {
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                isphoneexist();
            }
        });
    }

    private void isphoneexist() {
        final String new_phonenumber = phonenumber.getEditText().getText().toString().trim();

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(Forgetpassword.this);
                final UserEntity user = db.userDao().getUserByPhone(new_phonenumber);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (user != null) {
                            Intent intent = new Intent(Forgetpassword.this, setNewPassword.class);
                            intent.putExtra("phone_no", new_phonenumber);
                            intent.putExtra("username", user.getUsername());
                            startActivity(intent);
                        } else {
                            phonenumber.setError("Nomor telepon tidak terdaftar");
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Forgetpassword.this, login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private Boolean validphonenumber() {
        String val = phonenumber.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            phonenumber.setError("Masukkan nomor telepon");
            return false;
        } else if (val.length() < 8) {
            phonenumber.setError("Nomor telepon minimal 8 digit");
            return false;
        } else {
            phonenumber.setError(null);
            phonenumber.setErrorEnabled(false);
            return true;
        }
    }
}