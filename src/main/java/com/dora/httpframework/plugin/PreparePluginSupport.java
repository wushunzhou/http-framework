package com.dora.httpframework.plugin;

import com.dora.httpframework.exception.DoraException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Describe Cgi插件
 * @Author dora 1.0.1
 **/
@Component
@Slf4j
public class PreparePluginSupport {

    private static byte[] img;
    private static byte[] mp3;

    @PostConstruct
    public void init() {
        img = getStaticFile("default.jpg");
        mp3 = getStaticFile("default.mp3");
    }

    public static byte[] getImg() {
        return img;
    }

    public static byte[] getMp3() {
        return mp3;
    }

    public static String getMD5(byte[] bytes) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return Hex.encodeHexString(md5.digest(bytes)).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new DoraException("getMD5()", e.getCause());
        }
    }

    private byte[] getStaticFile(String fileName) {
        String targetStatic = "static/" + fileName;
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(targetStatic);
            if (is != null) {
                int n = 0;
                byte[] buffer = new byte[8096];
                while (-1 != (n = is.read(buffer))) {
                    out.write(buffer, 0, n);
                }
                return out.toByteArray();
            } else {
                throw new DoraException("未找到" + fileName);
            }
        } catch (IOException e) {
            throw new DoraException("getStaticBytes()", e.getCause());
        }
    }

}
