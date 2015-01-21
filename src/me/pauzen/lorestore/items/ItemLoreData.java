package me.pauzen.lorestore.items;

import me.pauzen.lorestore.ItemData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemLoreData implements ItemData {

    private static final ChatColor                    DEF_COLOUR    = ChatColor.WHITE;
    private static final Random                       RANDOM        = new Random();
    private static final String[]                     TESTS         = new String[]{": ", "="};
    private static       Map<ItemStack, ItemLoreData> itemLoreCache = new HashMap<>();
    private static       Map<String, ItemLoreData>    idCache       = new HashMap<>();
    private              Map<String, Object>          values        = new HashMap<>();
    private ItemStack    itemStack;
    private String       id;
    private List<String> lore;
    private Map<String, Map.Entry<String, Object>> entryCache = new HashMap<>();

    public ItemLoreData(ItemStack itemStack, String id) {
        this.itemStack = itemStack;
        this.id = id;
        this.readAll();
    }

    public ItemLoreData(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.readAll();
        this.id = getID();
    }

    public static String getID(ItemStack itemStack) {
        return getItemLoreData(itemStack).getID();
    }

    public static ItemLoreData getItemLoreData(ItemStack itemStack) {
        if (itemLoreCache.containsKey(itemStack)) {
            return itemLoreCache.get(itemStack);
        }
        ItemLoreData itemLoreData = new ItemLoreData(itemStack);
        itemLoreCache.put(itemStack, itemLoreData);
        idCache.put(itemLoreData.getID(), itemLoreData);
        return itemLoreData;
    }

    public static ItemLoreData getItemLoreData(String id) {
        return idCache.get(id);
    }

    @Override
    public String getID() {
        return getString("id");
    }

    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public String getString(String key) {
        return String.valueOf(this.values.get(key));
    }

    @Override
    public String putString(String key, String value) {
        return (String) writeValue(key, value);
    }

    @Override
    public Integer putInt(String key, Integer value) {
        return (Integer) writeValue(key, value);
    }

    @Override
    public Double putDouble(String key, Double value) {
        return (Double) writeValue(key, value);
    }

    @Override
    public Integer getInt(String key) {
        try {
            return (Integer) this.values.get(key);
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Override
    public Double getDouble(String key) {
        try {
            return (Double) this.values.get(key);
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Override
    public int countEntries() {
        return this.values.size();
    }

    @Override
    public Set<String> getKeys() {
        return this.values.keySet();
    }

    @Override
    public Map<String, Object> getValues() {
        return this.values;
    }

    @Override
    public boolean containsKey(String key) {
        return this.values.containsKey(key);
    }

    @Override
    public String getLoreElement(int index) {
        return this.lore.get(index);
    }

    @Override
    public Map.Entry<String, Object> getLoreEntry(int index) {
        if (this.entryCache.containsKey(getLoreElement(index))) {
            return this.entryCache.get(getLoreElement(index));
        }
        Map.Entry<String, Object> entry = toEntry(getLoreElement(index));
        this.entryCache.put(getLoreElement(index), entry);
        return entry;
    }

    private Object writeValue(String key, Object value) {
        return writeValue(DEF_COLOUR, key, value);
    }

    private Object writeValue(ChatColor chatColor, String key, Object value) {

        tryItemMeta();

        if (!checkID()) {
            writeValue(ChatColor.BLACK, "id", generateID(6));
        }

        ItemMeta itemMeta = itemStack.getItemMeta();

        List<String> itemLore = itemMeta.getLore();
        for (String line : itemLore) {
            String[] parts = getParts(line);

            if (parts[0].equalsIgnoreCase(key)) {
                String finishedLine = chatColor + key + ": " + value;
                itemLore.remove(line);
                itemLore.add(finishedLine);
            }
        }
        itemStack.setItemMeta(itemMeta);

        return this.values.put(key, value);
    }

    private void tryItemMeta() {
        if (!itemStack.hasItemMeta()) {
            Bukkit.getItemFactory().getItemMeta(itemStack.getType());
        }
    }

    private void readAll() {

        tryItemMeta();

        if (!(itemStack.getItemMeta().hasLore())) {
            List<String> itemLore = itemStack.getItemMeta().getLore();
            this.lore = itemLore;
            for (String line : itemLore) {
                Map.Entry<String, Object> entry = toEntry(line);
                this.values.put(entry.getKey(), entry.getValue());
            }
        }
        if (!checkID()) {
            writeValue(ChatColor.BLACK, "id", generateID(6));
        }
    }

    private String generateID(int length) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = ((char) (RANDOM.nextInt(93) + 33));
        }
        return new String(chars);
    }

    private String[] getParts(String string) {
        String[] keyVal;
        for (String test : TESTS) {
            if ((keyVal = string.split(test)).length == 2) {
                return keyVal;
            }
        }
        return new String[]{string};
    }

    private Object tryParse(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ignored) {
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
        }

        return value;

    }

    private Map.Entry<String, Object> toEntry(String line) {
        String[] keyVal = getParts(ChatColor.stripColor(line));
        return new AbstractMap.SimpleEntry<>(keyVal[0], tryParse(keyVal[1]));
    }

    private boolean checkID() {
        return this.values.get("id").equals(this.id);
    }
}
