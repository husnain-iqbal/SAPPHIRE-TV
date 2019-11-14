package com.live.sapphire.tv.Adapters;


import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.live.sapphire.tv.Customized.CategoryInfo;
import com.live.sapphire.tv.R;

import java.util.ArrayList;

public class CategoriesAdapter extends BaseAdapter {
    //    private Activity mActivity;
    private Context mContext;
    private SendData mSendData;
    private int mCurrentItemPosition;
    private LayoutInflater mLayoutInflater;
    private ArrayList <CategoryInfo> mCategoryList;
    private static boolean isCategoriesFragmentFocused;

    public CategoriesAdapter(SendData sendData, Activity activity, ArrayList <CategoryInfo> categoryList){
//        mActivity = activity;
        mContext = activity.getApplicationContext();
        mSendData = sendData;
        mCategoryList = categoryList;
        isCategoriesFragmentFocused = true;
        mLayoutInflater = LayoutInflater.from(activity.getApplicationContext());
    }

    @Override
    public int getCount(){
        return mCategoryList.size();
    }

    @Override
    public Object getItem(int position){
        return mCategoryList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent){
        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.adapter_layout_categories, parent, false);
            holder = new ViewHolder();
            holder.button = (Button)view.findViewById(R.id.list_button);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder)view.getTag();
        }
        holder.button.setFocusable(true);
        holder.button.setFocusableInTouchMode(true);
        CategoryInfo info = mCategoryList.get(position);
        holder.button.setText(info.getCategoryName());
        holder.button.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus){
                if (isCategoriesFragmentFocused) {
                    if (hasFocus) {
                        mSendData.sendData2NewScreen(mCategoryList.get(position));
                        holder.button.setTextColor(ContextCompat.getColor(mContext, R.color.golden));
                    }
                    else {
                        holder.button.setTextColor(ContextCompat.getColor(mContext, R.color.aquamarine));
                    }
                }
            }
        });
        //TODO: Only for testing on mobile phone... Delete if not required
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                mSendData.sendData2NewScreen(mCategoryList.get(position));
            }
        });
        holder.button.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent){
                int keyAction = keyEvent.getAction();
                if (keyAction == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        setCurrentItemPosition(position);
                        isCategoriesFragmentFocused = false;
                        mSendData.selectRightFragmentFirstItem();
                        return true;
                    }
                    else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        if (isLastItem(position)) {
                            mSendData.selectItem(0);
                            return true;
                        }
                    }
                    else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        if (isFirstItem(position)) {
                            mSendData.selectItem(getLastItemPosition());
                            return true;
                        }
                    }
                    return false;
                }
                else if (keyAction == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        int currentItemPosition = getCurrentItemPosition();
                        mSendData.selectItem(currentItemPosition);
                        isCategoriesFragmentFocused = true;
                        return true;
                    }
                    else if (keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        isCategoriesFragmentFocused = true;
                        setCurrentItemPosition(position);
                    }
                    return false;
                }
                return false;
            }
        });
        return view;
    }

    private boolean isLastItem(int position){
        return getCount() == position + 1;
    }

    private boolean isFirstItem(int position){
        return position == 0;
    }

    private int getLastItemPosition(){
        return getCount() - 1;
    }

    private int getCurrentItemPosition(){
        return mCurrentItemPosition;
    }

    private void setCurrentItemPosition(int position){
        mCurrentItemPosition = position;
    }

    private static class ViewHolder {
        Button button;
    }


    public interface SendData {
        void sendData2NewScreen(CategoryInfo categoryInfo);
        void selectItem(int position);
        void selectRightFragmentFirstItem();
    }
}
