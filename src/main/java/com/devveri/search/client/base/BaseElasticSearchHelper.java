package com.devveri.search.client.base;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.util.HashMap;
import java.util.Map;

/**
 * User: hilter
 * Date: 9/1/13
 * Time: 3:05 PM
 */
public abstract class BaseElasticSearchHelper {

    protected String clusterName;
    protected String hosts;
    protected String index;
    protected String type;

    protected Client client;

    public BaseElasticSearchHelper() {

    }

    public BaseElasticSearchHelper(String clusterName, String hosts, String index, String type) {
        this.clusterName = clusterName;
        this.hosts = hosts;
        this.index = index;
        this.type = type;
    }

    public void init() {
        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", clusterName).build();
        client = new TransportClient(settings);
        for (Map.Entry<String, Integer> entry : getHostsAsMap(hosts).entrySet()) {
            ((TransportClient) client).addTransportAddress(
                    new InetSocketTransportAddress(entry.getKey(), entry.getValue()));
        }
    }

    public void shutdown() {
        if (client != null) {
            client.close();
        }
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private Map<String, Integer> getHostsAsMap(String hosts) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String host : hosts.split(",")) {
            String[] values = host.split(":");
            map.put(values[0].trim(), Integer.parseInt(values[1]));
        }
        return map;
    }

}
