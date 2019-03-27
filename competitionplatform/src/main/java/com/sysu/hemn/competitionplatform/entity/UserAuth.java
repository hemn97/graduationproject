package com.sysu.hemn.competitionplatform.entity;

public class UserAuth {
    private Long id;
    private Long userId;
    private String identityType;
    private String identifier;
    private String credential;
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
    public String getIdentityType() {
        return identityType;
    }
    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }
    public String getIdentifier() {
        return identifier;
    }
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    public String getCredential() {
        return credential;
    }
    public void setCredential(String credential) {
        this.credential = credential;
    }
    @Override
    public String toString() {
        return "UserAuth [id=" + id + ", userId=" + userId + ", identityType=" + identityType + ", identifier="
                + identifier + ", credential=" + credential + "]";
    }
}
