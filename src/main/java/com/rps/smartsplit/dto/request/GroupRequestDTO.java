package com.rps.smartsplit.dto.request;

import java.util.UUID;

public class GroupRequestDTO {
    private String name;

    private String description;
    private String profileUrl;

    public GroupRequestDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}

