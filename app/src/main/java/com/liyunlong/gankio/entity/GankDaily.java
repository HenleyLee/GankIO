package com.liyunlong.gankio.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 每日干货数据
 *
 * @author liyunlong
 * @date 2018/7/3 15:02
 */
public class GankDaily extends BaseGank implements Parcelable {

    @SerializedName("category")
    private List<String> categorys;
    @SerializedName("results")
    private DailyResults dailyResults;

    public List<String> getCategorys() {
        return categorys;
    }

    public void setCategorys(List<String> categorys) {
        this.categorys = categorys;
    }

    public DailyResults getDailyResults() {
        return dailyResults;
    }

    public void setDailyResults(DailyResults dailyResults) {
        this.dailyResults = dailyResults;
    }

    public static class DailyResults implements Parcelable {

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
            dest.writeList(this.welfareData);
            dest.writeList(this.androidData);
            dest.writeList(this.iosData);
            dest.writeList(this.jsData);
            dest.writeList(this.videoData);
            dest.writeList(this.resourcesData);
            dest.writeList(this.appData);
            dest.writeList(this.recommendData);
        }

        public DailyResults() {
        }

        protected DailyResults(Parcel in) {
            this.welfareData = new ArrayList<GankEntity>();
            in.readList(this.welfareData, GankEntity.class.getClassLoader());
            this.androidData = new ArrayList<GankEntity>();
            in.readList(this.androidData, GankEntity.class.getClassLoader());
            this.iosData = new ArrayList<GankEntity>();
            in.readList(this.iosData, GankEntity.class.getClassLoader());
            this.jsData = new ArrayList<GankEntity>();
            in.readList(this.jsData, GankEntity.class.getClassLoader());
            this.videoData = new ArrayList<GankEntity>();
            in.readList(this.videoData, GankEntity.class.getClassLoader());
            this.resourcesData = new ArrayList<GankEntity>();
            in.readList(this.resourcesData, GankEntity.class.getClassLoader());
            this.appData = new ArrayList<GankEntity>();
            in.readList(this.appData, GankEntity.class.getClassLoader());
            this.recommendData = new ArrayList<GankEntity>();
            in.readList(this.recommendData, GankEntity.class.getClassLoader());
        }

        public static final Creator<DailyResults> CREATOR = new Creator<DailyResults>() {
            @Override
            public DailyResults createFromParcel(Parcel source) {
                return new DailyResults(source);
            }

            @Override
            public DailyResults[] newArray(int size) {
                return new DailyResults[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.categorys);
        dest.writeParcelable(this.dailyResults, flags);
    }

    public GankDaily() {
    }

    protected GankDaily(Parcel in) {
        this.categorys = in.createStringArrayList();
        this.dailyResults = in.readParcelable(DailyResults.class.getClassLoader());
    }

    public static final Parcelable.Creator<GankDaily> CREATOR = new Parcelable.Creator<GankDaily>() {
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
