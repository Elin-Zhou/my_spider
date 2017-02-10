package com.elin4it.spider.Util;

import com.elin4it.spider.Bean.Answer;
import com.elin4it.spider.Bean.User;

import java.util.List;

/**
 * Yumeitech.com.cn Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
public class ZhihuUtilTest {
    @org.junit.Test
    public void getQuestionAnswers() throws Exception {
        String questionId = "34856591";
        List<Answer> answers = ZhihuUtil.getQuestionAnswers(questionId);

        for (Answer answer : answers) {
            System.out.println(answer);
        }
    }


    @org.junit.Test
    public void getUserInfo() throws Exception {

        String username = "rxin";
        username = "rednaxelafx";
        username = "yipingan";
        User user = ZhihuUtil.getUserInfo(username);

        System.out.println(user);

    }

}