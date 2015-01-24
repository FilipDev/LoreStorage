package me.pauzen.lorestore;

import me.pauzen.lorestore.items.ItemLoreData;
import org.bukkit.inventory.ItemStack;

public final class ItemDataStorageFactory {

    private ItemDataStorageFactory() {
    }

    public static ItemData registerItemDataFor(ItemStack itemStack) {
        return ItemLoreData.getData(itemStack);
    }

    public static ItemData fromID(String id) {
        ItemData itemData = ItemLoreData.getData(id);
        if (itemData == null) {
            throw new NullPointerException("You have not registered the ItemData for this ID yet. Use registerItemDataFor() first.");
        }
        return itemData;
    }

}
