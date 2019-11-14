package com.live.sapphire.tv.Adapters;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.live.sapphire.tv.AppConfigurations;
import com.live.sapphire.tv.Customized.LinkInfoContainer;
import com.live.sapphire.tv.R;
import com.live.sapphire.tv.Utilities;

import java.util.ArrayList;


public class LinksLoaderFragmentAdapter extends RecyclerView.Adapter <LinksLoaderFragmentAdapter.ViewHolder> {
    //    private Activity mActivity;
    private Context mContext;
    private SendData mSendData;
    private final ArrayList <LinkInfoContainer> mInLinksList;


    public LinksLoaderFragmentAdapter(SendData sendData, Activity activity, ArrayList <LinkInfoContainer> inLinksInfoLint){
//        mActivity = activity;
        mContext = activity.getApplicationContext();
        mSendData = sendData;
        mInLinksList = inLinksInfoLint;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_common_full_screen_recycler_view, parent, false); //TODO: change parent param to null
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        viewHolder.bind(mInLinksList.get(position), position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public int getItemCount(){
        return mInLinksList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Button button;
        private ImageView imageView;

        public ViewHolder(View view){
            super(view);
            button = (Button)view.findViewById(R.id.common_grid_button);
            imageView = (ImageView)view.findViewById(R.id.common_grid_image);
        }

        void bind(final LinkInfoContainer info, final int position){
            button.setText(info.getTitle());
            AppConfigurations.setImage2ImageView(mContext, imageView, info.getThumbnailUrl());
            itemView.setFocusable(true);
            itemView.setFocusableInTouchMode(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    mSendData.sendData2NewScreen(info);
                }
            });
            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus){
                    if (hasFocus) {
                        AppConfigurations.setFocusOnCurrentItem(mContext, view, button);
                    }
                    else {
                        AppConfigurations.releaseFocusFromCurrentItem(mContext, view, button);
                    }
                }
            });
            itemView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int keyCode, KeyEvent keyEvent){
                    int keyAction = keyEvent.getAction();
                    if (keyAction == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) { // TODO: for moving UP in grid-view
                            int movingUpCursor = position - Utilities.mNumColumnsForSideGridView;
                            if (movingUpCursor >= 0) {
                                mSendData.moveUpAndDown(movingUpCursor);
                                return true;
//                                return false;
                            }
                            else {
                                return true;
                            }
                        }
                        else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) { // TODO: for moving DOWN in grid-view
                            int movingDownCursor = position + Utilities.mNumColumnsForSideGridView;
                            if (movingDownCursor <= getItemCount() - 1) {
                                mSendData.moveUpAndDown(movingDownCursor);
                                return true;
//                                return false;
                            }
                            else {
                                return true;
                            }
                        }
                        else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) { // TODO: for moving backward from (x+1)th row's first item to (x)th row's last item
                            if (position != 0 && position % Utilities.mNumColumnsForSideGridView == 0) {
                                mSendData.moveForwardAndBackward(position - 1);
                                return true;
                            }
                            return false;
                        }
                        else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            if (position % Utilities.mNumColumnsForSideGridView == (Utilities.mNumColumnsForSideGridView - 1)) { // TODO: move from xth row's last item to (x+1)th row's first item
                                mSendData.moveForwardAndBackward(position + 1);
                                return true;
                            }
                            else if (position == getItemCount() - 1) { // TODO: block the auto-move of grid-view's last item to prev row's last item
                                return true;
                            }
                        }
                        else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                            mSendData.sendData2NewScreen(mInLinksList.get(position));
                            return true;
                        }
                    }
                    else if (keyAction == KeyEvent.ACTION_UP) {
                        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) { // TODO: block switching between fragments by Down-Key
                            return true;
                        }
                        else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) { // TODO: to block the auto-focusing first item of grid when moving previous row
                            view.requestFocus();
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }


    public interface SendData {
        void sendData2NewScreen(LinkInfoContainer inLinksInfo);
        void moveForwardAndBackward(int position);
        void moveUpAndDown(int position);
    }
}
