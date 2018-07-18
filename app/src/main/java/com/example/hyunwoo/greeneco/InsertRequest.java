package com.example.hyunwoo.greeneco;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by hyunwoo on 2017-12-21.
 */

public class InsertRequest {
    private URL url;

    public InsertRequest(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    private String readStream(InputStream in) throws IOException {
        StringBuilder jsonHtml = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = null;

        while ((line = reader.readLine()) != null)
            jsonHtml.append(line);

        reader.close();
        return jsonHtml.toString();
    }

    public String PHPRequest(final String data1, final String data2, final Double data3, final int data4, final String company) {
        try {
            String postData = "Data1=" + data1 + "&" + "Data2=" + data2 + "&" + "Data3=" + data3 + "&" + "Data4=" + data4 + "&" + "Company=" + company;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(postData.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();
            String result1 = readStream(conn.getInputStream());
            conn.disconnect();
            return result1;
        } catch (Exception e) {
            Log.i("PHPRequest", "request was failed.");
            return null;
        }
    }
}
