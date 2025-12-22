package com.rps.smartsplit.service;

import com.rps.smartsplit.dto.request.SettingsRequestDTO;
import com.rps.smartsplit.dto.response.SettingsResponseDTO;
import com.rps.smartsplit.dto.settings.NotificationSettingsDTO;
import com.rps.smartsplit.dto.settings.PrivacySettingsDTO;
import com.rps.smartsplit.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SettingsService {

    @Autowired
    private UserService userService;

    /**
     * Get all user settings
     * Following SRP - SettingsService handles settings retrieval
     */
    public SettingsResponseDTO getSettings() {
        User user = userService.getLoggedInUser();
        SettingsResponseDTO settings = new SettingsResponseDTO();

        settings.setCurrency(user.getCurrency() != null ? user.getCurrency() : "USD");
        settings.setLanguage(user.getLanguage() != null ? user.getLanguage() : "en");

        // Build notification settings from user fields
        NotificationSettingsDTO notificationSettings = new NotificationSettingsDTO();
        notificationSettings.setEmailNotifications(user.getEmailNotifications());
        notificationSettings.setPushNotifications(user.getPushNotifications());
        notificationSettings.setSmsNotifications(user.getSmsNotifications());
        notificationSettings.setExpenseReminders(user.getExpenseReminders());
        notificationSettings.setSettlementReminders(user.getSettlementReminders());
        notificationSettings.setGroupInvites(user.getGroupInvites());
        settings.setNotifications(notificationSettings);

        // Build privacy settings from user fields
        PrivacySettingsDTO privacySettings = new PrivacySettingsDTO();
        privacySettings.setProfileVisible(user.getProfileVisible());
        privacySettings.setEmailVisible(user.getEmailVisible());
        privacySettings.setPhoneVisible(user.getPhoneVisible());
        privacySettings.setShowInSearch(user.getShowInSearch());
        settings.setPrivacy(privacySettings);

        return settings;
    }

    /**
     * Update user settings
     * Following SRP - SettingsService handles settings updates
     */
    @Transactional
    public SettingsResponseDTO updateSettings(SettingsRequestDTO settingsRequestDTO) {
        User user = userService.getLoggedInUser();

        if (settingsRequestDTO.getCurrency() != null) {
            user.setCurrency(settingsRequestDTO.getCurrency());
        }
        if (settingsRequestDTO.getLanguage() != null) {
            user.setLanguage(settingsRequestDTO.getLanguage());
        }
        if (settingsRequestDTO.getNotifications() != null) {
            NotificationSettingsDTO notifications = settingsRequestDTO.getNotifications();
            if (notifications.getEmailNotifications() != null) {
                user.setEmailNotifications(notifications.getEmailNotifications());
            }
            if (notifications.getPushNotifications() != null) {
                user.setPushNotifications(notifications.getPushNotifications());
            }
            if (notifications.getSmsNotifications() != null) {
                user.setSmsNotifications(notifications.getSmsNotifications());
            }
            if (notifications.getExpenseReminders() != null) {
                user.setExpenseReminders(notifications.getExpenseReminders());
            }
            if (notifications.getSettlementReminders() != null) {
                user.setSettlementReminders(notifications.getSettlementReminders());
            }
            if (notifications.getGroupInvites() != null) {
                user.setGroupInvites(notifications.getGroupInvites());
            }
        }
        if (settingsRequestDTO.getPrivacy() != null) {
            PrivacySettingsDTO privacy = settingsRequestDTO.getPrivacy();
            if (privacy.getProfileVisible() != null) {
                user.setProfileVisible(privacy.getProfileVisible());
            }
            if (privacy.getEmailVisible() != null) {
                user.setEmailVisible(privacy.getEmailVisible());
            }
            if (privacy.getPhoneVisible() != null) {
                user.setPhoneVisible(privacy.getPhoneVisible());
            }
            if (privacy.getShowInSearch() != null) {
                user.setShowInSearch(privacy.getShowInSearch());
            }
        }

        userService.saveUser(user);
        return getSettings();
    }

    /**
     * Get notification settings
     * Following SRP - SettingsService handles notification settings
     */
    public NotificationSettingsDTO getNotificationSettings() {
        User user = userService.getLoggedInUser();
        NotificationSettingsDTO notificationSettings = new NotificationSettingsDTO();
        notificationSettings.setEmailNotifications(user.getEmailNotifications());
        notificationSettings.setPushNotifications(user.getPushNotifications());
        notificationSettings.setSmsNotifications(user.getSmsNotifications());
        notificationSettings.setExpenseReminders(user.getExpenseReminders());
        notificationSettings.setSettlementReminders(user.getSettlementReminders());
        notificationSettings.setGroupInvites(user.getGroupInvites());
        return notificationSettings;
    }

    /**
     * Update notification settings
     * Following SRP - SettingsService handles notification settings updates
     */
    @Transactional
    public NotificationSettingsDTO updateNotificationSettings(NotificationSettingsDTO notificationSettingsDTO) {
        User user = userService.getLoggedInUser();
        
        if (notificationSettingsDTO.getEmailNotifications() != null) {
            user.setEmailNotifications(notificationSettingsDTO.getEmailNotifications());
        }
        if (notificationSettingsDTO.getPushNotifications() != null) {
            user.setPushNotifications(notificationSettingsDTO.getPushNotifications());
        }
        if (notificationSettingsDTO.getSmsNotifications() != null) {
            user.setSmsNotifications(notificationSettingsDTO.getSmsNotifications());
        }
        if (notificationSettingsDTO.getExpenseReminders() != null) {
            user.setExpenseReminders(notificationSettingsDTO.getExpenseReminders());
        }
        if (notificationSettingsDTO.getSettlementReminders() != null) {
            user.setSettlementReminders(notificationSettingsDTO.getSettlementReminders());
        }
        if (notificationSettingsDTO.getGroupInvites() != null) {
            user.setGroupInvites(notificationSettingsDTO.getGroupInvites());
        }
        
        userService.saveUser(user);
        return getNotificationSettings();
    }

    /**
     * Get currency preference
     * Following SRP - SettingsService handles currency settings
     */
    public String getCurrency() {
        User user = userService.getLoggedInUser();
        return user.getCurrency() != null ? user.getCurrency() : "USD";
    }

    /**
     * Update currency preference
     * Following SRP - SettingsService handles currency updates
     */
    @Transactional
    public String updateCurrency(String currency) {
        if (currency == null || currency.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency cannot be empty");
        }
        User user = userService.getLoggedInUser();
        user.setCurrency(currency);
        userService.saveUser(user);
        return user.getCurrency();
    }

    /**
     * Get language preference
     * Following SRP - SettingsService handles language settings
     */
    public String getLanguage() {
        User user = userService.getLoggedInUser();
        return user.getLanguage() != null ? user.getLanguage() : "en";
    }

    /**
     * Update language preference
     * Following SRP - SettingsService handles language updates
     */
    @Transactional
    public String updateLanguage(String language) {
        if (language == null || language.trim().isEmpty()) {
            throw new IllegalArgumentException("Language cannot be empty");
        }
        User user = userService.getLoggedInUser();
        user.setLanguage(language);
        userService.saveUser(user);
        return user.getLanguage();
    }

    /**
     * Get privacy settings
     * Following SRP - SettingsService handles privacy settings
     */
    public PrivacySettingsDTO getPrivacySettings() {
        User user = userService.getLoggedInUser();
        PrivacySettingsDTO privacySettings = new PrivacySettingsDTO();
        privacySettings.setProfileVisible(user.getProfileVisible());
        privacySettings.setEmailVisible(user.getEmailVisible());
        privacySettings.setPhoneVisible(user.getPhoneVisible());
        privacySettings.setShowInSearch(user.getShowInSearch());
        return privacySettings;
    }

    /**
     * Update privacy settings
     * Following SRP - SettingsService handles privacy settings updates
     */
    @Transactional
    public PrivacySettingsDTO updatePrivacySettings(PrivacySettingsDTO privacySettingsDTO) {
        User user = userService.getLoggedInUser();
        
        if (privacySettingsDTO.getProfileVisible() != null) {
            user.setProfileVisible(privacySettingsDTO.getProfileVisible());
        }
        if (privacySettingsDTO.getEmailVisible() != null) {
            user.setEmailVisible(privacySettingsDTO.getEmailVisible());
        }
        if (privacySettingsDTO.getPhoneVisible() != null) {
            user.setPhoneVisible(privacySettingsDTO.getPhoneVisible());
        }
        if (privacySettingsDTO.getShowInSearch() != null) {
            user.setShowInSearch(privacySettingsDTO.getShowInSearch());
        }
        
        userService.saveUser(user);
        return getPrivacySettings();
    }
}

