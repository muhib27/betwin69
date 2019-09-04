package com.betwin69.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.betwin69.R;
import com.betwin69.retrofit.RetrofitApiClient;
import com.betwin69.utils.AppSharedPreference;
import com.betwin69.utils.NetworkConnection;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonElement;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class SplashActivity extends AppCompatActivity {
    int SPLASH_TIME_OUT = 3000;
    boolean flag = false;
    boolean liveApp = true;

    @Override
    protected void onStart() {
        super.onStart();


//        new Handler().postDelayed(new Runnable() {
//
//
//            @Override
//            public void run() {
//                boolean isFirstTime = AppSharedPreference.getUsingFirstTime();
//                Intent intent;
//                if (isFirstTime) {
//                    intent = new Intent(SplashActivity.this, LoginActivity.class);
//                } else {
//                    intent = new Intent(SplashActivity.this, MainActivity.class);
//                }
//                startActivity(intent);
//                finish();
//            }
//        }, SPLASH_TIME_OUT);
    }

    String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        try {
//            ProviderInstaller.installIfNeeded(getApplicationContext());
//            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
//            sslContext.init(null, null, null);
//            SSLEngine engine = sslContext.createSSLEngine();
//        }catch (Exception e){
//
//        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        if (!NetworkConnection.getInstance().isNetworkAvailable()) {
            flag = true;
            openDialog();
            return;
        }

        BroadcastReceiver tokenReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String token = intent.getStringExtra("token");

                if (token != null) {
                    Log.e("firebase", String.valueOf(token));
                    regid = token;
                    if(AppSharedPreference.getFcm()!=null) {
                        AppSharedPreference.setFcm(regid);
                        sendRegistrationIdToBackend(regid);
                    }

                    // send token to your server
                }

            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(tokenReceiver,
                new IntentFilter("tokenReceiver"));


//            gcm = GoogleCloudMessaging.getInstance(this);
        regid = AppSharedPreference.getFcm();

        if (!TextUtils.isEmpty(regid)) {
            //AppSharedPreference.setFcm(regid);
            sendRegistrationIdToBackend(regid);
        } else {
            String token = FirebaseInstanceId.getInstance().getToken();
            if(token!=null) {
                AppSharedPreference.setFcm(token);
                sendRegistrationIdToBackend(regid);
            }
        }


    }


//    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//                Log.e("GCM", "This device is not supported.");
//                finish();
//            }
//            return false;
//        }
//        return true;
//    }

    private void sendRegistrationIdToBackend(String fcm_id) {
        if (!NetworkConnection.getInstance().isNetworkAvailable()) {
            //Toast.makeText(getActivity(), "No Connectivity", Toast.LENGTH_SHORT).show();
            flag = true;
            openDialog();
            return;
        }
//        uiHelper.showLoadingDialog("Authenticating...");
//        HashMap params = new HashMap();
//        params.put("username", username);
//        params.put("password", password);


        RetrofitApiClient.getApiInterfaceWithoutTime().addFcm(fcm_id)

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<JsonElement>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<JsonElement> value) {
                        //uiHelper.dismissLoadingDialog();

                        Log.e("login", "onResponse: " + value.body());
//                        Wrapper wrapper = GsonParser.getInstance().parseServerResponse2(
//                                value.body());

                        if (value.code() == 200) {
                            navigateToNextPage();
                        }
                        // AppSharedPreference.setUserNameAndPassword(username, password);
                        flag = true;

                    }


                    @Override
                    public void onError(Throwable e) {
                        flag = true;

                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onComplete() {
                        flag = true;
                    }
                });


    }

    private void getHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY_KEY_HASH:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    @Override
    public void onBackPressed() {
        if (flag) {
            super.onBackPressed();
            liveApp =false;
        }
    }


    private void navigateToNextPage() {

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                // make sure we close the splash screen so the user won't come back when it presses back key

//                finish();

//                if (!mIsBackButtonPressed) {
                boolean isFirstTime = AppSharedPreference.getUsingFirstTime();
                Intent intent;
                if(liveApp) {
                    flag = true;
//                    if (isFirstTime) {
//                        //intent = new Intent(SplashActivity.this, LoginActivity.class);
//                    } else {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
 //                   }
                    startActivity(intent);
//                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
                else {
                    finish();
                }

					/*intent= new Intent(SplashScreenActivity.this, SingleItemShowFragmentActivity.class);
                	intent= new Intent(SplashScreenActivity.this, HomePageFreeVersion.class);
                    SplashScreenActivity.this.startActivity(intent);

                	/*intent= new Intent(SplashScreenActivity.this, SingleItemShowActivity.class);
                    SplashScreenActivity.this.startActivity(intent);*/

//                }

            }

        }, SPLASH_TIME_OUT);// time in milliseconds (1 second = 1000 milliseconds) until the run() method will be called

    }

    public void openDialog() {


        LayoutInflater factory = LayoutInflater.from(SplashActivity.this);
        final View deleteDialogView = factory.inflate(R.layout.alert_dialog_no_connectivity, null);

        Button acceptBtn = (Button)deleteDialogView.findViewById(R.id.ok);

        final AlertDialog deleteDialog = new AlertDialog.Builder(SplashActivity.this).create();
        deleteDialog.setCancelable(false);
        if (deleteDialog.isShowing())
            deleteDialog.dismiss();
        deleteDialog.setView(deleteDialogView);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic

                if(AppSharedPreference.getFcm()!=null)
                    sendRegistrationIdToBackend(AppSharedPreference.getFcm());
                deleteDialog.dismiss();

            }
        });
        deleteDialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                deleteDialog.dismiss();
                finish();

            }
        });
        deleteDialog.show();
    }
}
