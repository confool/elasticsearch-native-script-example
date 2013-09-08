package com.devveri.search.util;

import com.google.gson.Gson;

/**
 * User: hilter
 * Date: 8/31/13
 * Time: 11:20 PM
 */
public abstract class JSONSerializable {

    public String toJSON() {
        return new Gson().toJson(this);
    }

}
