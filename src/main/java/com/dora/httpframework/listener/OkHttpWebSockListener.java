package com.dora.httpframework.listener;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Base64;

/**
 * @Describe WebSock监听事件
 * @Author dora 1.0.1
 **/
@Service
@Slf4j
public class OkHttpWebSockListener extends WebSocketListener {
    private final static String TAG = "Websocket";
    public WebSocket mWebsocket;
    public static String payloadId = "";
    public static String payload = "";

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        log.info(TAG,"websocket连接成功");
        super.onOpen(webSocket,response);
        mWebsocket = webSocket;
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, String text) {

        log.info(TAG, "收到服务端的消息");
        // 消息结构解析{需要自己定义解析}
        super.onMessage(webSocket, text);
        try {
            JSONObject jsonObject = JSONObject.parseObject(text);
            log.info("response=" + jsonObject);
            Assert.isTrue(jsonObject.containsKey("type"));
            String type= jsonObject.getString("type");
            switch (type){
                case "push":
                    Assert.isTrue(jsonObject.containsKey("data"));
                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                    Assert.isTrue(dataJsonObject.containsKey("payloadId"));
                    Assert.isTrue(dataJsonObject.containsKey("payload"));

                    payloadId = dataJsonObject.getString("payloadId");
                    String payloadBase64 = dataJsonObject.getString("payload");
                    payload = new String(Base64.getDecoder().decode(payloadBase64));
                    log.info("pushPayload=" + payload);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason){
        log.info("Websocket连接关闭中。" + webSocket.request().url());
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason){
        log.info("Websocket连接关闭。" + webSocket.request().url());
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        log.info("Websocket连接失败。" + webSocket.request().url());
    }
}
