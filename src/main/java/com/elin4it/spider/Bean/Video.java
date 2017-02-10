/**
 * elin4it.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package com.elin4it.spider.Bean;

/**
 * @author ElinZhou elin@elin4it.com
 * @version $Id: Video.java , v 0.1 2017/2/1 下午2:49 ElinZhou Exp $
 */
public class Video {

    private String name;
    private String url;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public String toString() {
        return "Video{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
