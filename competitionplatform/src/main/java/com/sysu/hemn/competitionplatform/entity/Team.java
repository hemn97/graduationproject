package com.sysu.hemn.competitionplatform.entity;

public class Team {
    private Long id;
    private Long comptId;
    private String comptTitle;
    private String teamStatus;
    private String name;
    private Long captainId;
    private String captainName;
    private String motto;
    private String status;
    private String register_time;
    private String joinTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getComptId() {
        return comptId;
    }

    public void setComptId(Long comptId) {
        this.comptId = comptId;
    }

    public String getComptTitle() {
        return comptTitle;
    }

    public void setComptTitle(String comptTitle) {
        this.comptTitle = comptTitle;
    }

    public String getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(String teamStatus) {
        this.teamStatus = teamStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCaptainId() {
        return captainId;
    }

    public void setCaptainId(Long captainId) {
        this.captainId = captainId;
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", comptId=" + comptId +
                ", comptTitle='" + comptTitle + '\'' +
                ", teamStatus='" + teamStatus + '\'' +
                ", name='" + name + '\'' +
                ", captainId=" + captainId +
                ", captainName='" + captainName + '\'' +
                ", motto='" + motto + '\'' +
                ", status='" + status + '\'' +
                ", register_time='" + register_time + '\'' +
                ", joinTime='" + joinTime + '\'' +
                '}';
    }
}
