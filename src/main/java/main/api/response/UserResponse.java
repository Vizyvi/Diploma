package main.api.response;

import main.entity.User;

public class UserResponse {
    private Long id;
    private String name;
    private String photo;
    private String email;
    private Boolean moderation;
    private Integer moderationCount;
    private Boolean settings;

    public UserResponse(User user, Integer moderationPostCount) {
        id = user.getId();
        name = user.getName();
        photo = user.getPhoto();
        email = user.getEmail();
        moderation = user.isModerator();
        moderationCount = moderationPostCount;
        settings = user.isModerator();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getModeration() {
        return moderation;
    }

    public void setModeration(Boolean moderation) {
        this.moderation = moderation;
    }

    public Integer getModerationCount() {
        return moderationCount;
    }

    public void setModerationCount(Integer moderationCount) {
        this.moderationCount = moderationCount;
    }

    public Boolean getSettings() {
        return settings;
    }

    public void setSettings(Boolean settings) {
        this.settings = settings;
    }

}
