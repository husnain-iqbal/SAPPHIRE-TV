package com.live.sapphire.tv.activities;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.live.sapphire.tv.Customized.FilmonInfo;
import com.live.sapphire.tv.Customized.LinkInfoContainer;
import com.live.sapphire.tv.Customized.StreamInfo;
import com.live.sapphire.tv.Customized.VideoInfo;
import com.live.sapphire.tv.R;
import com.live.sapphire.tv.Utilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;

public class AndroidNativePlayerActivity extends AppCompatActivity {
    private Activity mActivity;
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_native_player);
        mActivity = AndroidNativePlayerActivity.this;
        mVideoView = (VideoView) findViewById(R.id.video_view_native);
        Intent intent = getIntent();
        /*if (intent.hasExtra(LinksLoaderActivity.LINKS_LOADER_INFO_BUNDLE)) {
            Bundle bundle = intent.getBundleExtra(LinksLoaderActivity.LINKS_LOADER_INFO_BUNDLE);
            LinkInfoContainer linkInfo = (LinkInfoContainer) bundle.getSerializable(LinksLoaderActivity.LINKS_LOADER_INFO_TEXT);
            if (linkInfo != null) {
                final String linkUrl = linkInfo.getUrl();
                if (linkUrl != null) {
                    getPublicIpAndMakeUrl(linkUrl);
                }
            }
        } else*/
        if (intent.hasExtra(FilmonActivity.FILMON_BUNDLE)) { //TODO: For OtherLinks -> Filmon videos
            Bundle bundle = intent.getBundleExtra(FilmonActivity.FILMON_BUNDLE);
            FilmonInfo filmonInfo = (FilmonInfo) bundle.getSerializable(FilmonActivity.FILMON_INFO_TEXT);
            if (filmonInfo != null) {
                HashMap<String, StreamInfo> streamRecords = filmonInfo.getStreamRecords();
                if (streamRecords != null) {
                    if (streamRecords.containsKey(Utilities.FILMON_VIDEO_QUALITY_LOW)) {
                        StreamInfo streamInfo = streamRecords.get(Utilities.FILMON_VIDEO_QUALITY_LOW);
                        String linkUrl = streamInfo.getUrl();
                        playVideo(linkUrl, true);
                    } else if (streamRecords.containsKey(Utilities.FILMON_VIDEO_QUALITY_HIGH)) {
                        StreamInfo streamInfo = streamRecords.get(Utilities.FILMON_VIDEO_QUALITY_HIGH);
                        String linkUrl = streamInfo.getUrl();
                        playVideo(linkUrl, true);
                    } else if (streamRecords.size() > 0) {
                        StreamInfo streamInfo = (StreamInfo) streamRecords.values().toArray()[0];
                        String linkUrl = streamInfo.getUrl();
                        playVideo(linkUrl, true);
                    }
                }
            }
        } else if (intent.hasExtra(VideosActivity.VIDEOS_BUNDLE_TEXT)) { //TODO: For simple Videos
            Bundle bundle = intent.getBundleExtra(VideosActivity.VIDEOS_BUNDLE_TEXT);
            VideoInfo videoInfo = (VideoInfo) bundle.getSerializable(VideosActivity.VIDEOS_INFO_TEXT);
            if (videoInfo != null) {
                String videoLink = Utilities.getVideoUrl(videoInfo.getVideoId());
                playVideo(videoLink, true);
            }
        } else if (intent.hasExtra(LinksLoaderActivity.LINKS_LOADER_INFO_BUNDLE)) {
            Bundle args = intent.getBundleExtra(LinksLoaderActivity.LINKS_LOADER_INFO_BUNDLE);
            LinkInfoContainer info = (LinkInfoContainer) args.getSerializable(LinksLoaderActivity.LINKS_LOADER_INFO_TEXT);
            if (info == null) {
                Utilities.showLongToast(AndroidNativePlayerActivity.this, "Video Link is empty");
            } else {
                String videoLink = info.getUrl();
                String newVideoLink = "http://smartiptv.dyndns.org/yupp/yupp/youtubescrapper.php?id=";
                if (videoLink.contains(Utilities.YOUTUBE_LINK_PREFIX_1)) {
                    videoLink = videoLink.replace(Utilities.YOUTUBE_LINK_PREFIX_1, newVideoLink);
                } else {
                    videoLink = videoLink.replace(Utilities.YOUTUBE_LINK_PREFIX_2, newVideoLink);
                }
                playVideo(videoLink, false);
            }
        }
    }


    public void getPublicIpAndMakeUrl(final String linkUrl) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("http://www.checkip.org").get();
                    String ip = doc.getElementById("yourip").select("h1").first().select("span").text();
                    if (ip != null && !ip.equals("")) {
                        long unixEpochTime = System.currentTimeMillis() / 1000;
                        String temp = ip + '-' + "token3" + '-' + unixEpochTime;
                        String videoLink = linkUrl + "?token=" + Base64.encodeToString(temp.getBytes(), Base64.DEFAULT);
                        final String video = videoLink.trim();
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                playVideo(video, true);
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
                }
            }
        });
    }

    private void playVideo(final String link, boolean displayMediaControllers) {
//        mVideoView.setVideoPath(link);
        mVideoView.setVideoURI(Uri.parse(link));
        mVideoView.requestFocus();
        if (displayMediaControllers) {
            MediaController mediaController = new MediaController(this);
            mVideoView.setMediaController(mediaController);
        }
        mVideoView.start();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mediaPlayer) {
                if (!mVideoView.isPlaying()) {
                    mVideoView.start();
                }
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e("@AndroidPlayerActivity", "Type: " + what + ", Extra code is: " + extra);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(mActivity)
                                .setMessage("Streaming Error")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        AndroidNativePlayerActivity.this.finish();
                                    }
                                }).create().show();
                    }
                });
                return true;
            }
        });
//        mVideoView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
//                int keyAction = keyEvent.getAction();
//                if (keyAction == KeyEvent.ACTION_DOWN) {
//                    if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
//                        mVideoView.resume();
//                        return true;
//                    } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
//                        mVideoView.pause();
//                        return true;
//                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
//                        if (mVideoView.isPlaying()) {
//                            mVideoView.pause();
//                        } else {
//                            mVideoView.resume();
//                        }
//                        return true;
//                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD) {
//                        mVideoView.pause();
//                        int currentVideoPosition = mVideoView.getCurrentPosition();
//                        currentVideoPosition += 15000;
//                        mVideoView.seekTo(currentVideoPosition);
//                        mVideoView.resume();
//                        return true;
//                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS || keyCode == KeyEvent.KEYCODE_MEDIA_REWIND) {
//                        mVideoView.pause();
//                        int currentVideoPosition = mVideoView.getCurrentPosition();
//                        currentVideoPosition -= 3000;
//                        mVideoView.seekTo(currentVideoPosition);
//                        mVideoView.resume();
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
    }
}
