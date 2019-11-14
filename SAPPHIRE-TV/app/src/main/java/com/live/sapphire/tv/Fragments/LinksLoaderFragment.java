package com.live.sapphire.tv.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.live.sapphire.tv.Adapters.LinksLoaderFragmentAdapter;
import com.live.sapphire.tv.Customized.LinkInfoContainer;
import com.live.sapphire.tv.R;
import com.live.sapphire.tv.Utilities;
import com.live.sapphire.tv.activities.FilmonActivity;
import com.live.sapphire.tv.activities.FlussonicActivity;
import com.live.sapphire.tv.activities.PlayListsActivity;
import com.live.sapphire.tv.activities.VideosActivity;
import com.live.sapphire.tv.activities.WebViewActivity;
import com.live.sapphire.tv.activities.YoutubePlayerActivity;

import java.util.ArrayList;

public class LinksLoaderFragment extends Fragment implements LinksLoaderFragmentAdapter.SendData {

    private Activity mActivity;
    private Context mContext;
    //    private GridView mGridView;
    private RecyclerView mRecyclerView;
    private ArrayList <LinkInfoContainer> mInfoList;
    private LinksLoaderFragmentAdapter mInLinksAdapter;
    public static final String LINKS_LOADER_INFO_TEXT = "InLinksInfoSerializableObject";
    public static final String LINKS_LOADER_INFO_BUNDLE = "InLinksInfoBundle";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mContext = mActivity.getBaseContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_links_loader, container, false);
//        mGridView = (GridView) v.findViewById(R.id.gridView_links_loader);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.recyclerView_links_loader);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mInfoList = (ArrayList <LinkInfoContainer>)bundle.getSerializable(CategoriesFragment.CATEGORIES_FRAGMENT_SERIALIZABLE_OBJECT_TEXT);
            if (mInfoList != null) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, Utilities.mNumColumnsForSideGridView));
                mInLinksAdapter = new LinksLoaderFragmentAdapter(LinksLoaderFragment.this, mActivity, mInfoList);
                mRecyclerView.setAdapter(mInLinksAdapter);
                mRecyclerView.setNestedScrollingEnabled(false);
//                mInLinksAdapter = new LinksLoaderFragmentAdapter(LinksLoaderFragment.this, mActivity, mInfoList);
//                mGridView.setAdapter(mInLinksAdapter);
//                mGridView.setSmoothScrollbarEnabled(true);
//                mGridView.setNumColumns(Utilities.mNumColumnsForSideGridView);
                if (mInfoList.isEmpty()) {
                    Utilities.showLongToast(mContext, "No data available");
                }
            }
        }
        return v;
    }

    public void selectFirstItem(){
        View v = mRecyclerView.getLayoutManager().findViewByPosition(0);
        if (v != null) {
            v.requestFocus();
        }
    }

    @Override
    public void sendData2NewScreen(LinkInfoContainer inLinksInfo){
        if (inLinksInfo != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(LINKS_LOADER_INFO_TEXT, inLinksInfo);
            String linkType = inLinksInfo.getLinkType();
            if (linkType.equals(Utilities.LINK_TYPE_INDIVIDUAL_LINKS)) {
                if (inLinksInfo.getSource().equalsIgnoreCase(Utilities.SOURCE_YOUTUBE_TEXT)) {
//                    startActivity(new Intent(mActivity, AndroidNativePlayerActivity.class).putExtra(LINKS_LOADER_INFO_BUNDLE, bundle));
                    startActivity(new Intent(mActivity, YoutubePlayerActivity.class).putExtra(LINKS_LOADER_INFO_BUNDLE, bundle));
                }
                else if (inLinksInfo.getSource().equalsIgnoreCase(Utilities.SOURCE_FLUSSONIC_TEXT)) {
                    startActivity(new Intent(mActivity, FlussonicActivity.class).putExtra(LINKS_LOADER_INFO_BUNDLE, bundle));
                }
            }
            else if (linkType.equals(Utilities.LINK_TYPE_OTHER_LINKS)) {
                if (inLinksInfo.getSource().equalsIgnoreCase(Utilities.SOURCE_FILMON_TEXT)) {
                    startActivity(new Intent(mActivity, FilmonActivity.class).putExtra(LINKS_LOADER_INFO_BUNDLE, bundle));
                }
                else if (inLinksInfo.getSource().equalsIgnoreCase(Utilities.SOURCE_KARWAN_TEXT)) {
                    startActivity(new Intent(mActivity, WebViewActivity.class).putExtra(LINKS_LOADER_INFO_BUNDLE, bundle));
                }
            }
            else if (linkType.equals(Utilities.LINK_TYPE_CHANNELS)) {
                startActivity(new Intent(mActivity, PlayListsActivity.class).putExtra(LINKS_LOADER_INFO_BUNDLE, bundle));
            }
            else if (linkType.equals(Utilities.LINK_TYPE_PLAYLISTS)) {
                startActivity(new Intent(mActivity, VideosActivity.class).putExtra(LINKS_LOADER_INFO_BUNDLE, bundle));
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
}
