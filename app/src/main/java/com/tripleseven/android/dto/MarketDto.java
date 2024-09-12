package com.tripleseven.android.dto;

import java.io.Serializable;

public class MarketDto implements Serializable {
    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBidStartTime() {
        return bidStartTime;
    }

    public void setBidStartTime(String bidStartTime) {
        this.bidStartTime = bidStartTime;
    }

    public String getCloseBidEndTime() {
        return closeBidEndTime;
    }

    public void setCloseBidEndTime(String closeBidEndTime) {
        this.closeBidEndTime = closeBidEndTime;
    }

    public String getCloseResultTime() {
        return closeResultTime;
    }

    public void setCloseResultTime(String closeResultTime) {
        this.closeResultTime = closeResultTime;
    }

    public String getInfoText() {
        return infoText;
    }

    public void setInfoText(String closingText) {
        this.infoText = closingText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLeftNumber() {
        return leftNumber;
    }

    public void setLeftNumber(String leftNumber) {
        this.leftNumber = leftNumber;
    }

    public String getMidNumber() {
        return midNumber;
    }

    public void setMidNumber(String midNumber) {
        this.midNumber = midNumber;
    }

    public String getRightNumber() {
        return rightNumber;
    }

    public void setRightNumber(String rightNumber) {
        this.rightNumber = rightNumber;
    }

    private String marketId;
    private String name;
    private String bidStartTime;

    public String getOpenBidEndTime() {
        return openBidEndTime;
    }

    public void setOpenBidEndTime(String openBidEndTime) {
        this.openBidEndTime = openBidEndTime;
    }

    public String getOpenResultTime() {
        return openResultTime;
    }

    public void setOpenResultTime(String openResultTime) {
        this.openResultTime = openResultTime;
    }

    private String openBidEndTime;
    private String closeBidEndTime;

    private String openResultTime;
    private String closeResultTime;
    private String infoText;
    private String type;
    private String leftNumber;
    private String midNumber;
    private String rightNumber;
    private Boolean isOpenSessionOn;
    private Boolean isCloseSessionOn;

    public Boolean getOpenSessionOn() {
        return isOpenSessionOn;
    }

    public void setOpenSessionOn(Boolean openSessionOn) {
        isOpenSessionOn = openSessionOn;
    }

    public Boolean getCloseSessionOn() {
        return isCloseSessionOn;
    }

    public void setCloseSessionOn(Boolean closeSessionOn) {
        isCloseSessionOn = closeSessionOn;
    }
}