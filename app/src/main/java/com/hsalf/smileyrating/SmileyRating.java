package com.hsalf.smileyrating;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatRatingBar;

public class SmileyRating extends AppCompatRatingBar {

    public enum Type {
        NONE(0),
        TERRIBLE(1),
        BAD(2),
        OKAY(3),
        GOOD(4),
        GREAT(5);

        private final int rating;

        Type(int rating) {
            this.rating = rating;
        }

        public int getRating() {
            return rating;
        }

        public static Type fromRating(int rating) {
            for (Type type : values()) {
                if (type.rating == rating) {
                    return type;
                }
            }
            return NONE;
        }
    }

    public static class Smiley {
        private final Type type;

        public Smiley(Type type) {
            this.type = type;
        }

        public int getRating() {
            return type.getRating();
        }

        public Type getType() {
            return type;
        }
    }

    public SmileyRating(Context context) {
        super(context);
        init();
    }

    public SmileyRating(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmileyRating(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setNumStars(5);
        setStepSize(1.0f);
    }

    public void setRating(Type type, boolean animate) {
        if (type != null) {
            setRating((float) type.getRating());
        }
    }

    public void setRating(int rating, boolean animate) {
        setRating((float) rating);
    }

    public Smiley getSelectedSmiley() {
        int rating = Math.round(getRating());
        return new Smiley(Type.fromRating(rating));
    }
}
