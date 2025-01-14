package com.samratmatka.android;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;

import java.io.File;

public class Update extends AppCompatActivity {

    protected latobold update;
    protected latobold cancelButton;
    protected latobold updateLog;
    ProgressDialog pd;

    String link,log;
    String nameOfFile;

    public ProgressDialog waitingDialog;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update);
        initView();

        link = getIntent().getStringExtra("link");
        log = getIntent().getStringExtra("log");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateLog.setText(Html.fromHtml(log, Html.FROM_HTML_MODE_COMPACT));
        } else {
            updateLog.setText(Html.fromHtml(log));
        }

        PRDownloader.initialize(getApplicationContext());

        pd = new ProgressDialog(Update.this);
        pd.setTitle("Downloading Update");
        pd.setCancelable(false);

        String[] permissionArrays = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        }



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("update clicked", "test");
                progressDialog = new ProgressDialog(Update.this);
                progressDialog.setTitle("Starting download");
                progressDialog.setCancelable(false);
                progressDialog.show();

                waitingDialog = new ProgressDialog(Update.this);
                waitingDialog.setCancelable(false);


                String[] filename = link.split("/");
                nameOfFile = filename[filename.length-1];

                String path = getFilesDir() + "/updates/";

                PRDownloader.download(link, path, nameOfFile)
                        .build()
                        .setOnStartOrResumeListener(() -> {
                            progressDialog.dismiss();
                            waitingDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            waitingDialog.setTitle("Updating "+getString(R.string.app_name));
                            waitingDialog.setMessage("Downloading Update");
                            waitingDialog.show();

                        })
                        .setOnProgressListener(progress -> {
                            waitingDialog.setMax(Integer.parseInt(progress.totalBytes/1024+""));
                            waitingDialog.setProgress(Integer.parseInt(progress.currentBytes/1024+""));
                        })
                        .start(new OnDownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                try {
                                    waitingDialog.dismiss();

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        Uri uri = FileProvider.getUriForFile(Update.this, getPackageName() + ".provider",new File( path, nameOfFile));
                                        Intent i=new Intent(Intent.ACTION_VIEW);
                                        i.setDataAndType(uri, "application/vnd.android.package-archive");
                                        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        startActivity(i);
                                    } else {
                                        Uri apkUri = Uri.fromFile(new File( path, nameOfFile));
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                    Toast.makeText(Update.this, "Your device won't allowed us to update app directly please install it from our website", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onError(Error error) {
                                Log.e("isServerError",error.isServerError()+"");
                                Log.e("isConnectionError",error.isConnectionError()+"");
                                error.getConnectionException().printStackTrace();
                                Toast.makeText(Update.this, "Your device won't allowed us to update app directly please install it from our website", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("cancelling the update", "Yes");
                getSharedPreferences(constant.prefs, MODE_PRIVATE).edit()
                        .putLong("last_update_warning", System.currentTimeMillis()).apply();
                Intent in = new Intent(getApplicationContext(), splash.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();
            }
        });
    }

    private void initView() {
        update = findViewById(R.id.update);
        cancelButton = findViewById(R.id.cancelButton);
        updateLog = findViewById(R.id.updatelog);
    }
}
