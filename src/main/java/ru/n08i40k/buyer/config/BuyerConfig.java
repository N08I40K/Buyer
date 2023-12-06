package ru.n08i40k.buyer.config;

import lombok.Getter;
import lombok.Setter;
import ru.n08i40k.buyer.config.buyer.BuyerEntry;

import java.util.HashMap;

@Getter
@Setter
public class BuyerConfig {
    public static class BuyerEntryHashMap extends HashMap<String, BuyerEntry> {
        public void put(BuyerEntry value) {
            super.put(value.getId(), value);
        }

        @Override
        public BuyerEntry put(String key, BuyerEntry value) {
            return super.put(value.getId(), value);
        }
    }

    private BuyerEntryHashMap buyerCatalog;

    public BuyerConfig() {
        buyerCatalog = new BuyerEntryHashMap();
        buyerCatalog.put(new BuyerEntry());
    }
}
