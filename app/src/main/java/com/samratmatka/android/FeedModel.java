package com.samratmatka.android;

public class FeedModel {

    String title;
    String description;
    String islike;
    String media_type;
    String media_url;
    String noOfLikes;
    String id;
    String time;
    String fullContent;

    public FeedModel(String title, String description, String islike, String media_type, String media_url, String id,String noOfLikes, String time, String fullContent) {
        this.title = title;
        this.description = description;
        this.islike = islike;
        this.media_type = media_type;
        this.media_url = media_url;
        this.id = id;
        this.noOfLikes = noOfLikes;
        this.time = time;
        this.fullContent = fullContent;
    }

    public String getFullContent(){
        return fullContent;
    }

    public String getTime() {
        return time;
    }

    public String getLikes() {
        return noOfLikes;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getIslike() {
        return islike;
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getMedia_url() {
        return media_url;
    }



}
