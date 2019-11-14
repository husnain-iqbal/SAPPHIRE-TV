package com.live.sapphire.tv.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.live.sapphire.tv.Adapters.SubcategoriesAdapter;
import com.live.sapphire.tv.Customized.ChildCategoryInfo;
import com.live.sapphire.tv.Customized.SubcategoryInfo;
import com.live.sapphire.tv.R;
import com.live.sapphire.tv.Utilities;
import com.live.sapphire.tv.activities.ChildCategoriesActivity;
import com.live.sapphire.tv.activities.LinksLoaderActivity;

import java.util.ArrayList;

public class SubcategoriesFragment extends Fragment implements SubcategoriesAdapter.SendData {

    private Activity mActivity;
    //    private GridView mGridView;
    private RecyclerView mRecyclerView;
    private SubcategoriesAdapter mSubcategoryAdapter;
    private ArrayList <SubcategoryInfo> mSubcategoriesList;
    public static final String SUBCATEGORIES_SERIALIZABLE_INFO_TEXT = "SubcategoriesFragmentInfo";
    public static final String SUBCATEGORIES_BUNDLE_TEXT = "SubcategoriesFragmentBundle";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        mSubcategoriesList = new ArrayList <>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_subcategories, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recyclerView_subcategories);
        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList <SubcategoryInfo> subcategoryList = (ArrayList <SubcategoryInfo>)bundle.getSerializable(CategoriesFragment.CATEGORIES_FRAGMENT_SERIALIZABLE_OBJECT_TEXT);
            if (subcategoryList != null) {
                populateGridView(subcategoryList);
            }
        }
        return view;
    }

    public void populateGridView(ArrayList <SubcategoryInfo> subcategoryList){
        mSubcategoriesList = subcategoryList;
        mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, Utilities.mNumColumnsForSideGridView));
        mSubcategoryAdapter = new SubcategoriesAdapter(SubcategoriesFragment.this, mActivity, mSubcategoriesList);
        mRecyclerView.setAdapter(mSubcategoryAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
//        mSubcategoryAdapter = new SubcategoriesAdapter(this, mActivity, subcategoryList);
//        mGridView.setAdapter(mSubcategoryAdapter);
//        mGridView.setSmoothScrollbarEnabled(true);
//        mGridView.setNumColumns(Utilities.mNumColumnsForSideGridView);
    }

    public void selectFirstItem(){
        View v = mRecyclerView.getLayoutManager().findViewByPosition(0);
        if (v != null) {
            v.requestFocus();
        }
    }

    @Override
    public void sendData2NewScreen(SubcategoryInfo subcategoryInfo){
        ArrayList <ChildCategoryInfo> childCategoryList = subcategoryInfo.getChildCategoryList();
        if (childCategoryList != null && !childCategoryList.isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(SUBCATEGORIES_SERIALIZABLE_INFO_TEXT, childCategoryList);
            startActivity(new Intent(mActivity, ChildCategoriesActivity.class).putExtra(SUBCATEGORIES_BUNDLE_TEXT, bundle));
        }
        else {
            Bundle bundle = new Bundle();
            bundle.putSerializable(SUBCATEGORIES_SERIALIZABLE_INFO_TEXT, subcategoryInfo);
            startActivity(new Intent(mActivity, LinksLoaderActivity.class).putExtra(SUBCATEGORIES_BUNDLE_TEXT, bundle));
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
