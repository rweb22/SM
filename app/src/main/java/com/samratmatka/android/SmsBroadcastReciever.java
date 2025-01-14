package com.samratmatka.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

public class SmsBroadcastReciever extends BroadcastReceiver {

    public SmsBroadcastRecieverListner smsBroadcastRecieverListner;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction()== SmsRetriever.SMS_RETRIEVED_ACTION){

            Bundle extras = intent.getExtras();
            Status smsRetreiverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (smsRetreiverStatus.getStatusCode()) {


                case CommonStatusCodes
                        .SUCCESS:
                    Intent messageIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                    smsBroadcastRecieverListner.onSuccess(messageIntent);
                     break;


                case CommonStatusCodes.TIMEOUT:
                    smsBroadcastRecieverListner.onFailure();
                    break;

            }
        }

    }

    public interface SmsBroadcastRecieverListner{
        void onSuccess(Intent intent);
        void onFailure();
    }
}
