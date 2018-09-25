package net.vexkitsplus.nathan.utils;


import guava10.com.google.common.collect.HashBasedTable;
import guava10.com.google.common.collect.Table;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private static final Table<String, String, Long> cd = HashBasedTable.create();

    public static boolean hasCooldown(String player, String key) {
        return getCooldown(player, key) > 0;
    }

    public static boolean hasCooldown(UUID player, String key) {
        return getCooldown(player, key) > 0;
    }

    public static boolean hasCooldown(Player player, String key) {
        return getCooldown(player, key) > 0;
    }

    public static long getCooldown(String player, String key) {
        long time = calc(cd.get(player, key));
        if (time <= 0) cd.remove(player, key);
        return time;
    }

    public static long getCooldown(UUID player, String key) {
        long time = calc(cd.get(player.toString(), key));
        if (time <= 0) cd.remove(player.toString(), key);
        return time;
    }

    public static long getCooldown(Player player, String key) {
        long time = calc(cd.get(player.getUniqueId().toString(), key));
        if (time <= 0) cd.remove(player.getUniqueId().toString(), key);
        return time;
    }

    public static Map<String, Long> getAllCooldowns(Player player, String startsWith){
        Map<String, Long> ret = new HashMap<>();

        for(Table.Cell<String, String, Long> cell : cd.cellSet()){
            if(cell.getRowKey().equalsIgnoreCase(player.getUniqueId().toString())){
                if(cell.getColumnKey().startsWith(startsWith)) ret.put(cell.getColumnKey(), calc(cell.getValue()));
            }
        }

        return ret;
    }

    public static void setCooldown(String player, String key, long timer) {
        cd.put(player, key, timer);
    }

    public static void setCooldown(UUID player, String key, long timer) {
        cd.put(player.toString(), key, timer);
    }

    public static void setCooldown(Player player, String key, long timer) {
        cd.put(player.getUniqueId().toString(), key, timer);
    }

    public static void removeCooldown(String player, String key) {
        cd.remove(player, key);
    }

    public static void removeCooldown(UUID player, String key) {
        cd.remove(player.toString(), key);
    }

    public static void removeCooldown(Player player, String key) {
        cd.remove(player.getUniqueId().toString(), key);
    }

    public static void removeAllCooldowns(String key) {
        cd.column(key).clear();
    }

    public static void removeAllCooldowns(Player player) {
        cd.row(player.getUniqueId().toString()).clear();
    }

    public static void save() {
        Map<String, Map<String, Long>> map = cd.rowMap();
        YamlConfiguration config = ConfigManager.get("cooldowns.yml");
        for (Map.Entry<String, Map<String, Long>> entry : map.entrySet()) {
            for (Map.Entry<String, Long> ent : entry.getValue().entrySet()) {
                config.set(ent.getKey() + "." + entry.getKey(), ent.getValue());
            }
        }
        ConfigManager.save("cooldowns.yml");
    }

    public static void load() {
        YamlConfiguration config = ConfigManager.get("cooldowns.yml");
        for (String s : config.getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection(s);
            for (String player : section.getKeys(false)) {
                long time = section.getLong(player);
                if (time > 0)
                    cd.put(player, s, time);
            }
        }
    }

    private static long calc(Long time) {
        return time != null ? (time > System.currentTimeMillis() ? time - System.currentTimeMillis() : 0) : 0;
    }

}