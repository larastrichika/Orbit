package com.shreebrahmanitravels.app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.shreebrahmanitravels.app.data.AppDatabase;
import com.shreebrahmanitravels.app.data.UserEntity;

public class login extends AppCompatActivity {
    ImageView busimage;
    TextView welcometext, signintocontitxt;
    TextInputLayout username, password;
    Button Go_onLOGIN, new_userbtn;
    ProgressBar progressBar;
    TextView forgetPasswordtxt;
    TextView skipsignup;
    CheckBox checkboxRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Prepopulate demo user if database is empty
        seedDemoUserIfNeeded();

        // hookups
        busimage = findViewById(R.id.bus_image);
        welcometext = findViewById(R.id.welcomebacktext);
        signintocontitxt = findViewById(R.id.signintoconttext);
        username = findViewById(R.id.Username);
        password = findViewById(R.id.Password);
        Go_onLOGIN = findViewById(R.id.Go_button_on_login);
        new_userbtn = findViewById(R.id.signupbuttononlogin);
        progressBar = findViewById(R.id.loading);
        progressBar.setVisibility(View.INVISIBLE);
        skipsignup = findViewById(R.id.skipsignup);
        checkboxRemember = findViewById(R.id.checkboxRemember);
        forgetPasswordtxt = findViewById(R.id.ForgetText);

        forgetPasswordtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, Forgetpassword.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        new_userbtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(busimage, "bus_image");
                pairs[1] = new Pair<View, String>(welcometext, "Text");
                pairs[2] = new Pair<View, String>(signintocontitxt, "signin_tran");
                pairs[3] = new Pair<View, String>(new_userbtn, "SIgnup_transi");
                pairs[4] = new Pair<View, String>(username, "username_transi");
                pairs[5] = new Pair<View, String>(password, "password_transi");
                pairs[6] = new Pair<View, String>(Go_onLOGIN, "button_tran");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(login.this, pairs);
                Intent intent = new Intent(login.this, signup.class);

                startActivity(intent, options.toBundle());
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        Go_onLOGIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (getCurrentFocus() != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                } catch (Exception ignored) {
                }

                progressBar.setVisibility(View.VISIBLE);
                loginuser();
            }
        });

        skipsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, guestwarning.class));
            }
        });
    }

    private void seedDemoUserIfNeeded() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AppDatabase db = AppDatabase.getInstance(login.this);
                    if (db.userDao().getUserByUsername("kelompok4") == null) {
                        UserEntity demo = new UserEntity(
                                "kelompok4",
                                "Demo User",
                                "123456",
                                "0812345678",
                                "SB1",
                                "IT",
                                "2024",
                                "Terminal Pulo Gebang",
                                "0"
                        );
                        db.userDao().insertUser(demo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Apakah Anda yakin ingin keluar?").setCancelable(false).setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void loginuser() {
        if (!validateUsername() | !validpassword()) {
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        check_isUser();
    }

    private void check_isUser() {
        final String userstored_lower_username = username.getEditText().getText().toString().trim();
        final String userStored_data_password = password.getEditText().getText().toString().trim();

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(login.this);
                final UserEntity userEntity = db.userDao().getUserByUsername(userstored_lower_username);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (userEntity != null) {
                            username.setError(null);
                            username.setErrorEnabled(false);

                            if (userEntity.getPassword().equals(userStored_data_password)) {
                                password.setError(null);
                                password.setErrorEnabled(false);

                                sessionmanager sessionmanager_obj = new sessionmanager(login.this);
                                if (checkboxRemember != null && checkboxRemember.isChecked()) {
                                    sessionmanager_obj.createloginsession(userstored_lower_username, userStored_data_password);
                                } else {
                                    sessionmanager_obj.createloginsession(userstored_lower_username, userStored_data_password);
                                }

                                Toast.makeText(login.this, "Login Berhasil! Selamat datang " + userEntity.getName(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(login.this, welcomePage.class);
                                startActivity(intent);
                                finish();
                            } else {
                                password.setError("Password salah");
                                password.requestFocus();
                            }
                        } else {
                            username.setError("Pengguna tidak ditemukan");
                            username.requestFocus();
                        }
                    }
                });
            }
        }).start();
    }

    private boolean validpassword() {
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Password tidak boleh kosong");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUsername() {
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

    @Override
    protected void onStart() {
        super.onStart();
        sessionmanager sessionmanager_obj = new sessionmanager(login.this);
        String username = sessionmanager_obj.getuserdetail();
        if (username != null && !username.equals("Guest user")) {
            movetowelcomepage();
        }
    }

    private void movetowelcomepage() {
        Intent intent = new Intent(login.this, welcomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}