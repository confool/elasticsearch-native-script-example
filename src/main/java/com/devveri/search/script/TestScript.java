package com.devveri.search.script;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.xcontent.support.XContentMapValues;
import org.elasticsearch.index.fielddata.ScriptDocValues;
import org.elasticsearch.script.AbstractDoubleSearchScript;

import java.util.Map;

/**
 * User: hilter
 * Date: 8/30/13
 * Time: 4:13 PM
 */
public class TestScript extends AbstractDoubleSearchScript {

    public static final String PREFIX = "prefix";
    public static final String NAME = "name";

    private String memberPrefix;

    public TestScript(@Nullable Map<String,Object> params) {
        memberPrefix = params == null ? null : XContentMapValues.nodeStringValue(params.get(PREFIX), null);
    }

    @Override
    public double runAsDouble() {
        String currentMember = getFieldAsString(NAME);
        if (currentMember != null && currentMember.startsWith(memberPrefix)) {
            return 1;
        }
        return 0.1;
    }

    private String getFieldAsString(String fieldName) {
        ScriptDocValues.Strings fieldData = (ScriptDocValues.Strings) doc().get(fieldName);
        return fieldData.getValue();
    }

}
