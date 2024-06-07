package com.tianleyu.cs24mc;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;

public class Route {
    private Location start;

    public Route(Location base) {
        this.start = base.subtract(0, 1, 0);
    }

    private Location fall(Location curr) {
        Location below = curr.clone();
        while (below.getBlock().getType() != Material.STONE
                && below.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
            below = below.add(0, -1, 0);
            if (below.getBlockY() < -63) {
                return null;
            }
        }
        if (below.getBlock().getRelative(0, 1, 0).getType() != Material.AIR) {
            return null;
        }
        return below;
    }

    public String explore() {
        Material startType = start.getBlock().getType();
        if (startType != Material.STONE) {
            return "You need to start on a stone block!";
        }

        Queue<Location> queue = new LinkedList<Location>();
        HashMap<Location, Location> prev = new HashMap<Location, Location>();

        start.getBlock().setType(Material.DIAMOND_BLOCK);
        queue.add(start);

        Location desLocation = null;

        while (!queue.isEmpty()) {
            Location curr = queue.poll();
            Location east = curr.clone().add(1, 0, 0);
            Location west = curr.clone().add(-1, 0, 0);
            Location south = curr.clone().add(0, 0, 1);
            Location north = curr.clone().add(0, 0, -1);

            // 当前位置为终点
            if (curr.getBlock().getRelative(0, 1, 0).getType() == Material.REDSTONE_TORCH) {
                desLocation = curr;
                curr.getBlock().setType(Material.GOLD_BLOCK);
                break;
            }

            // 东西南北不顶头
            if (east.getBlock().getType() == Material.STONE
                    && (east.getBlock().getRelative(0, 1, 0).getType() == Material.AIR ||
                            east.getBlock().getRelative(0, 1, 0).getType() == Material.REDSTONE_TORCH)) {
                east.getBlock().setType(Material.LIGHT_BLUE_WOOL);
                queue.add(east);
                prev.put(east, curr);
            }

            if (west.getBlock().getType() == Material.STONE
                    && (west.getBlock().getRelative(0, 1, 0).getType() == Material.AIR ||
                            west.getBlock().getRelative(0, 1, 0).getType() == Material.REDSTONE_TORCH)) {
                west.getBlock().setType(Material.LIGHT_BLUE_WOOL);
                queue.add(west);
                prev.put(west, curr);
            }

            if (south.getBlock().getType() == Material.STONE
                    && (south.getBlock().getRelative(0, 1, 0).getType() == Material.AIR ||
                            south.getBlock().getRelative(0, 1, 0).getType() == Material.REDSTONE_TORCH)) {
                south.getBlock().setType(Material.LIGHT_BLUE_WOOL);
                queue.add(south);
                prev.put(south, curr);
            }

            if (north.getBlock().getType() == Material.STONE
                    && (north.getBlock().getRelative(0, 1, 0).getType() == Material.AIR ||
                            north.getBlock().getRelative(0, 1, 0).getType() == Material.REDSTONE_TORCH)) {
                north.getBlock().setType(Material.LIGHT_BLUE_WOOL);
                queue.add(north);
                prev.put(north, curr);
            }
            // 东西南北顶头一格

            if (east.getBlock().getRelative(0, 1, 0).getType() == Material.STONE
                    && (east.getBlock().getRelative(0, 2, 0).getType() == Material.AIR ||
                            east.getBlock().getRelative(0, 2, 0).getType() == Material.REDSTONE_TORCH)
                    && curr.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
                Location east2 = east.clone().add(0, 1, 0);
                east2.getBlock().setType(Material.BLUE_WOOL);
                queue.add(east2);
                prev.put(east2, curr);
            }

            if (west.getBlock().getRelative(0, 1, 0).getType() == Material.STONE
                    && (west.getBlock().getRelative(0, 2, 0).getType() == Material.AIR ||
                            west.getBlock().getRelative(0, 2, 0).getType() == Material.REDSTONE_TORCH)
                    && curr.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
                Location west2 = west.clone().add(0, 1, 0);
                west2.getBlock().setType(Material.BLUE_WOOL);
                queue.add(west2);
                prev.put(west2, curr);
            }

            if (south.getBlock().getRelative(0, 1, 0).getType() == Material.STONE
                    && (south.getBlock().getRelative(0, 2, 0).getType() == Material.AIR ||
                            south.getBlock().getRelative(0, 2, 0).getType() == Material.REDSTONE_TORCH)
                    && curr.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
                Location south2 = south.clone().add(0, 1, 0);
                south2.getBlock().setType(Material.BLUE_WOOL);
                queue.add(south2);
                prev.put(south2, curr);
            }

            if (north.getBlock().getRelative(0, 1, 0).getType() == Material.STONE
                    && (north.getBlock().getRelative(0, 2, 0).getType() == Material.AIR ||
                            north.getBlock().getRelative(0, 2, 0).getType() == Material.REDSTONE_TORCH)
                    && curr.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
                Location north2 = north.clone().add(0, 1, 0);
                north2.getBlock().setType(Material.BLUE_WOOL);
                queue.add(north2);
                prev.put(north2, curr);
            }
            // 下坠
            if (east.getBlock().getType() == Material.AIR
                    && east.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
                Location eastBelow = fall(east);
                if (eastBelow != null) {
                    if (eastBelow.getBlock().getType() == Material.REDSTONE_TORCH) {
                        desLocation = eastBelow;
                        break;
                    }
                    eastBelow.getBlock().setType(Material.RED_WOOL);
                    queue.add(eastBelow);
                    prev.put(eastBelow, curr);
                }
            }

            if (west.getBlock().getType() == Material.AIR
                    && west.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
                Location westBelow = fall(west);
                if (westBelow != null) {
                    if (westBelow.getBlock().getType() == Material.REDSTONE_TORCH) {
                        desLocation = westBelow;
                        break;
                    }
                    westBelow.getBlock().setType(Material.RED_WOOL);
                    queue.add(westBelow);
                    prev.put(westBelow, curr);
                }
            }

            if (south.getBlock().getType() == Material.AIR
                    && south.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
                Location southBelow = fall(south);
                if (southBelow != null) {
                    if (southBelow.getBlock().getType() == Material.REDSTONE_TORCH) {
                        desLocation = southBelow;
                        break;
                    }
                    southBelow.getBlock().setType(Material.RED_WOOL);
                    queue.add(southBelow);
                    prev.put(southBelow, curr);
                }
            }

            if (north.getBlock().getType() == Material.AIR
                    && north.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
                Location northBelow = fall(north);
                if (northBelow != null) {
                    if (northBelow.getBlock().getType() == Material.REDSTONE_TORCH) {
                        desLocation = northBelow;
                        break;
                    }
                    northBelow.getBlock().setType(Material.RED_WOOL);
                    queue.add(northBelow);
                    prev.put(northBelow, curr);
                }
            }

            // Chunk chunk = curr.getChunk();
            // curr.getWorld().refreshChunk(chunk.getX(), chunk.getZ());
            // try {
            //     Thread.sleep(100);
            // } catch (InterruptedException e) {
            //     e.printStackTrace();
            // }
        }

        // Backtrack

        if (desLocation != null) {
            Location curr = desLocation;
            while (prev.containsKey(curr)) {
                curr = prev.get(curr);
                curr.getBlock().setType(Material.GOLD_BLOCK);
            }
            return "Route found";
        }

        return "Route not found";
    }
}