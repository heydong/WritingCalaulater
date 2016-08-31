package com.myscript.atk.math.sample.network;

import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class JsonConnection {
    public static String getJSON(String json) {
        String mResult = "";
        boolean shouldDownLoad = true;

        if (true) {
            try {
                URL murl = new URL("http://yingxin.xmu.edu.cn/index.php/Home/Api/getAdByTime?time=" + json);
                //URL murl = new URL("http://106.0.4.149:8081/bootStrap/ClientPostServlet");
                //URL murl = new URL("http://192.168.66.43:8080/bootStrap/ClientPostServlet");
                //URL murl = new URL("http://106.0.4.149:8082/bootStrap/ClientPostServlet");
                HttpURLConnection connection = (HttpURLConnection) murl.openConnection();
                connection.setRequestProperty("Content-type", "application/json");
                if (Build.VERSION.SDK_INT > 13) {
                    connection.setRequestProperty("Connection", "close");
                }
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                connection.setReadTimeout(100000);
                connection.connect();
                OutputStream outStrm = connection.getOutputStream();
                Log.d("connection", "json" + json);
                //HttpURLconnection写数据与发送数据
                ObjectOutputStream objOutputStrm = new ObjectOutputStream(outStrm);
                objOutputStrm.writeObject(json);
                objOutputStrm.flush();                              //数据输出
                objOutputStrm.close();
                Log.d("connection", "写入成功");
                InputStream ins;

                int status = connection.getResponseCode();
                Log.d("connection", "写入成功" + status);
                ins = connection.getInputStream();

                Log.d("connection", "ObjectInputStream");
                ObjectInputStream objinput = new ObjectInputStream(ins);

                Log.d("connection", "input");
                mResult = (String) objinput.readObject();

                Log.d("connection", "读入成功" + mResult);

                Log.d("jsonre", json);
                return mResult;

            } catch (IOException e) {
                Log.d("Exception", e.toString());
                Log.d("connection", "Excption");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                Log.d("connection", "con");
            }
            Log.d("connection", mResult);
        } else {

        }

        return mResult;
    }

    public static String getURLResponse(String date) {
        HttpURLConnection conn = null; //连接对象
        InputStream is = null;
        String resultData = "";
        try {
            URL url = new URL("http://yingxin.xmu.edu.cn/index.php/Home/Api/getAdByTime?time=" + date); //URL对象
            conn = (HttpURLConnection) url.openConnection(); //使用URL打开一个链接
            conn.setDoInput(true); //允许输入流，即允许下载
            conn.setDoOutput(true); //允许输出流，即允许上传
            conn.setUseCaches(false); //不使用缓冲
            conn.setRequestMethod("GET"); //使用get请求
            is = conn.getInputStream();   //获取输入流，此时才真正建立链接
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bufferReader = new BufferedReader(isr);
            String inputLine = "";
            while ((inputLine = bufferReader.readLine()) != null) {
                resultData += inputLine + "\n";
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return resultData;
    }
}
