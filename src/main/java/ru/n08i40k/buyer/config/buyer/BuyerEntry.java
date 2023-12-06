package ru.n08i40k.buyer.config.buyer;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class BuyerEntry {
    private String id;
    private Map<Material, Integer> materialCosts;

    public BuyerEntry() {
        materialCosts = new HashMap<>();
        materialCosts.put(Material.DIAMOND, 1);

        id = "template_buyer";
    }
}
