package com.live.sapphire.tv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;

/**
 * Created by Husnain Iqnal on 19-May-17.
 */
public class Utilities {

    public static int mNumColumnsForSideGridView = 5;
    public static int mNumColumnsForFullScreenGridView = 6;
    public static final String FRAGMENT_LINKS_LOADER_TEXT = "LinksLoader";
    public static final String FRAGMENT_SUBCATEGORIES_TEXT = "Subcategories";
    public static String mCurrentFragment = FRAGMENT_SUBCATEGORIES_TEXT;
    public static final String FRAGMENT_CATEGORIES_TAG = "tagCategories";
    public static final String FRAGMENT_SUBCATEGORIES_TAG = "tagSubcategories";
    public static final String FRAGMENT_LINKS_LOADER_TAG = "tagLinksLoader";
    public static final String NO_INTERNET_AVAILABILITY_TEXT = "Your device has no access to the internet. Please check your internet connection";
    public static final String SERVER_ERROR_TEXT = "Server Error";
    public static final String POST_PARAMETER_NAME = "token";
    public static final String POST_PARAMETER_VALUE = "clVzaU5rUHdjSm9HdFlFMHBoN01ueTZhQlhJbEo4ZHM=";
//    public static final String POST_PARAMETER_VALUE = "Z3YxdDllSHlMWVN3TW9kQUtDOGRjdHZrN25zcVIyOXk=";

    public static final String VIDEO_AUTO_PLAY_CODE = "?enablejsapi=1&autoplay=1";
    public static final String VIDEO_AUTO_PLAY_CODE_2 = "?autoplay=1";
    public static final String YOUTUBE_LINK_PREFIX_1 = "https://www.youtube.com/watch?v=";
    public static final String YOUTUBE_LINK_PREFIX_2 = "http://www.youtube.com/watch?v=";
    public static final String YOUTUBE_FULL_SCREEN_OPTIMIZED_LINK_1 = "https://www.youtube.com/embed/VIDEOID";
    public static final String YOUTUBE_FULL_SCREEN_OPTIMIZED_LINK_2 = "http://www.youtube.com/watch_popup?v=VIDEOID";
    public static final String YOUTUBE_TV_OPTIMIZED_LINK = "https://www.youtube.com/tv#/watch?v=VIDEOID";
    public static final String YOUTUBE_TV_OPTIMIZED_LINK_2 = "https://www.youtube.com/tv#/watch/video/control?v=VIDEOID&resume";

    public static final String LINK_TYPE_INDIVIDUAL_LINKS = "IndividualLinks";
    public static final String LINK_TYPE_OTHER_LINKS = "OtherLinks";
    public static final String LINK_TYPE_CHANNELS = "Channels";
    public static final String LINK_TYPE_PLAYLISTS = "PlayLists";

    public static final String SOURCE_YOUTUBE_TEXT = "youtube";
    public static final String SOURCE_FLUSSONIC_TEXT = "flussonic";
    public static final String SOURCE_FILMON_TEXT = "filmon";
    public static final String SOURCE_KARWAN_TEXT = "karwan";

    public static final String FILMON_VIDEO_QUALITY_LOW = "low";
    public static final String FILMON_VIDEO_QUALITY_HIGH = "high";

    public static final String CATID_URL_PART = "&catid=";
    public static final String SUBCATID_URL_PART = "&subcatid=";
    public static final String MAINCATID_URL_PART = "&maincatid=";
    public static final String NEXT_PAGE_TOKEN = "nextpagetoken";
    public static final String PREV_PAGE_TOKEN = "prevpagetoken";
    public static final String APP_INFO_URL = "https://amaranthineapps.com/catalogue/api/DreamIptvAndroid?action=appinfo";
    public static final String CATEGORIES_URL = "https://amaranthineapps.com/catalogue/api/DreamIptvAndroid?action=categories";
    public static final String FLUSSONIC_AUTH_URL = "https://amaranthineapps.com/catalogue/flussonicauth";
    public static final String TEST_AAJ_NEWS_YOUTUBE_URL = "https://www.youtube.com/watch?v=JlT7TLZnPXY";
    public static final String TEST_MP4_DUMMY_URL = "http://www.ebookfrenzy.com/android_book/movie.mp4";
//        brightcoveVideoView.add(Video.createVideo("http://learning-services-media.brightcove.com/videos/mp4/Wildlife_Tiger.mp4", DeliveryType.MP4));
//        brightcoveVideoView.add(Video.createVideo("http://storage.googleapis.com/vrview/examples/video/hls/congo.m3u8"));

    public static String getIndividualLinksUrl(String categoryId, String subcategoryId, String mainCategoryId) {
        String individualLinkUrl = "https://amaranthineapps.com/catalogue/api/DreamIptvAndroid?action=inlinks";
        individualLinkUrl += CATID_URL_PART + categoryId + SUBCATID_URL_PART + subcategoryId + MAINCATID_URL_PART + mainCategoryId;
        return individualLinkUrl;
    }

    public static String getIndividualLinksUrl(String categoryId, String subcategoryId, String mainCategoryId, int start, int length) {
        String individualLinkUrl = "https://amaranthineapps.com/catalogue/api/DreamIptvAndroid?action=inlinks";
        individualLinkUrl += CATID_URL_PART + categoryId + SUBCATID_URL_PART + subcategoryId + MAINCATID_URL_PART + mainCategoryId + "&start=" + start + "&length=" + length;
        return individualLinkUrl;
    }

    public static String getChannelsUrl(String categoryId, String subcategoryId, String mainCategoryId) {
        String channelsUrl = "https://amaranthineapps.com/catalogue/api/DreamIptvAndroid?action=channels";
        channelsUrl += "&catid=" + categoryId + "&subcatid=" + subcategoryId + "&maincatid=" + mainCategoryId;
        return channelsUrl;
    }

    public static String getChannelsPlaylistUrl(String id, boolean pageToken) {
        String channelsPlaylistUrl = "https://amaranthineapps.com/catalogue/api/DreamIptvAndroid?action=channel";
        channelsPlaylistUrl += "&id=" + id;
        if (pageToken) {
            channelsPlaylistUrl += "&pagetoken=CDIQAA";
        }
        return channelsPlaylistUrl;
    }


    public static String getOtherLinksUrl(String categoryId, String subcategoryId, String mainCategoryId) {
        String channelsUrl = "https://amaranthineapps.com/catalogue/api/DreamIptvAndroid?action=otherlinks";
        channelsUrl += "&catid=" + categoryId + "&subcatid=" + subcategoryId + "&maincatid=" + mainCategoryId;
        return channelsUrl;
    }

    public static String getOtherLinksUrl(String categoryId, String subcategoryId, String mainCategoryId, int start, int length) {
        String channelsUrl = "https://amaranthineapps.com/catalogue/api/DreamIptvAndroid?action=otherlinks";
        channelsUrl += "&catid=" + categoryId + "&subcatid=" + subcategoryId + "&maincatid=" + mainCategoryId
                + "&start=" + start + "&length=" + length;
        return channelsUrl;
    }

//    public static String getChannelInfo(String id) {
//        String channelInfoUrl = "https://amaranthineapps.com/catalogue/api/DreamIptvAndroid?action=channel";
//        channelInfoUrl += "&id=" + id + "&pagetoken=CDIQAA";
//        return channelInfoUrl;
//
//    }

    public static String getPlayListsUrl(String categoryId, String subcategoryId, String mainCategoryId) {
        String playlistUrl = "https://amaranthineapps.com/catalogue/api/DreamIptvAndroid?action=playlists";
        playlistUrl += "&catid=" + categoryId + "&subcatid=" + subcategoryId + "&maincatid=" + mainCategoryId;
        return playlistUrl;
    }

    public static String getPlaylistVideos(String playlistId, String pageToken) {
        String playlistVideosUrl = "https://amaranthineapps.com/catalogue/api/DreamIptvAndroid?action=playlist";
        playlistVideosUrl += "&playlistid=" + playlistId;
        if (pageToken.equals(Utilities.NEXT_PAGE_TOKEN)) {
            playlistVideosUrl += "&pagetoken=nextpagetoken";
        } else if (pageToken.equals(Utilities.PREV_PAGE_TOKEN)) {
            playlistVideosUrl += "&pagetoken=prevpagetoken";
        }
        return playlistVideosUrl;
    }

    public static String getVideoUrl(String videoId) {
        String videoUrl = "https://amaranthineapps.com/catalogue/downloader?videoid=";
        return videoUrl + videoId;
    }

    public static void showShortToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showShortToastInUiThread(final Activity activity, final String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showShortToast(activity.getApplicationContext(), msg);
            }
        });
    }

    public static void showLongToastInUiThread(final Activity activity, final String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showLongToast(activity.getApplicationContext(), msg);
            }
        });
    }

    public static Drawable loadDrawableFromUrl(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "srcName");
        } catch (Exception e) {
            Log.v("@LoadImageFromUrl", e.getMessage());
            return null;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            Log.e("@IsNetworkAvailable", e.getMessage());
            return false;
        }
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddress = InetAddress.getByName("google.com");
            String ipAddressString = ipAddress.toString();
            if (ipAddressString != null) {
                return true;
            } else {
                return !ipAddressString.equals("");
            }
        } catch (Exception ex) {
            Log.e("@IsInternetAvailable", ex.getMessage());
            return false;
        }
    }

    public static void openNetworkSettings(Activity context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
        context.startActivity(intent);
    }

    public static boolean isThumbnailUrlValid(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty() && imageUrl.startsWith("http") && imageUrl.length() > 15) {
//            imageUrl = imageUrl.toUpperCase();
//            if (imageUrl.endsWith("PNG") || imageUrl.endsWith("GIF") || (imageUrl.endsWith("JPEG") || (imageUrl.endsWith("JPG"))
//                    || imageUrl.endsWith("JFIF") || imageUrl.endsWith("EXIF") || imageUrl.endsWith("TIFF")
//                    || imageUrl.endsWith("BMP") || imageUrl.endsWith("PPM") || imageUrl.endsWith("PGM")
//                    || imageUrl.endsWith("PBM") || imageUrl.endsWith("PNM") || imageUrl.endsWith("WEBP")
//                    || imageUrl.endsWith("HEIF") || imageUrl.endsWith("BAT") || imageUrl.endsWith("BPG")
//                    || imageUrl.endsWith("RAW") || imageUrl.endsWith("PSD"))) {
//                return true;
//            }
            return true;
        }
        return false;
    }

//    private static Drawable drawable = null;
//    public static Drawable loadImageFromUrl(final String url) {
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    InputStream is = (InputStream) new URL(url).getContent();
//                    drawable = Drawable.createFromStream(is, "srcName");
//                } catch (Exception e) {
//                    Log.v("@LoadImageFromUrl", e.getMessage());
//                }
//            }
//        });
//        return drawable;
//    }
}
