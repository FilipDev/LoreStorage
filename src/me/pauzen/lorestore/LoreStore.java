package me.pauzen.lorestore;

import me.pauzen.lorestoreimplementation.listeners.ItemInteractionListener;
import org.bukkit.plugin.java.JavaPlugin;

public class LoreStore extends JavaPlugin {
    
    @Override
    public void onEnable() {
        new ItemInteractionListener(this);
    }
    
}
