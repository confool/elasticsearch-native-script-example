package com.devveri.search.client;

import com.devveri.search.client.base.BaseDocument;
import com.devveri.search.client.base.BaseElasticSearchHelper;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.admin.indices.flush.FlushRequestBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

/**
 * User: hilter
 * Date: 8/30/13
 * Time: 5:43 PM
 */
public class ElasticSearchIndexHelper extends BaseElasticSearchHelper {

    private Vector<BaseDocument> docs;
    private int batchCount = 1000;

    public ElasticSearchIndexHelper() {

    }

    public ElasticSearchIndexHelper(String clusterName, String hosts, String index, String type) {
        super(clusterName, hosts, index, type);
        docs = new Vector<BaseDocument>();
    }

    @Override
    public void shutdown() {
        if (docs.size() > 0) {
            bulkUpdate(docs);
        }
        super.shutdown();
    }

    public boolean removeIndex() {
        DeleteIndexResponse deleteIndexResponse = client.admin().indices()
                .delete(new DeleteIndexRequest(index))
                .actionGet();
        return deleteIndexResponse.isAcknowledged();
    }

    public void update(BaseDocument doc) throws IOException {
        client.prepareIndex(index, type, doc.getId())
                .setSource(doc.toJSON())
                .execute()
                .actionGet();
    }

    public void updateAsync(BaseDocument doc) {
        docs.add(doc);
        if (docs.size() >= batchCount) {
            synchronized (this) {
                if (docs.size() >= batchCount) {
                    flush();
                }
            }
        }
    }

    public void flush() {
        if (docs.size() > 0) {
            // renew buffer
            Vector<BaseDocument> buffer = docs;
            docs = new Vector<BaseDocument>();
            bulkUpdate(buffer);
            buffer.clear();
            // flush index
            client.admin().indices().flush(new FlushRequest(index)).actionGet();
        }
    }

    public boolean bulkUpdate(Collection<BaseDocument> docs) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (BaseDocument doc : docs) {
            bulkRequest.add(client.prepareIndex(index, type, doc.getId())
                    .setSource(doc.toJSON()));
        }
        BulkResponse bulkResponse = bulkRequest.execute()
                .actionGet();
        return bulkResponse.hasFailures();
    }

    public void delete(String id) {
        client.prepareDelete(index, type, id)
                .execute()
                .actionGet();
    }

    public void deleteAll() {
        client.prepareDeleteByQuery(index)
                .setQuery(QueryBuilders.matchAllQuery())
                .setTypes(type)
                .execute()
                .actionGet();
    }

    public int getBatchCount() {
        return batchCount;
    }

    public void setBatchCount(int batchCount) {
        this.batchCount = batchCount;
    }

}
