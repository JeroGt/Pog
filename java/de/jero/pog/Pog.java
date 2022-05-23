package de.jero.pog;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Pog extends JavaPlugin {

    public static String prefix = "§7[§dPogPog§7] ";
    private static Pog instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.getCommand("gm").setExecutor(new Commands());
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new Join(), this);

    }

    @Override
    public void onDisable() {

    }

    public static Pog getInstance() {
        return instance;
    }
}
