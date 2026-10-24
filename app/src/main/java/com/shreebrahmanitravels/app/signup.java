package com.shreebrahmanitravels.app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.shreebrahmanitravels.app.data.AppDatabase;
import com.shreebrahmanitravels.app.data.UserEntity;

public class signup extends AppCompatActivity {

    Button Alreadyhaveacc, Gobuttononsignup;
    TextInputLayout iduser, idpass;
    ImageView busimage;
    TextView welcome, signuptocontitxt;
    TextInputLayout fullnameintextinput, phonenumber;
    ProgressBar progressBar;
    TextView busroutetext;
    CheckBox checkboxTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        busimage = findViewById(R.id.bus_image);
        welcome = findViewById(R.id.welcomeonsignup);
        signuptocontitxt = findViewById(R.id.signuptocontitxt);
        iduser = findViewById(R.id.UsernameSignup);
        idpass = findViewById(R.id.Passwordsignup);
        Gobuttononsignup = findViewById(R.id.gobuttomonsignup);
        Alreadyhaveacc = findViewById(R.id.ALreadyacc);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        busroutetext = findViewById(R.id.editText);
        fullnameintextinput = findViewById(R.id.name);
        phonenumber = findViewById(R.id.phonenumber);
        checkboxTerms = findViewById(R.id.checkboxTerms);

        final PopupMenu popupMenu = new PopupMenu(signup.this, busroutetext);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.dropdownbusselection, popupMenu.getMenu());

        busroutetext.setText("Pilih Rute Bus Anda");
        busroutetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
            }
        });

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                busroutetext.setText(menuItem.getTitle());
                return true;
            }
        });

        Alreadyhaveacc.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signup.this, login.class);
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View, String>(busimage, "bus_image");
                pairs[1] = new Pair<View, String>(welcome, "Text");
                pairs[2] = new Pair<View, String>(signuptocontitxt, "signin_tran");
                pairs[3] = new Pair<View, String>(Alreadyhaveacc, "SIgnup_transi");
                pairs[4] = new Pair<View, String>(iduser, "username_transi");
                pairs[5] = new Pair<View, String>(idpass, "password_transi");
                pairs[6] = new Pair<View, String>(Gobuttononsignup, "button_tran");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(signup.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });

        Gobuttononsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (getCurrentFocus() != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                } catch (Exception ignored) {
                }

                if (!validateName() | !validateUsername() | !validphonenumber() | !validpassword() | !validbusroute()) {
                    return;
                }

                if (checkboxTerms != null && !checkboxTerms.isChecked()) {
                    Toast.makeText(signup.this, "Harap setujui Syarat dan Ketentuan layanan terlebih dahulu", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                registerUserInRoom();
            }
        });
    }

    private void registerUserInRoom() {
        final String new_username = iduser.getEditText().getText().toString().trim();
        final String phonenum = phonenumber.getEditText().getText().toString().trim();
        final String name = fullnameintextinput.getEditText().getText().toString().trim();
        final String password = idpass.getEditText().getText().toString().trim();
        final String bus_no = busroutetext.getText().toString().trim();

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(signup.this);
                UserEntity existingUser = db.userDao().getUserByUsername(new_username);
                if (existingUser != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            iduser.setError("Username sudah terdaftar");
                            iduser.requestFocus();
                        }
                    });
                    return;
                }

                UserEntity existingPhone = db.userDao().getUserByPhone(phonenum);
                if (existingPhone != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            phonenumber.setError("Nomor telepon sudah terdaftar");
                            phonenumber.requestFocus();
                        }
                    });
                    return;
                }

                // Insert into Room
                UserEntity newUser = new UserEntity(
                        new_username,
                        name,
                        password,
                        phonenum,
                        bus_no,
                        "Umum",
                        "2026",
                        "Terminal Utama",
                        "0"
                );
                db.userDao().insertUser(newUser);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(signup.this, "Pendaftaran Berhasil! Silakan Masuk", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(signup.this, login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).start();
    }

    private Boolean validateName() {
        String val = fullnameintextinput.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            fullnameintextinput.setError("Nama tidak boleh kosong");
            return false;
        } else {
            fullnameintextinput.setError(null);
            fullnameintextinput.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateUsername() {
        String val = iduser.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            iduser.setError("Username tidak boleh kosong");
            return false;
        } else if (val.contains(" ")) {
            iduser.setError("Spasi tidak diperbolehkan");
            return false;
        } else {
            iduser.setError(null);
            iduser.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validphonenumber() {
        String val = phonenumber.getEditText().getText().toString().trim();
        if (val.isEmpty() || val.length() < 8) {
            phonenumber.setError("Masukkan nomor telepon yang valid");
            return false;
        } else {
            phonenumber.setError(null);
            phonenumber.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validpassword() {
        String val = idpass.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            idpass.setError("Password tidak boleh kosong");
            return false;
        } else if (val.length() < 4) {
            idpass.setError("Password minimal 4 karakter");
            return false;
        } else {
            idpass.setError(null);
            idpass.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validbusroute() {
        String val = busroutetext.getText().toString();
        if (val.equals("Pilih Rute Bus Anda") || val.isEmpty()) {
            busroutetext.setError("Silakan pilih rute bus");
            return false;
        } else {
            busroutetext.setError(null);
            return true;
        }
    }
}