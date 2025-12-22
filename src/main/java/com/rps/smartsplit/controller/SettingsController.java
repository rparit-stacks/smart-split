package com.rps.smartsplit.controller;

import com.rps.smartsplit.dto.request.SettingsRequestDTO;
import com.rps.smartsplit.dto.request.CurrencyRequestDTO;
import com.rps.smartsplit.dto.request.LanguageRequestDTO;
import com.rps.smartsplit.dto.response.SettingsResponseDTO;
import com.rps.smartsplit.dto.response.CurrencyResponseDTO;
import com.rps.smartsplit.dto.response.LanguageResponseDTO;
import com.rps.smartsplit.dto.settings.NotificationSettingsDTO;
import com.rps.smartsplit.dto.settings.PrivacySettingsDTO;
import com.rps.smartsplit.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final SettingsService settingsService;

    /**
     * GET /api/settings
     * Get user settings
     */
    @GetMapping
    public ResponseEntity<SettingsResponseDTO> getSettings() {
        try {
            return ResponseEntity.ok(settingsService.getSettings());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * PUT /api/settings
     * Update user settings
     */
    @PutMapping
    public ResponseEntity<SettingsResponseDTO> updateSettings(@RequestBody SettingsRequestDTO settingsRequestDTO) {
        try {
            return ResponseEntity.ok(settingsService.updateSettings(settingsRequestDTO));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/settings/notifications
     * Get notification settings
     */
    @GetMapping("/notifications")
    public ResponseEntity<NotificationSettingsDTO> getNotificationSettings() {
        try {
            return ResponseEntity.ok(settingsService.getNotificationSettings());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * PUT /api/settings/notifications
     * Update notification settings
     */
    @PutMapping("/notifications")
    public ResponseEntity<NotificationSettingsDTO> updateNotificationSettings(@RequestBody NotificationSettingsDTO notificationSettingsDTO) {
        try {
            return ResponseEntity.ok(settingsService.updateNotificationSettings(notificationSettingsDTO));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/settings/currency
     * Get currency preference
     */
    @GetMapping("/currency")
    public ResponseEntity<CurrencyResponseDTO> getCurrency() {
        try {
            return ResponseEntity.ok(new CurrencyResponseDTO(settingsService.getCurrency()));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * PUT /api/settings/currency
     * Update currency
     */
    @PutMapping("/currency")
    public ResponseEntity<CurrencyResponseDTO> updateCurrency(@RequestBody CurrencyRequestDTO currencyRequestDTO) {
        try {
            return ResponseEntity.ok(new CurrencyResponseDTO(settingsService.updateCurrency(currencyRequestDTO.getCurrency())));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/settings/language
     * Get language preference
     */
    @GetMapping("/language")
    public ResponseEntity<LanguageResponseDTO> getLanguage() {
        try {
            return ResponseEntity.ok(new LanguageResponseDTO(settingsService.getLanguage()));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * PUT /api/settings/language
     * Update language
     */
    @PutMapping("/language")
    public ResponseEntity<LanguageResponseDTO> updateLanguage(@RequestBody LanguageRequestDTO languageRequestDTO) {
        try {
            return ResponseEntity.ok(new LanguageResponseDTO(settingsService.updateLanguage(languageRequestDTO.getLanguage())));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/settings/privacy
     * Get privacy settings
     */
    @GetMapping("/privacy")
    public ResponseEntity<PrivacySettingsDTO> getPrivacySettings() {
        try {
            return ResponseEntity.ok(settingsService.getPrivacySettings());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * PUT /api/settings/privacy
     * Update privacy settings
     */
    @PutMapping("/privacy")
    public ResponseEntity<PrivacySettingsDTO> updatePrivacySettings(@RequestBody PrivacySettingsDTO privacySettingsDTO) {
        try {
            return ResponseEntity.ok(settingsService.updatePrivacySettings(privacySettingsDTO));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

