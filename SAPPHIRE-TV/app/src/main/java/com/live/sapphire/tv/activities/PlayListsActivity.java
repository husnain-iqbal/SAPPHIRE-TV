package com.live.sapphire.tv.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.live.sapphire.tv.Adapters.PlayListsAdapter;
import com.live.sapphire.tv.Customized.LinkInfoContainer;
import com.live.sapphire.tv.Customized.PlayListsInfo;
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

public class PlayListsActivity extends AppCompatActivity implements PlayListsAdapter.SendData {

    private Activity mActivity;
    //    private GridView mGridView;
    private RecyclerView mRecyclerView;
    private PlayListsAdapter mPlayListsAdapter;
    public static final String PLAYLISTS_INFO_TEXT = "PlayListsInfoText";
    public static final String PLAYLISTS_LINKS_BUNDLE_TEXT = "PlayListsInfoBundle";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_lists);
        mActivity = PlayListsActivity.this;
//        mGridView = (GridView) findViewById(R.id.gridView_playLists);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_playLists);
        Intent intent = getIntent();
        if (intent.hasExtra(LinksLoaderActivity.LINKS_LOADER_INFO_BUNDLE)) {
            Bundle bundle = intent.getBundleExtra(LinksLoaderActivity.LINKS_LOADER_INFO_BUNDLE);
            LinkInfoContainer linkInfo = (LinkInfoContainer)bundle.getSerializable(LinksLoaderActivity.LINKS_LOADER_INFO_TEXT);
            if (linkInfo != null) {
                new DataLoaderAsyncTask().execute(Utilities.getChannelsPlaylistUrl(linkInfo.getId(), false));
                showLoadingIndicatorView();
                ;
            }
        }
    }

    void showLoadingIndicatorView(){
        findViewById(R.id.avliv_activity_play_lists).setVisibility(View.VISIBLE);
    }

    void hideLoadingIndicatorView(){
        findViewById(R.id.avliv_activity_play_lists).setVisibility(View.GONE);
    }

    @Override
    public void sendData2NewScreen(PlayListsInfo playListInfo){
        Bundle bundle = new Bundle();
        bundle.putSerializable(PLAYLISTS_INFO_TEXT, playListInfo);
        startActivity(new Intent(PlayListsActivity.this, VideosActivity.class).putExtra(PLAYLISTS_LINKS_BUNDLE_TEXT, bundle));
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

    private void populateGridViewInUiThread(final ArrayList <PlayListsInfo> playListInfoList){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run(){
                mRecyclerView.setLayoutManager(new GridLayoutManager(PlayListsActivity.this, Utilities.mNumColumnsForFullScreenGridView));
                mPlayListsAdapter = new PlayListsAdapter(PlayListsActivity.this, mActivity, playListInfoList);
                mRecyclerView.setAdapter(mPlayListsAdapter);
                hideLoadingIndicatorView();
//                mRecyclerView.getLayoutManager().scrollToPosition(0);
//                Utilities.showShortToast(PlayListsActivity.this, "Playlists...");
//                mGridView.setAdapter(mPlayListsAdapter);
//                mGridView.setNumColumns(Utilities.mNumColumnsForFullScreenGridView);
//                if (mPlayListInfoList.size() >= 1) {
//                    mGridView.setSelection(0);
//                }
//                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                        if (!mPlayListInfoList.isEmpty()) {
//                            sendData2NewScreen(mPlayListInfoList.get(position));
//                        }
//                    }
//                });
            }
        });
    }

    private class DataLoaderAsyncTask extends AsyncTask <String, String, String> {
        @Override
        protected String doInBackground(String... params){
            HttpClient httpclient = new DefaultHttpClient();
            String playListUrl = Utilities.getChannelsPlaylistUrl(params[0], false);
            HttpPost httpPost = new HttpPost(playListUrl);
            try {
                List <NameValuePair> nameValuePairs = new ArrayList <>(1);
                nameValuePairs.add(new BasicNameValuePair(Utilities.POST_PARAMETER_NAME, Utilities.POST_PARAMETER_VALUE));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                String result = EntityUtils.toString(response.getEntity());

                if (result != null && !result.isEmpty()) {
                    JSONObject object = new JSONObject(result);
                    JSONObject channelInfoJsonObject = object.getJSONObject("channelinfo");
//                    String title = channelInfoJsonObject.getString("title");
//                    String url = channelInfoJsonObject.getString("url");
//                    String channelId = channelInfoJsonObject.getString("channelid");
//                    String description = channelInfoJsonObject.getString("description");
//                    String publishDate = channelInfoJsonObject.getString("publishedat");
//                    String totalResults = channelInfoJsonObject.getString("totalresults");
//                    String resultsPerPage = channelInfoJsonObject.getString("resultsperpage");
//                    String nextPageToken = channelInfoJsonObject.getString("nextpagetoken");
                    JSONArray playListJsonArray = channelInfoJsonObject.getJSONArray("playlists");
                    ArrayList <PlayListsInfo> playListInfoList = new ArrayList <>();
                    for (int i = 0; i < playListJsonArray.length(); i++) {
                        JSONObject jsonObject = playListJsonArray.getJSONObject(i);
                        String playlistId = jsonObject.getString("playlistid");
                        String playListPublishDate = jsonObject.getString("publishedat");
                        String playlistTitle = jsonObject.getString("title");
                        String thumbnailUrl = jsonObject.getString("thumb");
                        playListInfoList.add(new PlayListsInfo(playlistTitle, playlistId, playListPublishDate, thumbnailUrl));
                    }
                    populateGridViewInUiThread(playListInfoList);
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
