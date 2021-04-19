package com.veggie.thefarm.service;

import org.springframework.scheduling.annotation.Async;

/**
 * Created by Bohyun on 2021.04.16
 */
public interface SendMailService {

    @Async
    boolean sendMail(String receiverMail, String title, String content);

}
