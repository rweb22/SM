package com.samratmatka.android;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.samratmatka.android.dto.BetItem;
import com.samratmatka.android.dto.GameOption;
import com.samratmatka.android.dto.GameType;
import com.samratmatka.android.dto.MarketDto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SpMotor extends AppCompatActivity {
    ArrayList<String> singlePatti = new ArrayList<>();
    ArrayList<String> doublePatti = new ArrayList<>();
    ArrayList<String> triplePatti = new ArrayList<>();
    ArrayList<ArrayList<String>> families = new ArrayList<>();
    HashMap<String, ArrayList<String>> cycles = new HashMap<>();
    private EditText number;
    private EditText amount;
    private latobold add;
    private RecyclerView recyclerview;
    private EditText totalAmount;
    private latobold submit;
    String value = "";
    SharedPreferences prefs;
    ArrayList<String> numbers = new ArrayList<>();
    GameOption game;
    MarketDto market;
    ViewDialog progressDialog;
    String url;
    int total = 0;
    ArrayList<String> fillNumbers = new ArrayList<>();
    ArrayList<String> fillAmounts = new ArrayList<>();
    ArrayList<GameType> fillGameTypes = new ArrayList<>();
    private CheckBox singlePanna;
    private CheckBox doublePanna;
    private CheckBox triplePanna;
    private LinearLayout spDpTpPanel;
    private EditText firstDigit;
    private EditText secondDigit;
    private EditText thirdDigit;
    private EditText choiceAmount;
    private LinearLayout choicePanna;
    private LinearLayout enterPanel;
    private Spinner oddEven;
    private Spinner redBracket;
    private latobold title;
    private latonormal sessionText;
    private CheckBox spCheckbox;
    private CheckBox dpCheckbox;
    private CheckBox tpCheckbox;
    String session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_motor);
        initViews();
        url = constant.prefix2 + getString(R.string.bet);

        getCycles();

        singlePatti = singlepatti();
        doublePatti = doublepatti();
        triplePatti = triplepatti();

        prefs = getSharedPreferences(constant.prefs, MODE_PRIVATE);
        game = GameOption.valueOf(getIntent().getStringExtra("game"));
        market = (MarketDto) getIntent().getSerializableExtra("market");
        numbers = getIntent().getStringArrayListExtra("digits");
        session = getIntent().getStringExtra("session");

        title.setText(game.getDisplayName());
        sessionText.setText(getMarketName());

        if (game.equals(GameOption.GROUP_JODI)) {
            JodiFamily();
        } else {
            family();
        }

        switch (game) {
            case SP_DP_TP:
                spDpTpPanel.setVisibility(View.VISIBLE);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (amount.getText().toString().isEmpty() || Integer.parseInt(amount.getText().toString()) < constant.min_single) {
                            amount.setError("Enter amount between " + constant.min_single + " - " + constant.max_single);
                        }

                        if (singlePanna.isChecked()) {
                            for (int a = 0; a < singlePatti.size(); a++) {

                                int numberx = 0;
                                String comapre = "";

                                for (int i = 0; i < singlePatti.get(a).length(); i++) {
                                    numberx += Integer.parseInt(String.valueOf(singlePatti.get(a).charAt(i)));
                                }

                                if (numberx > 9) {
                                    comapre = String.valueOf(String.valueOf(numberx).charAt(1));
                                } else {
                                    comapre = String.valueOf(numberx);
                                }

                                if (comapre.equals(number.getText().toString())) {
                                    fillNumbers.add(singlePatti.get(a));
                                    fillAmounts.add(amount.getText().toString());
                                    fillGameTypes.add(GameType.SP);
                                }
                            }
                        }

                        if (doublePanna.isChecked()) {
                            for (int a = 0; a < doublePatti.size(); a++) {

                                int numberx = 0;
                                String comapre = "";

                                for (int i = 0; i < doublePatti.get(a).length(); i++) {
                                    numberx += Integer.parseInt(String.valueOf(doublePatti.get(a).charAt(i)));
                                }

                                if (numberx > 9) {
                                    comapre = String.valueOf(String.valueOf(numberx).charAt(1));
                                } else {
                                    comapre = String.valueOf(numberx);
                                }

                                if (comapre.equals(number.getText().toString())) {
                                    fillNumbers.add(doublePatti.get(a));
                                    fillAmounts.add(amount.getText().toString());
                                    fillGameTypes.add(GameType.DP);
                                }
                            }
                        }

                        if (triplePanna.isChecked()) {
                            for (int a = 0; a < triplePatti.size(); a++) {

                                int numberx = 0;
                                String comapre = "";

                                for (int i = 0; i < triplePatti.get(a).length(); i++) {
                                    numberx += Integer.parseInt(String.valueOf(triplePatti.get(a).charAt(i)));
                                }

                                if (numberx > 9) {
                                    comapre = String.valueOf(String.valueOf(numberx).charAt(1));
                                } else {
                                    comapre = String.valueOf(numberx);
                                }

                                if (comapre.equals(number.getText().toString())) {
                                    fillNumbers.add(triplePatti.get(a));
                                    fillAmounts.add(amount.getText().toString());
                                    fillGameTypes.add(GameType.TP);
                                }
                            }
                        }

                        AdapterSingleGames rc = new AdapterSingleGames(SpMotor.this, fillNumbers, fillAmounts, fillGameTypes);
                        recyclerview.setLayoutManager(new GridLayoutManager(SpMotor.this, 1));
                        recyclerview.setAdapter(rc);
                        number.setText(value);

                        if (!amount.getText().toString().isEmpty()) {
                            totalAmount.setText("" + (Integer.parseInt(amount.getText().toString()) * (fillAmounts.size())));
                        }
                    }
                });
                break;
            case PANEL_GROUP:
                family();
                break;
            case CHOICE_PANA:
                enterPanel.setVisibility(View.GONE);
                choicePanna.setVisibility(View.VISIBLE);

                break;
            case ODD_EVEN: {

                ArrayList<String> typeof = new ArrayList<>();

                typeof.add("Odd");
                typeof.add("Even");

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SpMotor.this, R.layout.simple_list_item_2, typeof);
                oddEven.setAdapter(arrayAdapter);
                number.setVisibility(View.GONE);
                oddEven.setVisibility(View.VISIBLE);

                break;
            }
            case RED_BRACKET: {

                ArrayList<String> typeof = new ArrayList<>();

                typeof.add("Half Bracket");
                typeof.add("Full Bracket");

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SpMotor.this, R.layout.simple_list_item_2, typeof);
                redBracket.setAdapter(arrayAdapter);
                number.setVisibility(View.GONE);
                redBracket.setVisibility(View.VISIBLE);

                break;
            }
        }

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String num = intent.getStringExtra("number");
                System.out.println("Number :" + num);
                System.out.println(fillNumbers.toString() + " - " + fillAmounts.toString() + " - " + fillGameTypes.toString());

                if (fillNumbers.isEmpty()) {
                    return;
                }
                if (num != null) {
                    fillAmounts.remove(Integer.parseInt(num));
                    fillNumbers.remove(Integer.parseInt(num));
                    fillGameTypes.remove(Integer.parseInt(num));
                }

                AdapterSingleGames rc = new AdapterSingleGames(SpMotor.this, fillNumbers, fillAmounts, fillGameTypes);
                recyclerview.setLayoutManager(new GridLayoutManager(SpMotor.this, 1));
                recyclerview.setAdapter(rc);
                rc.notifyDataSetChanged();


                total = 0;
                for (int a = 0; a < fillAmounts.size(); a++) {
                    total = total + Integer.parseInt(fillAmounts.get(a));
                }
                totalAmount.setText(total + "");
            }
        };

        IntentFilter intentFilter = new IntentFilter("android.intent.action.MAIN");
        registerReceiver(mReceiver, intentFilter);

        if (!game.equals(GameOption.SP_DP_TP)){
            add.setOnClickListener(v -> {
                if (!game.equals(GameOption.CHOICE_PANA) && (amount.getText().toString().isEmpty() || Integer.parseInt(amount.getText().toString()) < constant.min_single)) {
                    amount.setError("Enter amount between " + constant.min_single + " - " + constant.max_single);
                } else {
                    switch (game) {
                        case GROUP_JODI:
                            if (number.getText().length() != 2) {
                                number.setError("Enter a valid Jodi");
                            } else {
                                characterCountFamily(number.getText().toString());
                            }
                            break;
                        case PANEL_GROUP:
                            characterCountFamily(number.getText().toString());
                            break;
                        case TWO_D_PANEL:
                            characterCountCycle(number.getText().toString());
                            break;
                        case CHOICE_PANA:
                            if (choiceAmount.getText().toString().isEmpty() || Integer.parseInt(choiceAmount.getText().toString()) < constant.min_single) {
                                choiceAmount.setError("Enter amount between " + constant.min_single + " - " + constant.max_single);
                                return;
                            }

                            String getPan = "";
                            if (!firstDigit.getText().toString().isEmpty()) {
                                getPan = firstDigit.getText().toString();
                            } else {
                                getPan = "*";
                            }
                            Log.e("getPan",getPan);
                            if (!secondDigit.getText().toString().isEmpty()) {
                                getPan = getPan+secondDigit.getText().toString();
                            } else {
                                getPan = getPan+"*";
                            }
                            Log.e("getPan",getPan);
                            if (!thirdDigit.getText().toString().isEmpty()) {
                                getPan = getPan+thirdDigit.getText().toString();
                            } else {
                                getPan = getPan+"*";
                            }
                            Log.e("getPan",getPan);
                            Log.e("getPan",getPan);
                            if (!getPan.equals("***")) {
                                characterCountChoice(getPan);
                            }
                            break;
                        case ODD_EVEN:
                            oddEven();
                            break;
                        case SP_MOTOR:
                        case DP_MOTOR:
                            if (number.getText().toString().length() < 3) {
                                number.setError("Number should be least 3 digit long");
                            }
                            characterCount(number.getText().toString());
                            break;
                        case RED_BRACKET:
                            red_bracket();
                            break;
                        case JODI_MOTOR:
                            characterCountJodiCrossingFamily(number.getText().toString());
                            break;
                        default:
                            characterCount(number.getText().toString());
                            break;
                    }
                }
            });
        }

        submit.setOnClickListener(v -> {

//            fillGameTypes.clear();
//
//            for (int a = 0; a < fillNumbers.size(); a++) {
//                String numm = fillNumbers.get(a);
//                if (numm.length() == 1) {
//                    fillGameTypes.add("SINGLE");
//                } else if (numm.length() == 2) {
//                    fillGameTypes.add("JODI");
//                } else if (numm.length() == 3) {
//                    if (singlePatti.contains(numm)) {
//                        fillGameTypes.add("SINGLE_PANA");
//                    } else if (doublePatti.contains(numm)) {
//                        fillGameTypes.add("DOUBLE_PANA");
//                    } else if (triplePatti.contains(numm)) {
//                        fillGameTypes.add("TRIPLE_PANA");
//                    }
//                }
//
//            }

            if (fillNumbers.size() > 0) {
                if (total <= Integer.parseInt(prefs.getString("wallet", "0"))) {
                    apicall();
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SpMotor.this);
                    builder1.setMessage(getString(R.string.insufficient_balance));
                    builder1.setCancelable(true);
                    builder1.setNegativeButton(
                            "Close",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }

        });

    }

    private String getMarketName() {
        if (session == null || session.isEmpty()) {
            return market.getName();
        } else {
            return market.getName() + " " + session.substring(0, 1).toUpperCase() + session.substring(1).toLowerCase();
        }
    }

    public void oddEven() {
        if (oddEven.getSelectedItem().toString().equals("Odd")) {
            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("1");
            fillGameTypes.add(GameType.SINGLE);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("3");
            fillGameTypes.add(GameType.SINGLE);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("5");
            fillGameTypes.add(GameType.SINGLE);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("7");
            fillGameTypes.add(GameType.SINGLE);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("9");
            fillGameTypes.add(GameType.SINGLE);
        } else {
            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("0");
            fillGameTypes.add(GameType.SINGLE);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("2");
            fillGameTypes.add(GameType.SINGLE);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("4");
            fillGameTypes.add(GameType.SINGLE);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("6");
            fillGameTypes.add(GameType.SINGLE);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("8");
            fillGameTypes.add(GameType.SINGLE);
        }

        AdapterSingleGames rc = new AdapterSingleGames(SpMotor.this, fillNumbers, fillAmounts, fillGameTypes);
        recyclerview.setLayoutManager(new GridLayoutManager(SpMotor.this, 1));
        recyclerview.setAdapter(rc);
        number.setText(value);

        if (!amount.getText().toString().isEmpty()) {
            totalAmount.setText("" + (Integer.parseInt(amount.getText().toString()) * (fillAmounts.size())));
        }
    }

    public void red_bracket() {
        if (redBracket.getSelectedItem().toString().equals("Half Bracket")) {
            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("05");
            fillGameTypes.add(GameType.JODI);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("16");
            fillGameTypes.add(GameType.JODI);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("27");
            fillGameTypes.add(GameType.JODI);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("38");
            fillGameTypes.add(GameType.JODI);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("49");
            fillGameTypes.add(GameType.JODI);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("50");
            fillGameTypes.add(GameType.JODI);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("61");
            fillGameTypes.add(GameType.JODI);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("72");
            fillGameTypes.add(GameType.JODI);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("83");
            fillGameTypes.add(GameType.JODI);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("94");
            fillGameTypes.add(GameType.JODI);
        } else {
            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("00");
            fillGameTypes.add(GameType.JODI);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("11");
            fillGameTypes.add(GameType.JODI);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("22");
            fillGameTypes.add(GameType.JODI);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("33");
            fillGameTypes.add(GameType.JODI);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("44");
            fillGameTypes.add(GameType.JODI);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("55");
            fillGameTypes.add(GameType.JODI);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("66");
            fillGameTypes.add(GameType.JODI);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("77");
            fillGameTypes.add(GameType.JODI);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("88");
            fillGameTypes.add(GameType.JODI);

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add("99");
            fillGameTypes.add(GameType.JODI);

        }
        AdapterSingleGames rc2 = new AdapterSingleGames(SpMotor.this, fillNumbers, fillAmounts, fillGameTypes);
        recyclerview.setLayoutManager(new GridLayoutManager(SpMotor.this, 1));
        recyclerview.setAdapter(rc2);
        number.setText(value);

        if (!amount.getText().toString().isEmpty()) {
            totalAmount.setText("" + (Integer.parseInt(amount.getText().toString()) * (fillAmounts.size())));
        }
    }


    public void characterCountCycle(String inputString) {
        if (cycles.get(inputString) != null) {
            for (int ab = 0; ab < cycles.get(inputString).size(); ab++) {
                String number = cycles.get(inputString).get(ab);

                fillAmounts.add(amount.getText().toString());
                fillNumbers.add(number);
                if (singlePatti.contains(number)) {
                    fillGameTypes.add(GameType.SP);
                } else if (doublePatti.contains(number)) {
                    fillGameTypes.add(GameType.DP);
                } else if (triplePatti.contains(number)) {
                    fillGameTypes.add(GameType.TP);
                }
            }
        }


        AdapterSingleGames rc = new AdapterSingleGames(SpMotor.this, fillNumbers, fillAmounts, fillGameTypes);
        recyclerview.setLayoutManager(new GridLayoutManager(SpMotor.this, 1));
        recyclerview.setAdapter(rc);
        number.setText(value);

        if (!amount.getText().toString().isEmpty()) {
            totalAmount.setText("" + (Integer.parseInt(amount.getText().toString()) * (fillAmounts.size())));
        }
    }


    public void characterCountChoice(String inputString) {

        String firstDigit, SecondDigit, thirdDigit;

        if (inputString.length() == 1){
            inputString += inputString+"**";
        } else if (inputString.length() == 2){
            inputString += inputString+"*";
        }

        firstDigit = inputString.substring(0, 1);
        SecondDigit = inputString.substring(1, 2);
        thirdDigit = inputString.substring(2, 3);

        Log.e("firstDigit", firstDigit);
        Log.e("SecondDigit", SecondDigit);
        Log.e("thirdDigit", thirdDigit);

        if (spCheckbox.isChecked()) {
            for (int a = 0; a < singlePatti.size(); a++) {

                if (!firstDigit.equals("*") && !singlePatti.get(a).substring(0, 1).equals(firstDigit)) {
                    Log.e("spchec", firstDigit + "-" + singlePatti.get(a).substring(0, 1));
                    continue;
                }
                if (!SecondDigit.equals("*") && !singlePatti.get(a).substring(1, 2).equals(SecondDigit)) {
                    Log.e("spchec2", SecondDigit + "-" + singlePatti.get(a).substring(1, 2));
                    continue;
                }
                if (!thirdDigit.equals("*") && !singlePatti.get(a).substring(2, 3).equals(thirdDigit)) {
                    Log.e("spchec3", thirdDigit + "-" + singlePatti.get(a).substring(2, 3));
                    continue;
                }

                fillNumbers.add(singlePatti.get(a));
                fillAmounts.add(choiceAmount.getText().toString());
                fillGameTypes.add(GameType.SP);
            }
        }

        if (dpCheckbox.isChecked()) {
            for (int a = 0; a < doublePatti.size(); a++) {

                if (!firstDigit.equals("*") && !doublePatti.get(a).substring(0, 1).equals(firstDigit)) {
                    continue;
                }
                if (!SecondDigit.equals("*") && !doublePatti.get(a).substring(1, 2).equals(SecondDigit)) {
                    continue;
                }
                if (!thirdDigit.equals("*") && !doublePatti.get(a).substring(2, 3).equals(thirdDigit)) {
                    continue;
                }

                fillNumbers.add(doublePatti.get(a));
                fillAmounts.add(choiceAmount.getText().toString());
                fillGameTypes.add(GameType.DP);
            }
        }

        if (tpCheckbox.isChecked()) {
            for (int a = 0; a < triplePatti.size(); a++) {

                if (!firstDigit.equals("*") && !triplePatti.get(a).substring(0, 1).equals(firstDigit)) {
                    continue;
                }
                if (!SecondDigit.equals("*") && !triplePatti.get(a).substring(1, 2).equals(SecondDigit)) {
                    continue;
                }
                if (!thirdDigit.equals("*") && !triplePatti.get(a).substring(2, 3).equals(thirdDigit)) {
                    continue;
                }

                fillNumbers.add(triplePatti.get(a));
                fillAmounts.add(choiceAmount.getText().toString());
                fillGameTypes.add(GameType.TP);
            }
        }


        AdapterSingleGames rc = new AdapterSingleGames(SpMotor.this, fillNumbers, fillAmounts, fillGameTypes);
        recyclerview.setLayoutManager(new GridLayoutManager(SpMotor.this, 1));
        recyclerview.setAdapter(rc);
        number.setText(value);

        if (!choiceAmount.getText().toString().isEmpty()) {
            totalAmount.setText("" + (Integer.parseInt(choiceAmount.getText().toString()) * (fillAmounts.size())));
        }
    }

    public static Set<String> get2DCombinations(String input) {
        return backtracking("", input, 2) ;
    }

    public static List<String> findTwoDigitCombinations(String input) {
        return input.chars()
                .mapToObj(Character::getNumericValue)
                .flatMap(n1 -> input.chars()
                        .mapToObj(Character::getNumericValue)
                        .map(n2 -> "" + n1 + n2))
                .filter(s -> s.length() == 2)
                .distinct()
                .collect(Collectors.toList());
    }

    public static Set<String> backtracking(String actual, String remaining, int length) {
        if (actual.length() == length) {
            return new HashSet<>(Arrays.asList(actual));
        }

        Set<String> result = new HashSet<>();
        for(int i = 0; i < remaining.length(); i++) {
            result.addAll(backtracking(actual + remaining.charAt(i), remaining.substring(0, i) + remaining.substring(i + 1), length));
        }

        return result;
    }

    public void characterCountJodiCrossingFamily(String inputString) {
        ArrayList<String> list =new ArrayList<>();
        list.addAll(findTwoDigitCombinations(inputString));

        for (int a = 0; a < list.size(); a++) {
            String number = list.get(a);
            fillAmounts.add(amount.getText().toString());
            fillNumbers.add(number);
            fillGameTypes.add(GameType.JODI);
        }

        AdapterSingleGames rc = new AdapterSingleGames(SpMotor.this, fillNumbers, fillAmounts, fillGameTypes);
        recyclerview.setLayoutManager(new GridLayoutManager(SpMotor.this, 1));
        recyclerview.setAdapter(rc);
        number.setText(value);

        if (!amount.getText().toString().isEmpty()) {
            totalAmount.setText("" + (Integer.parseInt(amount.getText().toString()) * (fillAmounts.size())));
        }
    }

    public void characterCountFamily(String inputString) {
        ArrayList<String> list =new ArrayList<>();
        //list.addAll(findTwoDigitCombinations(inputString));
        for(int i=0; i < families.size(); i++) {
            if (families.get(i).contains(inputString)) {
                list.addAll(families.get(i));
            }
        }

        for (int a = 0; a < list.size(); a++) {
            String number = list.get(a);

            GameType gameType;
            if (number.length() > 2) {
                HashSet<Character> charSet = new HashSet<>();
                for(char c : number.toCharArray()) {
                    charSet.add(c);
                }

                if (charSet.size() == 1) {
                    gameType = GameType.TP;
                } else if (charSet.size() == 2) {
                    gameType = GameType.DP;
                } else {
                    gameType = GameType.SP;
                }
            } else {
                gameType = GameType.JODI;
            }

            fillAmounts.add(amount.getText().toString());
            fillNumbers.add(number);
            fillGameTypes.add(gameType);
        }

        AdapterSingleGames rc = new AdapterSingleGames(SpMotor.this, fillNumbers, fillAmounts, fillGameTypes);
        recyclerview.setLayoutManager(new GridLayoutManager(SpMotor.this, 1));
        recyclerview.setAdapter(rc);
        number.setText(value);

        if (!amount.getText().toString().isEmpty()) {
            totalAmount.setText("" + (Integer.parseInt(amount.getText().toString()) * (fillAmounts.size())));
        }
    }


    public void characterCountJodi(String inputString) {
        StringBuilder data = new StringBuilder();
        HashMap<Character, Integer> charCountMap
                = new HashMap<Character, Integer>();
        char[] strArray = inputString.toCharArray();
        for (char c : strArray) {
            if (charCountMap.containsKey(c)) {
                charCountMap.put(c, charCountMap.get(c) + 1);
            } else {
                charCountMap.put(c, 1);
            }
        }


        for (Map.Entry entry : charCountMap.entrySet()) {
            data.append(entry.getKey().toString());
        }

        value = data.toString();

        for (int a = 0; a < value.length(); a++) {

            for (int i = 0; i < value.length(); i++) {
                String nd = value.charAt(a) + "" + value.charAt(i) + "";

                fillAmounts.add(amount.getText().toString());
                fillNumbers.add(nd);
                fillGameTypes.add(GameType.JODI);
            }
        }

        AdapterSingleGames rc = new AdapterSingleGames(SpMotor.this, fillNumbers, fillAmounts, fillGameTypes);
        recyclerview.setLayoutManager(new GridLayoutManager(SpMotor.this, 1));
        recyclerview.setAdapter(rc);
        number.setText(value);

        if (!amount.getText().toString().isEmpty()) {
            totalAmount.setText("" + (Integer.parseInt(amount.getText().toString()) * (fillAmounts.size())));
        }
    }

    public void characterCount(String inputString) {
        StringBuilder data = new StringBuilder();
        HashMap<Character, Integer> charCountMap
                = new HashMap<Character, Integer>();
        char[] strArray = inputString.toCharArray();
        for (char c : strArray) {
            if (charCountMap.containsKey(c)) {
                charCountMap.put(c, charCountMap.get(c) + 1);
            } else {
                charCountMap.put(c, 1);
            }
        }


        for (Map.Entry entry : charCountMap.entrySet()) {
            data.append(entry.getKey().toString());
        }

        value = data.toString();

        for (int a = 0; a < value.length(); a++) {
            for (int i = 0; i < value.length(); i++) {
                for (int ia = 0; ia < value.length(); ia++) {
                    String nd = value.charAt(a) + "" + value.charAt(i) + "" + value.charAt(ia) + "";
                    Log.e("nd", nd);
                    if (numbers.contains(nd)) {
                        fillAmounts.add(amount.getText().toString());
                        fillNumbers.add(nd);
                        fillGameTypes.add(GameOption.toGameType(game));
                    }
                }
            }
        }

        AdapterSingleGames rc = new AdapterSingleGames(SpMotor.this, fillNumbers, fillAmounts, fillGameTypes);
        recyclerview.setLayoutManager(new GridLayoutManager(SpMotor.this, 1));
        recyclerview.setAdapter(rc);
        number.setText(value);

        if (!amount.getText().toString().isEmpty()) {
            totalAmount.setText("" + (Integer.parseInt(amount.getText().toString()) * (fillAmounts.size())));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void apicall() {

        progressDialog = new ViewDialog(SpMotor.this);
        progressDialog.showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String response = null;

        final StringRequest postRequest = new MyStringRequest(getSharedPreferences(constant.prefs, MODE_PRIVATE), Request.Method.POST, url,
                response1 -> {
                    progressDialog.hideDialog();
                    try {
                        JSONObject jsonObject1 = new JSONObject(response1);

                        if (jsonObject1.getString("active").equals("0")) {
                            Toast.makeText(SpMotor.this, "Your account temporarily disabled by admin", Toast.LENGTH_SHORT).show();

                            getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();
                            Intent in = new Intent(getApplicationContext(), signup.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                            finish();
                        }

//                        if (!jsonObject1.getString("session").equals(getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null))) {
//                            Toast.makeText(SpMotor.this, "Session expired ! Please login again", Toast.LENGTH_SHORT).show();
//
//                            getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();
//                            Intent in = new Intent(getApplicationContext(), signup.class);
//                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(in);
//                            finish();
//                        }

                        if (jsonObject1.getString("success").equalsIgnoreCase("1")) {
                            fillNumbers.clear();
                            fillAmounts.clear();
                            fillGameTypes.clear();

                            AlertDialog.Builder builder1 = new AlertDialog.Builder(SpMotor.this);
                            LayoutInflater factory = LayoutInflater.from(SpMotor.this);
                            View v = factory.inflate(R.layout.msg_dialog, null);

                            TextView close = v.findViewById(R.id.close);
                            TextView msgView = v.findViewById(R.id.msg);
                            msgView.setText(getString(R.string.bet_placed));

                            builder1.setView(v);
                            builder1.setCancelable(false);
                            AlertDialog alert11 = builder1.create();
                            alert11.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    Intent in = new Intent(SpMotor.this, HomeScreen.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.putExtra("market",constant.market);
                                    in.putExtra("is_open",constant.is_open);
                                    in.putExtra("is_close",constant.is_close);
                                    in.putExtra("market_type",constant.market_type);
                                    //startActivity(in);
                                    SpMotor.this.finish();
                                }
                            });
                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alert11.dismiss();

                                    Intent in = new Intent(SpMotor.this, HomeScreen.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    in.putExtra("market",constant.market);
                                    in.putExtra("is_open",constant.is_open);
                                    in.putExtra("is_close",constant.is_close);
                                    in.putExtra("market_type",constant.market_type);
                                    //startActivity(in);
                                    SpMotor.this.finish();
                                }
                            });
                            alert11.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            alert11.show();

                        } else {
                            Toast.makeText(getApplicationContext(), jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.hideDialog();
                    }
                },
                error -> {
                    error.printStackTrace();
                    progressDialog.hideDialog();
                    Toast.makeText(SpMotor.this, getString(R.string.api_error_msg), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                List<BetItem> betItems = new ArrayList<>();

                for(int i = 0; i < fillNumbers.size(); i++) {
                    BetItem item = new BetItem();
                    item.number1 = fillNumbers.get(i);
                    item.amount = fillAmounts.get(i);
                    item.gameType = fillGameTypes.get(i).getId();

                    betItems.add(item);
                }

                Gson gson = new Gson();
                String betItemsString = gson.toJson(betItems);

                params.put("items", betItemsString);
                params.put("market_id", market.getMarketId());
                params.put("market_name", market.getName());
                params.put("total", totalAmount.getText().toString());
                params.put("game_option", game.getId());
                params.put("game_session", session);
                params.put("mobile", prefs.getString("mobile", null));
                params.put("session", getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("session", null));

                System.out.println(params.toString());
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);
    }


    public ArrayList<String> doublepatti() {
        ArrayList<String> number = new ArrayList<>();
        number.add("100");
        number.add("119");
        number.add("155");
        number.add("227");
        number.add("335");
        number.add("344");
        number.add("399");
        number.add("588");
        number.add("669");
        number.add("200");
        number.add("110");
        number.add("228");
        number.add("255");
        number.add("336");
        number.add("499");
        number.add("660");
        number.add("688");
        number.add("778");
        number.add("300");
        number.add("166");
        number.add("229");
        number.add("337");
        number.add("355");
        number.add("445");
        number.add("599");
        number.add("779");
        number.add("788");
        number.add("400");
        number.add("112");
        number.add("220");
        number.add("266");
        number.add("338");
        number.add("446");
        number.add("455");
        number.add("699");
        number.add("770");
        number.add("500");
        number.add("113");
        number.add("122");
        number.add("177");
        number.add("339");
        number.add("366");
        number.add("447");
        number.add("799");
        number.add("889");
        number.add("600");
        number.add("114");
        number.add("277");
        number.add("330");
        number.add("448");
        number.add("466");
        number.add("556");
        number.add("880");
        number.add("899");
        number.add("700");
        number.add("115");
        number.add("133");
        number.add("188");
        number.add("223");
        number.add("377");
        number.add("449");
        number.add("557");
        number.add("566");
        number.add("800");
        number.add("116");
        number.add("224");
        number.add("233");
        number.add("288");
        number.add("440");
        number.add("477");
        number.add("558");
        number.add("990");
        number.add("900");
        number.add("117");
        number.add("144");
        number.add("199");
        number.add("225");
        number.add("388");
        number.add("559");
        number.add("577");
        number.add("667");
        number.add("550");
        number.add("668");
        number.add("244");
        number.add("299");
        number.add("226");
        number.add("488");
        number.add("677");
        number.add("118");
        number.add("334");

        return number;
    }

    public ArrayList<String> singlepatti() {
        ArrayList<String> number = new ArrayList<>();
        number.add("128");
        number.add("137");
        number.add("146");
        number.add("236");
        number.add("245");
        number.add("290");
        number.add("380");
        number.add("470");
        number.add("489");
        number.add("560");
        number.add("678");
        number.add("579");
        number.add("589");
        number.add("129");
        number.add("138");
        number.add("147");
        number.add("156");
        number.add("237");
        number.add("246");
        number.add("345");
        number.add("390");
        number.add("480");
        number.add("570");
        number.add("679");
        number.add("120");
        number.add("139");
        number.add("148");
        number.add("157");
        number.add("238");
        number.add("247");
        number.add("256");
        number.add("346");
        number.add("490");
        number.add("580");
        number.add("670");
        number.add("689");
        number.add("130");
        number.add("149");
        number.add("158");
        number.add("167");
        number.add("239");
        number.add("248");
        number.add("257");
        number.add("347");
        number.add("356");
        number.add("590");
        number.add("680");
        number.add("789");
        number.add("140");
        number.add("159");
        number.add("168");
        number.add("230");
        number.add("249");
        number.add("258");
        number.add("267");
        number.add("348");
        number.add("357");
        number.add("456");
        number.add("690");
        number.add("780");
        number.add("123");
        number.add("150");
        number.add("169");
        number.add("178");
        number.add("240");
        number.add("259");
        number.add("268");
        number.add("349");
        number.add("358");
        number.add("457");
        number.add("367");
        number.add("790");
        number.add("124");
        number.add("160");
        number.add("179");
        number.add("250");
        number.add("269");
        number.add("278");
        number.add("340");
        number.add("359");
        number.add("368");
        number.add("458");
        number.add("467");
        number.add("890");
        number.add("125");
        number.add("134");
        number.add("170");
        number.add("189");
        number.add("260");
        number.add("279");
        number.add("350");
        number.add("369");
        number.add("378");
        number.add("459");
        number.add("567");
        number.add("468");
        number.add("126");
        number.add("135");
        number.add("180");
        number.add("234");
        number.add("270");
        number.add("289");
        number.add("360");
        number.add("379");
        number.add("450");
        number.add("469");
        number.add("478");
        number.add("568");
        number.add("127");
        number.add("136");
        number.add("145");
        number.add("190");
        number.add("235");
        number.add("280");
        number.add("370");
        number.add("479");
        number.add("460");
        number.add("569");
        number.add("389");
        number.add("578");

        return number;
    }


    public ArrayList<String> triplepatti() {

        ArrayList<String> number = new ArrayList<>();
        number.add("000");
        number.add("111");
        number.add("222");
        number.add("333");
        number.add("444");
        number.add("555");
        number.add("666");
        number.add("777");
        number.add("888");
        number.add("999");

        return number;
    }


    public void JodiFamily() {
        ArrayList<String> family = new ArrayList<>();
        family.add("12");
        family.add("17");
        family.add("21");
        family.add("26");
        family.add("62");
        family.add("67");
        family.add("71");
        family.add("76");
        families.add(family);

        ArrayList<String> family2 = new ArrayList<>();
        family2.addAll(Arrays.asList("13 - 18 - 31 - 36 - 63 - 68 - 81 - 86".split(" - ")));
        families.add(family2);

        ArrayList<String> family3 = new ArrayList<>();
        family3.addAll(Arrays.asList("14 - 19 - 41 - 46 - 64 - 69 - 91 - 96".split(" - ")));
        families.add(family3);

        ArrayList<String> family4 = new ArrayList<>();
        family4.addAll(Arrays.asList("01 - 06 - 10 - 15 - 51 - 56 - 60 - 65".split(" - ")));
        families.add(family4);

        ArrayList<String> family5 = new ArrayList<>();
        family5.addAll(Arrays.asList("23 - 28 - 32 - 37 - 73 - 78 - 82 - 87".split(" - ")));
        families.add(family5);

        ArrayList<String> family6 = new ArrayList<>();
        family6.addAll(Arrays.asList("24 - 29 - 42 - 47 - 74 - 79 - 92 - 97".split(" - ")));
        families.add(family6);

        ArrayList<String> family7 = new ArrayList<>();
        family7.addAll(Arrays.asList("02 - 07 - 20 - 25 - 52 - 57 - 70 - 75".split(" - ")));
        families.add(family7);

        ArrayList<String> family8 = new ArrayList<>();
        family8.addAll(Arrays.asList("34 - 39 - 43 - 48 - 84 - 89 - 93 - 98".split(" - ")));
        families.add(family8);

        ArrayList<String> family9 = new ArrayList<>();
        family9.addAll(Arrays.asList("03 - 08 - 30 - 35 - 53 - 58 - 80 - 85".split(" - ")));
        families.add(family9);

        ArrayList<String> family10 = new ArrayList<>();
        family10.addAll(Arrays.asList("04 - 09 - 40 - 45 - 54 - 59 - 90 - 95".split(" - ")));
        families.add(family10);
    }


    public void family() {
        ArrayList<String> family = new ArrayList<>();
        family.add("111");
        family.add("116");
        family.add("166");
        family.add("666");
        families.add(family);
        ArrayList<String> family2 = new ArrayList<>();

        family2.add("112");
        family2.add("117");
        family2.add("126");
        family2.add("167");
        family2.add("266");
        family2.add("667");
        families.add(family2);
        ArrayList<String> family3 = new ArrayList<>();

        family3.add("113");
        family3.add("118");
        family3.add("136");
        family3.add("168");
        family3.add("366");
        family3.add("668");
        families.add(family3);
        ArrayList<String> family4 = new ArrayList<>();

        family4.add("114");
        family4.add("119");
        family4.add("146");
        family4.add("169");
        family4.add("466");
        family4.add("669");
        families.add(family4);
        ArrayList<String> family5 = new ArrayList<>();

        family5.add("110");
        family5.add("115");
        family5.add("156");
        family5.add("160");
        family5.add("566");
        family5.add("660");
        families.add(family5);
        ArrayList<String> family6 = new ArrayList<>();

        family6.add("122");
        family6.add("127");
        family6.add("177");
        family6.add("226");
        family6.add("267");
        family6.add("677");
        families.add(family6);
        ArrayList<String> family7 = new ArrayList<>();


        family7.add("123");
        family7.add("128");
        family7.add("137");
        family7.add("178");
        family7.add("236");
        family7.add("268");
        family7.add("367");
        family7.add("678");
        families.add(family7);
        ArrayList<String> family8 = new ArrayList<>();


        family8.add("124");
        family8.add("129");
        family8.add("147");
        family8.add("179");
        family8.add("246");
        family8.add("269");
        family8.add("467");
        family8.add("679");
        families.add(family8);
        ArrayList<String> family9 = new ArrayList<>();


        family9.add("120");
        family9.add("125");
        family9.add("157");
        family9.add("170");
        family9.add("256");
        family9.add("260");
        family9.add("567");
        family9.add("670");
        families.add(family9);
        ArrayList<String> family10 = new ArrayList<>();


        family10.add("133");
        family10.add("138");
        family10.add("188");
        family10.add("336");
        family10.add("368");
        family10.add("688");
        families.add(family10);
        ArrayList<String> family11 = new ArrayList<>();


        family11.add("134");
        family11.add("139");
        family11.add("148");
        family11.add("189");
        family11.add("346");
        family11.add("369");
        family11.add("468");
        family11.add("689");
        families.add(family11);
        ArrayList<String> family12 = new ArrayList<>();


        family12.add("130");
        family12.add("135");
        family12.add("158");
        family12.add("180");
        family12.add("356");
        family12.add("360");
        family12.add("568");
        family12.add("680");
        families.add(family12);
        ArrayList<String> family13 = new ArrayList<>();


        family13.add("144");
        family13.add("149");
        family13.add("199");
        family13.add("446");
        family13.add("469");
        family13.add("699");
        families.add(family13);
        ArrayList<String> family14 = new ArrayList<>();


        family14.add("140");
        family14.add("145");
        family14.add("159");
        family14.add("190");
        family14.add("456");
        family14.add("460");
        family14.add("569");
        family14.add("690");
        families.add(family14);
        ArrayList<String> family15 = new ArrayList<>();


        family15.add("100");
        family15.add("150");
        family15.add("155");
        family15.add("556");
        family15.add("560");
        family15.add("600");
        families.add(family15);
        ArrayList<String> family16 = new ArrayList<>();


        family16.add("222");
        family16.add("227");
        family16.add("277");
        family16.add("777");
        families.add(family16);
        ArrayList<String> family17 = new ArrayList<>();


        family17.add("223");
        family17.add("228");
        family17.add("237");
        family17.add("278");
        family17.add("377");
        family17.add("778");
        families.add(family17);
        ArrayList<String> family18 = new ArrayList<>();


        family18.add("224");
        family18.add("229");
        family18.add("247");
        family18.add("279");
        family18.add("477");
        family18.add("779");
        families.add(family18);
        ArrayList<String> family19 = new ArrayList<>();


        family19.add("220");
        family19.add("225");
        family19.add("257");
        family19.add("270");
        family19.add("577");
        family19.add("770");
        families.add(family19);
        ArrayList<String> family20 = new ArrayList<>();


        family20.add("233");
        family20.add("238");
        family20.add("288");
        family20.add("337");
        family20.add("378");
        family20.add("788");
        families.add(family20);
        ArrayList<String> family21 = new ArrayList<>();


        family21.add("234");
        family21.add("239");
        family21.add("248");
        family21.add("289");
        family21.add("347");
        family21.add("379");
        family21.add("478");
        family21.add("789");
        families.add(family21);
        ArrayList<String> family211 = new ArrayList<>();


        family211.add("230");
        family211.add("235");
        family211.add("258");
        family211.add("280");
        family211.add("357");
        family211.add("370");
        family211.add("578");
        family211.add("780");
        families.add(family211);
        ArrayList<String> family22 = new ArrayList<>();


        family22.add("244");
        family22.add("249");
        family22.add("299");
        family22.add("447");
        family22.add("479");
        family22.add("799");
        families.add(family22);
        ArrayList<String> family23 = new ArrayList<>();


        family23.add("240");
        family23.add("245");
        family23.add("259");
        family23.add("290");
        family23.add("457");
        family23.add("470");
        family23.add("579");
        family23.add("790");
        families.add(family23);
        ArrayList<String> family24 = new ArrayList<>();


        family24.add("200");
        family24.add("250");
        family24.add("255");
        family24.add("557");
        family24.add("570");
        family24.add("700");
        families.add(family24);
        ArrayList<String> family25 = new ArrayList<>();


        family25.add("333");
        family25.add("338");
        family25.add("388");
        family25.add("888");
        families.add(family25);
        ArrayList<String> family26 = new ArrayList<>();


        family26.add("334");
        family26.add("339");
        family26.add("348");
        family26.add("389");
        family26.add("488");
        family26.add("889");
        families.add(family26);
        ArrayList<String> family27 = new ArrayList<>();


        family27.add("330");
        family27.add("335");
        family27.add("358");
        family27.add("380");
        family27.add("588");
        family27.add("880");
        families.add(family27);
        ArrayList<String> family28 = new ArrayList<>();


        family28.add("344");
        family28.add("349");
        family28.add("399");
        family28.add("448");
        family28.add("489");
        family28.add("899");
        families.add(family28);
        ArrayList<String> family29 = new ArrayList<>();


        family29.add("340");
        family29.add("345");
        family29.add("359");
        family29.add("390");
        family29.add("458");
        family29.add("480");
        family29.add("589");
        family29.add("890");
        families.add(family29);
        ArrayList<String> family30 = new ArrayList<>();


        family30.add("300");
        family30.add("350");
        family30.add("355");
        family30.add("558");
        family30.add("580");
        family30.add("800");
        families.add(family30);
        ArrayList<String> family31 = new ArrayList<>();


        family31.add("444");
        family31.add("449");
        family31.add("499");
        family31.add("999");
        families.add(family31);
        ArrayList<String> family32 = new ArrayList<>();


        family32.add("440");
        family32.add("445");
        family32.add("459");
        family32.add("490");
        family32.add("599");
        family32.add("990");
        families.add(family32);
        ArrayList<String> family33 = new ArrayList<>();


        family33.add("400");
        family33.add("450");
        family33.add("455");
        family33.add("559");
        family33.add("590");
        family33.add("900");
        families.add(family33);
        ArrayList<String> family34 = new ArrayList<>();


        family34.add("000");
        family34.add("500");
        family34.add("550");
        family34.add("555");
        families.add(family34);

    }


    public void getCycles() {


        cycles.put("00", new ArrayList<String>(Arrays.asList("100, 200, 300, 400, 500, 600, 700, 800, 900, 000".split(", "))));
        cycles.put("10", new ArrayList<String>(Arrays.asList("100, 110, 120, 130, 140, 150, 160, 170, 180, 190".split(", "))));
        cycles.put("11", new ArrayList<String>(Arrays.asList("110, 111, 112, 113, 114, 115, 116, 117, 118, 119".split(", "))));
        cycles.put("12", new ArrayList<String>(Arrays.asList("112, 120, 122, 123, 124, 125, 126, 127, 128, 129".split(", "))));
        cycles.put("13", new ArrayList<String>(Arrays.asList("113, 123, 130, 133, 134, 135, 136, 137, 138, 139".split(", "))));
        cycles.put("14", new ArrayList<String>(Arrays.asList("114, 124, 134, 140, 144, 145, 146, 147, 148, 149".split(", "))));
        cycles.put("15", new ArrayList<String>(Arrays.asList("115, 125, 135, 145, 150, 155, 156, 157, 158, 159".split(", "))));
        cycles.put("16", new ArrayList<String>(Arrays.asList("116, 126, 136, 146, 156, 160, 166, 167, 168, 169".split(", "))));
        cycles.put("17", new ArrayList<String>(Arrays.asList("117, 127, 137, 147, 157, 167, 170, 177, 178, 179".split(", "))));
        cycles.put("18", new ArrayList<String>(Arrays.asList("118, 128, 138, 148, 158, 168, 178, 180, 188, 189".split(", "))));
        cycles.put("19", new ArrayList<String>(Arrays.asList("119, 129, 139, 149, 159, 169, 179, 189, 190, 199".split(", "))));
        cycles.put("20", new ArrayList<String>(Arrays.asList("120, 200, 220, 230, 240, 250, 260, 270, 280, 290".split(", "))));
        cycles.put("22", new ArrayList<String>(Arrays.asList("122, 220, 223, 224, 225, 226, 227, 228, 229, 222".split(", "))));
        cycles.put("23", new ArrayList<String>(Arrays.asList("123, 230, 233, 234, 235, 236, 237, 238, 239, 223".split(", "))));
        cycles.put("24", new ArrayList<String>(Arrays.asList("124, 240, 244, 245, 246, 247, 248, 249, 224, 234".split(", "))));
        cycles.put("25", new ArrayList<String>(Arrays.asList("125, 250, 255, 256, 257, 258, 259, 225, 235, 245".split(", "))));
        cycles.put("26", new ArrayList<String>(Arrays.asList("126, 260, 266, 267, 268, 269, 226, 236, 246, 256".split(", "))));
        cycles.put("27", new ArrayList<String>(Arrays.asList("127, 270, 277, 278, 279, 227, 237, 247, 257, 267".split(", "))));
        cycles.put("28", new ArrayList<String>(Arrays.asList("128, 280, 288, 289, 228, 238, 248, 258, 268, 278".split(", "))));
        cycles.put("29", new ArrayList<String>(Arrays.asList("129, 290, 299, 229, 239, 249, 259, 269, 279, 289".split(", "))));
        cycles.put("30", new ArrayList<String>(Arrays.asList("130, 230, 300, 330, 340, 350, 360, 370, 380, 390".split(", "))));
        cycles.put("34", new ArrayList<String>(Arrays.asList("134, 234, 334, 340, 344, 345, 346, 347, 348, 349".split(", "))));
        cycles.put("35", new ArrayList<String>(Arrays.asList("135, 350, 355, 335, 345, 235, 356, 357, 358, 359".split(", "))));
        cycles.put("36", new ArrayList<String>(Arrays.asList("136, 360, 366, 336, 346, 356, 367, 368, 369, 236".split(", "))));
        cycles.put("37", new ArrayList<String>(Arrays.asList("137, 370, 377, 337, 347, 357, 367, 378, 379, 237".split(", "))));
        cycles.put("38", new ArrayList<String>(Arrays.asList("138, 380, 388, 238, 338, 348, 358, 368, 378, 389".split(", "))));
        cycles.put("39", new ArrayList<String>(Arrays.asList("139, 390, 399, 349, 359, 369, 379, 389, 239, 339".split(", "))));
        cycles.put("40", new ArrayList<String>(Arrays.asList("140, 240, 340, 400, 440, 450, 460, 470, 480, 490".split(", "))));
        cycles.put("44", new ArrayList<String>(Arrays.asList("144, 244, 344, 440, 449, 445, 446, 447, 448, 444".split(", "))));
        cycles.put("45", new ArrayList<String>(Arrays.asList("145, 245, 345, 450, 456, 457, 458, 459, 445, 455".split(", "))));
        cycles.put("46", new ArrayList<String>(Arrays.asList("146, 460, 446, 467, 468, 469, 246, 346, 456, 466".split(", "))));
        cycles.put("47", new ArrayList<String>(Arrays.asList("147, 470, 447, 478, 479, 247, 347, 457, 467, 477".split(", "))));
        cycles.put("48", new ArrayList<String>(Arrays.asList("148, 480, 489, 248, 348, 448, 488, 458, 468, 478".split(", "))));
        cycles.put("49", new ArrayList<String>(Arrays.asList("149, 490, 499, 449, 459, 469, 479, 489, 249, 349".split(", "))));
        cycles.put("50", new ArrayList<String>(Arrays.asList("500, 550, 150, 250, 350, 450, 560, 570, 580, 590".split(", "))));
        cycles.put("55", new ArrayList<String>(Arrays.asList("155, 556, 557, 558, 559, 255, 355, 455, 555, 550".split(", "))));
        cycles.put("56", new ArrayList<String>(Arrays.asList("156, 556, 567, 568, 569, 356, 256, 456, 560, 566".split(", "))));
        cycles.put("57", new ArrayList<String>(Arrays.asList("157, 257, 357, 457, 557, 578, 579, 570, 567, 577".split(", "))));
        cycles.put("58", new ArrayList<String>(Arrays.asList("158, 558, 568, 578, 588, 589, 580, 258, 358, 458".split(", "))));
        cycles.put("59", new ArrayList<String>(Arrays.asList("159, 259, 359, 459, 559, 569, 579, 589, 590, 599".split(", "))));
        cycles.put("60", new ArrayList<String>(Arrays.asList("600, 160, 260, 360, 460, 560, 660, 670, 680, 690".split(", "))));
        cycles.put("66", new ArrayList<String>(Arrays.asList("660, 667, 668, 669, 666, 166, 266, 366, 466, 566".split(", "))));
        cycles.put("67", new ArrayList<String>(Arrays.asList("670, 167, 267, 367, 467, 567, 667, 678, 679, 677".split(", "))));
        cycles.put("68", new ArrayList<String>(Arrays.asList("680, 688, 668, 678, 168, 268, 368, 468, 568, 689".split(", "))));
        cycles.put("69", new ArrayList<String>(Arrays.asList("690, 169, 269, 369, 469, 569, 669, 679, 689, 699".split(", "))));
        cycles.put("70", new ArrayList<String>(Arrays.asList("700, 170, 270, 370, 470, 570, 670, 770, 780, 790".split(", "))));
        cycles.put("77", new ArrayList<String>(Arrays.asList("770, 177, 277, 377, 477, 577, 677, 778, 779, 777".split(", "))));
        cycles.put("78", new ArrayList<String>(Arrays.asList("178, 278, 378, 478, 578, 678, 778, 788, 789, 780".split(", "))));
        cycles.put("79", new ArrayList<String>(Arrays.asList("179, 279, 379, 479, 579, 679, 779, 789, 799, 790".split(", "))));
        cycles.put("80", new ArrayList<String>(Arrays.asList("180, 280, 380, 480, 580, 680, 780, 880, 800, 890".split(", "))));
        cycles.put("88", new ArrayList<String>(Arrays.asList("188, 288, 388, 488, 588, 688, 788, 889, 888, 880".split(", "))));
        cycles.put("89", new ArrayList<String>(Arrays.asList("189, 289, 389, 489, 589, 689, 789, 889, 890, 899".split(", "))));
        cycles.put("90", new ArrayList<String>(Arrays.asList("900, 190, 290, 390, 490, 590, 690, 790, 890, 900".split(", "))));
        cycles.put("99", new ArrayList<String>(Arrays.asList("199, 299, 399, 499, 599, 699, 799, 899, 990, 999".split(", "))));
    }


    private void initViews() {
        RelativeLayout backButton = findViewById(R.id.back);
        number = findViewById(R.id.number);
        amount = findViewById(R.id.amount2);
        add = findViewById(R.id.add);
        recyclerview = findViewById(R.id.recyclerview);
        totalAmount = findViewById(R.id.totalamount);
        submit = findViewById(R.id.submit);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fillNumbers.clear();
                fillAmounts.clear();
                fillGameTypes.clear();
                onBackPressed();
            }
        });
        latonormal balanceHome = findViewById(R.id.balance_home);

        TextView tx = findViewById(R.id.balance_home);
        tx.setText((Integer.parseInt(getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("wallet","0")))+" ₹");

        LinearLayout walletBlock = findViewById(R.id.wallet_block);
        RelativeLayout toolbar = findViewById(R.id.toolbar);
        Spinner type = findViewById(R.id.type);
        singlePanna = findViewById(R.id.single_panna);
        doublePanna = findViewById(R.id.double_panna);
        triplePanna = findViewById(R.id.triple_panna);
        spDpTpPanel = findViewById(R.id.spdptp_panel);
        NestedScrollView scrollView = findViewById(R.id.scrollView);
        firstDigit = findViewById(R.id.first_digit);
        secondDigit = findViewById(R.id.second_digit);
        thirdDigit = findViewById(R.id.third_digit);
        choiceAmount = findViewById(R.id.choice_amount);
        choicePanna = findViewById(R.id.choice_panna);
        enterPanel = findViewById(R.id.enter_panel);
        oddEven = findViewById(R.id.odd_even);
        redBracket = findViewById(R.id.red_bracket);
        title = findViewById(R.id.title);
        sessionText = findViewById(R.id.session);
        spCheckbox = findViewById(R.id.sp_checkbox);
        dpCheckbox = findViewById(R.id.dp_checkbox);
        tpCheckbox = findViewById(R.id.tp_checkbox);
    }
}