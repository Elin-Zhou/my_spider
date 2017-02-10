/**
 * elin4it.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package com.elin4it.spider.Bean;

import com.elin4it.spider.enums.SexEnum;

import java.util.List;

/**
 * 用户
 *
 * @author ElinZhou elin@elin4it.com
 * @version $Id: User.java , v 0.1 2017/1/31 下午4:30 ElinZhou Exp $
 */
public class User extends BasicUser {

    /**
     * 性别
     */
    protected SexEnum sex;

    /**
     * 新浪微博地址
     */
    protected String weiboUrl;

    /**
     * 学校-专业
     */
    protected List<String> educations;

    /**
     * 公司-职位
     */
    protected List<String> employments;

    /**
     * 行业
     */
    protected String business;

    /**
     * 现居地
     */
    protected List<String> locations;


    /**
     * 被赞同数
     */
    protected Long nBeAgree;

    /**
     * 被感谢数
     */
    protected Long nBeThank;

    /**
     * 被收藏数
     */
    protected Long nBeCollect;


    /**
     * 被关注列表
     */
    protected List<BasicUser> beFolows;

    /**
     * 关注列表
     */
    protected List<BasicUser> follows;

    /**
     * 关注话题
     */
    protected List<Topic> topics;

    /**
     * 关注专栏
     */
    protected List<Zhuanlan> zhuanlans;

    /**
     * 关注问题
     */
    protected List<Question> questions;

    /**
     * 关注收藏夹
     */
    protected List<Collection> collections;


    public SexEnum getSex() {
        return sex;
    }

    public void setSex(SexEnum sex) {
        this.sex = sex;
    }

    public Long getnBeAgree() {
        return nBeAgree;
    }

    public void setnBeAgree(Long nBeAgree) {
        this.nBeAgree = nBeAgree;
    }

    public Long getnBeThank() {
        return nBeThank;
    }

    public void setnBeThank(Long nBeThank) {
        this.nBeThank = nBeThank;
    }

    public Long getnBeCollect() {
        return nBeCollect;
    }

    public void setnBeCollect(Long nBeCollect) {
        this.nBeCollect = nBeCollect;
    }

    public List<BasicUser> getBeFolows() {
        return beFolows;
    }

    public void setBeFolows(List<BasicUser> beFolows) {
        this.beFolows = beFolows;
    }

    public List<BasicUser> getFollows() {
        return follows;
    }

    public void setFollows(List<BasicUser> follows) {
        this.follows = follows;
    }

    public List<Topic> getTopics() {
        return topics;
    }

    public void setTopics(List<Topic> topics) {
        this.topics = topics;
    }

    public List<Zhuanlan> getZhuanlans() {
        return zhuanlans;
    }

    public void setZhuanlans(List<Zhuanlan> zhuanlans) {
        this.zhuanlans = zhuanlans;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public List<Collection> getCollections() {
        return collections;
    }

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
    }

    public String getWeiboUrl() {
        return weiboUrl;
    }

    public void setWeiboUrl(String weiboUrl) {
        this.weiboUrl = weiboUrl;
    }

    public List<String> getEducations() {
        return educations;
    }

    public void setEducations(List<String> educations) {
        this.educations = educations;
    }

    public List<String> getEmployments() {
        return employments;
    }

    public void setEmployments(List<String> employments) {
        this.employments = employments;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return super.toString() + "\nUser{" +
                "sex=" + sex +
                ", weiboUrl='" + weiboUrl + '\'' +
                ", educations=" + educations +
                ", employments=" + employments +
                ", business='" + business + '\'' +
                ", locations='" + locations + '\'' +
                ", nBeAgree=" + nBeAgree +
                ", nBeThank=" + nBeThank +
                ", nBeCollect=" + nBeCollect +
                '}';
    }
}
