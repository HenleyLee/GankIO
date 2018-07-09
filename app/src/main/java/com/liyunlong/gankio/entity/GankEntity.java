package com.liyunlong.gankio.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * 干货数据
 *
 * @author liyunlong
 * @date 2018/7/3 14:46
 */
public class GankEntity implements Parcelable {

    @SerializedName("_id")
    private String id;                  // 对象id
    @SerializedName("createdAt")
    private Date createdTime;           // 创建时间
    @SerializedName("publishedAt")
    private Date publishedTime;         // 发布时间
    @SerializedName("desc")
    private String title;               // 标题
    @SerializedName(value = "images")
    private List<String> images;        // 图片
    @SerializedName("source")
    private String source;              // 来源
    @SerializedName("type")
    private String type;                // 类型
    @SerializedName("url")
    private String url;                 // 链接
    @SerializedName("used")
    private Boolean used;               // 是否可用
    @SerializedName("who")
    private String author;              // 发布人

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getPublishedTime() {
        return publishedTime;
    }

    public void setPublishedTime(Date publishedTime) {
        this.publishedTime = publishedTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeLong(this.createdTime != null ? this.createdTime.getTime() : -1);
        dest.writeLong(this.publishedTime != null ? this.publishedTime.getTime() : -1);
        dest.writeString(this.title);
        dest.writeStringList(this.images);
        dest.writeString(this.source);
        dest.writeString(this.type);
        dest.writeString(this.url);
        dest.writeValue(this.used);
        dest.writeString(this.author);
    }

    public GankEntity() {
    }

    protected GankEntity(Parcel in) {
        this.id = in.readString();
        long tmpCreatedTime = in.readLong();
        this.createdTime = tmpCreatedTime == -1 ? null : new Date(tmpCreatedTime);
        long tmpPublishedTime = in.readLong();
        this.publishedTime = tmpPublishedTime == -1 ? null : new Date(tmpPublishedTime);
        this.title = in.readString();
        this.images = in.createStringArrayList();
        this.source = in.readString();
        this.type = in.readString();
        this.url = in.readString();
        this.used = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.author = in.readString();
    }

    public static final Parcelable.Creator<GankEntity> CREATOR = new Parcelable.Creator<GankEntity>() {
        @Override
        public GankEntity createFromParcel(Parcel source) {
            return new GankEntity(source);
        }

        @Override
        public GankEntity[] newArray(int size) {
            return new GankEntity[size];
        }
    };

}
