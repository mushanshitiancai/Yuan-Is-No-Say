package com.yuan.yuanisnosay.server;

import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.UUID;

//interface ServerAccessable 
//{
//    public static String updateUserInfo(String openID, String nickName, String sex, String imgFilePath) throws IOException;
//    public static String uploadPic(String imgPath);
    /*
    */
//    public String registerNewUser(String accessToken, String openID) throws IOException;
//}

public class ServerAccess //implements ServerAccessable 
{
    private static final String HOST = "http://10.66.144.182:8080/";//"http://yswy.r4c00n.com/";
    private static final String REG_NEW_USER = "login";
    private static final String CHARSET = "utf-8";
    private static final int TIME_OUT = 10 * 1000; // 超时时间
    private static final String PREFIX = "--", LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型

    public static String uploadPic(String imgPath) throws IOException
    {
        return uploadFile("/", "access_token=123123", imgPath);
    }

    private static String packMutipartData(String boundary, String key, String value)
    {
        StringBuffer sb = new StringBuffer();
        String LINE_END = "\r\n";

        sb.append(PREFIX);
        sb.append(boundary);
        sb.append(LINE_END);
        sb.append("Content-Disposition: form-data; name=\""+key+"\""+LINE_END);
        sb.append("Content-Type: text/plain; charset=utf-8"+LINE_END);
        sb.append(LINE_END);
        sb.append(value);
        sb.append(LINE_END);
        sb.append(PREFIX);
        sb.append(boundary);
        sb.append(PREFIX+LINE_END);
        
        return sb.toString();
    }
    
    private static String uploadFile(String uri, String params, String filePath) throws IOException
    {
        int responseCode = 0;
        String result = null;
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        File file = new File(filePath);
        URL url = new URL(HOST+uri);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        conn.setReadTimeout(TIME_OUT);
        conn.setConnectTimeout(TIME_OUT);
        conn.setDoInput(true); // 允许输入流
        conn.setDoOutput(true); // 允许输出流
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST"); // 请求方式
        conn.setRequestProperty("Charset", CHARSET); // 设置编码
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="+ BOUNDARY);

//        if (null != file) 
        {
            /**
             * 当文件不为空时执行上传
             */
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb = new StringBuffer();
            
            //sb.append(params);
            //sb.append("\r\n\r\n");
            /*sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINE_END);
            sb.append("Content-Disposition: form-data; name=\"access_token\""+LINE_END);
            sb.append("Content-Type: text/plain; charset=utf-8"+LINE_END);
            sb.append(LINE_END);
            sb.append(params);
            sb.append(LINE_END);
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(PREFIX+LINE_END);
            */
            String[] paramsArray = params.split("&");
            for (int i = 0; i < paramsArray.length; i++) 
            {
                int idxOfEqu = paramsArray[i].indexOf("=");
	        //sb.append(packMutipartData(BOUNDARY, paramsArray[i].substring(0, idxOfEqu), paramsArray[i].substring(idxOfEqu+1)));
	        System.out.println(paramsArray[i].substring(0, idxOfEqu)+paramsArray[i].substring(idxOfEqu+1));
            }
            
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINE_END);
            /**
             * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
             * filename是文件的名字，包含后缀名
             */
            sb.append("Content-Disposition: form-data; name=\"UpFile\"; filename=\""
                + file.getName() + "\"" + LINE_END);
            sb.append("Content-Type: application/octet-stream; charset="
                + CHARSET + LINE_END);
            sb.append(LINE_END);

            //System.out.println(sb);
            dos.write(sb.toString().getBytes());
            
            InputStream is = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            
            while ((len = is.read(bytes)) != -1) 
            {
                dos.write(bytes, 0, len);
            }
            is.close();
            
            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                .getBytes();
            dos.write(end_data);
            dos.flush();
            /**
             * 获取响应码 200=成功 当响应成功，获取响应的流
             */
            responseCode = conn.getResponseCode();
            System.out.println(responseCode);
            //System.out.println(TAG+":\n\tresponse code:" + res);
            if (200 == responseCode) 
            {
                //System.out.println(TAG+"\n\trequest success");
                InputStream input = conn.getInputStream();
                StringBuffer sb1 = new StringBuffer();
                int ss;
                
                while ((ss = input.read()) != -1) 
                {
                    sb1.append((char) ss);
                }
                result = sb1.toString();
                //System.out.println(TAG+":\n\tresult : " + result);
            }
        }
//	else
//	{
//	    throw new IOException();
//	}
        
        return result;
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

            //System.out.println("1");

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
