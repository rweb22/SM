package com.tripleseven.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import im.crisp.client.Crisp;

public class HomeScreen extends AppCompatActivity {

    private BottomNavigationView bottomBar;
    SharedPreferences preferences;
    ActivityResultLauncher<Intent> lockScreenLauncher;
    private LinearLayout walletBlock;
    static public latobold star;
    static public latonormal games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        initViews();
        preferences = getSharedPreferences(constant.prefs, MODE_PRIVATE);
        bottomBar.setOnNavigationItemSelectedListener(navListener);

        Crisp.configure(getApplicationContext(), "3c5bc8ba-942d-4e26-b07e-ab33356d18f5");

        star.setText("SAMRAT");
        games.setText("MATKA");

        TextView tx = findViewById(R.id.balance_home);
        tx.setText((Integer.parseInt(getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("wallet","0")))+" ₹");

        walletBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, wallet.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        constant.setMenu(HomeScreen.this);

    }

    @Override
    protected void onResume() {
        TextView tx = findViewById(R.id.balance_home);
        tx.setText((Integer.parseInt(getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("wallet","0")))+" ₹");

        checkLock();
        super.onResume();
    }

    private void checkLock() {
//        if (Betplay.getIsLocked()) {
//            if (!preferences.getString("is_pin_asked", "").equals("true") || !preferences.getString("mpin", "").equals("")) {
//                Intent intent = new Intent(this, MPIN.class);
//                lockScreenLauncher.launch(intent);
//                return;
//            }
//        }

        TextView tx = findViewById(R.id.balance_home);
        tx.setText((Integer.parseInt(getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("wallet","0")))+" ₹");
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                    TextView tx = findViewById(R.id.balance_home);
                    tx.setText((Integer.parseInt(getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("wallet","0")))+" ₹");

                    selectedFragment = new DashboardFragment();
                    break;
                case R.id.charts:
                    selectedFragment = new TransactionFragment();
                    break;
                case R.id.wallet:
                    startActivity(new Intent(HomeScreen.this, played.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case R.id.played:
                    startActivity(new Intent(HomeScreen.this, wallet.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;
                case R.id.feed:
                    openWhatsApp(HomeScreen.this);
//                    Intent crispIntent = new Intent(HomeScreen.this, ChatActivity.class);
//                    startActivity(crispIntent);

                   // selectedFragment = new ContactFragment();
                    break;
            }


            if (selectedFragment == null) {
                return false;
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
            return true;
        }
    };

    static public String getWhatsapp(Context context){

        String number = context.getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("whatsapp",null);
        if (number.contains("+91")){
            return  "http://wa.me/"+context.getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("whatsapp",null)+"?text=Hi Admin\nMy Login Mobile No. - "+context.getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile",null);
        } else {
            return  "http://wa.me/+91"+context.getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("whatsapp",null)+"?text=Hi Admin\nMy Login Mobile No. - "+context.getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile",null);
        }

    }


    static private void openWhatsApp(Context context) {
        String url = constant.getWhatsapp(context);

        Uri uri = Uri.parse(url);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(sendIntent);
    }

    private void initViews() {
        TextView tx = findViewById(R.id.balance_home);
        tx.setText((Integer.parseInt(getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("wallet","0")))+" ₹");
        bottomBar = findViewById(R.id.bottom_bar);

        lockScreenLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            checkLock();
                        }
                    }
                });
        walletBlock = findViewById(R.id.wallet_block);
        star = findViewById(R.id.star);
        games = findViewById(R.id.games);
    }
}