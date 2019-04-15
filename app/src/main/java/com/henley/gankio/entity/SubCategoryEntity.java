package com.henley.gankio.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * 闲读子分类数据
 *
 * @author Henley
 * @date 2018/7/9 14:45
 */
public class SubCategoryEntity {

    @SerializedName("_id")
    private String id;                  // 子分类ID
    @SerializedName("title")
    private String title;               // 子分类名称
    @SerializedName("icon")
    private String icon;                // 子分类图标
    @SerializedName("id")
    private String categoryId;          // 子分类ID
    @SerializedName("created_at")
    private Date createdTime;           // 子分类创建时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

}
