package net.penguincoders.aipainting.reponse;

import com.google.gson.annotations.SerializedName;

public class UserProfileResponse {
    @SerializedName("userId")
    private String userId;
    @SerializedName("account")
    private String account; // 账号/手机号
    @SerializedName("password")
    private String password;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("gender")
    private String gender;
    @SerializedName("modelLibrary")
    private String modelLibrary; // 存储模型库的 JSON 字段
    @SerializedName("avater")
    private String avater;

    public String getNickname() {
        return nickname;
    }

    public String getGender() {
        return gender;
    }

    public String getModelLibrary() {
        return modelLibrary;
    }

    public String getAvater() {
        return avater;
    }
}
