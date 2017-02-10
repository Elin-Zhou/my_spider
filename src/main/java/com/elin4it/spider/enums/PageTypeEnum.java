package com.elin4it.spider.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 页面类型
 *
 * @author ElinZhou
 * @version $Id: PageTypeEnum.java , v 0.1 2017/1/31 下午4:05 ElinZhou Exp $
 */
public enum PageTypeEnum {

    /**
     * 话题
     */
    TOPIC("/topic/"),


    /**
     * 用户
     */
    USER("/people/"),


    /**
     * 专栏
     */
    ZHUAN_LAN("zhuanlan."),


    /**
     * 收藏夹
     */
    COLLECTION("/collection/"),


    /**
     * 问题
     */
    QUESTION("/question/"),


    /**
     * 未知
     */
    UNKNOW("");

    /**
     * 知乎默认域名
     */
    private static final String BASIC_KEY = ".zhihu.com";

    private String keyWord;

    PageTypeEnum(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getKeyWord() {
        return keyWord;
    }

    /**
     * 通过URL获取页面类型
     *
     * @param url
     * @return
     */
    public static PageTypeEnum confirmType(String url) {

        if (StringUtils.isBlank(url)) {
            return UNKNOW;
        }

        if (url.indexOf(BASIC_KEY) == -1) {
            return UNKNOW;
        }


        for (PageTypeEnum typeEnum : values()) {
            if (url.indexOf(typeEnum.keyWord) != -1) {
                return typeEnum;
            }
        }

        return UNKNOW;
    }

}
