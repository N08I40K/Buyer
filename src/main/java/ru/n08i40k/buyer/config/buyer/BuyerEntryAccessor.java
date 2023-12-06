package ru.n08i40k.buyer.config.buyer;

import com.google.common.base.Preconditions;
import lombok.NonNull;
import org.bukkit.Material;
import ru.n08i40k.buyer.BuyerPlugin;
import ru.n08i40k.buyer.config.BuyerConfig;
import ru.n08i40k.npluginlocale.LocaleRequestBuilder;
import ru.n08i40k.npluginlocale.MultipleLocaleResult;
import ru.n08i40k.npluginlocale.SingleLocaleResult;

import java.util.Map;

public class BuyerEntryAccessor {
    private static final LocaleRequestBuilder buyerLocaleRoot =
            new LocaleRequestBuilder(null, "buyer");

    public static BuyerConfig.BuyerEntryHashMap getBuyerCatalog() {
        return BuyerPlugin.getINSTANCE().getBuyerConfigData().getBuyerCatalog();
    }

    public static BuyerEntry getBuyerEntry(@NonNull String id) {
        BuyerConfig.BuyerEntryHashMap buyerCatalog = getBuyerCatalog();

        Preconditions.checkState(buyerCatalog.containsKey(id),
                "Can't find BuyerEntry with id %s!", id);

        return buyerCatalog.get(id);
    }

    public static Map<Material, Integer> getMaterialCosts(@NonNull String id) {
        BuyerEntry buyerEntry = getBuyerEntry(id);

        return buyerEntry.getMaterialCosts();
    }

    public static SingleLocaleResult getDisplayName(@NonNull String id) {
        getBuyerEntry(id); // check this BuyerEntry exists

        return buyerLocaleRoot.extend(id).get("name").getSingle();
    }

    public static MultipleLocaleResult getDescription(@NonNull String id) {
        return buyerLocaleRoot.extend(id).get("description")
                .format(Map.of("items_count", getMaterialCosts(id).size()))
                .getMultiple();
    }
}
