package com.samratmatka.android.dto;

import java.io.Serializable;

public class ClubDto implements Serializable {
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

    public String getIconImageSlug() {
        return iconImageSlug;
    }

    public void setIconImageSlug(String iconImageSlug) {
        this.iconImageSlug = iconImageSlug;
    }

    public String getTotalPlayingUser() {
        return totalPlayingUser;
    }

    public void setTotalPlayingUser(String totalPlayingUser) {
        this.totalPlayingUser = totalPlayingUser;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    private String id;
    private String name;
    private String iconImageSlug;
    private String totalPlayingUser;
    private boolean isEnabled;
}
