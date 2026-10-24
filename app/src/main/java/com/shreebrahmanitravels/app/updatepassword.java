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

public class updatepassword extends AppCompatActivity {
    TextInputLayout currentpassword, newpassword, conformnewpassword;
    Button nextbutton;
    ImageView backarrow;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepassword);

        final String username = new sessionmanager(getBaseContext()).getuserdetail();

        currentpassword = findViewById(R.id.current_password);
        newpassword = findViewById(R.id.new_password);
        conformnewpassword = findViewById(R.id.re_entered_password);
        nextbutton = findViewById(R.id.nextbuttononchangepasswordscreen);
        backarrow = findViewById(R.id.backarrowonupdatepass);
        progressBar = findViewById(R.id.progressbaronupdatepssword);
        progressBar.setVisibility(View.INVISIBLE);

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (getCurrentFocus() != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                } catch (Exception ignored) {
                }

                if (!validcurrentpassword() | !validnewpassword() | !validnewconfirmpassword()) {
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                final String currentPass = currentpassword.getEditText().getText().toString().trim();
                final String newPass = conformnewpassword.getEditText().getText().toString().trim();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase db = AppDatabase.getInstance(updatepassword.this);
                        final UserEntity user = db.userDao().getUserByUsername(username);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                                if (user != null) {
                                    if (user.getPassword().equals(currentPass)) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AppDatabase.getInstance(updatepassword.this).userDao().updatePassword(username, newPass);
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Toast.makeText(updatepassword.this, "Password berhasil diubah", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(updatepassword.this, updatedpasswordsucess.class));
                                                        finish();
                                                    }
                                                });
                                            }
                                        }).start();
                                    } else {
                                        currentpassword.setError("Password saat ini salah");
                                    }
                                } else {
                                    Toast.makeText(updatepassword.this, "Pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).start();
            }
        });
    }

    private Boolean validcurrentpassword() {
        String val = currentpassword.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            currentpassword.setError("Password tidak boleh kosong");
            return false;
        } else {
            currentpassword.setError(null);
            currentpassword.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validnewpassword() {
        String val = newpassword.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            newpassword.setError("Password tidak boleh kosong");
            return false;
        } else if (val.length() < 4) {
            newpassword.setError("Password minimal 4 karakter");
            return false;
        } else {
            newpassword.setError(null);
            newpassword.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validnewconfirmpassword() {
        String val = newpassword.getEditText().getText().toString().trim();
        String val1 = conformnewpassword.getEditText().getText().toString().trim();
        if (!val.equals(val1)) {
            conformnewpassword.setError("Konfirmasi password tidak cocok");
            return false;
        } else {
            conformnewpassword.setError(null);
            conformnewpassword.setErrorEnabled(false);
            return true;
        }
    }
}