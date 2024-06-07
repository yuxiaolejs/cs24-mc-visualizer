package com.tianleyu.cs24mc;

import java.util.ArrayList;
import java.util.HashMap;

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
        if (cmd.getName().equalsIgnoreCase("astar"))
            return astarCommand(sender, cmd, label, args);
        if (args.length < 1) {
            sender.sendMessage("Wrong usage, use /cs24 <your-file>");
            return true;
        }
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location base = player.getLocation();
            World curr = base.getWorld();

            curr.getUID().toString();

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

            if (args[0].equals("coord")) {
                player.sendMessage(msgPrefix
                        + "Finding co0rd.");
                player.sendMessage(msgPrefix
                        + Map.findCoord(base));
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
            m1.putSigns(player.getLocation());
            player.sendMessage(msgPrefix + "Map creation complete.");
            return true;
        } else {
            sender.sendMessage(msgPrefix + "This command can only be used by a player.");
            return false;
        }
    }

    HashMap<Player, AStar> astarMap = new HashMap<Player, AStar>();

    private boolean astarCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return false;
        }
        Player player = (Player) sender;
        if (args.length < 1) {
            sender.sendMessage("Usage: /astar <action>, where action can be: ");
            sender.sendMessage("  reset: reset the AStar instance for you.");
            sender.sendMessage("  start: set the start point for AStar.");
            sender.sendMessage("  end: set the end point for AStar.");
            sender.sendMessage("  run: run the AStar algorithm.");
            sender.sendMessage("  step: run the AStar algorithm step by step.");
            return true;
        }
        // Make sure instance is created for the player
        if (astarMap.get(player) == null) {
            astarMap.put(player, new AStar());
            sender.sendMessage("Creating new AStar instance for you.");
        }

        // Reset the instance
        if (args[0].equals("reset")) {
            astarMap.put(player, new AStar());
            sender.sendMessage("Resetting AStar instance for you.");
            return true;
        }

        AStar astar = astarMap.get(player);

        // Set the start point
        if (args[0].equals("start")) {
            try {
                astar.setStart(player.getLocation());
            } catch (Exception e) {
                sender.sendMessage(e.getMessage());
                return true;
            }
            sender.sendMessage("Start point set to your current location.");
            return true;
        }

        // Set the end point
        if (args[0].equals("end")) {
            astar.setEnd(player.getLocation());
            sender.sendMessage("End point set to your current location.");
            return true;
        }

        if (args[0].equals("height")) {
            try {
                astar.setHeight(player.getLocation());
            } catch (Exception e) {
                sender.sendMessage(e.getMessage());
                return true;
            }
            return true;
        }

        // Run one step
        if (args[0].equals("step")) {
            int step = 1;
            if (args.length > 1) {
                try {
                    step = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage("Invalid step number.");
                    return true;
                }
            }
            for (int i = 0; i < step; i++) {
                try {
                    astar.step();
                } catch (Exception e) {
                    sender.sendMessage(e.getMessage());
                    return true;
                }
            }
            sender.sendMessage("AStar algorithm ran " + step + " step(s).");
            return true;
        }

        sender.sendMessage("Usage: /astar <action>, where action can be: ");
        sender.sendMessage("  reset: reset the AStar instance for you.");
        sender.sendMessage("  start: set the start point for AStar.");
        sender.sendMessage("  end: set the end point for AStar.");
        sender.sendMessage("  run: run the AStar algorithm.");
        sender.sendMessage("  height: set the highest point the algorithm can stand on.");
        sender.sendMessage("  step: run the AStar algorithm step by step.");
        return true;
    }
}
