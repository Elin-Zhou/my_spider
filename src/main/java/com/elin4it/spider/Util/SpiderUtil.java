/**
 * elin4it.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package com.elin4it.spider.Util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ElinZhou elin@elin4it.com
 * @version $Id: SpiderUtil.java , v 0.1 2017/1/31 下午4:03 ElinZhou Exp $
 */
public class SpiderUtil {

    /**
     * 获取爬取得到的对象
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static Document getDocument(String url) throws IOException {

//        HttpUtils.setProxyIp();

        Map<String, String> header = new HashMap<String, String>();
        header.put("User-Agent", "	Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0");
        header.put("Accept", "	text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        header.put("Accept-Language", "zh-cn,zh;q=0.5");
        header.put("Accept-Charset", "	GB2312,utf-8;q=0.7,*;q=0.7");
        header.put("Connection", "keep-alive");

        return Jsoup.connect(url).data(header)
                .timeout(3000).get();
    }

}
