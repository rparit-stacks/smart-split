package com.rps.smartsplit.dto;

import com.rps.smartsplit.dto.settings.NotificationSettingsDTO;
import com.rps.smartsplit.dto.settings.PrivacySettingsDTO;

public class SettingsRequestDTO {
    private String currency;
    private String language;
    private NotificationSettingsDTO notifications;
    private PrivacySettingsDTO privacy;

    public SettingsRequestDTO() {
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public NotificationSettingsDTO getNotifications() {
        return notifications;
    }

    public void setNotifications(NotificationSettingsDTO notifications) {
        this.notifications = notifications;
    }

    public PrivacySettingsDTO getPrivacy() {
        return privacy;
    }

    public void setPrivacy(PrivacySettingsDTO privacy) {
        this.privacy = privacy;
    }
}

