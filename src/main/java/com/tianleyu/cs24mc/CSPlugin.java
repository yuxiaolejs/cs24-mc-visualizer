package com.tianleyu.cs24mc;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CSPlugin extends JavaPlugin {

    public final String msgPrefix = "[CS24] ";
    public final String header = "" + //
            "  ___  ___  ___   __  \n" + //
            " / __)/ __)(__ \\ /. | \n" + //
            "( (__ \\__ \\ / _/(_  _)    by Tianle Yu\n" + //
            " \\___)(___/(____) (_)        me@tianleyu.com\n" + //
            "  Just for the vox lab!\n  Learn more / import your map at\n" + ChatColor.BLUE
            + "    https://vox.proxied.tianleyu.com"
            + ChatColor.WHITE;

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
        // for (int i = 0; i < args.length; i++) {
        // getLogger().info("WITH ARG: " + args[i]);
        // }
        if (args.length < 1) {
            sender.sendMessage("Wrong usage, use /cs24 <your-file>");
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location base = player.getLocation();
            World curr = base.getWorld();

            player.sendMessage(header);

            getLogger().info(player.getName() + " used command /cs24 " + args[0]);

            if (args[0].equals("creative")) {
                player.setGameMode(GameMode.CREATIVE);
                player.sendMessage(msgPrefix
                        + "You have been set to creative mode, but please respect other people's work. We have core protect enabled.");
                return true;
            }

            if (args[0].equals("route")) {
                player.sendMessage(msgPrefix
                        + "Finding Route.");
                Route r1 = new Route(base);
                player.sendMessage(msgPrefix
                        + r1.explore());
                return true;
            }

            Map m1 = new Map(args[0]);
            String res = m1.parse();
            if (!res.equals("OK")) {
                player.sendMessage(msgPrefix + "Failed to parse: ");
                player.sendMessage(msgPrefix + res);
                System.out.println(res);
                return true;
            }
            ArrayList<Location> blocks = m1.toLocations(curr);

            for (int i = 0; i < blocks.size(); i++) {
                Location l = blocks.get(i);
                l.add(base);
                l.add(1, 0, 1);
                l.getBlock().setType(Material.STONE);
                // base.getBlock().setType(Material.STONE); // Change to desired block type
                // base.add(0, 0, 1);
            }
            player.sendMessage(msgPrefix + "Map creation complete.");
            return true;
        } else {
            sender.sendMessage(msgPrefix + "This command can only be used by a player.");
            return false;
        }
    }
}
