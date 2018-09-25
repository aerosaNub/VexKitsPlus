package net.vexkitsplus.nathan.storage;

import net.vexkitsplus.nathan.KitsPlus;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class KitsFlatFile {

    private File file;
    private YamlConfiguration configuration;

    public KitsFlatFile() {
        this.file = new File(KitsPlus.getInstance().getDataFolder(), "savedKits.yml");
        this.configuration = YamlConfiguration.loadConfiguration(file);

        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
                this.configuration.save(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public FileConfiguration getConfig() {
        return this.configuration;
    }

    public void saveKitsData() {
        try {
            this.configuration.save(file);
        } catch (Exception e) {
            System.out.println("Unable to save the file 'savedKits.yml'!");
        }
    }
}
