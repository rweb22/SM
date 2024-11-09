package com.tripleseven.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.tripleseven.android.dto.GameOption;
import com.tripleseven.android.dto.GameType;
import com.tripleseven.android.dto.MarketDto;

import java.util.ArrayList;

public class MarketGames extends AppCompatActivity {
    Context context;
    android.app.AlertDialog alert11;

    private CardView singleGame;
    private CardView jodiGame;
    private CardView singlePannaGame;
    private CardView doublePannaGame;
    private CardView tripePannaGame;
    private CardView halfSangamGame;
    private CardView fullSangamGame;
    private MarketDto market;
    private CardView spMotorGame;
    private CardView dpMotorGame;
    private CardView groupJodiGame;
    private CardView spDpTpGame;
    private CardView oddEvenGame;
    private CardView redBracketGame;
    private CardView choicePannaGame;
    private CardView panelGroupGame;
    private CardView twoDigitPanelGame;
    private latobold title, triple_panna_text;
    private latobold crossingOp;

    private ArrayList<String> digits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);
        context = this;
        initViews();

        this.market = (MarketDto) getIntent().getSerializableExtra("market");
        assert market != null;

        title.setText(market.getName());

        if ("TWO_DIGIT".equalsIgnoreCase(market.getType())) {
            jodiGame.setVisibility(View.VISIBLE);
            tripePannaGame.setVisibility(View.VISIBLE);

            triple_panna_text.setText(GameOption.HARF.getDisplayName());
            crossingOp.setText(GameOption.JODI_MOTOR.getDisplayName());
            //crossingGame.setVisibility(View.VISIBLE);

            //Disable other games
            redBracketGame.setVisibility(View.GONE);
            oddEvenGame.setVisibility(View.GONE);
            singleGame.setVisibility(View.GONE);
            singlePannaGame.setVisibility(View.GONE);
            doublePannaGame.setVisibility(View.GONE);
            halfSangamGame.setVisibility(View.GONE);
            fullSangamGame.setVisibility(View.GONE);
            spMotorGame.setVisibility(View.GONE);
            dpMotorGame.setVisibility(View.GONE);
            spDpTpGame.setVisibility(View.GONE);
            choicePannaGame.setVisibility(View.GONE);
            panelGroupGame.setVisibility(View.GONE);
            twoDigitPanelGame.setVisibility(View.GONE);
        } else {
            singlePannaGame.setVisibility(View.VISIBLE);
            doublePannaGame.setVisibility(View.VISIBLE);
            tripePannaGame.setVisibility(View.VISIBLE);
            halfSangamGame.setVisibility(View.VISIBLE);
            fullSangamGame.setVisibility(View.VISIBLE);
            spMotorGame.setVisibility(View.VISIBLE);
            dpMotorGame.setVisibility(View.VISIBLE);
            spDpTpGame.setVisibility(View.VISIBLE);
            choicePannaGame.setVisibility(View.VISIBLE);
            panelGroupGame.setVisibility(View.VISIBLE);
            twoDigitPanelGame.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.back).setOnClickListener(v -> finish());

        singleGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                digits.clear();
                digitsForSingleGame();
                askOpenClose(GameOption.SINGLE, null);
            }
        });

        jodiGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (market.getOpenSessionOn()) {
                    digits.clear();
                    jodiDigits();
                    startActivity(new Intent(MarketGames.this, BettingScreen.class)
                            .putExtra("market", market)
                            .putExtra("game", GameOption.JODI.name())
                            .putExtra("session", "")
                            .putExtra("digits", digits).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    );
                } else {
                    android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
                    LayoutInflater factory = LayoutInflater.from(context);
                    View v2 = factory.inflate(R.layout.msg_dialog_error, null);

                    TextView close = v2.findViewById(R.id.close);
                    TextView msgView = v2.findViewById(R.id.msg);
                    msgView.setText(getString(R.string.bet_closed));

                    builder1.setView(v2);
                    builder1.setCancelable(false);
                    android.app.AlertDialog alert11 = builder1.create();

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert11.dismiss();
                        }
                    });

                    alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    alert11.show();
                }
            }
        });

        singlePannaGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                digits.clear();
                singlePannaDigits();
                askOpenClose(GameOption.SP, null);
            }
        });

        doublePannaGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                digits.clear();
                doublePannaDigits();
                askOpenClose(GameOption.DP, null);
            }
        });

        tripePannaGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                digits.clear();
                triplePannaDigits();
                askOpenClose(GameOption.TP, null);
            }
        });


        spMotorGame.setOnClickListener(v -> {
            digits.clear();
            singlePannaDigits();
            askOpenClose(GameOption.SP_MOTOR, SpMotor.class);
        });


        choicePannaGame.setOnClickListener(v -> {
            digits.clear();
            singlePannaDigits();
            askOpenClose(GameOption.CHOICE_PANA, SpMotor.class);

        });


        dpMotorGame.setOnClickListener(v -> {
            digits.clear();
            doublePannaDigits();
            askOpenClose(GameOption.DP, SpMotor.class);

        });

        spDpTpGame.setOnClickListener(v -> {
            digits.clear();
            doublePannaDigits();
            askOpenClose(GameOption.SP_DP_TP, SpMotor.class);
        });


        groupJodiGame.setOnClickListener(v -> {
            if (market.getOpenSessionOn()) {
                digits.clear();
                jodiDigits();

                GameOption gameOption;
                if ("TWO_DIGIT".equalsIgnoreCase(market.getType())) {
                    gameOption = GameOption.JODI_MOTOR;
                } else {
                    gameOption = GameOption.GROUP_JODI;
                }

                startActivity(new Intent(MarketGames.this, SpMotor.class)
                        .putExtra("market", market)
                        .putExtra("game", gameOption.name())
                        .putExtra("session", "")
                        .putExtra("digits", digits).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                );
            } else {
                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
                LayoutInflater factory = LayoutInflater.from(context);
                View v2 = factory.inflate(R.layout.msg_dialog_error, null);

                TextView close = v2.findViewById(R.id.close);
                TextView msgView = v2.findViewById(R.id.msg);
                msgView.setText(getString(R.string.bet_closed));

                builder1.setView(v2);
                builder1.setCancelable(false);
                android.app.AlertDialog alert11 = builder1.create();

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert11.dismiss();
                    }
                });
                alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alert11.show();
            }
        });


        panelGroupGame.setOnClickListener(v -> {
            digits.clear();
            jodiDigits();
            askOpenClose(GameOption.PANEL_GROUP, SpMotor.class);
        });

        twoDigitPanelGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                digits.clear();
                jodiDigits();
                askOpenClose(GameOption.TWO_D_PANEL, SpMotor.class);
            }
        });

        oddEvenGame.setOnClickListener(v -> {
            digits.clear();
            jodiDigits();
            askOpenClose(GameOption.ODD_EVEN, SpMotor.class);
        });


        redBracketGame.setOnClickListener(v -> {
            digits.clear();
            jodiDigits();
            if (market.getOpenSessionOn()) {
                digits.clear();
                jodiDigits();
                startActivity(new Intent(MarketGames.this, SpMotor.class)
                        .putExtra("market", market)
                        .putExtra("game", GameOption.RED_BRACKET.name())
                        .putExtra("session", "")
                        .putExtra("digits", digits).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                );
            }
            else {
                android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
                LayoutInflater factory = LayoutInflater.from(context);
                View v2 = factory.inflate(R.layout.msg_dialog_error, null);

                TextView close = v2.findViewById(R.id.close);
                TextView msgView = v2.findViewById(R.id.msg);
                msgView.setText(getString(R.string.bet_closed));

                builder1.setView(v2);
                builder1.setCancelable(false);
                android.app.AlertDialog alert11 = builder1.create();

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert11.dismiss();
                    }
                });
                alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                alert11.show();

            }

        });

        halfSangamGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (market.getOpenSessionOn()) {
                    startActivity(new Intent(MarketGames.this, HalfSangam.class)
                            .putExtra("market", market)
                            .putExtra("game", GameOption.HALF_SANGAM.name())
                            .putExtra("digits", digits)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    );

                } else {
                    android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
                    LayoutInflater factory = LayoutInflater.from(context);
                    View v2 = factory.inflate(R.layout.msg_dialog_error, null);

                    TextView close = v2.findViewById(R.id.close);
                    TextView msgView = v2.findViewById(R.id.msg);
                    msgView.setText(getString(R.string.bet_closed));

                    builder1.setView(v2);
                    builder1.setCancelable(false);
                    android.app.AlertDialog alert11 = builder1.create();

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert11.dismiss();
                        }
                    });
                    alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    alert11.show();
                }

            }
        });

        fullSangamGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (market.getOpenSessionOn()) {
                    startActivity(new Intent(MarketGames.this, FullSangam.class)
                            .putExtra("market", market)
                            .putExtra("game", GameOption.FULL_SANGAM.name())
                            .putExtra("digits", digits)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    );

                } else {
                    android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
                    LayoutInflater factory = LayoutInflater.from(context);
                    View v2 = factory.inflate(R.layout.msg_dialog_error, null);

                    TextView close = v2.findViewById(R.id.close);
                    TextView msgView = v2.findViewById(R.id.msg);
                    msgView.setText(getString(R.string.bet_closed));

                    builder1.setView(v2);
                    builder1.setCancelable(false);
                    android.app.AlertDialog alert11 = builder1.create();

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert11.dismiss();
                        }
                    });
                    alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    alert11.show();
                }

            }
        });

    }

    public void askOpenClose(GameOption game, Class<? extends Activity> ActivityToOpen) {
        if (ActivityToOpen == null) {
            ActivityToOpen = BettingScreen.class;
        }

        if (market.getCloseSessionOn()) {
            android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
            LayoutInflater factory = LayoutInflater.from(context);
            View v2 = factory.inflate(R.layout.single_bet_popup, null);

            TextView open = v2.findViewById(R.id.open);
            TextView close = v2.findViewById(R.id.close);

            if ("TWO_DIGIT".equalsIgnoreCase(market.getType())) {
                open.setText(getString(R.string.andar));
                close.setText(getString(R.string.bahar));
            }

            Class<? extends Activity> finalActivityToOpen = ActivityToOpen;
            open.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert11.dismiss();
                   if ("TWO_DIGIT".equalsIgnoreCase(market.getType())){
                        startActivity(new Intent(MarketGames.this, finalActivityToOpen)
                                .putExtra("market", market)
                                .putExtra("game", GameOption.HARF.name())
                                .putExtra("session", "OPEN")
                                .putExtra("digits", digits).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        );
                   } else{
                       startActivity(new Intent(MarketGames.this, finalActivityToOpen)
                               .putExtra("market", market)
                               .putExtra("game", game.name())
                               .putExtra("session", "OPEN")
                               .putExtra("digits", digits).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                       );
                   }
                }
            });

            if (!market.getOpenSessionOn()) {
                open.setTextColor(getResources().getColor(R.color.gray));
                open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MarketGames.this, getString(R.string.bet_closed), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert11.dismiss();
                    if ("TWO_DIGIT".equalsIgnoreCase(market.getType())) {
                        startActivity(new Intent(MarketGames.this, finalActivityToOpen)
                                .putExtra("market", market)
                                .putExtra("game", GameOption.HARF.name())
                                .putExtra("session", "CLOSE")
                                .putExtra("digits", digits).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        );
                    }else{
                        startActivity(new Intent(MarketGames.this, finalActivityToOpen)
                                .putExtra("market", market)
                                .putExtra("game", game.name())
                                .putExtra("session", "CLOSE")
                                .putExtra("digits", digits).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        );
                    }
                }
            });

            builder1.setView(v2);
            builder1.setCancelable(true);
            alert11 = builder1.create();
            alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alert11.show();
        } else {
            android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
            LayoutInflater factory = LayoutInflater.from(context);
            View v2 = factory.inflate(R.layout.msg_dialog_error, null);

            TextView close = v2.findViewById(R.id.close);
            TextView msgView = v2.findViewById(R.id.msg);
            msgView.setText(getString(R.string.bet_closed));

            builder1.setView(v2);
            builder1.setCancelable(false);
            android.app.AlertDialog alert11 = builder1.create();

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert11.dismiss();
                }
            });
            alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alert11.show();
        }
    }

    public void digitsForSingleGame() {
        digits.add("0");
        digits.add("1");
        digits.add("2");
        digits.add("3");
        digits.add("4");
        digits.add("5");
        digits.add("6");
        digits.add("7");
        digits.add("8");
        digits.add("9");
    }

    public void doublePannaDigits() {
        digits.add("100");
        digits.add("119");
        digits.add("155");
        digits.add("227");
        digits.add("335");
        digits.add("344");
        digits.add("399");
        digits.add("588");
        digits.add("669");
        digits.add("200");
        digits.add("110");
        digits.add("228");
        digits.add("255");
        digits.add("336");
        digits.add("499");
        digits.add("660");
        digits.add("688");
        digits.add("778");
        digits.add("300");
        digits.add("166");
        digits.add("229");
        digits.add("337");
        digits.add("355");
        digits.add("445");
        digits.add("599");
        digits.add("779");
        digits.add("788");
        digits.add("400");
        digits.add("112");
        digits.add("220");
        digits.add("266");
        digits.add("338");
        digits.add("446");
        digits.add("455");
        digits.add("699");
        digits.add("770");
        digits.add("500");
        digits.add("113");
        digits.add("122");
        digits.add("177");
        digits.add("339");
        digits.add("366");
        digits.add("447");
        digits.add("799");
        digits.add("889");
        digits.add("600");
        digits.add("114");
        digits.add("277");
        digits.add("330");
        digits.add("448");
        digits.add("466");
        digits.add("556");
        digits.add("880");
        digits.add("899");
        digits.add("700");
        digits.add("115");
        digits.add("133");
        digits.add("188");
        digits.add("223");
        digits.add("377");
        digits.add("449");
        digits.add("557");
        digits.add("566");
        digits.add("800");
        digits.add("116");
        digits.add("224");
        digits.add("233");
        digits.add("288");
        digits.add("440");
        digits.add("477");
        digits.add("558");
        digits.add("990");
        digits.add("900");
        digits.add("117");
        digits.add("144");
        digits.add("199");
        digits.add("225");
        digits.add("388");
        digits.add("559");
        digits.add("577");
        digits.add("667");
        digits.add("550");
        digits.add("668");
        digits.add("244");
        digits.add("299");
        digits.add("226");
        digits.add("488");
        digits.add("677");
        digits.add("118");
        digits.add("334");
    }


    public void singlePannaDigits() {
        digits.add("128");
        digits.add("137");
        digits.add("146");
        digits.add("236");
        digits.add("245");
        digits.add("290");
        digits.add("380");
        digits.add("470");
        digits.add("489");
        digits.add("560");
        digits.add("678");
        digits.add("579");
        digits.add("129");
        digits.add("138");
        digits.add("147");
        digits.add("156");
        digits.add("237");
        digits.add("246");
        digits.add("345");
        digits.add("390");
        digits.add("480");
        digits.add("570");
        digits.add("679");
        digits.add("120");
        digits.add("139");
        digits.add("148");
        digits.add("157");
        digits.add("238");
        digits.add("247");
        digits.add("256");
        digits.add("346");
        digits.add("490");
        digits.add("580");
        digits.add("670");
        digits.add("689");
        digits.add("130");
        digits.add("149");
        digits.add("158");
        digits.add("167");
        digits.add("239");
        digits.add("248");
        digits.add("257");
        digits.add("347");
        digits.add("356");
        digits.add("590");
        digits.add("680");
        digits.add("789");
        digits.add("140");
        digits.add("159");
        digits.add("168");
        digits.add("230");
        digits.add("249");
        digits.add("258");
        digits.add("267");
        digits.add("348");
        digits.add("357");
        digits.add("456");
        digits.add("690");
        digits.add("780");
        digits.add("123");
        digits.add("150");
        digits.add("169");
        digits.add("178");
        digits.add("240");
        digits.add("259");
        digits.add("268");
        digits.add("349");
        digits.add("358");
        digits.add("457");
        digits.add("367");
        digits.add("790");
        digits.add("124");
        digits.add("160");
        digits.add("179");
        digits.add("250");
        digits.add("269");
        digits.add("278");
        digits.add("340");
        digits.add("359");
        digits.add("368");
        digits.add("458");
        digits.add("467");
        digits.add("890");
        digits.add("125");
        digits.add("134");
        digits.add("170");
        digits.add("189");
        digits.add("260");
        digits.add("279");
        digits.add("350");
        digits.add("369");
        digits.add("378");
        digits.add("459");
        digits.add("567");
        digits.add("468");
        digits.add("126");
        digits.add("135");
        digits.add("180");
        digits.add("234");
        digits.add("270");
        digits.add("289");
        digits.add("360");
        digits.add("379");
        digits.add("450");
        digits.add("469");
        digits.add("478");
        digits.add("568");
        digits.add("127");
        digits.add("136");
        digits.add("145");
        digits.add("190");
        digits.add("235");
        digits.add("280");
        digits.add("370");
        digits.add("479");
        digits.add("460");
        digits.add("569");
        digits.add("389");
        digits.add("578");
        digits.add("589");
    }


    public void triplePannaDigits() {
        digits.add("000");
        digits.add("111");
        digits.add("222");
        digits.add("333");
        digits.add("444");
        digits.add("555");
        digits.add("666");
        digits.add("777");
        digits.add("888");
        digits.add("999");
    }

    public void jodiDigits() {
        for (int i = 0; i < 100; i++) {
            String temp = String.format("%02d", i);
            digits.add(temp);
        }
    }


    private void initViews() {
        triple_panna_text =findViewById(R.id.triple_panna_text);
        singleGame = findViewById(R.id.single);
        jodiGame = findViewById(R.id.jodi);
        singlePannaGame = findViewById(R.id.singlepatti);
        doublePannaGame = findViewById(R.id.doublepatti);
        tripePannaGame = findViewById(R.id.tripepatti);
        halfSangamGame = findViewById(R.id.halfsangam);
        fullSangamGame = findViewById(R.id.fullsangam);
        spMotorGame = findViewById(R.id.sp_motor);
        dpMotorGame = findViewById(R.id.dp_motor);
        groupJodiGame = findViewById(R.id.group_jodi);
        spDpTpGame = findViewById(R.id.spdptp);
        oddEvenGame = findViewById(R.id.odd_even);
        redBracketGame = findViewById(R.id.red_bracket);
        choicePannaGame = findViewById(R.id.choice_panna);
        panelGroupGame = findViewById(R.id.panel_group);
        twoDigitPanelGame = findViewById(R.id.two_digit_panel);
        title = findViewById(R.id.title);
        crossingOp = findViewById(R.id.crossingOp);
    }
}