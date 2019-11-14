package com.live.sapphire.tv.Customized;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Husnain Iqnal on 19-May-17.
 */
public class CategoryInfo implements Serializable{
    private String mId;
    private String mCategoryName;
    private String mSubcategoryId;
    private String mMainCategoryId;
    private String mThumbnailUrl;
    private ArrayList<SubcategoryInfo> mSubcategoriesList;
    private ArrayList<LinkInfoContainer> mLinksInfoContainerList;

    public CategoryInfo(String id, String categoryName, String subcategoryId, String mainCategoryId, String thumbnail) {
        mId = id;
        mCategoryName = categoryName;
        mSubcategoryId = subcategoryId;
        mMainCategoryId = mainCategoryId;
        mThumbnailUrl = thumbnail;
    }

    public CategoryInfo(String id, String categoryName, String subcategoryId, String mainCategoryId, String thumbnail, ArrayList<SubcategoryInfo> subcategoriesList) {
        mId = id;
        mCategoryName = categoryName;
        mSubcategoryId = subcategoryId;
        mMainCategoryId = mainCategoryId;
        mThumbnailUrl = thumbnail;
        mSubcategoriesList = subcategoriesList;
    }


    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        mCategoryName = categoryName;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        mThumbnailUrl = thumbnailUrl;
    }

    public String getSubcategoryId() {
        return mSubcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        mSubcategoryId = subcategoryId;
    }

    public String getMainCategoryId() {
        return mMainCategoryId;
    }

    public void setMainCategoryId(String mainCategoryId) {
        mMainCategoryId = mainCategoryId;
    }

    public ArrayList<SubcategoryInfo> getSubcategoriesList() {
        return mSubcategoriesList;
    }

    public void setSubcategoriesList(ArrayList<SubcategoryInfo> subcategoriesList) {
        mSubcategoriesList = subcategoriesList;
    }

    public ArrayList<LinkInfoContainer> getLinksInfoContainerList() {
        return mLinksInfoContainerList;
    }

    public void setLinksInfoContainerList(ArrayList<LinkInfoContainer> list) {
        mLinksInfoContainerList = list;
    }
}
