package com.example.hyunwoo.greeneco;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by hyunwoo on 2017-12-07.
 */

public class HttpConnection {

    private OkHttpClient client;
    private static HttpConnection instance = new HttpConnection();

    public static HttpConnection getInstance() {
        return instance;
    }

    private HttpConnection() {
        this.client = new OkHttpClient();
    }

    /**
     * 웹 서버로 요청을 한다
     */
    //바코드
    public void requestWebServer(String parameter, Callback callback) {
        RequestBody body = new FormBody.Builder()
                .add("bcode", parameter)
                .build();

        Request request = new Request.Builder()
                .url("http://221.144.213.95/Code2.php")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //발주량
    public void requestWebServer2(String parameter1, String parameter2, Callback callback2) {
        RequestBody body = new FormBody.Builder()
                .add("company", parameter1)
                .add("date", parameter2)
                .build();

        Request request = new Request.Builder()
                .url("http://221.144.213.95/Orderlist.php")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback2);
    }

    //PackingOut
    public void requestWebServer4(String parameter1, String parameter2, Callback callback4) {
        RequestBody body = new FormBody.Builder()
                .add("company", parameter1)
                .add("date", parameter2)
                .build();

        Request request = new Request.Builder()
                .url("http://221.144.213.95/PackingOut.php")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback4);
    }

    //수정, 삭제
    public void requestWebServer5(String parameter1, String parameter2, String parameter3, Double parameter4, Double parameter5, int parameter6, int parameter7, Callback callback5) {
        RequestBody body = new FormBody.Builder()
                .add("bcode", parameter1)
                .add("date", parameter2)
                .add("company", parameter3)
                .add("amount", String.valueOf(parameter4))
                .add("editkg", String.valueOf(parameter5))
                .add("check", String.valueOf(parameter6))
                .add("pro", String.valueOf(parameter7))
                .build();

        Request request = new Request.Builder()
                .url("http://221.144.213.95/ProducePack.php")
                .post(body)
                .build();
        client.newCall(request).enqueue(callback5);
    }
}
