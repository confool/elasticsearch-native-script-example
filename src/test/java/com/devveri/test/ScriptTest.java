package com.devveri.test;

import com.devveri.search.script.TestScript;
import com.devveri.test.base.ElasticSearchTestServer;
import com.devveri.test.base.TestDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: hilter
 * Date: 9/8/13
 * Time: 12:11 PM
 */
public class ScriptTest {

    private static final int TEST_DOCS = 1000;

    private ElasticSearchTestServer testServer;

    @Before
    public void before() {
        testServer = new ElasticSearchTestServer();
        testServer.init();
        createTestData();
    }

    @After
    public void after() {
        testServer.shutdown();
    }

    @Test
    public void test() {
        // test documents exists
        List<String> docs = testServer.getSearcher().search("name", "member2", 0, 10);
        assert docs != null;
        assert docs.size() == 1;

        // sort by the members who has given prefix
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(TestScript.PREFIX, "member3");
        docs = testServer.getSearcher().search("testscript", parameters, 0, 10);
        assert docs != null;
        assert docs.size() == 10;
        System.out.println(docs);
    }

    private void createTestData() {
        for (int i = 1; i <= TEST_DOCS; i++) {
            TestDocument testDocument = new TestDocument();
            testDocument.setMemberId(i);
            testDocument.setName("member" + i);
            testDocument.setEmail("member" + i + "@example.com");
            testServer.getIndexer().updateAsync(testDocument);
        }
        testServer.getIndexer().flush();
    }

}
