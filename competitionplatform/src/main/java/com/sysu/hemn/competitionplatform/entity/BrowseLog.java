package com.sysu.hemn.competitionplatform.entity;

public class BrowseLog {
    private Long id;
    private String themeType;
    private Long themeId;
    private Long userId;
    private String LogTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThemeType() {
        return themeType;
    }

    public void setThemeType(String themeType) {
        this.themeType = themeType;
    }

    public Long getThemeId() {
        return themeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLogTime() {
        return LogTime;
    }

    public void setLogTime(String logTime) {
        LogTime = logTime;
    }

    @Override
    public String toString() {
        return "BrowseLog{" +
                "id=" + id +
                ", themeType='" + themeType + '\'' +
                ", themeId='" + themeId + '\'' +
                ", userId=" + userId +
                ", LogTime='" + LogTime + '\'' +
                '}';
    }
}
