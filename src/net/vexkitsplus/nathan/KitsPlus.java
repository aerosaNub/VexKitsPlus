package net.vexkitsplus.nathan;

import net.vexkitsplus.nathan.commands.KitsCommand;
import net.vexkitsplus.nathan.listeners.KitsListener;
import net.vexkitsplus.nathan.manager.KitsManager;
import net.vexkitsplus.nathan.storage.KitsFlatFile;
import net.vexkitsplus.nathan.utils.CooldownManager;
import org.bukkit.plugin.java.JavaPlugin;

public class KitsPlus extends JavaPlugin {

    private static KitsPlus instance;
    private KitsFlatFile kitsFlatFile;

    public void onEnable() {

        instance = this;

        if (!this.getDataFolder().exists()) this.getDataFolder().mkdir();

        CooldownManager.load();

        this.kitsFlatFile = new KitsFlatFile();
        new KitsManager(this);

        getCommand("kits").setExecutor(new KitsCommand(this));
        getServer().getPluginManager().registerEvents(new KitsListener(), this);
    }

    public void onDisable() {

        try {
            this.kitsFlatFile.saveKitsData();
            CooldownManager.save();
        } catch (Exception e) {
            System.out.println("Unable to save kitData!");
        }

        instance = null;
    }

    public static KitsPlus getInstance() {
        return instance;
    }

    public KitsFlatFile getKitsFlatFile() {
        return kitsFlatFile;
    }
}
