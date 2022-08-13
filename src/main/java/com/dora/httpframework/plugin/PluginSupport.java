package com.dora.httpframework.plugin;

import com.dora.httpframework.core.base.Response;
import com.dora.httpframework.parse.BaseRequest;
import com.dora.httpframework.utils.ContextDataStorage;
import com.dora.httpframework.utils.SomeUtils;
import org.springframework.stereotype.Component;

/**
 * @Describe 通用插件支持
 * @Author dora 1.0.1
 **/
@Component
public class PluginSupport {

    private ContextDataStorage saveData = ContextDataStorage.getInstance();


    public boolean isTest() {
        return false;
    }

    public void createTimestamp() {
        saveData.setMethodAttribute("timestamp", String.valueOf(System.currentTimeMillis()));
    }

    public void setUptest(BaseRequest req) {
        System.out.println("setUptest:" + req.getRequests());
    }

    public void teardowm(String arg1, String arg2) {
        System.out.println("teardowm:" + arg1 + "," + arg2);
    }

    public void requestDowm(Response response, String arg2) {
        System.out.println("request:" + arg2);
        System.out.println("request-path:" + response.getRequests().getPath());
    }

    /**
     * 生成length位随机数,存入缓存
     *
     * @param length
     */
    public void randomInt(int length) {
        saveData.setAttribute("Random10", SomeUtils.getRandomInt(length));
    }

    /**
     * 生成orderid,存入缓存
     */
    public void randomAmapOrderId() {
        saveData.setMethodAttribute("randomOrderId", SomeUtils.getRandomInt(10));
    }

    /**
     * 生成当前localDateTime,存入缓存
     */
    public void localDateTime() {
        saveData.setAttribute("LocalDateTime", SomeUtils.getLocalDataTime());
    }
}
