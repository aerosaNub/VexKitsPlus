package net.vexkitsplus.nathan.commands;

import net.vexkitsplus.nathan.KitsPlus;
import net.vexkitsplus.nathan.events.KitsUpdateEvent;
import net.vexkitsplus.nathan.manager.KitsManager;
import net.vexkitsplus.nathan.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class KitsCommand implements CommandExecutor {

    private final KitsPlus plugin;

    public KitsCommand(KitsPlus plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            p.openInventory(KitsManager.init().createInventory(p));
            //sender.sendMessage(ChatColor.YELLOW + "Opening kits inventory...");
            if (sender.hasPermission("command.kits.admin")) {
                p.sendMessage(ChatColor.RED + "/kits create <name> <displayItem> <displayName>");
                p.sendMessage(ChatColor.RED + "/kits setdescription <name> <description>");
                sender.sendMessage(ChatColor.RED + "/kits addcooldown <kit> <seconds>");
                p.sendMessage(ChatColor.RED + "/kits setdisplay <name> <displayItem>");
                p.sendMessage(ChatColor.RED + "/kits remove <name>");
                return true;
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (args.length < 3)
            {
                sender.sendMessage(ChatColor.RED + "Usage: /kits create <name> <displayItem> <displayName>");
                return true;
            }
            if(KitsManager.init().getKits().contains(args[1].toLowerCase())){
                sender.sendMessage(ChatColor.RED + "A kit with that name already exists");
                return true;
            }
            KitsManager.init().setKitDisplayItem(args[1], args[2]);
            KitsManager.init().setKitDisplayName(args[1], args[3]);
            KitsManager.init().setKitDescription(args[1], new ArrayList<>());
            KitsManager.init().setALLContents(args[1], p);
            sender.sendMessage(ChatColor.YELLOW + "The kit " + args[0] + " has been created successfully!");
            sender.sendMessage(ChatColor.YELLOW + "Give players the permission: " + ChatColor.RED + ChatColor.UNDERLINE.toString() + KitsManager.KITPERMS + args[1].toLowerCase());
            Bukkit.getPluginManager().callEvent(new KitsUpdateEvent(KitsUpdateEvent.Type.CREATE, args[1].toLowerCase()));

           // KitsPlus.getInstance().getKitsFlatFile().saveKitsData();

            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (args.length < 2)
            {
                sender.sendMessage(ChatColor.RED + "Usage: /kits remove <name>");
                return true;
            }
            boolean b = KitsManager.init().removeKit(args[1]);
            String message = b ? ChatColor.YELLOW + "Kit " + ChatColor.RED + args[1].toLowerCase() + ChatColor.YELLOW + " deleted successfully" : ChatColor.RED + "Kit does not exist";
            sender.sendMessage(message);
            Bukkit.getPluginManager().callEvent(new KitsUpdateEvent(KitsUpdateEvent.Type.DELETE, args[1].toLowerCase()));

           // KitsPlus.getInstance().getKitsFlatFile().saveKitsData();

            return true;

        }

        if (args[0].equalsIgnoreCase("addcooldown")) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "/kits addcooldown <kit> <seconds>");
                return true;
            }

            if (!KitsManager.init().getKits().contains(args[1].toLowerCase())) {
                sender.sendMessage(ChatColor.RED + "Kit doesn't exist!");
                return true;
            }

            long seconds = Long.parseLong(args[2]);

            if (seconds < 0) {
                sender.sendMessage("Bad time!");
                return true;
            }


            KitsManager.init().setCooldown(args[1].toLowerCase(), seconds);

            sender.sendMessage(ChatColor.GREEN + "Time added to the kit " + seconds);
            return true;


        }

        if (args[0].equalsIgnoreCase("setdescription")) {
            if(args.length < 3){
                sender.sendMessage(ChatColor.RED + "Usage: /kits setdescription <name> <description>");
            }
            else{
                if(KitsManager.init().getKits().contains(args[1].toLowerCase())){
                    String s = "";
                    for(int i = 2; i < args.length; i++){
                        s += args[i] + " ";
                    }
                    List<String> description = new ArrayList<>(Arrays.asList(s.split("<n>")));
                    KitsManager.init().setKitDescription(args[1], description);
                    sender.sendMessage(ChatColor.YELLOW + "Set the description name of the kit " + ChatColor.RED + args[1].toLowerCase() + ChatColor.YELLOW + " to: ");

                    for(String descriptionline: KitsManager.init().getKitDescription(args[1])){
                        sender.sendMessage(C.c(descriptionline));
                    }
                    Bukkit.getPluginManager().callEvent(new KitsUpdateEvent(KitsUpdateEvent.Type.EDIT, args[1].toLowerCase()));

                  //  KitsPlus.getInstance().getKitsFlatFile().saveKitsData();
                }
                else{
                    sender.sendMessage(ChatColor.YELLOW + "A kit with the name " + ChatColor.RED + args[1].toLowerCase() + ChatColor.YELLOW + " does not exist");
                }
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("setdisplay")) {
            if(args.length < 3){
                sender.sendMessage(ChatColor.RED + "Usage: /kits setdisplay <name> <displayItem>");
            }
            else{
                if(KitsManager.init().getKits().contains(args[1].toLowerCase())){
                    String s = "";

                    KitsManager.init().setKitDisplayItem(args[1], s);
                    sender.sendMessage(ChatColor.YELLOW + "Set the display name of the kit " + ChatColor.RED + args[1].toLowerCase() + ChatColor.YELLOW + " to " + s.toUpperCase() + "!");

                    Bukkit.getPluginManager().callEvent(new KitsUpdateEvent(KitsUpdateEvent.Type.EDIT, args[1].toLowerCase()));

                   // KitsPlus.getInstance().getKitsFlatFile().saveKitsData();
                }
                else{
                    sender.sendMessage(ChatColor.YELLOW + "A kit with the name " + ChatColor.RED + args[1].toLowerCase() + ChatColor.YELLOW + " does not exist");
                }
            }
            return true;
        }

        return false;
    }
}
