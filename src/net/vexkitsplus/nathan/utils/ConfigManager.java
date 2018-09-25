package net.vexkitsplus.nathan.utils;

import net.vexkitsplus.nathan.KitsPlus;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private static Map<String, YamlConfiguration> cached_conf = new HashMap<String, YamlConfiguration>();
    private static Map<String, File> cached_file = new HashMap<String, File>();

    public static YamlConfiguration get(String name) {
        try {
            if (cached_conf.containsKey(name)) return cached_conf.get(name);
            File folder = KitsPlus.getInstance().getDataFolder();
            if (!folder.exists()) folder.mkdir();
            File file = new File(folder, name);
            if (!file.exists()) file.createNewFile();
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            cached_file.put(name, file);
            cached_conf.put(name, config);
            return config;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new YamlConfiguration();
    }

    @SuppressWarnings("all")
    public static void set(String name, String key, Object value) {
        get(name).set(key, value);
        save(name);
    }

    public static <T> T get(String name, String key) {
        return (T) get(name).get(key);
    }

    public static void save(String name) {
        try {
            if (cached_file.containsKey(name) && cached_conf.containsKey(name)) {
                YamlConfiguration conf = cached_conf.get(name);
                File f = cached_file.get(name);
                conf.save(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void save(YamlConfiguration conf) {
        save(conf.getName());
    }

}