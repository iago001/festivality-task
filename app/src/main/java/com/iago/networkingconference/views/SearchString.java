package com.iago.networkingconference.views;

import java.util.Objects;

public class SearchString {

    private String searchStr;

    public SearchString(String searchStr) {
        this.searchStr = searchStr;
    }

    public String getSearchStr() {
        return searchStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SearchString)) return false;
        SearchString that = (SearchString) o;
        return Objects.equals(searchStr, that.searchStr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(searchStr);
    }
}
