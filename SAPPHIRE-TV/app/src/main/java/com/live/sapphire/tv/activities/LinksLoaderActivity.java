package com.live.sapphire.tv.activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.live.sapphire.tv.Adapters.LinksLoaderActivityAdapter;
import com.live.sapphire.tv.Customized.CategoryInfo;
import com.live.sapphire.tv.Customized.ChildCategoryInfo;
import com.live.sapphire.tv.Customized.LinkInfoContainer;
import com.live.sapphire.tv.Customized.SubcategoryInfo;
import com.live.sapphire.tv.Fragments.SubcategoriesFragment;
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

public class LinksLoaderActivity extends AppCompatActivity implements LinksLoaderActivityAdapter.SendData {

    private Context mContext;
    private Activity mActivity;
    private RecyclerView mRecyclerView;
    //    private GridView mGridView;
    private LinksLoaderActivityAdapter mInLinksAdapter;
    private ArrayList <LinkInfoContainer> mInLinkInfoList;
    public static final String LINKS_LOADER_INFO_TEXT = "InLinksInfoSerializableObject";
    public static final String LINKS_LOADER_INFO_BUNDLE = "InLinksInfoBundle";

    private void init(){
        mActivity = LinksLoaderActivity.this;
        mContext = mActivity.getApplicationContext();
        mInLinkInfoList = new ArrayList <>();
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_LinksInfoContainer);
//        mGridView = (GridView) findViewById(R.id.gridView_LinksInfoContainer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links_loader);
        init();
        try {
            String categoryId = null;
            String subcategoryId = null;
            String mainCategoryId = null;
            Intent intent = getIntent();
            if (intent.hasExtra(HomeActivity.CATEGORIES_BUNDLE_TEXT)) {
                Bundle bundle = intent.getBundleExtra(HomeActivity.CATEGORIES_BUNDLE_TEXT);
                CategoryInfo categoryInfo = (CategoryInfo)bundle.getSerializable(HomeActivity.CATEGORIES_SERIALIZABLE_OBJECT_TEXT);
                if (categoryInfo != null) {
                    categoryId = categoryInfo.getId();
                    subcategoryId = categoryInfo.getSubcategoryId();
                    mainCategoryId = categoryInfo.getMainCategoryId();
                }
            } else if (intent.hasExtra(SubcategoriesFragment.SUBCATEGORIES_BUNDLE_TEXT)) {
                Bundle bundle = intent.getBundleExtra(SubcategoriesFragment.SUBCATEGORIES_BUNDLE_TEXT);
                SubcategoryInfo subcategoryInfo = (SubcategoryInfo)bundle.getSerializable(SubcategoriesFragment.SUBCATEGORIES_SERIALIZABLE_INFO_TEXT);
                if (subcategoryInfo != null) {
                    categoryId = subcategoryInfo.getId();
                    subcategoryId = subcategoryInfo.getSubcategoryId();
                    mainCategoryId = subcategoryInfo.getMainCategoryId();
                }
            } else if (intent.hasExtra(ChildCategoriesActivity.CHILD_CATEGORIES_BUNDLE_TEXT)) {
                Bundle bundle = intent.getBundleExtra(ChildCategoriesActivity.CHILD_CATEGORIES_BUNDLE_TEXT);
                ChildCategoryInfo childCategoryInfo = (ChildCategoryInfo)bundle.getSerializable(ChildCategoriesActivity.CHILD_CATEGORIES_SERIALIZABLE_OBJECT_TEXT);
                if (childCategoryInfo != null) {
                    categoryId = childCategoryInfo.getCategoryId();
                    subcategoryId = childCategoryInfo.getSubcategoryId();
                    mainCategoryId = childCategoryInfo.getMainCategoryId();
                }
            }

            if (categoryId != null && subcategoryId != null && mainCategoryId != null) {
                String[] params = {categoryId, subcategoryId, mainCategoryId};
                new InLinksLoaderAsyncTask().execute(params);
                new OtherLinksLoaderAsyncTask().execute(params);
                new ChannelsLoaderAsyncTask().execute(params);
                new PlayListsLoaderAsyncTask().execute(params);
                showLoadingIndicatorView();
            }
        } catch (Exception e) {
            Log.e("@IndividualLinkActivity", e.getMessage());
        }
    }

    private void showLoadingIndicatorView(){
        findViewById(R.id.avliv_activity_links_loader).setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicatorView(){
        findViewById(R.id.avliv_activity_links_loader).setVisibility(View.GONE);
    }

    private void setAdapterToListViewAndCancelDialog(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run(){
                mRecyclerView.setLayoutManager(new GridLayoutManager(LinksLoaderActivity.this, Utilities.mNumColumnsForFullScreenGridView));
                mInLinksAdapter = new LinksLoaderActivityAdapter(LinksLoaderActivity.this, mActivity, mInLinkInfoList);
                mRecyclerView.setAdapter(mInLinksAdapter);
                mRecyclerView.setNestedScrollingEnabled(false);
//                mRecyclerView.getLayoutManager().scrollToPosition(0);

//                mGridView.setAdapter(mInLinksAdapter);
//                mGridView.setSmoothScrollbarEnabled(true);
//                mGridView.setNumColumns(Utilities.mNumColumnsForFullScreenGridView);
//                mGridView.requestFocus();
//                mGridView.setSelection(0);
//                mGridView.setChoiceMode(GridView.CHOICE_lMODE_MULTIPLE);
//                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                        return;
//                    }
//                });
//                mGridView.setOnKeyListener(new View.OnKeyListener() {
//                    @Override
//                    public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
//                        int keyAction = keyEvent.getAction();
//                        int position = mGridView.getSelectedItemPosition();
//                        if (keyAction == KeyEvent.ACTION_DOWN) {
//                            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) { // TODO: for moving UP in grid-view
//                                int movingUpCursor = position - Utilities.mNumColumnsForFullScreenGridView;
//                                if (movingUpCursor >= 0) {
//                                    moveUpAndDown(movingUpCursor);
//                                    return false;
//                                } else {
//                                    return true;
//                                }
//                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) { // TODO: for moving DOWN in grid-view
//                                int movingDownCursor = position + Utilities.mNumColumnsForFullScreenGridView;
//                                if (movingDownCursor <= getListSize() - 1) {
//                                    moveUpAndDown(movingDownCursor);
//                                    return false;
//                                } else {
//                                    return true;
//                                }
//                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) { // TODO: for moving backward from (x+1)th row's first item to (x)th row's last item
//                                if (position != 0 && position % Utilities.mNumColumnsForFullScreenGridView == 0) {
//                                    moveBackward(position - 1);
//                                    return true;
//                                }
//                                return false;
//                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
//                                if (position % Utilities.mNumColumnsForFullScreenGridView == (Utilities.mNumColumnsForFullScreenGridView - 1)) { // TODO: move from xth row's last item to (x+1)th row's first item
//                                    moveForwardAndBackward(position + 1);
//                                    return true;
//                                } else if (position == getListSize() - 1) { // TODO: block the auto-move of grid-view's last item to prev row's last item
//                                    return true;
//                                }
//                            } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
//                                sendData2NewScreen(mInLinkInfoList.get(position));
//                                return true;
//                            }
//                        } else if (keyAction == KeyEvent.ACTION_UP) {
//                            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) { // TODO: block switching between fragments by Down-Key
//                                return true;
//                            }
//                        }
//                        return false;
//                    }
//                });
                if (mInLinkInfoList.isEmpty()) {
                    Utilities.showLongToast(mContext, "No data available");
                }
                hideLoadingIndicatorView();
            }
        });
    }

    @Override
    public void sendData2NewScreen(LinkInfoContainer info){
        if (info != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(LINKS_LOADER_INFO_TEXT, info);
            String linkType = info.getLinkType();
            if (linkType.equals(Utilities.LINK_TYPE_INDIVIDUAL_LINKS)) {
                if (info.getSource().equalsIgnoreCase(Utilities.SOURCE_YOUTUBE_TEXT)) {
////                    startActivity(new Intent(LinksLoaderActivity.this, AndroidNativePlayerActivity.class).putExtra(LINKS_LOADER_INFO_BUNDLE, bundle));
                    startActivity(new Intent(LinksLoaderActivity.this, YoutubePlayerActivity.class).putExtra(LINKS_LOADER_INFO_BUNDLE, bundle));
                } else if (info.getSource().equalsIgnoreCase(Utilities.SOURCE_FLUSSONIC_TEXT)) {
                    startActivity(new Intent(LinksLoaderActivity.this, FlussonicActivity.class).putExtra(LINKS_LOADER_INFO_BUNDLE, bundle));
                }
            } else if (linkType.equals(Utilities.LINK_TYPE_OTHER_LINKS)) {
                if (info.getSource().equalsIgnoreCase(Utilities.SOURCE_FILMON_TEXT)) {
                    startActivity(new Intent(LinksLoaderActivity.this, FilmonActivity.class).putExtra(LINKS_LOADER_INFO_BUNDLE, bundle));
                } else if (info.getSource().equalsIgnoreCase(Utilities.SOURCE_KARWAN_TEXT)) {
                    startActivity(new Intent(LinksLoaderActivity.this, WebViewActivity.class).putExtra(LINKS_LOADER_INFO_BUNDLE, bundle));
                }
            } else if (linkType.equals(Utilities.LINK_TYPE_CHANNELS)) {
                startActivity(new Intent(LinksLoaderActivity.this, PlayListsActivity.class).putExtra(LINKS_LOADER_INFO_BUNDLE, bundle));
            } else if (linkType.equals(Utilities.LINK_TYPE_PLAYLISTS)) {
                startActivity(new Intent(LinksLoaderActivity.this, VideosActivity.class).putExtra(LINKS_LOADER_INFO_BUNDLE, bundle));
            }
        }
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

    private class InLinksLoaderAsyncTask extends AsyncTask <String, String, String> {
        @Override
        protected String doInBackground(String... params){
            HttpClient httpclient = new DefaultHttpClient();
            String url = Utilities.getIndividualLinksUrl(params[0], params[1], params[2]);
            HttpPost httpPost = new HttpPost(url);
            try {
                List <NameValuePair> nameValuePairs = new ArrayList <>(1);
                nameValuePairs.add(new BasicNameValuePair(Utilities.POST_PARAMETER_NAME, Utilities.POST_PARAMETER_VALUE));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                String result = EntityUtils.toString(response.getEntity());
                if (result != null && !result.isEmpty()) {
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray = object.getJSONArray("individuallinks");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String title = jsonObject.getString("title");
                        String videoUrl = jsonObject.getString("url");
                        String thumbnailUrl = jsonObject.getString("thumbnail");
                        String source = jsonObject.getString("source");
                        String ebound = jsonObject.getString("ebound");
                        String linkId = jsonObject.getString("linkid");
                        mInLinkInfoList.add(new LinkInfoContainer(title, videoUrl, thumbnailUrl, linkId, source, ebound, Utilities.LINK_TYPE_INDIVIDUAL_LINKS));
                    }
                } else {
                    Utilities.showLongToastInUiThread(mActivity, Utilities.SERVER_ERROR_TEXT);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private class OtherLinksLoaderAsyncTask extends AsyncTask <String, String, String> {
        @Override
        protected String doInBackground(String... params){
            HttpClient httpclient = new DefaultHttpClient();
            String url = Utilities.getOtherLinksUrl(params[0], params[1], params[2]);
            HttpPost httpPost = new HttpPost(url);
            try {
                List <NameValuePair> nameValuePairs = new ArrayList <>(1);
                nameValuePairs.add(new BasicNameValuePair(Utilities.POST_PARAMETER_NAME, Utilities.POST_PARAMETER_VALUE));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                String result = EntityUtils.toString(response.getEntity());
                if (result != null && !result.isEmpty()) {
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray = object.getJSONArray("channels");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String title = jsonObject.getString("title");
                        String videoUrl = jsonObject.getString("url");
                        String source = jsonObject.getString("source");
                        String thumbnailUrl = jsonObject.getString("thumbnail");
                        mInLinkInfoList.add(new LinkInfoContainer(id, title, videoUrl, thumbnailUrl, source, Utilities.LINK_TYPE_OTHER_LINKS));
                    }
                } else {
                    Utilities.showLongToastInUiThread(mActivity, Utilities.SERVER_ERROR_TEXT);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private class ChannelsLoaderAsyncTask extends AsyncTask <String, String, String> {
        @Override
        protected String doInBackground(String... params){
            HttpClient httpclient = new DefaultHttpClient();
            String url = Utilities.getChannelsUrl(params[0], params[1], params[2]);
            HttpPost httpPost = new HttpPost(url);
            try {
                List <NameValuePair> nameValuePairs = new ArrayList <>(1);
                nameValuePairs.add(new BasicNameValuePair(Utilities.POST_PARAMETER_NAME, Utilities.POST_PARAMETER_VALUE));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                String result = EntityUtils.toString(response.getEntity());
                if (result != null && !result.isEmpty()) {
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray = object.getJSONArray("channels");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String title = jsonObject.getString("title");
                        String videoUrl = jsonObject.getString("url");
                        String playlistId = jsonObject.getString("channelid");
                        String description = jsonObject.getString("description");
                        String publishDate = jsonObject.getString("publishedat");
                        String thumbnailUrl = jsonObject.getString("thumbnail");
                        mInLinkInfoList.add(new LinkInfoContainer(id, title, videoUrl, thumbnailUrl, playlistId, description, publishDate, Utilities.LINK_TYPE_CHANNELS));
                    }
                } else {
                    Utilities.showLongToastInUiThread(mActivity, Utilities.SERVER_ERROR_TEXT);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private class PlayListsLoaderAsyncTask extends AsyncTask <String, String, String> {
        @Override
        protected String doInBackground(String... params){
            HttpClient httpclient = new DefaultHttpClient();
            String url = Utilities.getPlayListsUrl(params[0], params[1], params[2]);
            HttpPost httpPost = new HttpPost(url);
            try {
                List <NameValuePair> nameValuePairs = new ArrayList <>(1);
                nameValuePairs.add(new BasicNameValuePair(Utilities.POST_PARAMETER_NAME, Utilities.POST_PARAMETER_VALUE));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                String result = EntityUtils.toString(response.getEntity());
                if (result != null && !result.isEmpty()) {
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray = object.getJSONArray("playlists");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String title = jsonObject.getString("title");
                        String videoUrl = jsonObject.getString("url");
                        String thumbnailUrl = jsonObject.getString("thumbnail");
                        String playlistId = jsonObject.getString("playlistid");
                        String description = jsonObject.getString("description");
                        String publishDate = jsonObject.getString("publishedat");
                        mInLinkInfoList.add(new LinkInfoContainer(null, title, videoUrl, thumbnailUrl, playlistId, description, publishDate, Utilities.LINK_TYPE_PLAYLISTS));
                    }
                } else {
                    Utilities.showLongToastInUiThread(mActivity, Utilities.SERVER_ERROR_TEXT);
                }
                setAdapterToListViewAndCancelDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
