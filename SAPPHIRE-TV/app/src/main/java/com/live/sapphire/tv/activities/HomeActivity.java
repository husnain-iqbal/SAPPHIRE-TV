package com.live.sapphire.tv.activities;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.live.sapphire.tv.Fragments.CategoriesFragment;
import com.live.sapphire.tv.Fragments.SubcategoriesFragment;
import com.live.sapphire.tv.R;
import com.live.sapphire.tv.Utilities;
import com.wang.avi.AVLoadingIndicatorView;

public class HomeActivity extends AppCompatActivity {

    private static int mTurnCounter = 0;
    private AVLoadingIndicatorView mIndicatorView;
    public static final String CATEGORIES_SERIALIZABLE_OBJECT_TEXT = "serializable_object_activity1";
    public static final String CATEGORIES_BUNDLE_TEXT = "serializable_object_bundle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.categories_fragment, new CategoriesFragment(), Utilities.FRAGMENT_CATEGORIES_TAG);
        fragmentTransaction.add(R.id.subcategories_fragment, new SubcategoriesFragment(), Utilities.FRAGMENT_SUBCATEGORIES_TAG);
        fragmentTransaction.commit();
        showLoadingIndicatorView();
    }

    private void showLoadingIndicatorView() {
        mIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.avliv_activity_categories);
        mIndicatorView.setVisibility(View.VISIBLE);
//        colorTimer(1000);
    }

    public void hideLoadingIndicatorView() {
        mIndicatorView.setVisibility(View.GONE);
//        findViewById(R.id.avliv_activity_categories).setVisibility(View.GONE);
    }

    private void colorTimer(final int time) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIndicatorView.getVisibility() != View.VISIBLE) {
                    return;
                }
                int indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.red);
                switch (mTurnCounter) {
                    case 1:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.deep_pink);
                        break;
                    case 2:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.orange);
                        break;
                    case 3:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.blue);
                        break;
                    case 4:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.indigo);
                        break;
                    case 5:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.purple);
                        break;
                    case 6:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.maroon);
                        break;
                    case 7:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.black);
                        break;
                    case 8:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.shadow);
                        break;
                    case 9:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.grey);
                        break;
                    case 10:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.light_cyan);
                        break;
                    case 11:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.aquamarine);
                        break;
                    case 12:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.dark_cyan);
                        break;
                    case 13:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.teal);
                        break;
                    case 14:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.green);
                        break;
                    case 15:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.lime);
                        break;
                    case 16:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.green_yellow);
                        break;
                    case 17:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.light_yellow);
                        break;
                    case 18:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.yellow);
                        break;
                    case 19:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.golden);
                        mTurnCounter = 0;
                        break;
                    default:
                        indicatorViewColor = ContextCompat.getColor(HomeActivity.this, R.color.red);
                        mTurnCounter = 0;
                        break;
                }
                mIndicatorView.setIndicatorColor(indicatorViewColor);
                mTurnCounter += 1;
            }
        }, time);
    }
}
