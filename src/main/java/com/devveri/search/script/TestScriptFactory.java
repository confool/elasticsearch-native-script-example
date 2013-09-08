package com.devveri.search.script;

import org.elasticsearch.common.Nullable;
import org.elasticsearch.script.ExecutableScript;
import org.elasticsearch.script.NativeScriptFactory;

import java.util.Map;

/**
 * User: hilter
 * Date: 8/30/13
 * Time: 3:36 PM
 */
public class TestScriptFactory implements NativeScriptFactory {

    @Override public ExecutableScript newScript (@Nullable Map<String,Object> params) {
        return new TestScript(params);
    }

}
