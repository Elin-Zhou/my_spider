/**
 * elin4it.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package com.elin4it.spider.Bean;

/**
 * 基础用户信息
 *
 * @author ElinZhou elin@elin4it.com
 * @version $Id: BasicUser.java , v 0.1 2017/1/31 下午4:48 ElinZhou Exp $
 */
public class BasicUser {
    /**
     * 昵称
     */
    protected String name;

    /**
     * 用户名
     */
    protected String username;


    /**
     * 用户简介
     */
    protected String summary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        return "BasicUser{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }
}
