package com.shreebrahmanitravels.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.shreebrahmanitravels.app.data.AppDatabase;
import com.shreebrahmanitravels.app.data.UserEntity;

public class setNewPassword extends AppCompatActivity {

    TextInputLayout new_password, newConfirmpassword;
    Button nextbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        new_password = findViewById(R.id.Newpsswrd);
        newConfirmpassword = findViewById(R.id.newconfirmpsswrd);
        nextbtn = findViewById(R.id.nextbuttononforgetpasswordscreen);

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (getCurrentFocus() != null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                } catch (Exception ignored) {
                }

                if (!validnewpassword() | !validnewconfirmpassword()) {
                    return;
                }
                changepassword();
            }
        });
    }

    private void changepassword() {
        final String password = newConfirmpassword.getEditText().getText().toString().trim();
        final String username = getIntent().getStringExtra("username");
        final String phoneNo = getIntent().getStringExtra("phone_no");

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(setNewPassword.this);
                if (username != null && !username.isEmpty()) {
                    db.userDao().updatePassword(username, password);
                } else if (phoneNo != null && !phoneNo.isEmpty()) {
                    UserEntity user = db.userDao().getUserByPhone(phoneNo);
                    if (user != null) {
                        db.userDao().updatePassword(user.getUsername(), password);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(setNewPassword.this, "Password berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(setNewPassword.this, forgetpassword_sucessmsg.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).start();
    }

    private Boolean validnewpassword() {
        String val = new_password.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            new_password.setError("Password tidak boleh kosong");
            return false;
        } else if (val.length() < 4) {
            new_password.setError("Password minimal 4 karakter");
            return false;
        } else {
            new_password.setError(null);
            new_password.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validnewconfirmpassword() {
        String val = new_password.getEditText().getText().toString().trim();
        String val1 = newConfirmpassword.getEditText().getText().toString().trim();

        if (!val.equals(val1)) {
            newConfirmpassword.setError("Konfirmasi password tidak cocok");
            return false;
        } else {
            newConfirmpassword.setError(null);
            newConfirmpassword.setErrorEnabled(false);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}