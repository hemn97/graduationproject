package com.sysu.hemn.competitionplatform.entity;

public class User {
    private Long id;
    private String nickname;
    private String gender;
    private String realname;
    private String telNumber;
    private Integer roleType; // 用户角色
    private String credential;
    private String activateCode;  // 激活码
    private String registerIp;
    private String registerTime;
    private String username;
    private String email;
    private String skills;  // 擅长技能
    private String school;  // 就读学校
    private String avatar;  // 头像

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public String getActivateCode() {
        return activateCode;
    }

    public void setActivateCode(String activateCode) {
        this.activateCode = activateCode;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", gender='" + gender + '\'' +
                ", realname='" + realname + '\'' +
                ", telNumber='" + telNumber + '\'' +
                ", roleType=" + roleType +
                ", credential='" + credential + '\'' +
                ", activateCode='" + activateCode + '\'' +
                ", registerIp='" + registerIp + '\'' +
                ", registerTime='" + registerTime + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", skills='" + skills + '\'' +
                ", school='" + school + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
