package net.penguincoders.aipainting.util;

public enum UserResult {
    SUCCESS, // 登录成功
    INVALID_CREDENTIALS, // 无效的凭据（账号或密码错误）
    ACCOUNT_NOT_FOUND, // 找不到账号
    ACCOUNT_EXISTS, // 账号已存在
    REGISTER_ERROR,//注册失败
    SERVER_ERROR,
}
