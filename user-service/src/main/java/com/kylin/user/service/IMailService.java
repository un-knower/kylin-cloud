package com.kylin.user.service;

/**
 * @author liugh
 * @since on 2018/6/8.
 */
public interface IMailService {

     void sendSimpleMail(String subject,String content,String toEmail);
}
