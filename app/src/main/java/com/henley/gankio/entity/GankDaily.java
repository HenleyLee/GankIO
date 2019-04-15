package com.henley.gankio.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 每日干货数据
 *
 * @author Henley
 * @date 2018/7/3 15:02
 */
public class GankDaily implements Parcelable {

    @SerializedName("福利")
    private ArrayList<GankEntity> welfareData;
    @SerializedName("Android")
    private ArrayList<GankEntity> androidData;
    @SerializedName("iOS")
    private ArrayList<GankEntity> iosData;
    @SerializedName("前端")
    private ArrayList<GankEntity> jsData;
    @SerializedName("休息视频")
    private ArrayList<GankEntity> videoData;
    @SerializedName("拓展资源")
    private ArrayList<GankEntity> resourcesData;
    @SerializedName("App")
    private ArrayList<GankEntity> appData;
    @SerializedName("瞎推荐")
    private ArrayList<GankEntity> recommendData;

    public ArrayList<GankEntity> getWelfareData() {
        return welfareData;
    }

    public void setWelfareData(ArrayList<GankEntity> welfareData) {
        this.welfareData = welfareData;
    }

    public ArrayList<GankEntity> getAndroidData() {
        return androidData;
    }

    public void setAndroidData(ArrayList<GankEntity> androidData) {
        this.androidData = androidData;
    }

    public ArrayList<GankEntity> getIosData() {
        return iosData;
    }

    public void setIosData(ArrayList<GankEntity> iosData) {
        this.iosData = iosData;
    }

    public ArrayList<GankEntity> getJsData() {
        return jsData;
    }

    public void setJsData(ArrayList<GankEntity> jsData) {
        this.jsData = jsData;
    }

    public ArrayList<GankEntity> getVideoData() {
        return videoData;
    }

    public void setVideoData(ArrayList<GankEntity> videoData) {
        this.videoData = videoData;
    }

    public ArrayList<GankEntity> getResourcesData() {
        return resourcesData;
    }

    public void setResourcesData(ArrayList<GankEntity> resourcesData) {
        this.resourcesData = resourcesData;
    }

    public ArrayList<GankEntity> getAppData() {
        return appData;
    }

    public void setAppData(ArrayList<GankEntity> appData) {
        this.appData = appData;
    }

    public ArrayList<GankEntity> getRecommendData() {
        return recommendData;
    }

    public void setRecommendData(ArrayList<GankEntity> recommendData) {
        this.recommendData = recommendData;
    }

    public List<List<GankEntity>> getDailyResults() {
        List<List<GankEntity>> typeResults = new ArrayList<>();
        if (welfareData != null && !welfareData.isEmpty()) {
            typeResults.add(welfareData);
        }
        if (androidData != null && !androidData.isEmpty()) {
            typeResults.add(androidData);
        }
        if (iosData != null && !iosData.isEmpty()) {
            typeResults.add(iosData);
        }
        if (jsData != null && !jsData.isEmpty()) {
            typeResults.add(jsData);
        }
        if (videoData != null && !videoData.isEmpty()) {
            typeResults.add(videoData);
        }
        if (resourcesData != null && !resourcesData.isEmpty()) {
            typeResults.add(resourcesData);
        }
        if (appData != null && !appData.isEmpty()) {
            typeResults.add(appData);
        }
        if (recommendData != null && !recommendData.isEmpty()) {
            typeResults.add(recommendData);
        }
        return typeResults;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.welfareData);
        dest.writeTypedList(this.androidData);
        dest.writeTypedList(this.iosData);
        dest.writeTypedList(this.jsData);
        dest.writeTypedList(this.videoData);
        dest.writeTypedList(this.resourcesData);
        dest.writeTypedList(this.appData);
        dest.writeTypedList(this.recommendData);
    }

    public GankDaily() {
    }

    protected GankDaily(Parcel in) {
        this.welfareData = in.createTypedArrayList(GankEntity.CREATOR);
        this.androidData = in.createTypedArrayList(GankEntity.CREATOR);
        this.iosData = in.createTypedArrayList(GankEntity.CREATOR);
        this.jsData = in.createTypedArrayList(GankEntity.CREATOR);
        this.videoData = in.createTypedArrayList(GankEntity.CREATOR);
        this.resourcesData = in.createTypedArrayList(GankEntity.CREATOR);
        this.appData = in.createTypedArrayList(GankEntity.CREATOR);
        this.recommendData = in.createTypedArrayList(GankEntity.CREATOR);
    }

    public static final Creator<GankDaily> CREATOR = new Creator<GankDaily>() {
        @Override
        public GankDaily createFromParcel(Parcel source) {
            return new GankDaily(source);
        }

        @Override
        public GankDaily[] newArray(int size) {
            return new GankDaily[size];
        }
    };

}
