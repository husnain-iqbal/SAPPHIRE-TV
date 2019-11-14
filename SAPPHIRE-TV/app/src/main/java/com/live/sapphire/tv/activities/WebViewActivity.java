package com.live.sapphire.tv.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.live.sapphire.tv.Customized.LinkInfoContainer;
import com.live.sapphire.tv.R;
import com.live.sapphire.tv.Utilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebViewActivity extends AppCompatActivity {

    private Activity mActivity;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mActivity = WebViewActivity.this;
        mWebView = (WebView)findViewById(R.id.web_view);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 16) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        Intent intent = getIntent();
        if (intent.hasExtra(LinksLoaderActivity.LINKS_LOADER_INFO_BUNDLE)) {
            Bundle bundle = intent.getBundleExtra(LinksLoaderActivity.LINKS_LOADER_INFO_BUNDLE);
            LinkInfoContainer linkInfo = (LinkInfoContainer)bundle.getSerializable(LinksLoaderActivity.LINKS_LOADER_INFO_TEXT);
            if (linkInfo == null) {
                Utilities.showLongToast(WebViewActivity.this, "Video Link is empty");
            }
            else {
                if (linkInfo.getSource().equalsIgnoreCase(Utilities.SOURCE_KARWAN_TEXT)) {
                    mWebView.loadUrl(linkInfo.getUrl() + Utilities.VIDEO_AUTO_PLAY_CODE);
//                    mWebView.loadUrl("javascript:playVideo()");
//                    mWebView.performClick();
//                    mWebView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view){
//                            view.performClick();
//                        }
//                    });
                }
                else if (linkInfo.getSource().equalsIgnoreCase(Utilities.SOURCE_FLUSSONIC_TEXT)) {
                    final String linkUrl = linkInfo.getUrl();
                    if (linkUrl != null) {
                        getPublicIpAndMakeUrl(linkUrl);
                    }
                }
            }
        }
        else if (intent.hasExtra(YoutubePlayerActivity.YOUTUBE_PLAYER_BUNDLE_TEXT)) {
            Bundle bundle = intent.getBundleExtra(YoutubePlayerActivity.YOUTUBE_PLAYER_BUNDLE_TEXT);
            LinkInfoContainer linkInfo = (LinkInfoContainer)bundle.getSerializable(YoutubePlayerActivity.YOUTUBE_PLAYER_INFO_TEXT);
            if (linkInfo == null) {
                Utilities.showLongToast(WebViewActivity.this, "Video Link is empty");
            }
            else {
                String youtubeVideoLink = linkInfo.getUrl();
                if (youtubeVideoLink.contains(Utilities.YOUTUBE_LINK_PREFIX_1)) {
                    youtubeVideoLink = youtubeVideoLink.replace(Utilities.YOUTUBE_LINK_PREFIX_1, "");
                }
                else {
                    youtubeVideoLink = youtubeVideoLink.replace(Utilities.YOUTUBE_LINK_PREFIX_2, "");
                }
                youtubeVideoLink = Utilities.YOUTUBE_FULL_SCREEN_OPTIMIZED_LINK_1.replace("VIDEOID", youtubeVideoLink) + Utilities.VIDEO_AUTO_PLAY_CODE;
                mWebView.loadUrl(youtubeVideoLink);
                mWebView.performClick();
                mWebView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        view.performClick();
                    }
                });
//                ScreenDimensions screenDimensions = AppConfigurations.getScreenDimensions (WebViewActivity.this);

            }
        }
        else {
            Utilities.showLongToast(WebViewActivity.this, "Video Link is empty");
        }
    }

    public void getPublicIpAndMakeUrl(final String linkUrl){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run(){
                try {
                    Document doc = Jsoup.connect("http://www.checkip.org").get();
                    String ip = doc.getElementById("yourip").select("h1").first().select("span").text();
                    if (ip != null && !ip.equals("")) {
                        long unixEpochTime = System.currentTimeMillis() / 1000;
                        unixEpochTime += 14400;
                        String temp = ip + '-' + "token3" + '-' + unixEpochTime;
                        String videoLink = linkUrl + "?token=" + Base64.encodeToString(temp.getBytes(), Base64.DEFAULT);
                        final String link = videoLink.trim();
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run(){
                                mWebView.loadUrl(link + "?enablejsapi=1&autoplay=1");
                            }
                        });
                    }
                }
                catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }
        });
    }

//    @Override
//    public void onBackPressed(){
//        if (mWebView.canGoBack()) {
//            mWebView.goBack();
//        }
//        else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public void onDestroy(){
        mWebView.loadUrl("about:blank");
        super.onDestroy();
    }
}
