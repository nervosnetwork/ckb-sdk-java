package org.nervos.indexer;

import org.nervos.ckb.Network;

public class Configuration {
    private IndexerType indexerType = IndexerType.CkbModule;

    public void setIndexerUrl(String indexerUrl) {
        this.indexerUrl = indexerUrl;
    }

    private String indexerUrl;
    private static final class InstanceHolder {
        static final Configuration instance = new Configuration();
    }

    public static Configuration getInstance() {
        return InstanceHolder.instance;
    }

    public void setIndexType(IndexerType type) {
        this.indexerType = type;
    }

    public IndexerType getIndexerType() {
        return this.indexerType;
    }

    /**
     * Get index url, if indexerUrl is set, use the indexerUrl, or use the value in IndexerType.
     * @param network main net or test net.
     * @return The indexer url.
     */
    public String getUrl(Network network) {
        if (this.indexerUrl != null) {
            return indexerUrl;
        }
        return indexerType.getUrl(network);
    }
}
