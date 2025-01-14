package com.samratmatka.android.dto;

import java.io.Serializable;
import java.util.List;

public class ClubDashboardApiResponse extends BaseApiResponse implements Serializable {
    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public List<MarketDto> getMarkets() {
        return markets;
    }

    public void setMarkets(List<MarketDto> markets) {
        this.markets = markets;
    }

    public String getClubType() {
        return clubType;
    }

    public void setClubType(String clubType) {
        this.clubType = clubType;
    }

    private String clubId;
    private String clubName;
    private String clubType;
    private List<MarketDto> markets;
}