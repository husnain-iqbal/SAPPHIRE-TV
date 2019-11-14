package com.live.sapphire.tv.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.live.sapphire.tv.Config;
import com.live.sapphire.tv.Customized.LinkInfoContainer;
import com.live.sapphire.tv.R;
import com.live.sapphire.tv.Utilities;

public class YoutubePlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private Activity mActivity;
    private LinkInfoContainer mLinkInfo;
    private YouTubePlayer mYouTubeVideoPlayer;
    private YouTubePlayerView mYouTubeVideoView;
    private static String mVideoLinkId;
    private static final int RECOVERY_REQUEST = 1;
    public static final String YOUTUBE_PLAYER_INFO_TEXT = "YoutubePlayerInfotext";
    public static final String YOUTUBE_PLAYER_BUNDLE_TEXT = "YoutubePlayerBundletext";

    private static String getVideoLinkId() {
        return mVideoLinkId;
    }

    private void setVideoLinkId(String videoId) {
        mVideoLinkId = videoId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
        mActivity = YoutubePlayerActivity.this;
        mYouTubeVideoView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        mYouTubeVideoView.initialize(Config.YOUTUBE_API_KEY, this);
        Intent intent = getIntent();
        if (intent.hasExtra(LinksLoaderActivity.LINKS_LOADER_INFO_BUNDLE)) {
            Bundle args = intent.getBundleExtra(LinksLoaderActivity.LINKS_LOADER_INFO_BUNDLE);
            LinkInfoContainer info = (LinkInfoContainer) args.getSerializable(LinksLoaderActivity.LINKS_LOADER_INFO_TEXT);
            if (info == null) {
                Utilities.showLongToast(YoutubePlayerActivity.this, "Video Link is empty");
            } else {
                mLinkInfo = info;
                String link = info.getUrl();

                if (link.contains(Utilities.YOUTUBE_LINK_PREFIX_1)) {
                    link = link.replace(Utilities.YOUTUBE_LINK_PREFIX_1, "");
                } else {
                    link = link.replace(Utilities.YOUTUBE_LINK_PREFIX_2, "");
                }
              setVideoLinkId(link);

                // TODO: delete it ... added only for testing
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(YOUTUBE_PLAYER_INFO_TEXT, mLinkInfo);
//                startActivity(new Intent(YoutubePlayerActivity.this, WebViewActivity.class).putExtra(YOUTUBE_PLAYER_BUNDLE_TEXT, bundle));
//                finish();
            }
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player, boolean wasRestored) {
        mYouTubeVideoPlayer = player;
        if (mVideoLinkId != null && !mVideoLinkId.isEmpty()) {
//            player.cueVideo(getVideoLinkId());
            player.loadVideo(getVideoLinkId());
            player.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                @Override
                public void onLoading() {
                }

                @Override
                public void onLoaded(String s) {
                    player.play();
                    Log.e("YoutubePlayerActivity", "video loaded");
                }

                @Override
                public void onAdStarted() {

                }

                @Override
                public void onVideoStarted() {
                    Log.e("YoutubePlayerActivity", "video started");
                }

                @Override
                public void onVideoEnded() {
                }

                @Override
                public void onError(YouTubePlayer.ErrorReason errorReason) {
                    Log.e("YoutubePlayerActivity", errorReason.name());
                }
            });
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
//            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
            Bundle bundle = new Bundle();
            bundle.putSerializable(YOUTUBE_PLAYER_INFO_TEXT, mLinkInfo);
            startActivity(new Intent(YoutubePlayerActivity.this, WebViewActivity.class).putExtra(YOUTUBE_PLAYER_BUNDLE_TEXT, bundle));
            finish();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(Config.YOUTUBE_API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return mYouTubeVideoView;
    }

    @Override
    protected void onDestroy() {
        if (mYouTubeVideoPlayer != null) {
            mYouTubeVideoPlayer.release();
        }
        super.onDestroy();
    }
}