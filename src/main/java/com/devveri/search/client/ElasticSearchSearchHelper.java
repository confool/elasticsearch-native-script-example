package com.devveri.search.client;

import com.devveri.search.client.base.BaseElasticSearchHelper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: hilter
 * Date: 8/30/13
 * Time: 5:43 PM
 */
public class ElasticSearchSearchHelper extends BaseElasticSearchHelper {

    public ElasticSearchSearchHelper() {

    }

    public ElasticSearchSearchHelper(String clusterName, String hosts, String index, String type) {
        super(clusterName, hosts, index, type);
    }

    public List<String> search(String text, int start, int rows) {
        return search("_source", text, start, rows);
    }

    public List<String> search(String field, String text, int start, int rows) {
        SearchResponse response = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.termQuery(field, text))
                .setFrom(start).setSize(rows).setExplain(true)
                .execute()
                .actionGet();

        return getDocIds(response);
    }

    public List<String> search(String method, Map<String, Object> parameters, int start, int rows) {
        SearchResponse response = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(QueryBuilders.customScoreQuery(QueryBuilders.matchAllQuery())
                        .lang("native").script(method).params(parameters))
                .setFrom(start).setSize(rows).setExplain(true)
                .execute()
                .actionGet();

        return getDocIds(response);
    }

    private List<String> getDocIds(SearchResponse response) {
        List<String> ids = new ArrayList<String>();
        SearchHits hits = response.getHits();
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()) {
            SearchHit searchHit = (SearchHit) iterator.next();
            if (searchHit.getScore() > 0) {
                ids.add(searchHit.getId());
            }
        }
        return ids;
    }

}
