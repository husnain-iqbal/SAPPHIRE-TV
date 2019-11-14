package com.live.sapphire.tv.Fragments;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.live.sapphire.tv.Adapters.CategoriesAdapter;
import com.live.sapphire.tv.Customized.CategoryInfo;
import com.live.sapphire.tv.Customized.ChildCategoryInfo;
import com.live.sapphire.tv.Customized.LinkInfoContainer;
import com.live.sapphire.tv.Customized.SubcategoryInfo;
import com.live.sapphire.tv.R;
import com.live.sapphire.tv.Utilities;
import com.live.sapphire.tv.activities.HomeActivity;

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

public class CategoriesFragment extends Fragment implements CategoriesAdapter.SendData {

    private Activity mActivity;
    private ListView mListView;
    private boolean isAdapterSet;
    private CategoriesAdapter mCategoryAdapter;
    private ArrayList <CategoryInfo> mCategoriesList;
    public static final String CATEGORIES_FRAGMENT_SERIALIZABLE_OBJECT_TEXT = "serializable_object_activity1";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        isAdapterSet = false;
        mActivity = getActivity();
        mCategoriesList = new ArrayList <>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        mListView = (ListView)view.findViewById(R.id.gridView_categories);
        new DataLoaderAsyncTask().execute(Utilities.CATEGORIES_URL);
        return view;
    }

    @Override
    public void sendData2NewScreen(CategoryInfo info){
        ArrayList <SubcategoryInfo> subcategoryList = info.getSubcategoriesList();
        if (subcategoryList != null && !subcategoryList.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(CATEGORIES_FRAGMENT_SERIALIZABLE_OBJECT_TEXT, subcategoryList);
            Fragment fragment = new SubcategoriesFragment();
            fragment.setArguments(bundle);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.subcategories_fragment, fragment, Utilities.FRAGMENT_SUBCATEGORIES_TAG);
            ft.commit();
            Utilities.mCurrentFragment = Utilities.FRAGMENT_SUBCATEGORIES_TEXT;
        }
        else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(CATEGORIES_FRAGMENT_SERIALIZABLE_OBJECT_TEXT, info.getLinksInfoContainerList());
            Fragment fragment = new LinksLoaderFragment();
            fragment.setArguments(bundle);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.subcategories_fragment, fragment, Utilities.FRAGMENT_LINKS_LOADER_TAG);
            ft.commit();
            Utilities.mCurrentFragment = Utilities.FRAGMENT_LINKS_LOADER_TEXT;
        }
    }

    @Override
    public void selectItem(int position){
        mListView.setSelection(position);
//        mListView.setItemChecked(position, true);
    }

    @Override
    public void selectRightFragmentFirstItem(){
        if (Utilities.mCurrentFragment.equals(Utilities.FRAGMENT_SUBCATEGORIES_TEXT)) {
            SubcategoriesFragment fragment = (SubcategoriesFragment)getFragmentManager().findFragmentById(R.id.subcategories_fragment);
            fragment.selectFirstItem();
        }
        else if (Utilities.mCurrentFragment.equals(Utilities.FRAGMENT_LINKS_LOADER_TEXT)) {
            LinksLoaderFragment fragment = (LinksLoaderFragment)getFragmentManager().findFragmentById(R.id.subcategories_fragment);
            fragment.selectFirstItem();
        }
    }

    //    private void loadCategories(String url) {
//        JSONArray jsonArray = new JSONArray();
//        try {
//            jsonArray.put(1, "e");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        JsonArrayRequest jsonObject = new JsonArrayRequest(Request.Method.POST, url, jsonArray, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                for (int i = 0; i < response.length(); i++) {
//                    try {
//                        JSONObject jsonCategoryObject = response.getJSONObject(i);
//                        String categoryName = jsonCategoryObject.getString("Main_Cat_name");
//                        String categoryThumbnail = jsonCategoryObject.getString("thumbnail");
//                        String categoryId = jsonCategoryObject.getString("id");
//                        String subcategoryId = jsonCategoryObject.getString("subcatid");
//                        String mainCategoryId = jsonCategoryObject.getString("maincatid");
//                        if (jsonCategoryObject.has("subcategories")) {
//                            JSONArray subcategoriesArray = jsonCategoryObject.getJSONArray("subcategories");
//                            for (int j = 0; j < subcategoriesArray.length(); j++) {
//                                JSONObject jsonSubcategoryObject = subcategoriesArray.getJSONObject(j);
//                                String subcategoryName = jsonSubcategoryObject.getString("Sub_Cat_name");
//                                String subcategoryThumbnail = jsonSubcategoryObject.getString("thumbnail");
//                                String id = jsonSubcategoryObject.getString("id");
//                                String subcatid = jsonSubcategoryObject.getString("subcatid");
//                                String maincatId = jsonSubcategoryObject.getString("maincatid");
//                            }
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.v("@MyWebService", error.getMessage());
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("token", "Z3YxdDllSHlMWVN3TW9kQUtDOGRjdHZrN25zcVIyOXk=");
//                return super.getParams();
//            }
//
//            @Override
//            protected Response<JSONArray> parseNetworkResponse(NetworkResponse networkResponse) {
//                try {
//                    String jsonString = new String(networkResponse.data,
//                            HttpHeaderParser
//                                    .parseCharset(networkResponse.headers));
//                    return Response.success(new JSONArray(jsonString),
//                            HttpHeaderParser
//                                    .parseCacheHeaders(networkResponse));
//                } catch (UnsupportedEncodingException e) {
//                    return Response.error(new ParseError(e));
//                } catch (JSONException je) {
//                    return Response.error(new ParseError(je));
//                }
//            }
//        };
//        AppController.getInstances().addToRequestQueue(jsonObject);
//    }

//    private void loadCategories2(String url) {
//        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
//                url, null,
//                new Response.Listener<JSONArray>() {
//
//                    @Override
//                    public void onResponse(JSONArray response) {
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("token", "Z3YxdDllSHlMWVN3TW9kQUtDOGRjdHZrN25zcVIyOXk=");
//                return headers;
//            }
//        };
//        AppController.getInstances().addToRequestQueue(jsonObjReq);
//    }

    private void loadMoreData(){
        for (int i = 0; i < mCategoriesList.size(); i++) {
            CategoryInfo categoryInfo = mCategoriesList.get(i);
            ArrayList <SubcategoryInfo> subcategoryInfoList = categoryInfo.getSubcategoriesList();
            if (subcategoryInfoList == null || subcategoryInfoList.isEmpty()) {
                new InLinksLoaderAsyncTask(i).execute(categoryInfo);
            }
        }
    }

    private void populateGridViewAndSetAdapter(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run(){
                if (isAdapterSet) {
                    mCategoryAdapter.notifyDataSetChanged();
                }
                else {
                    mCategoryAdapter = new CategoriesAdapter(CategoriesFragment.this, mActivity, mCategoriesList);
                    mListView.setAdapter(mCategoryAdapter);
                    mListView.setItemsCanFocus(true);
                    if (mCategoriesList.size() >= 1) {
                        mListView.setSelection(0);
//                        mListView.setItemChecked(0, true);
                        sendData2NewScreen(mCategoriesList.get(0));
                    }
                    isAdapterSet = true;
                }
                ((HomeActivity)getActivity()).hideLoadingIndicatorView();
            }
        });
    }

    private class DataLoaderAsyncTask extends AsyncTask <String, String, String> {
        @Override
        protected String doInBackground(String... urls){
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(urls[0]);
            try {
                List <NameValuePair> nameValuePairs = new ArrayList <>(1);
                nameValuePairs.add(new BasicNameValuePair(Utilities.POST_PARAMETER_NAME, Utilities.POST_PARAMETER_VALUE));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                String result = EntityUtils.toString(response.getEntity());
                if (result != null && !result.isEmpty()) {
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonCategoryArray = object.getJSONArray("categories");
                    for (int i = 0; i < jsonCategoryArray.length(); i++) {
                        JSONObject jsonCategoryObject = jsonCategoryArray.getJSONObject(i);
                        String categoryId = jsonCategoryObject.getString("id");
                        String categoryName = jsonCategoryObject.getString("Main_Cat_name");
                        String categoryThumbnailUrl = jsonCategoryObject.getString("thumbnail");
                        String subcategoryId = jsonCategoryObject.getString("subcatid");
                        String mainCategoryId = jsonCategoryObject.getString("maincatid");

                        ArrayList <SubcategoryInfo> subcategoriesList = new ArrayList <>();
                        if (jsonCategoryObject.has("subcategories")) {
                            JSONArray subcategoriesArray = jsonCategoryObject.getJSONArray("subcategories");
                            for (int j = 0; j < subcategoriesArray.length(); j++) {
                                JSONObject jsonSubcategoryObject = subcategoriesArray.getJSONObject(j);
                                String sub_categoryId = jsonSubcategoryObject.getString("id");
                                String sub_categoryName = jsonSubcategoryObject.getString("Sub_Cat_name");
                                String sub_categoryThumbnailUrl = jsonSubcategoryObject.getString("thumbnail");
                                String sub_subcategoryId = jsonSubcategoryObject.getString("subcatid");
                                String sub_mainCategoryId = jsonSubcategoryObject.getString("maincatid");

                                ArrayList <ChildCategoryInfo> childCategoriesList = new ArrayList <>();
                                if (jsonSubcategoryObject.has("childcategories")) {
                                    JSONArray childCategoriesArray = jsonSubcategoryObject.getJSONArray("childcategories");
                                    for (int k = 0; k < childCategoriesArray.length(); k++) {
                                        JSONObject jsonMainCategoryObject = childCategoriesArray.getJSONObject(k);
                                        String child_categoryName = jsonMainCategoryObject.getString("Child_Cat_name");
                                        String child_categoryId = jsonMainCategoryObject.getString("catid");
                                        String child_subcategoryId = jsonMainCategoryObject.getString("subcatid");
                                        String child_mainCategoryId = jsonMainCategoryObject.getString("maincatid");
                                        String child_thumbnailUrl = jsonMainCategoryObject.getString("thumbnail");
                                        childCategoriesList.add(new ChildCategoryInfo(child_categoryName, child_categoryId, child_subcategoryId, child_mainCategoryId, child_thumbnailUrl));
                                    }
                                }
                                subcategoriesList.add(new SubcategoryInfo(sub_categoryId, sub_categoryName, sub_subcategoryId, sub_mainCategoryId, sub_categoryThumbnailUrl, childCategoriesList));
                            }
                        }
                        mCategoriesList.add(new CategoryInfo(categoryId, categoryName, subcategoryId, mainCategoryId, categoryThumbnailUrl, subcategoriesList));
                    }
                    loadMoreData();
//                    populateGridViewAndSetAdapter();
                }
                else {
                    Utilities.showLongToastInUiThread(mActivity, Utilities.SERVER_ERROR_TEXT);
                }
            }
            catch (Exception e) {
                e.printStackTrace();

            }
            return null;
        }
    }


    private class InLinksLoaderAsyncTask extends AsyncTask <CategoryInfo, String, String> {

        private int categoryListItemIndex;

        InLinksLoaderAsyncTask(int index){
            categoryListItemIndex = index;
        }

        @Override
        protected String doInBackground(CategoryInfo... params){
            HttpClient httpclient = new DefaultHttpClient();
            final CategoryInfo categoryInfo = params[0];
            String url = Utilities.getIndividualLinksUrl(categoryInfo.getId(), categoryInfo.getSubcategoryId(), categoryInfo.getMainCategoryId());
            HttpPost httpPost = new HttpPost(url);
            try {
                List <NameValuePair> nameValuePairs = new ArrayList <>(1);
                nameValuePairs.add(new BasicNameValuePair(Utilities.POST_PARAMETER_NAME, Utilities.POST_PARAMETER_VALUE));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                String result = EntityUtils.toString(response.getEntity());
                if (result != null && !result.isEmpty()) {
                    ArrayList <LinkInfoContainer> inLinkInfoList = new ArrayList <>();
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
                        inLinkInfoList.add(new LinkInfoContainer(title, videoUrl, thumbnailUrl, linkId, source, ebound, Utilities.LINK_TYPE_INDIVIDUAL_LINKS));
                    }
                    categoryInfo.setLinksInfoContainerList(inLinkInfoList);
                    mCategoriesList.remove(categoryListItemIndex);
                    mCategoriesList.add(categoryListItemIndex, categoryInfo);
                }
                else {
                    Utilities.showLongToastInUiThread(mActivity, Utilities.SERVER_ERROR_TEXT);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run(){
                    new OtherLinksLoaderAsyncTask(categoryListItemIndex).execute(categoryInfo);
                }
            });
            return null;
        }
    }


    private class OtherLinksLoaderAsyncTask extends AsyncTask <CategoryInfo, String, String> {

        private int categoryListItemIndex;

        OtherLinksLoaderAsyncTask(int index){
            categoryListItemIndex = index;
        }

        @Override
        protected String doInBackground(CategoryInfo... params){
            HttpClient httpclient = new DefaultHttpClient();
            final CategoryInfo categoryInfo = params[0];
            String url = Utilities.getOtherLinksUrl(categoryInfo.getId(), categoryInfo.getSubcategoryId(), categoryInfo.getMainCategoryId());
            HttpPost httpPost = new HttpPost(url);
            try {
                List <NameValuePair> nameValuePairs = new ArrayList <>(1);
                nameValuePairs.add(new BasicNameValuePair(Utilities.POST_PARAMETER_NAME, Utilities.POST_PARAMETER_VALUE));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                String result = EntityUtils.toString(response.getEntity());
                if (result != null && !result.isEmpty()) {
                    ArrayList <LinkInfoContainer> inLinkInfoList = categoryInfo.getLinksInfoContainerList();
                    JSONObject object = new JSONObject(result);
                    JSONArray jsonArray = object.getJSONArray("channels");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String title = jsonObject.getString("title");
                        String videoUrl = jsonObject.getString("url");
                        String source = jsonObject.getString("source");
                        String thumbnailUrl = jsonObject.getString("thumbnail");
                        inLinkInfoList.add(new LinkInfoContainer(id, title, videoUrl, thumbnailUrl, source, Utilities.LINK_TYPE_OTHER_LINKS));
                    }
                    categoryInfo.setLinksInfoContainerList(inLinkInfoList);
                    mCategoriesList.remove(categoryListItemIndex);
                    mCategoriesList.add(categoryListItemIndex, categoryInfo);
                }
                else {
                    Utilities.showLongToastInUiThread(mActivity, Utilities.SERVER_ERROR_TEXT);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run(){
                    new ChannelsLoaderAsyncTask(categoryListItemIndex).execute(categoryInfo);
                }
            });
            return null;
        }
    }


    private class ChannelsLoaderAsyncTask extends AsyncTask <CategoryInfo, String, String> {

        private int categoryListItemIndex;

        ChannelsLoaderAsyncTask(int index){
            categoryListItemIndex = index;
        }

        @Override
        protected String doInBackground(CategoryInfo... params){
            HttpClient httpclient = new DefaultHttpClient();
            final CategoryInfo categoryInfo = params[0];
            String url = Utilities.getChannelsUrl(categoryInfo.getId(), categoryInfo.getSubcategoryId(), categoryInfo.getMainCategoryId());
            HttpPost httpPost = new HttpPost(url);
            try {
                List <NameValuePair> nameValuePairs = new ArrayList <>(1);
                nameValuePairs.add(new BasicNameValuePair(Utilities.POST_PARAMETER_NAME, Utilities.POST_PARAMETER_VALUE));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                String result = EntityUtils.toString(response.getEntity());
                if (result != null && !result.isEmpty()) {
                    ArrayList <LinkInfoContainer> inLinkInfoList = categoryInfo.getLinksInfoContainerList();
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
                        inLinkInfoList.add(new LinkInfoContainer(id, title, videoUrl, thumbnailUrl, playlistId, description, publishDate, Utilities.LINK_TYPE_CHANNELS));
                    }
                    categoryInfo.setLinksInfoContainerList(inLinkInfoList);
                    mCategoriesList.remove(categoryListItemIndex);
                    mCategoriesList.add(categoryListItemIndex, categoryInfo);
                }
                else {
                    Utilities.showLongToastInUiThread(mActivity, Utilities.SERVER_ERROR_TEXT);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run(){
                    new PlayListsLoaderAsyncTask(categoryListItemIndex).execute(categoryInfo);
                }
            });
            return null;
        }
    }


    private class PlayListsLoaderAsyncTask extends AsyncTask <CategoryInfo, String, String> {

        private int categoryListItemIndex;

        PlayListsLoaderAsyncTask(int index){
            categoryListItemIndex = index;
        }

        @Override
        protected String doInBackground(CategoryInfo... params){
            HttpClient httpclient = new DefaultHttpClient();
            CategoryInfo categoryInfo = params[0];
            String url = Utilities.getPlayListsUrl(categoryInfo.getId(), categoryInfo.getSubcategoryId(), categoryInfo.getMainCategoryId());
            HttpPost httpPost = new HttpPost(url);
            try {
                List <NameValuePair> nameValuePairs = new ArrayList <>(1);
                nameValuePairs.add(new BasicNameValuePair(Utilities.POST_PARAMETER_NAME, Utilities.POST_PARAMETER_VALUE));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                String result = EntityUtils.toString(response.getEntity());
                if (result != null && !result.isEmpty()) {
                    ArrayList <LinkInfoContainer> inLinkInfoList = categoryInfo.getLinksInfoContainerList();
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
                        inLinkInfoList.add(new LinkInfoContainer(null, title, videoUrl, thumbnailUrl, playlistId, description, publishDate, Utilities.LINK_TYPE_PLAYLISTS));
                    }
                    categoryInfo.setLinksInfoContainerList(inLinkInfoList);
                    mCategoriesList.remove(categoryListItemIndex);
                    mCategoriesList.add(categoryListItemIndex, categoryInfo);
                }
                else {
                    Utilities.showLongToastInUiThread(mActivity, Utilities.SERVER_ERROR_TEXT);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            populateGridViewAndSetAdapter();
            return null;
        }
    }
}
