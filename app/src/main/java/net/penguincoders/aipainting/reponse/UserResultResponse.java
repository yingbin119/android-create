package net.penguincoders.aipainting.reponse;

import com.google.gson.annotations.SerializedName;

public class UserResultResponse {
    @SerializedName("userResult")
    private String userResult;

    @SerializedName("userId")
    private String userId;  // 根据 JSON 数据类型来定义字段类型，这里假设 userId 是整数
    // 其他可能的字段

    // 添加 getter 和 setter 方法
    public String getResult() {
        return userResult;
    }

    public void setResult(String result) {
        this.userResult = result;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}


