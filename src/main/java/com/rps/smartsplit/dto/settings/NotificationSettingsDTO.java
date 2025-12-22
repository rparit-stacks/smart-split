package com.rps.smartsplit.dto.settings;

public class NotificationSettingsDTO {
    private Boolean emailNotifications;
    private Boolean pushNotifications;
    private Boolean smsNotifications;
    private Boolean expenseReminders;
    private Boolean settlementReminders;
    private Boolean groupInvites;

    public NotificationSettingsDTO() {
    }

    public Boolean getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public Boolean getPushNotifications() {
        return pushNotifications;
    }

    public void setPushNotifications(Boolean pushNotifications) {
        this.pushNotifications = pushNotifications;
    }

    public Boolean getSmsNotifications() {
        return smsNotifications;
    }

    public void setSmsNotifications(Boolean smsNotifications) {
        this.smsNotifications = smsNotifications;
    }

    public Boolean getExpenseReminders() {
        return expenseReminders;
    }

    public void setExpenseReminders(Boolean expenseReminders) {
        this.expenseReminders = expenseReminders;
    }

    public Boolean getSettlementReminders() {
        return settlementReminders;
    }

    public void setSettlementReminders(Boolean settlementReminders) {
        this.settlementReminders = settlementReminders;
    }

    public Boolean getGroupInvites() {
        return groupInvites;
    }

    public void setGroupInvites(Boolean groupInvites) {
        this.groupInvites = groupInvites;
    }
}

