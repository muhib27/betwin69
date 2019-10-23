package com.betwin69.activity;

/*
 * Android Smart WebView is an Open Source Project available on GitHub.
 * Developed by Ghazi Khan (https://github.com/mgks) under MIT Open Source License.
 * This program is free to use for private and commercial purposes.
 * Please mention project source or developer credits in your Application's License(s) Wiki.
 * Giving right credit to developers encourages them to create better projects, just want you to know that :)
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.betwin69.R;
import com.betwin69.utils.AppSharedPreference;
import com.betwin69.utils.NetworkConnection;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.droidsonroids.gif.GifImageView;

//https://stackoverflow.com/questions/9627774/android-allow-portrait-and-landscape-for-tablets-but-force-portrait-on-phone
public class MainActivity extends AppCompatActivity {
    private String cureentUrl = "";
    String type = "";
    GifImageView progressgif;

    static boolean ASWP_JSCRIPT = SmartWebView.ASWP_JSCRIPT;
    static boolean ASWP_FUPLOAD = SmartWebView.ASWP_FUPLOAD;
    static boolean ASWP_CAMUPLOAD = SmartWebView.ASWP_CAMUPLOAD;
    static boolean ASWP_ONLYCAM = SmartWebView.ASWP_ONLYCAM;
    static boolean ASWP_MULFILE = SmartWebView.ASWP_MULFILE;
    static boolean ASWP_LOCATION = SmartWebView.ASWP_LOCATION;
    static boolean ASWP_RATINGS = SmartWebView.ASWP_RATINGS;
    static boolean ASWP_PBAR = SmartWebView.ASWP_PBAR;
    static boolean ASWP_ZOOM = SmartWebView.ASWP_ZOOM;
    static boolean ASWP_SFORM = SmartWebView.ASWP_SFORM;
    static boolean ASWP_OFFLINE = SmartWebView.ASWP_OFFLINE;
    static boolean ASWP_EXTURL = SmartWebView.ASWP_EXTURL;

    //Configuration variables
    private static String ASWV_URL = SmartWebView.ASWV_URL;
    private static String ASWV_F_TYPE = SmartWebView.ASWV_F_TYPE;

    public static String ASWV_HOST = aswm_host(ASWV_URL);


    //Careful with these variable names if altering
    WebView asw_view;
    //    ProgressBar asw_progress;
    ProgressDialog progressBar;
    TextView asw_loading_text;
    NotificationManager asw_notification;
    Notification asw_notification_new;

    private String asw_cam_message;
    private ValueCallback<Uri> asw_file_message;
    private ValueCallback<Uri[]> asw_file_path;
    private final static int asw_file_req = 1;

    private final static int loc_perm = 1;
    private final static int file_perm = 2;

    private SecureRandom random = new SecureRandom();

    private static final String TAG = MainActivity.class.getSimpleName();
    WebSettings webSettings;
    LinearLayout errorLayout;
    View bg;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            Uri[] results = null;
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == asw_file_req) {
                    if (null == asw_file_path) {
                        return;
                    }
                    if (intent == null || intent.getData() == null) {
                        if (asw_cam_message != null) {
                            results = new Uri[]{Uri.parse(asw_cam_message)};
                        }
                    } else {
                        String dataString = intent.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        } else {
                            if (ASWP_MULFILE) {
                                if (intent.getClipData() != null) {
                                    final int numSelectedFiles = intent.getClipData().getItemCount();
                                    results = new Uri[numSelectedFiles];
                                    for (int i = 0; i < numSelectedFiles; i++) {
                                        results[i] = intent.getClipData().getItemAt(i).getUri();
                                    }
                                }
                            }
                        }
                    }
                }
            }
            asw_file_path.onReceiveValue(results);
            asw_file_path = null;
        } else {
            if (requestCode == asw_file_req) {
                if (null == asw_file_message) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                asw_file_message.onReceiveValue(result);
                asw_file_message = null;
            }
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w("READ_PERM = ", Manifest.permission.READ_EXTERNAL_STORAGE);
        Log.w("WRITE_PERM = ", Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //Prevent the app from being started again when it is still alive in the background

        if (!NetworkConnection.getInstance().isNetworkAvailable()) {
            //Toast.makeText(getActivity(), "No Connectivity", Toast.LENGTH_SHORT).show();
            openDialogNet("");
        }
        String orderId = "";
//        gotoPigeonholeFragment();


        if (!isTaskRoot()) {
            finish();
            return;
        }
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.activity_main);
        progressgif = findViewById(R.id.loader);
        bg = findViewById(R.id.bg);
        errorLayout = (LinearLayout)findViewById(R.id.error_layout);
        asw_view = findViewById(R.id.msw_view);



       // asw_view.requestFocusFromTouch();
        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
//start
        BroadcastReceiver mainToken = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //String title = intent.getStringExtra("subject");
                String body = intent.getStringExtra("message");
                String target_view = intent.getStringExtra("target_view");

                if (target_view != null) {

                    try {
                        if (MainActivity.this.getWindow().getDecorView().getRootView().isShown())
                            openDialog(target_view, body);
                        else {
                            ASWV_URL = target_view;

                            get_info();
                            //Webview settings; defaults are customized for best performance
                            webSettings = asw_view.getSettings();
                            loadUrlwith();
                            ASWV_URL = SmartWebView.ASWV_URL;

                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    // send token to your server
                }

            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mainToken,
                new IntentFilter("target_url_token"));//end


//        if (ASWP_PBAR) {
//            asw_progress = findViewById(R.id.msw_progress);
//        } else {
//            findViewById(R.id.msw_progress).setVisibility(View.GONE);
//        }
//		asw_loading_text = findViewById(R.id.msw_loading_text);
        Handler handler = new Handler();

        //Launching app rating request
//		if (ASWP_RATINGS) {
//			handler.postDelayed(new Runnable() {
//				public void run() {
//					get_rating();
//				}
//			}, 1000 * 60); //running request after few moments
//		}

        //Getting basic device information
        //get_info();


        Bundle extras = getIntent().getExtras();
        if (extras != null && !extras.isEmpty()) {


            String id = "";
            if (extras.getString("target_view") != null)
                type = extras.getString("target_view");
            //aswm_view(type, false);
            ASWV_URL = type;
            cureentUrl = type;

            get_info();
            //Webview settings; defaults are customized for best performance
            webSettings = asw_view.getSettings();
            loadUrlwith();
            ASWV_URL = SmartWebView.ASWV_URL;
            //ASWV_URL = type;
            //aswm_view(type, false);
        } else {
            get_info();
            //Webview settings; defaults are customized for best performance
            webSettings = asw_view.getSettings();
            loadUrlwith();
        }


    }

    private void loadUrlwith() {
        if (!ASWP_OFFLINE) {
            webSettings.setJavaScriptEnabled(ASWP_JSCRIPT);
        }
        webSettings.setSaveFormData(ASWP_SFORM);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(ASWP_ZOOM);
        webSettings.setGeolocationEnabled(ASWP_LOCATION);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDomStorageEnabled(true);


        //asw_view.requestFocus(View.FOCUS_DOWN);
        //asw_view.addJavascriptInterface(new JavaScriptInterface(this), addMyFCM());

        asw_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        asw_view.setHapticFeedbackEnabled(false);

        asw_view.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {

                if (!check_permission(2)) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, file_perm);
                } else {
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                    request.setMimeType(mimeType);
                    String cookies = CookieManager.getInstance().getCookie(url);
                    request.addRequestHeader("cookie", cookies);
                    request.addRequestHeader("User-Agent", userAgent);
                    request.setDescription(getString(R.string.dl_downloading));
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
                    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                    assert dm != null;
                    dm.enqueue(request);
                    Toast.makeText(getApplicationContext(), getString(R.string.dl_downloading2), Toast.LENGTH_LONG).show();
                }
            }
        });

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            asw_view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        } else if (Build.VERSION.SDK_INT >= 19) {
            asw_view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        asw_view.setVerticalScrollBarEnabled(false);
        asw_view.setWebViewClient(new Callback());

        //Rendering the default URL
        aswm_view(ASWV_URL, false);

        asw_view.setWebChromeClient(new WebChromeClient() {
            //Handling input[type="file"] requests for android API 16+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                if (ASWP_FUPLOAD) {
                    asw_file_message = uploadMsg;
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType(ASWV_F_TYPE);
                    if (ASWP_MULFILE) {
                        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    }
                    startActivityForResult(Intent.createChooser(i, getString(R.string.fl_chooser)), asw_file_req);
                }
            }

            public void onSelectionStart(WebView view) {
                // Parent class aborts the selection, which seems like a terrible default.
                //Log.i("DroidGap", "onSelectionStart called");
            }

            //Handling input[type="file"] requests for android API 21+
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (check_permission(2) && check_permission(3)) {
                    if (ASWP_FUPLOAD) {
                        if (asw_file_path != null) {
                            asw_file_path.onReceiveValue(null);
                        }
                        asw_file_path = filePathCallback;
                        Intent takePictureIntent = null;
                        if (ASWP_CAMUPLOAD) {
                            takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
                                File photoFile = null;
                                try {
                                    photoFile = create_image();
                                    takePictureIntent.putExtra("PhotoPath", asw_cam_message);
                                } catch (IOException ex) {
                                    Log.e(TAG, "Image file creation failed", ex);
                                }
                                if (photoFile != null) {
                                    asw_cam_message = "file:" + photoFile.getAbsolutePath();
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                                } else {
                                    takePictureIntent = null;
                                }
                            }
                        }
                        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        if (!ASWP_ONLYCAM) {
                            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                            contentSelectionIntent.setType(ASWV_F_TYPE);
                            if (ASWP_MULFILE) {
                                contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            }
                        }
                        Intent[] intentArray;
                        if (takePictureIntent != null) {
                            intentArray = new Intent[]{takePictureIntent};
                        } else {
                            intentArray = new Intent[0];
                        }

                        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                        chooserIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.fl_chooser));
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                        startActivityForResult(chooserIntent, asw_file_req);
                    }
                    return true;
                } else {
                    get_file();
                    return false;
                }
            }

        });
        if (getIntent().getData() != null) {
            String path = getIntent().getDataString();
            /*
            If you want to check or use specific directories or schemes or hosts

            Uri data        = getIntent().getData();
            String scheme   = data.getScheme();
            String host     = data.getHost();
            List<String> pr = data.getPathSegments();
            String param1   = pr.get(0);
            */
            aswm_view(path, false);
        }
    }

    @Override
    public void onNewIntent(Intent intent){
        //called when a new intent for this class is created.
        // The main case is when the app was in background, a notification arrives to the tray, and the user touches the notification

        super.onNewIntent(intent);

        Bundle extras = getIntent().getExtras();
        if (extras != null && !extras.isEmpty()) {


            String id = "";
            if (extras.getString("target_view") != null)
                type = extras.getString("target_view");
            //aswm_view(type, false);
            ASWV_URL = type;
            cureentUrl = type;

            get_info();
            //Webview settings; defaults are customized for best performance
            webSettings = asw_view.getSettings();
            loadUrlwith();
            ASWV_URL = SmartWebView.ASWV_URL;
            //ASWV_URL = type;
            //aswm_view(type, false);
        }
    }


    private String addMyFCM() {
        String st = "alert('here')";
//        var loginInput = document.getElementById(\"fcm_id_for_mobile\");\n" +
//                "        if(loginInput){\n" +
//                "\n" +
//                "            loginInput.value='" + AppSharedPreference.getFcm() + "';\n" +
//                "        }\n" +
//                "        var logoutlink = document.getElementById(\"logout_fcm_id\");\n" +
//                "        if(logoutlink){\n" +
//                "            logoutlink.href=logoutlink.href+\"?fcm_id=" + AppSharedPreference.getFcm() + "\"; }\n";

//        String st = " var loginInput = document.getElementById(\"fcm_id\");\n" +
//                "        if(loginInput){\n" +
//                "\n" +
//                "            loginInput.value='" + AppSharedPreference.getFcm() + "';\n" +
//                "        }\n" +
//                "        var logoutlink = document.getElementById(\"logout_link_fcm\");\n" +
//                "        if(logoutlink){\n" +
//                "            logoutlink.href=logoutlink.href+\"?fcm_id=" + AppSharedPreference.getFcm() + "\"; }\n";



//        var loginInput = document.getElementById("fcm_id_for_mobile");
//        if(loginInput){
//
//            loginInput.value='fcm_id....';
//        }
//        var logoutlink = document.getElementById("logout_fcm_id");
//        if(logoutlink){
//            logoutlink.href=logoutlink.href+"?fcm_id=fcm_id....";
//        }
        return st;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Coloring the "recent apps" tab header; doing it onResume, as an insurance
        if (Build.VERSION.SDK_INT >= 23) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription taskDesc;
            taskDesc = new ActivityManager.TaskDescription(getString(R.string.app_name), bm, getColor(R.color.colorPrimary));
            MainActivity.this.setTaskDescription(taskDesc);
        }
        //get_location();
    }

    boolean errorFlag = false;
    //Setting activity layout visibility
    private class Callback extends WebViewClient {
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // get_location();
//            if (!progressBar.isShowing()) {
//                progressBar.show();
//            }
            if(progressgif.getVisibility() != View.VISIBLE) {
                bg.setVisibility(View.VISIBLE);
                progressgif.setVisibility(View.VISIBLE);
//                Glide.with(getApplicationContext())
//                        .asGif()
//                        .load(R.drawable.spinner)
//                        .into(progressgif);
            }
        }

        public void onPageFinished(WebView view, String url) {
            //findViewById(R.id.msw_welcome).setVisibility(View.GONE);
            //if(url.equals("http://www.ndc.local/main/login"))
            //{
            // asw_view.addJavascriptInterface(new JavaScriptInterface(getApplicationContext()), addMyFCM());
            //}
            String s =AppSharedPreference.getFcm();
            asw_view.loadUrl("javascript:load_fcm('" + AppSharedPreference.getFcm() + "')");
            if(errorFlag) {
                asw_view.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
            else {
                findViewById(R.id.msw_view).setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
            }
            if(!errorFlag)
            findViewById(R.id.msw_view).setVisibility(View.VISIBLE);
//            if (progressBar.isShowing()) {
//                progressBar.dismiss();
//            }
            if(progressgif.getVisibility() == View.VISIBLE) {
                progressgif.setVisibility(View.GONE);
                bg.setVisibility(View.GONE);
            }
        }

        @Override
        @TargetApi(Build.VERSION_CODES.M)
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            //Your code to do
            // Toast.makeText(getActivity(), "Your Internet Connection May not be active Or " + error, Toast.LENGTH_LONG).show();

//            aswm_view("file:///android_asset/my_page.html", false);
//            asw_view.clearHistory();
//            WebBackForwardList mWebBackForwardList = asw_view.copyBackForwardList();
            if (error.getDescription().equals("net::ERR_INTERNET_DISCONNECTED")) {
//                aswm_view("file:///android_asset/my_page.html", false);
                //asw_view.loadUrl("file:///android_asset/my_page.html");
                errorLayout.setVisibility(View.VISIBLE);
                errorFlag = true;

            }
//            if (progressBar.isShowing()) {
//                progressBar.dismiss();
//            }
            if(progressgif.getVisibility() == View.VISIBLE) {
                progressgif.setVisibility(View.GONE);
                bg.setVisibility(View.GONE);
            }
        }

        //For android below API 23
        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            //Toast.makeText(getApplicationContext(), getString(R.string.went_wrong), Toast.LENGTH_SHORT).show();
//            aswm_view("file:///android_asset/my_page.html", false);
//            asw_view.clearHistory();
//            WebBackForwardList mWebBackForwardList = asw_view.copyBackForwardList();
            //openDialogNet("");
//            if (progressBar.isShowing()) {
//                progressBar.dismiss();
//            }
            if(progressgif.getVisibility() == View.VISIBLE) {
                progressgif.setVisibility(View.GONE);
                bg.setVisibility(View.GONE);
            }

        }

//        @Override
//        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//            super.onReceivedError(view, request, error);
//            if (progressBar.isShowing()) {
//                progressBar.dismiss();
//            }
//        }

        //Overriding webview URLs
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!NetworkConnection.getInstance().isNetworkAvailable()) {
                //Toast.makeText(getActivity(), "No Connectivity", Toast.LENGTH_SHORT).show();
                openDialogNet(url);
                return false;
            } else
                return url_actions(url);
        }

        //Overriding webview URLs for API 23+ [suggested by github.com/JakePou]
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (!NetworkConnection.getInstance().isNetworkAvailable()) {
                //Toast.makeText(getActivity(), "No Connectivity", Toast.LENGTH_SHORT).show();
                openDialogNet(request.getUrl().toString());
                return false;
            } else
                return url_actions(request.getUrl().toString());
        }
    }

    public class JavaScriptInterface {
        public Context mContext;

        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public String getFromAndroid() {
            return AppSharedPreference.getFcm();
        }
    }

    //Random ID creation function to help get fresh cache every-time webview reloaded
    public String random_id() {
        return new BigInteger(130, random).toString(32);
    }

    //Opening URLs inside webview with request
    void aswm_view(String url, Boolean tab) {
        if (tab) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } else {
            if (url.contains("?")) { // check to see whether the url already has query parameters and handle appropriately.
                url += "&";
            } else {
                url += "?";
            }
//			url += "rid=" + random_id();
            url += "rid=";
            asw_view.loadUrl(url);
        }
    }

    //Actions based on shouldOverrideUrlLoading
//    public boolean url_actions(WebView view, String url) {
    public boolean url_actions(String url) {

        boolean a = true;
        //Show toast error if not connected to the network
        if (!ASWP_OFFLINE && !DetectConnection.isInternetAvailable(MainActivity.this)) {
            Toast.makeText(getApplicationContext(), getString(R.string.check_connection), Toast.LENGTH_SHORT).show();

            //Use this in a hyperlink to redirect back to default URL :: href="refresh:android"
        } else if (url.startsWith("refresh:")) {
            aswm_view(ASWV_URL, false);

            //Use this in a hyperlink to launch default phone dialer for specific number :: href="tel:+919876543210"
        }
//		else if (url.startsWith("tel:")) {
//			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
//			startActivity(intent);
//
//			//Use this to open your apps page on google play store app :: href="rate:android"
//		}
//		else if (url.startsWith("rate:")) {
//			final String app_package = getPackageName(); //requesting app package name from Context or Activity object
//			try {
//				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + app_package)));
//			} catch (ActivityNotFoundException anfe) {
//				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + app_package)));
//			}
//
//			//Sharing content from your webview to external apps :: href="share:URL" and remember to place the URL you want to share after share:___
//		}
//		else if (url.startsWith("share:")) {
//			Intent intent = new Intent(Intent.ACTION_SEND);
//			intent.setType("text/plain");
//			intent.putExtra(Intent.EXTRA_SUBJECT, view.getTitle());
//			intent.putExtra(Intent.EXTRA_TEXT, view.getTitle() + "\nVisit: " + (Uri.parse(url).toString()).replace("share:", ""));
//			startActivity(Intent.createChooser(intent, getString(R.string.share_w_friends)));
//
//			//Use this in a hyperlink to exit your app :: href="exit:android"
//		}
        else if (url.startsWith("exit:")) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            //Getting location for offline files
        }
//		else if (url.startsWith("offloc:")) {
//			String offloc = ASWV_URL+"?loc="+get_location();
//			aswm_view(offloc,false);
//			Log.d("OFFLINE LOC REQ",offloc);
//
//		 	//Opening external URLs in android default web browser
//		}
        else if (ASWP_EXTURL && !aswm_host(url).equals(ASWV_HOST)) {
            aswm_view(url, true);
        } else {
            a = false;
        }
        return a;
    }

    //Getting host name
    public static String aswm_host(String url) {
        if (url == null || url.length() == 0) {
            return "";
        }
        int dslash = url.indexOf("//");
        if (dslash == -1) {
            dslash = 0;
        } else {
            dslash += 2;
        }
        int end = url.indexOf('/', dslash);
        end = end >= 0 ? end : url.length();
        int port = url.indexOf(':', dslash);
        end = (port > 0 && port < end) ? port : end;
        Log.w("URL Host: ", url.substring(dslash, end));
        return url.substring(dslash, end);
    }

    //Getting device basic information
    public void get_info() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(ASWV_URL, "DEVICE=android");
        cookieManager.setCookie(ASWV_URL, "DEV_API=" + Build.VERSION.SDK_INT);
    }

    //Checking permission for storage and camera for writing and uploading images
    public void get_file() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        //Checking for storage permission to write images for upload
        if (ASWP_FUPLOAD && ASWP_CAMUPLOAD && !check_permission(2) && !check_permission(3)) {
            ActivityCompat.requestPermissions(MainActivity.this, perms, file_perm);

            //Checking for WRITE_EXTERNAL_STORAGE permission
        } else if (ASWP_FUPLOAD && !check_permission(2)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, file_perm);

            //Checking for CAMERA permissions
        } else if (ASWP_CAMUPLOAD && !check_permission(3)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, file_perm);
        }
    }

//    //Using cookies to update user locations
//	public String get_location(){
//		String newloc = "0,0";
//		//Checking for location permissions
//		if (ASWP_LOCATION && ((Build.VERSION.SDK_INT >= 23 && check_permission(1)) || Build.VERSION.SDK_INT < 23)) {
//			CookieManager cookieManager = CookieManager.getInstance();
//			cookieManager.setAcceptCookie(true);
//			GPSTrack gps;
//			gps = new GPSTrack(MainActivity.this);
//			double latitude = gps.getLatitude();
//			double longitude = gps.getLongitude();
//			if (gps.canGetLocation()) {
//				if (latitude != 0 || longitude != 0) {
//					if(!ASWP_OFFLINE) {
//						cookieManager.setCookie(ASWV_URL, "lat=" + latitude);
//						cookieManager.setCookie(ASWV_URL, "long=" + longitude);
//					}
//					//Log.w("New Updated Location:", latitude + "," + longitude);  //enable to test dummy latitude and longitude
//					newloc = latitude+","+longitude;
//				} else {
//					Log.w("New Updated Location:", "NULL");
//				}
//			} else {
//				show_notification(1, 1);
//				Log.w("New Updated Location:", "FAIL");
//			}
//		}
//		return newloc;
//	}

    //Checking if particular permission is given or not
    public boolean check_permission(int permission) {
        switch (permission) {
            case 1:
                return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

            case 2:
                return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

            case 3:
                return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        }
        return false;
    }

    //Creating image file for upload
    private File create_image() throws IOException {
        @SuppressLint("SimpleDateFormat")
        String file_name = new SimpleDateFormat("yyyy_mm_ss").format(new Date());
        String new_name = "file_" + file_name + "_";
        File sd_directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(new_name, ".jpg", sd_directory);
    }


//    //Creating custom notifications with IDs
//    public void show_notification(int type, int id) {
//        long when = System.currentTimeMillis();
//        asw_notification = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent i = new Intent();
//        if (type == 1) {
//            i.setClass(MainActivity.this, MainActivity.class);
//        } else if (type == 2) {
//            i.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//        } else {
//            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            i.addCategory(Intent.CATEGORY_DEFAULT);
//            i.setData(Uri.parse("package:" + MainActivity.this.getPackageName()));
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//        }
//        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "");
//        switch(type){
//            case 1:
//                builder.setTicker(getString(R.string.app_name));
//                builder.setContentTitle(getString(R.string.loc_fail));
//                builder.setContentText(getString(R.string.loc_fail_text));
//                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.loc_fail_more)));
//                builder.setVibrate(new long[]{350,350,350,350,350});
//                builder.setSmallIcon(R.mipmap.ic_launcher);
//            break;
//
//            case 2:
//                builder.setTicker(getString(R.string.app_name));
//                builder.setContentTitle(getString(R.string.loc_perm));
//                builder.setContentText(getString(R.string.loc_perm_text));
//                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.loc_perm_more)));
//                builder.setVibrate(new long[]{350, 700, 350, 700, 350});
//                builder.setSound(alarmSound);
//                builder.setSmallIcon(R.mipmap.ic_launcher);
//            break;
//        }
//        builder.setOngoing(false);
//        builder.setAutoCancel(true);
//        builder.setContentIntent(pendingIntent);
//        builder.setWhen(when);
//        builder.setContentIntent(pendingIntent);
//        asw_notification_new = builder.build();
//        asw_notification.notify(id, asw_notification_new);
//    }

    //Checking if users allowed the requested permissions or not
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //get_location();
                }
            }
        }
    }

    //Action on back key tap/click
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                  if (!cureentUrl.isEmpty() && cureentUrl.equals(type)) {
                        type = "";

                        get_info();
                        webSettings = asw_view.getSettings();
                        loadUrlwith();
                    }
//                    else if(!cureentUrl.isEmpty() && type.isEmpty())
//                    {
//                        finish();
//                    }
                    else if (asw_view.canGoBack() && cureentUrl.isEmpty()) {
                        if (!NetworkConnection.getInstance().isNetworkAvailable()) {
                            //Toast.makeText(getActivity(), "No Connectivity", Toast.LENGTH_SHORT).show();
                            openDialogNet("");
                            return false;
                        } else {
                            if(errorFlag) {
                                asw_view.clearHistory();
                                WebBackForwardList mWebBackForwardList = asw_view.copyBackForwardList();
//                                get_info();
//                                webSettings = asw_view.getSettings();
//                                loadUrlwith();
                                errorFlag = false;

                            }
                            else
                            asw_view.goBack();
                        }
                    } else {
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        asw_view.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        asw_view.restoreState(savedInstanceState);
    }


    public void openDialog(final String target_view, final String body) {
        Log.e(TAG, "process time: " + "dddddddddddddddddddddddddddddd");


        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        final View deleteDialogView = factory.inflate(R.layout.alert_dialog_new_order, null);
        //TextView titletext = (TextView) deleteDialogView.findViewById(R.id.title);
        TextView bodytext = (TextView) deleteDialogView.findViewById(R.id.body);
        Button acceptBtn = (Button) deleteDialogView.findViewById(R.id.ok);
//        acceptBtn.setText("Accept");
//        text.setText(title + "\n" + body);
       // titletext.setText(title);
        bodytext.setText(body);
        final AlertDialog deleteDialog = new AlertDialog.Builder(MainActivity.this).create();
        deleteDialog.setCancelable(false);
        if (deleteDialog.isShowing())
            deleteDialog.dismiss();
        deleteDialog.setView(deleteDialogView);
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
//                type = target_view;
//                cureentUrl = target_view;
                ASWV_URL = target_view;

                get_info();
                //Webview settings; defaults are customized for best performance
                webSettings = asw_view.getSettings();
                loadUrlwith();
                ASWV_URL = SmartWebView.ASWV_URL;
                deleteDialog.dismiss();

            }
        });
        deleteDialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                deleteDialog.dismiss();

            }
        });
        deleteDialog.show();
    }

    public void openDialogNet(final String url) {

        LayoutInflater factory = LayoutInflater.from(MainActivity.this);
        final View deleteDialogView = factory.inflate(R.layout.alert_dialog_no_connectivity_main, null);

        //Button acceptBtn = (Button) deleteDialogView.findViewById(R.id.ok);

        final AlertDialog deleteDialogCon = new AlertDialog.Builder(MainActivity.this).create();
        deleteDialogCon.setCancelable(false);
        if (deleteDialogCon.isShowing())
            deleteDialogCon.dismiss();
        deleteDialogCon.setView(deleteDialogView);
//        acceptBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //your business logic
//                if (NetworkConnection.getInstance().isNetworkAvailable()) {
//
//                    asw_view.clearHistory();
//                        get_info();
//                        //Webview settings; defaults are customized for best performance
//                        webSettings = asw_view.getSettings();
//                        loadUrlwith();
//
//                } else {
//                    openDialogNet("");
//                }
//                deleteDialogCon.dismiss();
//
//            }
//        });
        deleteDialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //your business logic
                deleteDialogCon.dismiss();
                finish();

            }
        });
        deleteDialogCon.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
