package com.live.sapphire.tv.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.live.sapphire.tv.Adapters.ChildCategoriesAdapter;
import com.live.sapphire.tv.Customized.ChildCategoryInfo;
import com.live.sapphire.tv.Fragments.SubcategoriesFragment;
import com.live.sapphire.tv.R;
import com.live.sapphire.tv.Utilities;

import java.util.ArrayList;

public class ChildCategoriesActivity extends AppCompatActivity implements ChildCategoriesAdapter.SendData {

    //    private GridView mGridView;
    private RecyclerView mRecyclerView;
    public static final String CHILD_CATEGORIES_SERIALIZABLE_OBJECT_TEXT = "ChildCategoriesSerializableObject";
    public static final String CHILD_CATEGORIES_BUNDLE_TEXT = "ChildCategoriesBundle";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_categories);
//        mGridView = (GridView) findViewById(R.id.gridView_child_categories);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView_child_categories);
        Intent intent = getIntent();
        if (intent.hasExtra(SubcategoriesFragment.SUBCATEGORIES_BUNDLE_TEXT)) {
            Bundle bundle = intent.getBundleExtra(SubcategoriesFragment.SUBCATEGORIES_BUNDLE_TEXT);
            final ArrayList <ChildCategoryInfo> childCategoryInfoList = (ArrayList <ChildCategoryInfo>)bundle.getSerializable(SubcategoriesFragment.SUBCATEGORIES_SERIALIZABLE_INFO_TEXT);
            if (childCategoryInfoList != null) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(ChildCategoriesActivity.this, Utilities.mNumColumnsForFullScreenGridView));
                ChildCategoriesAdapter childCategoryAdapter = new ChildCategoriesAdapter(ChildCategoriesActivity.this, ChildCategoriesActivity.this, childCategoryInfoList);
                mRecyclerView.setAdapter(childCategoryAdapter);
//                mRecyclerView.getLayoutManager().scrollToPosition(0);
//                Utilities.showShortToast(ChildCategoriesActivity.this, "Child Categories...");

//                final ChildCategoriesAdapter childCategoryAdapter = new ChildCategoriesAdapter(this, ChildCategoriesActivity.this, childCategoryInfoList);
//                mGridView.setAdapter(childCategoryAdapter);
//                mGridView.setNumColumns(Utilities.mNumColumnsForFullScreenGridView);
//                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                    if (childCategoryInfoList != null && !childCategoryInfoList.isEmpty()) {
//                        sendData2NewScreen(childCategoryInfoList.get(position));
//                    }
//                }
//            });
            }
        }
    }

    @Override
    public void sendData2NewScreen(ChildCategoryInfo info){
        Bundle bundle = new Bundle();
        bundle.putSerializable(CHILD_CATEGORIES_SERIALIZABLE_OBJECT_TEXT, info);
        startActivity(new Intent(ChildCategoriesActivity.this, LinksLoaderActivity.class).putExtra(CHILD_CATEGORIES_BUNDLE_TEXT, bundle));
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
