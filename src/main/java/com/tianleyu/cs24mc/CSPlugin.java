package com.tianleyu.cs24mc;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CSPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("onEnable is called!");
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        getLogger().info(sender.toString());
        getLogger().info(cmd.toString());
        for (int i = 0; i < args.length; i++) {
            getLogger().info("WITH ARG: " + args[i]);
        }
        if (args.length < 1) {
            sender.sendMessage("Wrong usage, use /cs24 <your-file>");
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location base = player.getLocation();
            World curr = base.getWorld();

            if (args[0] == "creative") {
                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage("You have been set to creative mode, but please respect other people's work. We have core protect enabled.");
                return true;
            }

            Map m1 = new Map(args[0]);
            String res = m1.parse();
            if (res != "OK") {
                player.sendMessage("Failed to parse: ");
                player.sendMessage(res);
                System.out.println(res);
                return true;
            }
            ArrayList<Location> blocks = m1.toLocations(curr);

            for (int i = 0; i < blocks.size(); i++) {
                Location l = blocks.get(i);
                l.add(base);
                l.getBlock().setType(Material.STONE);
                // base.getBlock().setType(Material.STONE); // Change to desired block type
                // base.add(0, 0, 1);
            }
            player.sendMessage("Map creation complete.");
            return true;
        } else {
            sender.sendMessage("This command can only be used by a player.");
            return false;
        }
    }
}
