package com.rps.smartsplit.dto;

public class PrivacySettingsDTO {
    private Boolean profileVisible;
    private Boolean emailVisible;
    private Boolean phoneVisible;
    private Boolean showInSearch;

    public PrivacySettingsDTO() {
    }

    public Boolean getProfileVisible() {
        return profileVisible;
    }

    public void setProfileVisible(Boolean profileVisible) {
        this.profileVisible = profileVisible;
    }

    public Boolean getEmailVisible() {
        return emailVisible;
    }

    public void setEmailVisible(Boolean emailVisible) {
        this.emailVisible = emailVisible;
    }

    public Boolean getPhoneVisible() {
        return phoneVisible;
    }

    public void setPhoneVisible(Boolean phoneVisible) {
        this.phoneVisible = phoneVisible;
    }

    public Boolean getShowInSearch() {
        return showInSearch;
    }

    public void setShowInSearch(Boolean showInSearch) {
        this.showInSearch = showInSearch;
    }
}

