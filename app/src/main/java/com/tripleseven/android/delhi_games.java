package com.tripleseven.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;

import java.util.ArrayList;

public class delhi_games extends AppCompatActivity {

    private CardView back;
    private CardView toolbar;
    private CardView single;
    private CardView jodi;
    private CardView singlepatti;
    private CardView doublepatti;
    private CardView tripepatti;
    private CardView halfsangam;
    private CardView fullsangam;
    private CardView crossing;
    ArrayList<String> number = new ArrayList<>();
    String market = "",is_open= "0", is_close = "0",market_type = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delhi_games);
    }
}