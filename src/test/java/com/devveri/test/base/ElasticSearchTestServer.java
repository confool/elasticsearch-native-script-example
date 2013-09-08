package com.devveri.test.base;

import com.devveri.search.client.ElasticSearchIndexHelper;
import com.devveri.search.client.ElasticSearchSearchHelper;
import com.devveri.search.script.TestScriptFactory;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;

import java.util.HashMap;
import java.util.Map;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 * User: hilter
 * Date: 9/8/13
 * Time: 12:13 PM
 */
public class ElasticSearchTestServer {

    private Node node;
    private Client client;

    private ElasticSearchIndexHelper indexer;
    private ElasticSearchSearchHelper searcher;

    public void init() {

        // cluster settings
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", "test")
                .put("script.native.testscript.type", TestScriptFactory.class.getName())
                 // following setting doesn't work, it always creates the data directory!
                .put("index.store.type", "memory")
                .build();

        // create node and get client
        node = nodeBuilder().local(true).node();
        client = node.client();

        // initialize indexer and searcher
        indexer = new ElasticSearchIndexHelper("test", "localhost:9200", "test", "doc");
        indexer.setClient(client);
        searcher = new ElasticSearchSearchHelper("test", "localhost:9200", "test", "doc");
        searcher.setClient(client);
    }

    public void shutdown() {
        if (client != null) {
            client.close();
        }
        if (node != null) {
            node.close();
        }
    }

    public ElasticSearchSearchHelper getSearcher() {
        return searcher;
    }

    public ElasticSearchIndexHelper getIndexer() {
        return indexer;
    }

}
