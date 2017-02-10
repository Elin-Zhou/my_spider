package com.elin4it.spider.Util;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * Yumeitech.com.cn Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
public class FileDownloadUtilTest {
    @org.junit.Test
    public void doDownload() throws Exception {

        String url = "https://download.sublimetext.com/Sublime%20Text%20Build%203126.dmg";
        String path = "/Users/ElinZhou/Documents";
        String fileName = "测试.dmg";
        Long start = System.currentTimeMillis();
        FileDownloadUtil.doDownload(url, path, fileName);
        Long end = System.currentTimeMillis();

        System.out.println(end - start);
    }

}