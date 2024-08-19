package com.tripleseven.android;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

public class BettingScreen extends AppCompatActivity {

    private RelativeLayout back;
    private latonormal balanceHome;
    private LinearLayout walletBlock;
    private RelativeLayout toolbar;
    private latobold easyText;
    private View easyLine;
    private LinearLayout easyMode;
    private latobold sepcialText;
    private View specialLine;
    private LinearLayout specialMode;
    private FragmentContainerView fragmentContainer;
    int red, white, font;
    private latobold title;
    private latonormal game;
    String timing, type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting_screen);
        initViews();

        red = getResources().getColor(R.color.red);
        white = getResources().getColor(R.color.background);
        font = getResources().getColor(R.color.black);

        TextView tx = findViewById(R.id.balance_home);
        tx.setText((Integer.parseInt(getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("wallet","0")))+" ₹");


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String bgame = getIntent().getStringExtra("game").replace("singlepatti","SP");
        bgame = bgame.replace("doublepatti","DP");
        bgame = bgame.replace("triplepatti","TP");
        bgame = bgame.replace("single","Single");
        bgame = bgame.replace("jodi","Jodi");

        title.setText(bgame);
        game.setText(getIntent().getStringExtra("market").replace("_"," "));
        if (getIntent().hasExtra("timin")){
            timing = getIntent().getStringExtra("timin");
        }
        if (getIntent().hasExtra("type")){
            type = getIntent().getStringExtra("type");
        }
        if (getIntent().getStringExtra("game").equals("singlepatti") || getIntent().getStringExtra("game").equals("doublepatti")) {
            EasyBettingPanna easyBetting = new EasyBettingPanna();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, easyBetting)
                    .commit();
        } else {
            EasyBetting easyBetting = new EasyBetting();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, easyBetting)
                    .commit();
        }

        easyMode.setOnClickListener(v -> {
            easyText.setTextColor(red);
            easyLine.setBackgroundColor(red);
            specialLine.setBackgroundColor(white);
            sepcialText.setTextColor(font);

            if (getIntent().getStringExtra("game").equals("singlepatti") || getIntent().getStringExtra("game").equals("doublepatti")) {
                EasyBettingPanna easyBetting = new EasyBettingPanna();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, easyBetting)
                        .commit();
            } else {
                EasyBetting easyBetting = new EasyBetting();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, easyBetting)
                        .commit();
            }
        });

        specialMode.setOnClickListener(v -> {
            easyText.setTextColor(font);
            easyLine.setBackgroundColor(white);
            specialLine.setBackgroundColor(red);
            sepcialText.setTextColor(red);

            SpecialMode specialMode = new SpecialMode();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, specialMode)
                    .commit();
        });
    }

    private void initViews() {
        back = findViewById(R.id.back);
        balanceHome = findViewById(R.id.balance_home);
        walletBlock = findViewById(R.id.wallet_block);
        toolbar = findViewById(R.id.toolbar);
        easyText = findViewById(R.id.easy_text);
        easyLine = findViewById(R.id.easy_line);
        easyMode = findViewById(R.id.easy_mode);
        sepcialText = findViewById(R.id.sepcial_text);
        specialLine = findViewById(R.id.special_line);
        specialMode = findViewById(R.id.special_mode);
        fragmentContainer = findViewById(R.id.fragment_container);
        title = findViewById(R.id.title);
        game = findViewById(R.id.game);
    }
}