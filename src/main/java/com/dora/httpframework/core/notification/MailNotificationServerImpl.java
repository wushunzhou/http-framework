package com.dora.httpframework.core.notification;

import com.dora.httpframework.exception.DoraException;
import com.dora.httpframework.core.base.RepResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.testng.annotations.Test;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @Author: dora
 * @Date: 2019/9/16 9:42.
 * 邮件通知 SererImpl
 */
@Service
@Slf4j
public class MailNotificationServerImpl implements BaseNotificationServer {
    @Value("${notification.mail.sendAddress}")
    private String sendAddress;
//    private String sendAddress = "wushunzhou@lizhi.fm";

    @Value("${notification.mail.sendAccount}")
    private String sendAccount;
//    private String sendAccount = "wushunzhou@lizhi.fm";

    @Value("${notification.mail.sendPassword}")
    private String sendPassword;
//    private String sendPassword = "13144095352.wsz";

    @Value("${notification.mail.recipientAddress}")
    private String recipientAddress;
//    private String recipientAddress = "shengyawen@lizhi.fm";

    @Value("${notification.mail.protocol}")
    private String protocol;
//    private String protocol = "smtp";

    @Value("${notification.mail.host}")
    private String host;
//    private String host = "mail.lizhi.fm";

    @Override
    public void notification(NotificationRequest request) throws ParseException, MessagingException {
        if (request instanceof MailNotificationRequest) {
            Properties properties = new Properties();
            //设置用户的认证方式
            properties.setProperty("mail.smtp.auth", "true");
            //设置传输协议
            properties.setProperty("mail.transport.protocol", protocol);
            //设置发件人的SMTP服务器地址
            properties.setProperty("mail.smtp.host", host);
            //2、创建定义整个应用程序所需的环境信息的 Session 对象
            Session session = Session.getInstance(properties);

            //创建一封邮件的实例对象
            MimeMessage msg = new MimeMessage(session);
            //设置发件人地址
            msg.setFrom(new InternetAddress(sendAddress));
            /**
             * 设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
             * MimeMessage.RecipientType.TO:发送
             * MimeMessage.RecipientType.CC：抄送
             * MimeMessage.RecipientType.BCC：密送
             */
            String[] recipients = recipientAddress.split(",");
            InternetAddress[] internetAddress = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                internetAddress[i] = new InternetAddress(recipients[i]);
            }
            msg.setRecipients(MimeMessage.RecipientType.TO, internetAddress);
            //设置邮件主题

            msg.setSubject(((MailNotificationRequest) request).getSubject(), "UTF-8");
            //设置邮件正文
            msg.setContent(((MailNotificationRequest) request).getContext(), "text/html;charset=UTF-8");
            //设置邮件的发送时间,默认立即发送
            msg.setSentDate(StringUtils.isBlank(((MailNotificationRequest) request).getDate()) ? new Date() : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(((MailNotificationRequest) request).getDate()));

            //根据session对象获取邮件传输对象Transport
            Transport transport = session.getTransport();
            //设置发件人的账户名和密码
            transport.connect(sendAccount, sendPassword);
            //发送邮件，并发送到所有收件人地址，message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
            transport.sendMessage(msg, msg.getAllRecipients());
            log.info("邮件发送成功,to:{}", recipientAddress);

            //关闭邮件连接
            transport.close();
        } else {
            throw new DoraException("Mail请求参数错误");
        }

    }

    @Override
    public RepResult notificationResult(NotificationRequest request) throws Exception {
        try {
            notification(request);
        } catch (DoraException ex) {
            return RepResult.notificationFail("Mail请求参数错误");
        }
        return RepResult.success("邮件发送成功，to: {" + recipientAddress + "}");
    }

    //  测试邮件通知
    @Test
    private void MailTest() throws Exception {
//        MailNotificationRequest request = MailNotificationRequest.builder()
//                .subject("Test-subject")
//                .context("Test-context")
//                .build();
//
//        notification(request);
    }
}
