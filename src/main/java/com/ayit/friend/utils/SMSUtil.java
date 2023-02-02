package com.ayit.friend.utils;

import com.ayit.friend.properties.MailProperties;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class SMSUtil extends Thread{
    private MailProperties mailProperties;

    //收件人信息
    private String email;

    private String code;
    public SMSUtil(String email, String code,MailProperties mailProperties){
        this.email = email;
        this.code = code;

        this.mailProperties = mailProperties;
    }

    @Override
    public void run() {
        try {
            Properties properties = new Properties();

            properties.setProperty("mail.host","smtp.qq.com");

            properties.setProperty("mail.transport.protocol","smtp");

            properties.setProperty("mail.smtp.auth","true");

            //QQ存在一个特性设置SSL加密，
            //注意如果不是最新的mail.jar包可能会没有这个MailSSLSocketFactory
            MailSSLSocketFactory sf = null;
            try {
                sf = new MailSSLSocketFactory();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);

            //创建一个session对象
            Session session = Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailProperties.getUsername(), mailProperties.getPassword());
                }
            });

            //开启debug模式
//				session.setDebug(true);

            //获取连接对象
            Transport transport = null;
            try {
                transport = session.getTransport();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }

            //连接服务器
            transport.connect(mailProperties.getHost(),mailProperties.getUsername(),mailProperties.getPassword());


            //创建一个邮件发送对象
            MimeMessage mimeMessage = new MimeMessage(session);
            //邮件发送人
            mimeMessage.setFrom(new InternetAddress(mailProperties.getUsername(),"Friend"));

            //邮件接收人
            mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress(email));

            //邮件标题
            mimeMessage.setSubject("校园friend验证码");

            //邮件内容
//				mimeMessage.setContent("网站注册成功，密码为"+user.getPassword()+"，请妥善保管密码","text/html;charset=UTF-8");

            mimeMessage.setContent("您好，感谢您在校园friend注册账户!: <br>您的账户激活邮箱验证码为:<b>" + code +"</b><br><br>如果不是本人操作，请忽略。<br><br>--<br>校园friend-大学生的交友家园。<br>朋友就在校园!","text/html;charset=UTF-8");
            //发送邮件
            transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());

            transport.close();

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
