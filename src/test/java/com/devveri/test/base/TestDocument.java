package com.devveri.test.base;

import com.devveri.search.client.base.BaseDocument;

/**
 * User: hilter
 * Date: 9/8/13
 * Time: 12:36 PM
 */
public class TestDocument extends BaseDocument {

    private Integer memberId;
    private String name;
    private String email;

    @Override
    public String getId() {
        return memberId.toString();
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
