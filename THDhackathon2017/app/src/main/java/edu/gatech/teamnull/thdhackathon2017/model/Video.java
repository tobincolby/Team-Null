package edu.gatech.teamnull.thdhackathon2017.model;

import com.google.api.services.youtube.model.Thumbnail;

/**
 * Created by karshinlin on 9/17/17.
 */

public class Video {
    private String title;
    private Thumbnail thumbnail;
    private String id;

    public Video(String title, Thumbnail tn, String id) {
        this.title = title;
        this.thumbnail = tn;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Thumbnail getThumbnail() {

        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
