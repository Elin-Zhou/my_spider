/**
 * elin4it.com Inc.
 * Copyright (c) 2014-2017 All Rights Reserved.
 */
package com.elin4it.spider.Bean;

/**
 * 回答者
 *
 * @author ElinZhou elin@elin4it.com
 * @version $Id: Answer.java , v 0.1 2017/1/31 下午4:19 ElinZhou Exp $
 */
public class Answer extends BasicUser {
    protected int nAgree;
    protected int nCommont;

    public int getnAgree() {
        return nAgree;
    }

    public void setnAgree(int nAgree) {
        this.nAgree = nAgree;
    }

    public int getnCommont() {
        return nCommont;
    }

    public void setnCommont(int nCommont) {
        this.nCommont = nCommont;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "nAgree=" + nAgree +
                ", nCommont=" + nCommont +
                '}';
    }
}
