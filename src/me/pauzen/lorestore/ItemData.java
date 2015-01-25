package me.pauzen.lorestore;

import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;

public interface ItemData {

    public Integer getInt(String key);

    public Double getDouble(String key);

    public String getString(String key);

    public String putString(String key, String value);

    public Integer putInt(String key, Integer value);

    public Double putDouble(String key, Double value);

    public String getID();

    public ItemStack getItemStack();

    public Set<String> getKeys();

    public Map<String, Object> getValues();

    public int countEntries();

    public boolean containsKey(String key);

    public String getLoreElement(int index);

    public Map.Entry<String, Object> getLoreEntry(int index);

    public void updateItemStack(ItemStack newItemStack);
}
