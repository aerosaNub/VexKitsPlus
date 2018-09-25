package net.vexkitsplus.nathan.manager;

import net.vexkitsplus.nathan.KitsPlus;
import net.vexkitsplus.nathan.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permissible;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KitsManager {

    private static KitsManager km;
    private final KitsPlus instance;

    public static KitsManager init() {
        if (km == null) {
            km = new KitsManager(KitsPlus.getInstance());
        }

        return km;
    }

    public KitsManager(KitsPlus instance) {
        this.instance = instance;
    }

    public static final String KITPERMS = "kitplus.kit.", INVENTORY_NAME = ChatColor.YELLOW + "Kits Menu";

    public String getKitDisplayName(String kit) {
        return this.instance.getKitsFlatFile().getConfig().getString(kit.toLowerCase() + ".displayName");
    }

    public void setKitDisplayName(String kit, String displayName) {
        this.instance.getKitsFlatFile().getConfig().set(kit.toLowerCase() + ".displayName", displayName);
    }

    public void setKitDescription(String kit, List<String> desc) {
        this.instance.getKitsFlatFile().getConfig().set(kit.toLowerCase() + ".description", desc.toArray(new String[desc.size()]));
    }

    public void setCooldown(String kit, long time) {
        this.instance.getKitsFlatFile().getConfig().set(kit.toLowerCase() + ".cooldown", time);
    }

    public long
    getCooldownFromKit(String kit) {
        return this.instance.getKitsFlatFile().getConfig().getLong(kit.toLowerCase() + ".cooldown");
    }

    public List<String> getKitDescription(String kit){
        List<String> description = new ArrayList<>();
        Object got = this.instance.getKitsFlatFile().getConfig().get(kit.toLowerCase() + ".description");
        if(got instanceof Iterable) {
            for (String s : (Iterable<String>) got) {
                description.add(ChatColor.translateAlternateColorCodes('&', s));
            }
        }
        else if(got instanceof String[]){
            for (String s : (String[]) got) {
                description.add(ChatColor.translateAlternateColorCodes('&', s));
            }
        }
        return description;
    }

    public Inventory createInventory(Permissible permissible) {
        int kits = getKits().size();
        Inventory inv = Bukkit.createInventory(null, kits + 18 - (kits % 9), INVENTORY_NAME);
        int i = 0;
        for (String kit : getKits()) {
            boolean hasPerms = permissible.hasPermission(KITPERMS + kit);
            ItemStack item;

            if (hasPerms) {
                item = new ItemStack(Material.getMaterial(materialName(kit)));
            } else {
                item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) DyeColor.RED.getData());
            }
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(C.c(getKitDisplayName(kit)));
            List<String > lore = new ArrayList<>();
            for (String s : getKitDescription(kit)) {
                lore.add(C.c(s));
            }

            lore.add("");
            lore.add(hasPerms ? C.c("&aYou have permissions to select this kit!") : C.c("&cYou have no permissions to use this kit!"));
            lore.add("");

            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(i, item);
            i++;
        }
        return inv;
    }

    public void loadKit(String kit, Player player) {
        ItemStack[] content = ((List<ItemStack>) this.instance.getKitsFlatFile().getConfig().get(kit.toLowerCase() + ".armor")).toArray(new ItemStack[0]);
        player.getInventory().setArmorContents(content);
        ItemStack[] conten1t = (((List<ItemStack>) this.instance.getKitsFlatFile().getConfig().get(kit.toLowerCase() + ".inventory")).toArray(new ItemStack[0]));
        player.getInventory().setContents(conten1t);
    }

    public ItemStack[] getContents(String kit) {
        return ((List<ItemStack>) this.instance.getKitsFlatFile().getConfig().get(kit.toLowerCase() + ".inventory")).toArray(new ItemStack[0]);
    }

    public ItemStack[] getArmor(String kit) {
        return ((List<ItemStack>) this.instance.getKitsFlatFile().getConfig().get(kit.toLowerCase() + ".armor")).toArray(new ItemStack[0]);
    }

    public boolean removeKit(String kit){
        kit = kit.toLowerCase();
        boolean b = getKits().contains(kit);
        this.instance.getKitsFlatFile().getConfig().set(kit, null);
        return b;
    }

    public void setALLContents(String kit, Player p) {
        this.instance.getKitsFlatFile().getConfig().set(kit.toLowerCase() + ".armor", p.getInventory().getArmorContents());
        this.instance.getKitsFlatFile().getConfig().set(kit.toLowerCase() + ".inventory", p.getInventory().getContents());
    }

    public void setKitDisplayItem(String kit, String mat) {
        this.instance.getKitsFlatFile().getConfig().set(kit.toLowerCase() + ".displayITEM", mat.toUpperCase());
    }

    private String materialName(String kit) {
        return this.instance.getKitsFlatFile().getConfig().getString(kit.toLowerCase() + ".displayITEM");
    }

    public List<String> getKits() {
        return new ArrayList<>(this.instance.getKitsFlatFile().getConfig().getKeys(false));
    }

    public void cleanKit(String kit){
        for(String s: this.instance.getKitsFlatFile().getConfig().getKeys(false)){
            if(this.instance.getKitsFlatFile().getConfig().getString(s).equals(kit)){
                this.instance.getKitsFlatFile().getConfig().set(s, null);
            }
        }
    }
}
