package com.tripleseven.android;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class constant {
    public static final String SPACE = " ";
    // DO NOT EDIT
    static String prefs = "codegente";

    ///// CONFIGURATION /////

    // API FOLDER URL
    static String prefix = "https://samratmatka.com/";
    static String prefix2 = "http://10.0.2.2:5001/";

    static String SUCCESS = "success";
    static String ACTIVE = "active";
    static String ONE = "1";
    static String ZERO = "0";

    // APK DOWNLOAD LINK0938
    static String link = "https://samratmatka.com/admin/static/download/samrat.apk";

    // PROJECT ROOT URL
    static String project_root = "http://10.0.2.2:5001/";

    // PROJECT ROOT URL
    static String admin_root = "http://10.0.2.2:5001/admin/";

    // MIN AMOUNT ALLOWED IN TOTAL FOR BETTING
    static int min_total = 5;

    // MAX AMOUNT ALLOWED IN TOTAL FOR BETTING
    static int max_total = 10000;

    // MIN AMOUNT ALLOWED FOR SINGLE BET
    static int min_single = 5;

    // MAX AMOUNT ALLOWED FOR SINGLE BET
    static int max_single = 10000;

    // MIN DEPOSIT AMOUNT THROUGH PAYMENT GATEWAY
    static int min_deposit = 500;

    static public String market = "", is_open = "0", is_close = "0", market_type;

    static public String getWhatsapp(Context context){

        String number = context.getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("whatsapp",null);
        if (number.contains("+91")){
            return  "http://wa.me/"+context.getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("whatsapp",null)+"?text=Hi Admin\nMy Login Mobile No. - "+context.getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile",null);
        } else {
            return  "http://wa.me/+91"+context.getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("whatsapp",null)+"?text=Hi Admin\nMy Login Mobile No. - "+context.getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("mobile",null);
        }

    }

    static public void setMenu(Activity context){

        Typeface face = Typeface.createFromAsset(context.getAssets(), "Oxygen-Bold.ttf");

        PrimaryDrawerItem played = new PrimaryDrawerItem().withName(R.string.my_bids).withIcon(R.drawable.my_bid).withIdentifier(9).withTypeface(face);
        PrimaryDrawerItem cardBidHistory = new PrimaryDrawerItem().withName("Card Bid History").withIcon(R.drawable.my_bid).withIdentifier(911).withTypeface(face);
        PrimaryDrawerItem carResultHistory = new PrimaryDrawerItem().withName("Card Chart").withIcon(R.drawable.my_bid).withIdentifier(711).withTypeface(face);
        PrimaryDrawerItem ledger = new PrimaryDrawerItem().withName(R.string.passbook).withIcon(R.drawable.passbook).withIdentifier(6).withTypeface(face);
        PrimaryDrawerItem deposit = new PrimaryDrawerItem().withName(R.string.funds).withIcon(R.drawable.funds).withIdentifier(4).withTypeface(face);
        PrimaryDrawerItem notice = new PrimaryDrawerItem().withName(R.string.notice_board).withIcon(R.drawable.notice_board).withIdentifier(3).withTypeface(face);
        PrimaryDrawerItem bank = new PrimaryDrawerItem().withName(R.string.add_bank_account).withIcon(R.drawable.notice_board).withIdentifier(32).withTypeface(face);
        PrimaryDrawerItem notifications = new PrimaryDrawerItem().withName("Notifications").withIcon(R.drawable.notification).withIdentifier(31).withTypeface(face);
        PrimaryDrawerItem rate = new PrimaryDrawerItem().withName(R.string.game_rates).withIdentifier(2).withIcon(R.drawable.game_rates).withTypeface(face);
        PrimaryDrawerItem charts = new PrimaryDrawerItem().withName(R.string.market_charts).withIdentifier(24).withIcon(R.drawable.game_rates).withTypeface(face);
        PrimaryDrawerItem stats = new PrimaryDrawerItem().withName(R.string.stats).withIcon(R.drawable.game_rates).withIdentifier(101).withTypeface(face);
        PrimaryDrawerItem howto = new PrimaryDrawerItem().withName(R.string.how_to).withIcon(R.drawable.play_video).withIdentifier(10).withTypeface(face);
        PrimaryDrawerItem earn = new PrimaryDrawerItem().withName(R.string.refer).withIcon(R.drawable.refer_and_earn).withIdentifier(21).withTypeface(face);
        PrimaryDrawerItem settings = new PrimaryDrawerItem().withName(R.string.settings).withIcon(R.drawable.settingss).withIdentifier(72).withTypeface(face);


        final Drawer drawer = new DrawerBuilder()
                .withHeaderDivider(true)
                .withActivity(context)
                .withSliderBackgroundColor(context.getResources().getColor(R.color.background))
                .withTranslucentStatusBar(true)
                .withHeader(R.layout.header)
                .withFooter(R.layout.footer)
                .withActionBarDrawerToggle(false)
                .addDrawerItems(
                        played, ledger, deposit, bank, notice, rate, charts, earn
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem.equals(1)) {
                            context.startActivity(new Intent(context, profile.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(911)) {
                            context.startActivity(new Intent(context, cardBidHistory.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(711)) {
                            context.startActivity(new Intent(context, card_chart_menu.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }

                        if (drawerItem.equals(31)) {
                            context.startActivity(new Intent(context, Notifications.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(24)) {
                            context.startActivity(new Intent(context, chart_menu.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(32)) {
                            context.startActivity(new Intent(context, change_bank.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(72)) {
                            context.startActivity(new Intent(context, settings.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(26)) {
                            context.startActivity(new Intent(context, deposit_history.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(101)) {
                            context.startActivity(new Intent(context, MarketList.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(2)) {
                            context.startActivity(new Intent(context, rate.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(21)) {
                            context.startActivity(new Intent(context, earn.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(3)) {
                            context.startActivity(new Intent(context, notice.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                        if (drawerItem.equals(4)) {
                            context.startActivity(new Intent(context, wallet.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                            if (context.getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("is_gateway", "1").equals("1")) {
//                                context.startActivity(new Intent(context, deposit_money.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                            } else {
//                                openWhatsApp(context);
//                            }
                        }
                        if (drawerItem.equals(41)) {
                            openWhatsApp(context);
                        }
                        if (drawerItem.equals(10)) {
                            context.startActivity(new Intent(context, howot.class));
                        }
                        if (drawerItem.equals(11)) {

                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT,
                                    "Download " + context.getString(R.string.app_name) + " and earn coins at home, Download link - " + constant.link);
                            sendIntent.setType("text/plain");
                            context.startActivity(sendIntent);
                        }
                        if (drawerItem.equals(7)) {
                            context.getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();
                            Intent in = new Intent(context, signup.class);
                            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(in);
                            context.finish();
                        }
                        if (drawerItem.equals(6)) {
                            context.startActivity(new Intent(context, ledger.class));
                        }
                        if (drawerItem.equals(8)) {
                            context.startActivity(new Intent(context, WithdrawHistory.class));
                        }
                        if (drawerItem.equals(9)) {
                            context.startActivity(new Intent(context, played.class));
                        }

                        return false;
                    }
                })
                .build();

        DividerItemDecoration itemDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(context, R.drawable.divider));

        drawer.getRecyclerView().addItemDecoration(itemDecorator);

        TextView mobile = drawer.getHeader().findViewById(R.id.create_pass);
        if(!context.getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("name","").equals("")){


            String str =context.getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("name", "");
            String cap = str.substring(0,1).toUpperCase() + str.substring(1);
            mobile.setText(cap);
        }else{
            mobile.setText("SAMRAT SATTA USER");

        }


        drawer.getFooter().findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Betplay.setIsLocked(true);
                context.getSharedPreferences(constant.prefs, MODE_PRIVATE).edit().clear().apply();
                context.getSharedPreferences(constant.prefs,MODE_PRIVATE).edit().putString("is_pin_asked", "false").apply();
                Intent in = new Intent(context, login.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
                context.finish();
            }
        });


        drawer.getHeader().findViewById(R.id.deposit_money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.getSharedPreferences(constant.prefs, MODE_PRIVATE).getString("is_gateway", "1").equals("1")) {
                    context.startActivity(new Intent(context, wallet.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra("action", "deposit"));
                } else {
                    openWhatsApp(context);
                }
            }
        });

        drawer.getHeader().findViewById(R.id.withdraw_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, withdraw.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        drawer.getHeader().findViewById(R.id.view_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              context.startActivity(new Intent(context, profile.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        drawer.getHeader().findViewById(R.id.r_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, profile.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        context.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen()) {
                    drawer.closeDrawer();
                } else {
                    drawer.openDrawer();
                }
            }
        });

        context.findViewById(R.id.wallet_block).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, wallet.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        TextView tx = context.findViewById(R.id.balance_home);
        tx.setText((Integer.parseInt(context.getSharedPreferences(constant.prefs,MODE_PRIVATE).getString("wallet","0")))+" ₹");

    }


    public static void showMessage(Context context, String msg){

        android.app.AlertDialog.Builder builder1 = new android.app.AlertDialog.Builder(context);
        LayoutInflater factory = LayoutInflater.from(context);
        View v = factory.inflate(R.layout.msg_dialog, null);

        TextView close = v.findViewById(R.id.close);
        TextView msgView = v.findViewById(R.id.msg);
        msgView.setText(msg);

        builder1.setView(v);
        builder1.setCancelable(true);
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

    static private void openWhatsApp(Context context) {
        String url = constant.getWhatsapp(context);

        Uri uri = Uri.parse(url);
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(sendIntent);
    }


}
