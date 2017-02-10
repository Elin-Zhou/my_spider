/**
 * elin4it.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package com.elin4it.spider.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 性别
 *
 * @author ElinZhou elin@elin4it.com
 * @version $Id: SexEnum.java , v 0.1 2017/1/31 下午4:34 ElinZhou Exp $
 */
public enum SexEnum {

    MALE("他"),

    FEMALE("她"),

    UNKNOW(""),;


    private String keyWord;

    private SexEnum(String keyWord) {
        this.keyWord = keyWord;
    }

    public static SexEnum confirmSex(String content) {

        if (StringUtils.isBlank(content)) {
            return UNKNOW;
        }


        for (SexEnum sexEnum : values()) {
            if (content.indexOf(sexEnum.keyWord) != -1) {
                return sexEnum;
            }
        }
        return UNKNOW;
    }


}
