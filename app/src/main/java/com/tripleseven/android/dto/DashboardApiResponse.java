package com.tripleseven.android.dto;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DashboardApiResponse extends BaseApiResponse implements Serializable {
    private String totalBalance;
    private String winningBalance;
    private String depositBalance;
    private String bonusBalance;
    private String userName;
    private String referralCode;

    @Expose
    @SerializedName("clubs")
    private List<ClubDto> clubs;
    private String whatsAppLink;
    private String telegramLink;
    private String instagramLink;
    private String youtubeLink;
    private String marqueeText;
    private List<String> sliderImageSlugs;
    private String minDeposit;
    private String minWithdraw;
    private String upiId;
    private String bankDetails;
    private String withdrawOpenTime;
    private String withdrawCloseTime;
    private String notice;
    private String upiGatewayKey;

    public String getWinningBalance() {
        return winningBalance;
    }

    public void setWinningBalance(String winningBalance) {
        this.winningBalance = winningBalance;
    }

    public String getDepositBalance() {
        return depositBalance;
    }

    public void setDepositBalance(String depositBalance) {
        this.depositBalance = depositBalance;
    }

    public String getBonusBalance() {
        return bonusBalance;
    }

    public void setBonusBalance(String bonusBalance) {
        this.bonusBalance = bonusBalance;
    }

    public String getMinDeposit() {
        return minDeposit;
    }

    public void setMinDeposit(String minDeposit) {
        this.minDeposit = minDeposit;
    }

    public String getMinWithdraw() {
        return minWithdraw;
    }

    public void setMinWithdraw(String minWithdraw) {
        this.minWithdraw = minWithdraw;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(String bankDetails) {
        this.bankDetails = bankDetails;
    }

    public String getWithdrawOpenTime() {
        return withdrawOpenTime;
    }

    public void setWithdrawOpenTime(String withdrawOpenTime) {
        this.withdrawOpenTime = withdrawOpenTime;
    }

    public String getWithdrawCloseTime() {
        return withdrawCloseTime;
    }

    public void setWithdrawCloseTime(String withdrawCloseTime) {
        this.withdrawCloseTime = withdrawCloseTime;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getUpiGatewayKey() {
        return upiGatewayKey;
    }

    public void setUpiGatewayKey(String upiGatewayKey) {
        this.upiGatewayKey = upiGatewayKey;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public List<ClubDto> getClubs() {
        return clubs;
    }

    public void setClubs(List<ClubDto> clubs) {
        this.clubs = clubs;
    }

    public String getWhatsAppLink() {
        return whatsAppLink;
    }

    public void setWhatsAppLink(String whatsAppLink) {
        this.whatsAppLink = whatsAppLink;
    }

    public String getTelegramLink() {
        return telegramLink;
    }

    public void setTelegramLink(String telegramLink) {
        this.telegramLink = telegramLink;
    }

    public String getInstagramLink() {
        return instagramLink;
    }

    public void setInstagramLink(String instagramLink) {
        this.instagramLink = instagramLink;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public List<String> getSliderImageSlugs() {
        return sliderImageSlugs;
    }

    public void setSliderImageSlugs(List<String> sliderImageSlugs) {
        this.sliderImageSlugs = sliderImageSlugs;
    }

    public void setMarqueeText(String marqueeText) {
        this.marqueeText = marqueeText;
    }

    public String getMarqueeText() {
        return marqueeText;
    }
}
