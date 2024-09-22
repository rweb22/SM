package com.tripleseven.android;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import com.tripleseven.android.dto.GameOption;
import com.tripleseven.android.dto.MarketDto;

import java.util.Objects;

public class BettingScreen extends AppCompatActivity {
    private latobold easyText;
    private View easyLine;
    private LinearLayout easyMode;
    private latobold sepcialText;
    private View specialLine;
    private LinearLayout specialMode;
    int red, white, font;
    private latobold title;
    private latonormal game;
    private MarketDto market;
    private String session;
    private GameOption gameOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_betting_screen);
        initViews();

        this.market = (MarketDto) getIntent().getSerializableExtra("market");
        this.session = getIntent().getStringExtra("session");
        this.gameOption = GameOption.valueOf(getIntent().getStringExtra("game"));

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

        title.setText(gameOption.getDisplayName());
        game.setText(getMarketName());

        if (gameOption.equals(GameOption.SP) || gameOption.equals(GameOption.DP)) {
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

            if (gameOption.equals(GameOption.SP) || gameOption.equals(GameOption.DP)) {
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

    private String getMarketName() {
        if (Objects.isNull(session) || Objects.requireNonNull(session).isEmpty()) {
            return market.getName();
        } else {
            return market.getName() + " " + session.substring(0, 1).toUpperCase() + session.substring(1).toLowerCase();
        }
    }

    private void initViews() {
        RelativeLayout back = findViewById(R.id.back);
        latonormal balanceHome = findViewById(R.id.balance_home);
        LinearLayout walletBlock = findViewById(R.id.wallet_block);
        RelativeLayout toolbar = findViewById(R.id.toolbar);
        easyText = findViewById(R.id.easy_text);
        easyLine = findViewById(R.id.easy_line);
        easyMode = findViewById(R.id.easy_mode);
        sepcialText = findViewById(R.id.sepcial_text);
        specialLine = findViewById(R.id.special_line);
        specialMode = findViewById(R.id.special_mode);
        FragmentContainerView fragmentContainer = findViewById(R.id.fragment_container);
        title = findViewById(R.id.title);
        game = findViewById(R.id.game);
    }
}