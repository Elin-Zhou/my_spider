/**
 * elin4it.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package com.elin4it.spider.Util;

import com.elin4it.spider.Bean.Video;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ElinZhou elin@elin4it.com
 * @version $Id: SevetTav5Util.java , v 0.1 2017/2/1 下午2:08 ElinZhou Exp $
 */
public class SevetTav5Util {

    private static final String HOST = "http://7tav5.top";

    private static final String zipaiHome = "http://7tav5.top/list3/1.html";


    public static int getZipaiPageNum() throws IOException {

        Document document = SpiderUtil.getDocument(zipaiHome);

        Elements elements = document.select(".pagination a");

        String lastPageUrl = elements.get(elements.size() - 1).attr("href");

        //   /list1/260.html

        int start = "/list1/".length();

        int end = lastPageUrl.indexOf(".html");

        String num = lastPageUrl.substring(start, end);

        return Integer.parseInt(num);

    }


    public static List<String> getMp4Pages(String url) throws IOException {

        Document document = SpiderUtil.getDocument(url);

        Elements elements = document.select("#container #content .span-755 .box div[style=margin-left:9px;] .video_box  a[href]");

        List<String> list = new ArrayList<>(elements.size());

        for (Element element : elements) {
            list.add(HOST + element.attr("href"));
        }


        return list;
    }


    public static Video getMp4Url(String url) throws IOException {
        Document document = SpiderUtil.getDocument(url);
        String newUrl = HOST + document.select("#container #content .video_download a[href]").first().attr("href");
        String name = document.select("#container #content h1#title_video").first().text();

        Video video = new Video();
        video.setUrl(newUrl);
        video.setName(name);

        return video;


    }


    public static boolean downloadFile(String url, String path, String fileName) {
        try {
            FileDownloadUtil.doDownload(url, path, fileName);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * 网上获取文件
     *
     * @param savepath 保存路径
     * @param resurl   资源路径
     * @param fileName 自定义资源名
     */
    public static void getInternetRes(String savepath, String resurl, String fileName) {
        URL url = null;
        HttpURLConnection con = null;
        InputStream in = null;
        FileOutputStream out = null;
        try {
            url = new URL(resurl);
            //建立http连接，得到连接对象
            con = (HttpURLConnection) url.openConnection();
            //con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            in = con.getInputStream();
            byte[] data = getByteData(in);//转化为byte数组

            File file = new File(savepath);
            if (!file.exists()) {
                file.mkdirs();
            }

            File res = new File(file + File.separator + fileName);
            out = new FileOutputStream(res);
            out.write(data);
            System.out.println("downloaded successfully!");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out)
                    out.close();
                if (null != in)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param in
     * @return
     * @throws IOException
     */
    private static byte[] getByteData(InputStream in) throws IOException {
        byte[] b = new byte[1024];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int len = 0;
        while ((len = in.read(b)) != -1) {
            bos.write(b, 0, len);
        }
        if (null != bos) {
            bos.close();
        }
        return bos.toByteArray();
    }

}
