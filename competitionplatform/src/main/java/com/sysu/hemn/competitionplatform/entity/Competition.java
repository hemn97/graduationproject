package com.sysu.hemn.competitionplatform.entity;

public class Competition {
    private Long id;
    private String title;
    private String logo;
    private String category;
    private String sponsor;
    private String startTime;
    private String endTime;
    private String status;
    private String tags;
    private String details;
    private Long viewNumber;
    private Integer teamNumber;
    private String link;
    private Long heat;
    private String registerTime;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getLogo() {
        return logo;
    }
    public void setLogo(String logo) {
        this.logo = logo;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getSponsor() {
        return sponsor;
    }
    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    public String getRegisterTime() {
        return registerTime;
    }
    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }
    public Long getViewNumber() {
        return viewNumber;
    }
    public void setViewNumber(Long viewNumber) {
        this.viewNumber = viewNumber;
    }
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public Integer getTeamNumber() {
        return teamNumber;
    }

    public Long getHeat() {
        return heat;
    }

    public void setHeat(Long heat) {
        this.heat = heat;
    }

    public void setTeamNumber(Integer teamNumber) {
        this.teamNumber = teamNumber;
    }

    @Override
    public String toString() {
        return "Competition{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", logo='" + logo + '\'' +
                ", category='" + category + '\'' +
                ", sponsor='" + sponsor + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", status='" + status + '\'' +
                ", tags='" + tags + '\'' +
                ", details='" + details + '\'' +
                ", viewNumber=" + viewNumber +
                ", teamNumber=" + teamNumber +
                ", link='" + link + '\'' +
                ", heat=" + heat +
                ", registerTime='" + registerTime + '\'' +
                '}';
    }
}
