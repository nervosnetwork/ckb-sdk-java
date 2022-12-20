package org.nervos.indexer;

import org.nervos.ckb.Network;

public enum IndexerType {
    StandAlone("https://mainnet.ckb.dev/indexer", "https://testnet.ckb.dev/indexer"),
    CkbModule("https://mainnet.ckb.dev/", "https://testnet.ckb.dev/");

    final String mainNetUrl;
    final String testNetUrl;

    IndexerType(String mainNetUrl, String testNetUrl) {
        this.mainNetUrl = mainNetUrl;
        this.testNetUrl = testNetUrl;
    }

    public String getUrl(Network network) {
        switch (network) {
            case MAINNET:
                return this.mainNetUrl;
            case TESTNET:
                return this.testNetUrl;
            default:
                throw new IllegalStateException("Unsupported network:"+ network);
        }
    }
}
