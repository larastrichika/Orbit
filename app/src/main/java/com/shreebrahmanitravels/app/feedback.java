package com.shreebrahmanitravels.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.hsalf.smileyrating.SmileyRating;

public class feedback extends AppCompatActivity {

    SmileyRating smileRating;
    TextInputLayout message_feedback;
    ImageView backbutton;
    Button submitfeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        message_feedback = findViewById(R.id.msgfeedback);
        submitfeedback = findViewById(R.id.submitfeedback);
        backbutton = findViewById(R.id.backarrowonfeedback);
        smileRating = findViewById(R.id.smile_ratingid);
        TextView guestnotw = findViewById(R.id.guestnote);

        if (new sessionmanager(getBaseContext()).getuserdetail() != null &&
                new sessionmanager(getBaseContext()).getuserdetail().equals("Guest user")) {
            if (guestnotw != null) guestnotw.setVisibility(View.VISIBLE);
        }

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (smileRating != null) {
            smileRating.setRating(SmileyRating.Type.TERRIBLE, true);
            smileRating.setRating(1, true);
        }

        submitfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message_feedback.getEditText() != null) {
                    message_feedback.getEditText().getText().clear();
                    message_feedback.clearFocus();
                }
                Toast.makeText(feedback.this, "Terima kasih atas ulasan dan masukan Anda!", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}