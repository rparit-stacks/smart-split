package com.rps.smartsplit.dto.search;

import java.util.List;

public class SearchSuggestionsResponseDTO {
    private List<String> users;
    private List<String> groups;

    public SearchSuggestionsResponseDTO() {
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }
}

