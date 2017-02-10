/**
 * elin4it.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package com.elin4it.spider.Util;

import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.*;

/**
 * @author ElinZhou elin@elin4it.com
 * @version $Id: TTest.java , v 0.1 2017/2/1 下午1:27 ElinZhou Exp $
 */
public class TTest {


    public static void main(String... args) {
//        ExecutorService executorService = Executors.newCachedThreadPool();

//        BlockingQueue<Future<Boolean>> queue = new LinkedBlockingQueue<>();
//
//        ExecutorCompletionService exec = new ExecutorCompletionService(executorService, queue);

        String host = "http://7tav5.top";

        String url = "http://7tav5.top/vod/8165.html";

//        for (int i = 0; i < 1000; i++) {

//        executorService.submit(() -> {

        try {


            Document document = SpiderUtil.getDocument(url);
            String newUrl = host + document.select("#container #content .video_download a[href]").first().attr("href");

            System.out.println(newUrl);

            URL url1 = new URL(newUrl);
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            //得到输入流
            InputStream inputStream = conn.getInputStream();
            //获取自己数组
//            byte[] getData = readInputStream(inputStream);

            byte[] buffer = new byte[1024];
            int len = 0;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();


            //文件保存位置
            File saveDir = new File("/Users/ElinZhou");
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            File file = new File(saveDir + File.separator + "a.mp4");
            FileOutputStream fos = new FileOutputStream(file);
            while ((len = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }


//            fos.write(getData);
            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }


            System.out.println("info:" + url + " download success");


        } catch (HttpStatusException e) {
            System.out.println("失败：" + e.getStatusCode());
        } catch (IOException e) {
            System.out.println(e);
        }

//        });
//        }


    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
