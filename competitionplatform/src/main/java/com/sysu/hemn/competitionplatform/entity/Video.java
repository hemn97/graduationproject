package com.sysu.hemn.competitionplatform.entity;

public class Video {
    private Long id;
    private String title;
    private String author;
    private String introduction;
    private String figure;
    private String category;
    private String tags;
    private String src;
    private Long viewNumber;
    private Long evaluateNumber;
    private String score;
    private String releaseTime;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getFigure() {
        return figure;
    }

    public void setFigure(String figure) {
        this.figure = figure;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Long getViewNumber() {
        return viewNumber;
    }

    public void setViewNumber(Long viewNumber) {
        this.viewNumber = viewNumber;
    }

    public Long getEvaluateNumber() {
        return evaluateNumber;
    }

    public void setEvaluateNumber(Long evaluateNumber) {
        this.evaluateNumber = evaluateNumber;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", introduction='" + introduction + '\'' +
                ", figure='" + figure + '\'' +
                ", category='" + category + '\'' +
                ", tags='" + tags + '\'' +
                ", src='" + src + '\'' +
                ", viewNumber=" + viewNumber +
                ", evaluateNumber=" + evaluateNumber +
                ", score='" + score + '\'' +
                ", releaseTime='" + releaseTime + '\'' +
                '}';
    }
}
