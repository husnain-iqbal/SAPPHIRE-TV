package com.live.sapphire.tv;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.live.sapphire.tv.Customized.ImageDimensions;
import com.live.sapphire.tv.Customized.ScreenDimensions;
import com.squareup.picasso.Picasso;

/**
 * Created by Husnain Iqnal on 04-Jun-17.
 */
public class AppConfigurations {

    public static void setFocusOnCurrentItem(Context context, View view){
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.golden));
    }

    public static void setFocusOnCurrentItem(Context context, View view, Button button){
        button.setTypeface(null, Typeface.BOLD);
        button.setTextColor(ContextCompat.getColor(context, R.color.black));
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.golden));
    }

    public static void releaseFocusFromCurrentItem(Context context, View view){
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
    }

    public static void releaseFocusFromCurrentItem(Context context, View view, Button button){
        button.setTypeface(null, Typeface.NORMAL);
        button.setTextColor(ContextCompat.getColor(context, R.color.aquamarine));
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
    }

    public static ScreenDimensions getScreenDimensions(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return new ScreenDimensions(size.x, size.y);
    }

    public static ImageDimensions getImageDimensions(Context context){
        Resources r = context.getResources();
        int imageWidth = (int)r.getDimension(R.dimen.customized_grid_view_image_view_width);
        int imageHeight = (int)r.getDimension(R.dimen.customized_grid_view_image_view_height);
        return new ImageDimensions(imageWidth, imageHeight);
    }


    public static void setImage2ImageView(Context context, ImageView imageView, String thumbnailUrl){
        try {
            ImageDimensions imageDimensions = AppConfigurations.getImageDimensions(context);
            if (Utilities.isThumbnailUrlValid(thumbnailUrl)) {
                Picasso.with(context)
                        .load(thumbnailUrl)
                        .placeholder(R.drawable.app_icon)
                        .resize(imageDimensions.getWidth(), imageDimensions.getHeight())
                        .into(imageView);
            }
            else {
                Picasso.with(context)
                        .load(R.drawable.app_icon)
                        .resize(imageDimensions.getWidth(), imageDimensions.getHeight())
                        .into(imageView);
            }
        }
        catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }
}
