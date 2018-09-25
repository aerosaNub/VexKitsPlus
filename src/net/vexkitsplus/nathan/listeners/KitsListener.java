package net.vexkitsplus.nathan.listeners;

import net.vexkitsplus.nathan.events.KitsUpdateEvent;
import net.vexkitsplus.nathan.manager.KitsManager;
import net.vexkitsplus.nathan.utils.C;
import net.vexkitsplus.nathan.utils.CooldownManager;
import net.vexkitsplus.nathan.utils.Time;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KitsListener implements Listener

{


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){

        if (e.getInventory() == null) return;
        if (e.getInventory().getName() == null) return;

        if(e.getWhoClicked() instanceof Player){
            Player player = (Player) e.getWhoClicked();
            if(e.getInventory().getName().equals(KitsManager.INVENTORY_NAME)) {
                int slot = e.getSlot();
                if (slot < KitsManager.init().getKits().size()) {
                    String kit = KitsManager.init().getKits().get(slot);

                    if (player.hasPermission(KitsManager.KITPERMS + kit)) {


                        if (CooldownManager.hasCooldown(player.getUniqueId(), kit.toLowerCase() + "_cooldown")) {
                            if (CooldownManager.getCooldown(player.getUniqueId(), kit.toLowerCase() + "_cooldown") > 0) {
                                long time = CooldownManager.getCooldown(player.getUniqueId(), kit.toLowerCase() + "_cooldown");
                                player.sendMessage(ChatColor.RED + "Cooldown left for " + kit + " is " + Time.format(time, Time.FormatType.LONG_TIME));
                                e.setCancelled(true);
                                return;
                            }
                        }


                        KitsManager.init().loadKit(kit, player);

                        checkInventory(player, kit);

                        player.sendMessage(ChatColor.YELLOW + "You selected " +  C.c(KitsManager.init().getKitDisplayName(kit)) + " kit!");


                        CooldownManager.setCooldown(player.getUniqueId(), kit.toLowerCase() + "_cooldown", System.currentTimeMillis() + KitsManager.init().getCooldownFromKit(kit) * 1000);

                        Inventory inventory = KitsManager.init().createInventory(player);
                        Inventory existinginventory = e.getInventory();
                        if(inventory.getSize() != existinginventory.getSize()){
                            player.openInventory(inventory);
                        }
                        else{
                            existinginventory.setContents(inventory.getContents());
                        }
                    }
                    else{
                        player.sendMessage(ChatColor.RED + "You do not have permission to use " + C.c(KitsManager.init().getKitDisplayName(kit)));
                    }
                }
                e.setCancelled(true);
            }
            else if(e.getView().getTopInventory() != null && e.getView().getTopInventory().getName().equals(KitsManager.INVENTORY_NAME)){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onKitsApply(KitsUpdateEvent e){

        if (e.getType() == KitsUpdateEvent.Type.DELETE) {
            KitsManager.init().cleanKit(e.getKitName());
        }

        for(Player p: Bukkit.getOnlinePlayers()){
            if(p.getOpenInventory() != null && p.getOpenInventory().getTopInventory() != null && p.getOpenInventory().getTopInventory().getName().equals(KitsManager.INVENTORY_NAME)){
                Inventory inventory = KitsManager.init().createInventory(p);
                Inventory existinginventory = p.getOpenInventory().getTopInventory();
                if(inventory.getSize() != existinginventory.getSize()){
                    p.openInventory(inventory);
                }
                else{
                    existinginventory.setContents(inventory.getContents());
                }
            }
        }
    }

    private void checkInventory(Player p, String kit) {
        Inventory in = p.getInventory();

        ItemStack[] armor = KitsManager.init().getArmor(kit.toLowerCase());
        ItemStack[] items = KitsManager.init().getContents(kit.toLowerCase());

        for (int i = 0; i<in.getContents().length; i++) {

            if (in.firstEmpty() != -1) {
                in.addItem(items);

                if (in.firstEmpty() == -1) {
                    ItemStack[] items2 = items;

                    for (ItemStack itemss : items2) {
                        p.getWorld().dropItemNaturally(p.getLocation(), itemss);
                    }
                }
            }
        }
    }

}
