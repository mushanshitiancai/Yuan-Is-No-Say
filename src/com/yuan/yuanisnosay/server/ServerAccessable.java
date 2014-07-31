package com.yuan.yuanisnosay.server;

import java.io.IOException;

public interface ServerAccessable {
    public String uploadImg();

    public String registerNewUser(String accessToken, String openID) throws IOException;
}
