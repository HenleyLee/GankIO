package com.henley.gankio.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * 闲读文章数据
 *
 * @author Henley
 * @date 2018/7/9 15:02
 */
public class IdleReadingEntity {

    @SerializedName("_id")
    private String id;                  // 文章ID
    @SerializedName("title")
    private String title;               // 文章标题
    @SerializedName("content")
    private String content;             // 文章内容
    @SerializedName("url")
    private String url;                 // 文章链接
    @SerializedName("uid")
    private String uid;                 // 文章唯一标识
    @SerializedName("cover")
    private String cover;               // 文章封面
    @SerializedName("crawled")
    private String crawled;             // 文章爬虫
    @SerializedName("deleted")
    private Boolean deleted;            // 文章删除标记
    @SerializedName("created_at")
    private Date createdTime;           // 文章创建时间
    @SerializedName("published_at")
    private Date publishedTime;         // 文章发布时间
    @SerializedName("raw")
    private String raw;                 // 文章内容(Html格式的)
    @SerializedName("site")
    private Site site;                  // 文章发布网站信息

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCrawled() {
        return crawled;
    }

    public void setCrawled(String crawled) {
        this.crawled = crawled;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public static final class Site {

        @SerializedName("id")
        private String id;                  // 子分类ID
        @SerializedName("name")
        private String name;                // 子分类名称
        @SerializedName("icon")
        private String icon;                // 子分类图标
        @SerializedName("cat_en")
        private String categoryEN;          // 主分类名称(英文)
        @SerializedName("cat_cn")
        private String categoryCN;          // 主分类名称(中文)
        @SerializedName("type")
        private String type;                // 文章共享方式
        @SerializedName("desc")
        private String desc;                // 文章摘取网站描述
        @SerializedName("url")
        private String url;                 // 文章摘取网站链接
        @SerializedName("feed_id")
        private String feedId;              // 文章发布标识
        @SerializedName("subscribers")
        private Long subscribers;           // 文章订阅人数

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getFeedId() {
            return feedId;
        }

        public void setFeedId(String feedId) {
            this.feedId = feedId;
        }

        public Long getSubscribers() {
            return subscribers;
        }

        public void setSubscribers(Long subscribers) {
            this.subscribers = subscribers;
        }

    }

}
