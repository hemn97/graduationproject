package com.sysu.hemn.competitionplatform.entity;

public class Comment {
    private Long id;
    private Long userId;    // 评论人id
    private String nickname;    // 评论人昵称
    private String avatar;  // 评论人头像
    private String school;  // 评论人学校
    private Long replyId;   // 相关评论id
    private Long replyUserId;   // 相关评论人id
    private String replyNickname;   // 相关评论人昵称
    private String replyContent;    // 相关评论内容
    private String themeType;  // 评论主题
    private Long themeId;   // 评论主题id
    private Long fartherId; // 所属父级评论id
    private String content; // 评论内容
    private String releaseTime; // 评论时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSchool() {
        return avatar;
    }

    public void setSchool(String school) {
        this.school = school;
    }
    public Long getReplyId() {
        return replyId;
    }

    public void setReplyId(Long replyId) {
        this.replyId = replyId;
    }

    public Long getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(Long replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getReplyNickname() {
        return replyNickname;
    }

    public void setReplyNickname(String replyNickname) {
        this.replyNickname = replyNickname;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
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

    public Long getFartherId() {
        return fartherId;
    }

    public void setFartherId(Long fartherId) {
        this.fartherId = fartherId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", userId=" + userId +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", school='" + school + '\'' +
                ", replyId=" + replyId +
                ", replyUserId=" + replyUserId +
                ", replyNickname='" + replyNickname + '\'' +
                ", replyContent='" + replyContent + '\'' +
                ", themeType='" + themeType + '\'' +
                ", themeId=" + themeId +
                ", fartherId=" + fartherId +
                ", content='" + content + '\'' +
                ", releaseTime='" + releaseTime + '\'' +
                '}';
    }
}
