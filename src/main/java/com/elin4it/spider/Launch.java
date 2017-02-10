/**
 * elin4it.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package com.elin4it.spider;

import com.elin4it.spider.Bean.Video;
import com.elin4it.spider.Util.SevetTav5Util;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ElinZhou elin@elin4it.com
 * @version $Id: Launch.java , v 0.1 2017/2/1 下午2:42 ElinZhou Exp $
 */
public class Launch {

    private String path;

    private BlockingQueue<String> listQueue = new LinkedBlockingQueue<>();

    private BlockingQueue<String> pageQueue = new LinkedBlockingQueue<>(200);
    private BlockingQueue<Video> urlQueue = new LinkedBlockingQueue<>(200);

    private ExecutorService executorService = Executors.newFixedThreadPool(200);

    public Launch(String path) {
        this.path = path;
    }

    public void start() throws Exception {
        int pagenum = SevetTav5Util.getZipaiPageNum();

        for (int i = 1; i <= pagenum; i++) {
            String url = "http://7tav5.top/list3/" + i + ".html";

            listQueue.put(url);


        }
        int pageThread = 10;
        for (int i = 0; i < pageThread; i++) {
            executorService.submit(() -> {

                while (!Thread.interrupted()) {
                    try {

                        String url = listQueue.take();
                        try {

                            List<String> list = SevetTav5Util.getMp4Pages(url);

                            if (list != null && !list.isEmpty()) {
                                pageQueue.addAll(list);
                            }
                        } catch (Exception e) {
                            listQueue.put(url);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });


        }


        int nThread = 50;

        for (int i = 0; i < nThread; i++) {
            executorService.submit(() -> {

                while (!Thread.interrupted()) {
                    try {
                        String pageUrl = pageQueue.take();

//

                        Video video = SevetTav5Util.getMp4Url(pageUrl);

                        System.out.println("获取：" + video);

                        urlQueue.put(video);


                    } catch (Exception e) {

                    }
                }

            });
        }

        int nDownload = 10;


        for (int i = 0; i < nDownload; i++) {

            executorService.submit(() -> {
                while (!Thread.interrupted()) {
                    try {
                        Video video = urlQueue.take();
                        System.out.println("准备下载：" + video);

                        boolean success = SevetTav5Util.downloadFile(video.getUrl(), path, video.getName() + ".mp4");
                        if (!success) {
                            urlQueue.put(video);
                        } else {

                            System.out.println("下载完毕：" + video);
                        }

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            });


        }


    }


}
