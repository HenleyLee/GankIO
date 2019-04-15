package com.henley.gankio.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * 闲读主分类数据
 *
 * @author Henley
 * @date 2018/7/9 14:31
 */
public class CategoryEntity implements Parcelable {

    @SerializedName("_id")
    private String id;                  // 主分类ID
    @SerializedName("en_name")
    private String categoryEN;          // 主分类名称(英文)
    @SerializedName("name")
    private String categoryCN;          // 主分类名称(中文)
    @SerializedName("rank")
    private Integer rank;               // 主分类排名

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryEN() {
        return categoryEN;
    }

    public void setCategoryEN(String categoryEN) {
        this.categoryEN = categoryEN;
    }

    public String getCategoryCN() {
        return categoryCN;
    }

    public void setCategoryCN(String categoryCN) {
        this.categoryCN = categoryCN;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.categoryEN);
        dest.writeString(this.categoryCN);
        dest.writeValue(this.rank);
    }

    public CategoryEntity() {
    }

    protected CategoryEntity(Parcel in) {
        this.id = in.readString();
        this.categoryEN = in.readString();
        this.categoryCN = in.readString();
        this.rank = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<CategoryEntity> CREATOR = new Creator<CategoryEntity>() {
        @Override
        public CategoryEntity createFromParcel(Parcel source) {
            return new CategoryEntity(source);
        }

        @Override
        public CategoryEntity[] newArray(int size) {
            return new CategoryEntity[size];
        }
    };

}
