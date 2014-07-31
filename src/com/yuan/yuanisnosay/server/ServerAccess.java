package com.yuan.yuanisnosay.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ServerAccess implements ServerAccessable 
{
    private static final String HOST = "http://10.66.144.182:8080/";//"http://yswy.r4c00n.com/";
    private static final String REG_NEW_USER = "login";
    private static final String CHARSET = "utf-8";

    public String uploadImg() 
    {
            return "/aa.png";
    }

    public String registerNewUser(String accessToken, String openID) throws IOException
    {
        String response = null;

        response = doPost(REG_NEW_USER, "access_token=" + accessToken + "&openid=" + openID);

        return response;
    }

    private static String doPost(String uri, String params) throws IOException
    {
        String response = null;
        PrintWriter out = null;
        BufferedReader in = null;

        //try
        //{
            URL url = new URL(HOST+uri);
            URLConnection conn = (HttpURLConnection) url.openConnection();

            System.out.println("1");

            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            //conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码

            out = new PrintWriter(conn.getOutputStream());
            out.print(params);
            out.flush();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = "";
            String line;
            while ((line = in.readLine()) != null)
            {
                response = response + line;
            }
//        }

        return response;
    }

}
