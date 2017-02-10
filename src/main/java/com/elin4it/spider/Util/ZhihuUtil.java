/**
 * elin4it.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package com.elin4it.spider.Util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.elin4it.spider.Bean.Answer;
import com.elin4it.spider.Bean.BasicUser;
import com.elin4it.spider.Bean.User;
import com.elin4it.spider.enums.PageTypeEnum;
import com.elin4it.spider.enums.SexEnum;
import com.elin4it.spider.exception.PageContentException;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 知乎页面解析工具类
 *
 * @author ElinZhou elin@elin4it.com
 * @version $Id: ZhihuUtil.java , v 0.1 2017/1/31 下午4:01 ElinZhou Exp $
 */
public class ZhihuUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZhihuUtil.class);

    public static final String HOST = "https://www.zhihu.com";


    private static final ExecutorService spiderExec = new ThreadPoolExecutor(20, 500, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>(5000));

    /**
     * 获取问题下的回答者信息
     *
     * @param questionId
     * @return
     * @throws IOException
     */
    public static List<Answer> getQuestionAnswers(String questionId) throws IOException {

        Document document;
        try {
            document = SpiderUtil.getDocument(createUrl(questionId, PageTypeEnum.QUESTION));
        } catch (HttpStatusException e) {
            LOGGER.error("访问问题{}错误，错误码:{}", questionId, e.getStatusCode());
            return null;
        }

        Element root = document.select("#zh-question-answer-wrap").first();

        if (root == null) {
            throw new PageContentException("页面为空");
        }

        Elements allAnswers = root.children();

        List<Answer> answers = new ArrayList<Answer>(allAnswers.size());

        Answer answer;

        for (Element element : allAnswers) {
            Element head = element.select(".answer-head").first();
            Element authorInfo = head.select(".zm-item-answer-author-info").first();
            Element vote = head.select(".zm-item-vote-info .voters .js-voteCount").first();

            answer = new Answer();
            answers.add(answer);

            answer.setnAgree(-1);
            if (vote == null) {
                throw new PageContentException("获取投票数异常");
            } else {
                answer.setnAgree(Integer.parseInt(vote.text()));
            }

            //判断是否为匿名用户
            if (authorInfo.childNodeSize() == 3) {
                //匿名用户
                answer.setName("匿名用户");

            } else {
                Elements info = authorInfo.children().get(1).children();

                Element author = info.get(0);
                answer.setUsername(author.child(0).attr("href").substring(PageTypeEnum.USER.getKeyWord().length()));
                answer.setName(author.text());

                if (info.size() >= 2) {
                    answer.setSummary(info.get(1).text());
                }
            }

            Element panel = element.select(".answer-actions").first();

            String comment = panel.select("a.toggle-comment").text();

            if (comment.equals("添加评论")) {
                answer.setnCommont(0);
            } else {
                answer.setnCommont(Integer.parseInt(comment.split(" ")[0]));
            }


        }

        return answers;


    }

    /**
     * 获取用户的详情
     *
     * @param username
     * @return
     */
    public static User getUserInfo(String username) throws IOException {

        LOGGER.info("获取用户{}详情", username);

        User user = new User();

        user.setUsername(username);

        Document document;
        try {

            document = SpiderUtil.getDocument(createUrl(username, PageTypeEnum.USER));
        } catch (HttpStatusException e) {
            LOGGER.error("访问用户{}主页错误，错误码:{}", username, e.getStatusCode());
            return null;
        }

        //个人信息从json中读取
        JSONObject json = getData(document).getJSONObject("entities").getJSONObject("users");

        if (json == null) {
            return null;
        }

        JSONObject info = json.getJSONObject(username);

        if (info == null) {
            return null;
        }

        String weiboUrl = info.getString("sinaWeiboUrl");
        if (StringUtils.isNoneBlank(weiboUrl)) {
            user.setWeiboUrl(weiboUrl);
        }

        JSONArray educations = info.getJSONArray("educations");
        if (educations != null) {

            List<String> educationList = new ArrayList<>(educations.size());
            user.setEducations(educationList);

            JSONObject education;
            JSONObject major;
            JSONObject school;
            for (Object o : educations) {


                if (o != null) {
                    education = (JSONObject) o;

                    major = education.getJSONObject("major");
                    school = education.getJSONObject("school");

                    String m = "";
                    String s = "";
                    if (major != null) {
                        m = major.getString("name");
                    }
                    if (school != null) {
                        s = school.getString("name");
                    }

                    educationList.add(s + "-" + m);

                }

            }
        }

        JSONArray employments = info.getJSONArray("employments");
        if (employments != null) {

            List<String> employmentList = new ArrayList<String>(employments.size());
            user.setEmployments(employmentList);

            JSONObject employment;
            JSONObject company;
            JSONObject job;
            for (Object o : employments) {


                if (o != null) {
                    employment = (JSONObject) o;

                    company = employment.getJSONObject("company");
                    job = employment.getJSONObject("job");

                    String c = "";
                    String j = "";
                    if (company != null) {
                        c = company.getString("name");
                    }
                    if (job != null) {
                        j = job.getString("name");
                    }

                    employmentList.add(c + "-" + j);

                }

            }
        }

        JSONObject business = info.getJSONObject("business");

        if (business != null) {
            user.setBusiness(business.getString("name"));
        }

        JSONArray locations = info.getJSONArray("locations");

        if (locations != null) {
            JSONObject location;
            List<String> locationList = new ArrayList<String>();
            user.setLocations(locationList);
            for (Object o : locations) {
                if (o != null) {
                    location = (JSONObject) o;

                    String temp = location.getString("name");
                    if (StringUtils.isNoneBlank(temp)) {
                        locationList.add(temp);
                    }

                }
            }
        }


        Element profile = document.select(".ProfileHeader").first();

        String title = profile.select("h1.ProfileHeader-title span.ProfileHeader-name").first().text();

        user.setName(title);

        Element followButton = profile.select(".ProfileHeader-contentFooter button.FollowButton").first();

        String followButtonText = followButton.text();

        user.setSex(SexEnum.confirmSex(followButtonText));

        Element main = document.select(".Profile-main").first();

        Elements sides = main.select(".Profile-sideColumnItem");

        for (Element side : sides) {

            Element agreeEle = side.select(".IconGraf").first();

            if (agreeEle == null) {
                continue;
            }

            String agreeText = agreeEle.text();


            int length = "获得".length();

            int end = agreeText.indexOf("次赞同");

            if (end == -1) {
                continue;
            }

            String agree = agreeText.substring(length, end).trim();

            user.setnBeAgree(Long.parseLong(agree));

            String valueText = side.select(".Profile-sideColumnItemValue").first().text();

            String thankText = "次感谢，";

            end = valueText.indexOf(thankText);

            String thank = valueText.substring(length, end).trim();

            user.setnBeThank(Long.parseLong(thank));

            String collect = valueText.substring(end + thankText.length(), valueText.indexOf("次收藏")).trim();

            user.setnBeCollect(Long.parseLong(collect));
        }


        //ta的关注

//        try {
//            user.setFollows(getFollowingList(username));
//        } catch (IOException e) {
//            LOGGER.info("获取关注列表失败：", e);
//        }


        //关注ta的人
//        try {
//            user.setBeFolows(getFollowsList(username));
//        } catch (IOException e) {
//            LOGGER.info("获取被关注列表失败：", e);
//        }


        //话题
//        String topciStr = "following/topics";

        //专栏

        //问题

        //收藏夹

        return user;


    }


    /**
     * 获取用户的被关注列表
     *
     * @param username
     * @return
     * @throws IOException
     */
    public static List<BasicUser> getFollowsList(String username) throws IOException {
        return getFollowingOrFollowsList(createUrl(username, PageTypeEnum.USER) + "/followers?page=");
    }

    /**
     * 获取用户的关注列表
     *
     * @param username
     * @return
     * @throws IOException
     */
    public static List<BasicUser> getFollowingList(String username) throws IOException {
        return getFollowingOrFollowsList(createUrl(username, PageTypeEnum.USER) + "/following?page=");
    }


    public static List<BasicUser> getFollowingOrFollowsList(String url) throws IOException {

        List<BasicUser> follows = new ArrayList<>();

        BlockingQueue<Future<List<BasicUser>>> complateQueue = new LinkedBlockingQueue<>();
        ExecutorCompletionService executorCompletionService = new ExecutorCompletionService(spiderExec, complateQueue);

        int count = 0;


        for (int i = 1; i < 3934; i++) {


            String newUrl = url + i;
            executorCompletionService.submit(() -> {
                List<BasicUser> followingList = new ArrayList<>();
                Document followingDoc;
                try {
                    followingDoc = SpiderUtil.getDocument(newUrl);
                } catch (HttpStatusException e) {
                    LOGGER.error("访问链接{}错误，错误码:{}", newUrl, e.getStatusCode());
                    System.out.println("访问链接" + newUrl + "错误，错误码:" + e.getStatusCode());
                    return null;
                }

                Elements followings = followingDoc.select(".Profile-main .List .List-item");
                if (followings != null && followings.size() != 0) {

                    BasicUser basicUser;

                    JSONObject users = getData(followingDoc).getJSONObject("entities").getJSONObject("users");
                    JSONObject user;
                    for (Object o : users.values()) {
                        user = (JSONObject) o;
                        basicUser = new BasicUser();
                        basicUser.setName(user.getString("name"));
                        basicUser.setUsername(user.getString("urlToken"));
                        basicUser.setSummary(user.getString("headline"));

                        followingList.add(basicUser);

                    }

                }
                return followingList;
            });
            count++;

        }

        try {

            for (int i = 0; i < count; i++) {
                Future<List<BasicUser>> future = complateQueue.take();

                try {
                    List<BasicUser> temp = future.get();
                    if (temp != null) {
                        follows.addAll(temp);
                    }
                } catch (ExecutionException e) {
                    Throwable throwable = e.getCause();
                    LOGGER.error("获取关注列表失败，失败原因：", throwable);
                }

            }
        } catch (InterruptedException e) {
            LOGGER.error("获取关注列表被中断：", e);
            return follows;
        }

        return follows;

    }


    private static JSONObject getData(Element root) {
        String json = root.select("div#data[data-state]").first().attr("data-state");
        return JSONObject.parseObject(json);
    }

    private static String createUrl(String key, PageTypeEnum typeEnum) {
        if (StringUtils.isBlank(key) || typeEnum == null || typeEnum == PageTypeEnum.UNKNOW) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(HOST);
        if (typeEnum != PageTypeEnum.ZHUAN_LAN) {
            stringBuilder.append(typeEnum.getKeyWord()).append(key);
        } else {
            return null;
        }
        return stringBuilder.toString();
    }


}
