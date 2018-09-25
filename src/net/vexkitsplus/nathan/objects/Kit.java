package net.vexkitsplus.nathan.objects;

import org.bukkit.inventory.ItemStack;

public class Kit {

    private ItemStack[] contents;
    private ItemStack[] armorContents;
    private String name;
    private String description;

    public Kit(String name, String description, ItemStack[] contents, ItemStack[] armorContents) {
        this.name = name;
        this.description = description;
        this.armorContents = armorContents;
        this.contents = contents;
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public ItemStack[] getArmorContents() {
        return armorContents;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
