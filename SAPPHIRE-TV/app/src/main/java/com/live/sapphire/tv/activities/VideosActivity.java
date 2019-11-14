package com.live.sapphire.tv.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.live.sapphire.tv.Adapters.VideosAdapter;
import com.live.sapphire.tv.Customized.LinkInfoContainer;
import com.live.sapphire.tv.Customized.PlayListsInfo;
import com.live.sapphire.tv.Customized.VideoInfo;
import com.live.sapphire.tv.R;
import com.live.sapphire.tv.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.util.EntityUtils;

public class VideosActivity extends AppCompatActivity implements VideosAdapter.SendData {

    private Activity mActivity;
    //    private GridView mGridView;
    private RecyclerView mRecyclerView;
    private VideosAdapter mVideosAdapter;
    public static final String VIDEOS_INFO_TEXT = "VideosInfoText";
    public static final String VIDEOS_BUNDLE_TEXT = "VideosBundleText";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        mActivity = VideosActivity.this;
//        mGridView = (GridView) findViewById(R.id.gridView_videos);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_videos);
        Intent intent = getIntent();
        if (intent.hasExtra(LinksLoaderActivity.LINKS_LOADER_INFO_BUNDLE)) {
            Bundle bundle = intent.getBundleExtra(LinksLoaderActivity.LINKS_LOADER_INFO_BUNDLE);
            LinkInfoContainer playListLinkInfo = (LinkInfoContainer)bundle.getSerializable(LinksLoaderActivity.LINKS_LOADER_INFO_TEXT);
            if (playListLinkInfo != null) {
                String videoLink = Utilities.getPlaylistVideos(playListLinkInfo.getChannelOrPlaylistId(), "");
                new PlayListVideosLoaderAsyncTask().execute(videoLink);
            }
        } else if (intent.hasExtra(PlayListsActivity.PLAYLISTS_LINKS_BUNDLE_TEXT)) {
            Bundle bundle = intent.getBundleExtra(PlayListsActivity.PLAYLISTS_LINKS_BUNDLE_TEXT);
            PlayListsInfo playListInfo = (PlayListsInfo)bundle.getSerializable(PlayListsActivity.PLAYLISTS_INFO_TEXT);
            if (playListInfo != null) {
                String url = Utilities.getPlaylistVideos(playListInfo.getPlayListId(), "");
                new PlayListVideosLoaderAsyncTask().execute(url);
            }
        }
        showLoadingIndicatorView();
    }

    void showLoadingIndicatorView(){
        findViewById(R.id.avliv_activity_videos).setVisibility(View.VISIBLE);
    }

    void hideLoadingIndicatorView(){
        findViewById(R.id.avliv_activity_videos).setVisibility(View.GONE);
    }

    @Override
    public void sendData2NewScreen(VideoInfo videoInfo){
        Bundle bundle = new Bundle();
        bundle.putSerializable(VIDEOS_INFO_TEXT, videoInfo);
        startActivity(new Intent(VideosActivity.this, AndroidNativePlayerActivity.class).putExtra(VIDEOS_BUNDLE_TEXT, bundle));
    }

    @Override
    public void moveForwardAndBackward(int position){
        mRecyclerView.getLayoutManager().scrollToPosition(position);
        View v = mRecyclerView.getLayoutManager().findViewByPosition(position);
        if (v != null) {
            v.requestFocus();
        }
    }

    @Override
    public void moveUpAndDown(int position){
        View v = mRecyclerView.getLayoutManager().findViewByPosition(position);
        if (v != null) {
            v.requestFocus();
            mRecyclerView.getLayoutManager().scrollToPosition(position);
        }
    }

    private void populateGridViewInUiThread(final ArrayList <VideoInfo> videoList){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run(){
                mRecyclerView.setLayoutManager(new GridLayoutManager(VideosActivity.this, Utilities.mNumColumnsForFullScreenGridView));
                mVideosAdapter = new VideosAdapter(VideosActivity.this, mActivity, videoList);
                mRecyclerView.setAdapter(mVideosAdapter);
                hideLoadingIndicatorView();
//                mRecyclerView.getLayoutManager().scrollToPosition(0);
//                Utilities.showShortToast(VideosActivity.this, "Videos...");
//                mVideosAdapter = new VideosAdapter(VideosActivity.this, mActivity, videoList);
//                mGridView.setAdapter(mVideosAdapter);
//                mGridView.setNumColumns(Utilities.mNumColumnsForFullScreenGridView);
//                if (mVideoList.size() >= 1) {
//                    mGridView.setSelection(0);
//                }
//                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                        if (!mVideoList.isEmpty()) {
//                            sendData2NewScreen(mVideoList.get(position));
//                        }
//                    }
//                });
            }
        });
    }

    private class PlayListVideosLoaderAsyncTask extends AsyncTask <String, String, String> {
        @Override
        protected String doInBackground(String... params){
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(params[0]);
            try {
                List <NameValuePair> nameValuePairs = new ArrayList <>(1);
                nameValuePairs.add(new BasicNameValuePair(Utilities.POST_PARAMETER_NAME, Utilities.POST_PARAMETER_VALUE));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                String result = EntityUtils.toString(response.getEntity());

                if (result != null && !result.isEmpty()) {
                    JSONObject object = new JSONObject(result);

                    JSONObject playListsVideosJsonObject = object.getJSONObject("playlistvideos");
//                    String totalResults = playListsVideosJsonObject.getString("totalresults");
//                    String resultsPerPage = playListsVideosJsonObject.getString("resultsperpage");
//                    String nextPageToken = playListsVideosJsonObject.getString("nextpagetoken");
                    JSONArray videosJSONArray = playListsVideosJsonObject.getJSONArray("videos");
                    final ArrayList <VideoInfo> videoList = new ArrayList <>();
                    for (int i = 0; i < videosJSONArray.length(); i++) {
                        JSONObject jsonObject = videosJSONArray.getJSONObject(i);
                        String videoId = jsonObject.getString("videoid");
                        String title = jsonObject.getString("title");
                        String thumbnailUrl = jsonObject.getString("thumb");
                        videoList.add(new VideoInfo(videoId, title, thumbnailUrl));
                    }
                    populateGridViewInUiThread(videoList);
                } else {
                    Utilities.showLongToastInUiThread(mActivity, Utilities.SERVER_ERROR_TEXT);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
