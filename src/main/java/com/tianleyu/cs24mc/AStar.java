package com.tianleyu.cs24mc;

import java.util.ArrayList;
import java.util.PriorityQueue;

import org.bukkit.Location;
import org.bukkit.Material;

public class AStar {
    private Location start;
    private Location end;
    private double height;

    private PriorityQueue<Node> openSet;

    public AStar() {
        openSet = new PriorityQueue<Node>((a, b) -> {
            if (a.f < b.f) {
                return -1;
            } else if (a.f > b.f) {
                return 1;
            } else {
                return 0;
            }
        });
    }

    public void setStart(Location start) {
        if (this.end == null) {
            throw new RuntimeException("End location not set, please set end location first!");
        }
        this.start = start.add(0, -1, 0).clone();
        openSet.clear();
        openSet.add(new Node(start, 0));
        start.getBlock().setType(Material.WHITE_WOOL);
    }

    public void setEnd(Location end) {
        this.end = end.add(0, -1, 0).clone();
        end.getBlock().setType(Material.RED_WOOL);
    }

    public void setHeight(Location location) {
        this.height = location.getY() - 1;
    }

    // Here it's run ASTAR by one step
    public void step() {
        Node curr = openSet.poll();
        if (curr == null)
            throw new RuntimeException("No path found: open set is empty.");
        if (equals(curr.location, end)) {
            openSet.clear();
            trackBack(curr);
        }

        curr.location.getBlock().setType(Material.LIGHT_BLUE_WOOL);

        for (Location neighbor : neighbors(curr.location)) {
            if (neighbor.getY() > height) {
                continue;
            }
            // if (neighbor.getBlock().getType() == Material.RED_WOOL) {
            // throw new RuntimeException("Path found." + neighbor.toString() + " " +
            // end.toString());
            // }
            Node neighborNode = new Node(neighbor, curr.g + 1);
            if (openSet.contains(neighborNode)) {
                continue;
            }
            neighborNode.parent = curr;
            openSet.add(neighborNode);
            neighbor.getBlock().setType(Material.WHITE_WOOL);
        }
    }

    private void trackBack(Node node) {
        while (node != null) {
            node.location.getBlock().setType(Material.BLUE_WOOL);
            node = node.parent;
        }
        throw new RuntimeException("Path found.");
    }

    private class Node {
        public Location location;
        public double g;
        public double h;
        public double f;
        public Node parent;

        public Node(Location location, double g) {
            this.location = location;
            this.g = g;
            this.h = distance(location);
            this.f = g + h;
        }

        public void updateG(double g) {
            this.g = g;
            this.f = g + h;
        }
    }

    private boolean equals(Location a, Location b) {
        return Math.abs(a.getX() - b.getX()) < 1 && Math.abs(a.getZ() - b.getZ()) < 1
                && Math.abs(a.getY() - b.getY()) < 1;
    }

    private double distance(Location base) {
        return Math.pow(base.getX() - end.getX(), 2) + Math.pow(base.getZ() - end.getZ(), 2)
                + Math.pow(base.getY() - end.getY(), 2);
    }

    private ArrayList<Location> neighbors(Location curr) {
        ArrayList<Location> neighbors = new ArrayList<Location>();
        Location east = curr.clone().add(1, 0, 0);
        Location west = curr.clone().add(-1, 0, 0);
        Location south = curr.clone().add(0, 0, 1);
        Location north = curr.clone().add(0, 0, -1);

        // 东西南北不顶头
        if ((east.getBlock().getType() == Material.STONE || east.getBlock().getType() == Material.RED_WOOL)
                && east.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
            neighbors.add(east);
        }

        if ((west.getBlock().getType() == Material.STONE || west.getBlock().getType() == Material.RED_WOOL)
                && west.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
            neighbors.add(west);
        }

        if ((south.getBlock().getType() == Material.STONE || south.getBlock().getType() == Material.RED_WOOL)
                && south.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
            neighbors.add(south);
        }

        if ((north.getBlock().getType() == Material.STONE || north.getBlock().getType() == Material.RED_WOOL)
                && north.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
            neighbors.add(north);
        }
        // 东西南北顶头一格

        if ((east.getBlock().getRelative(0, 1, 0).getType() == Material.STONE
                || east.getBlock().getRelative(0, 1, 0).getType() == Material.RED_WOOL)
                && east.getBlock().getRelative(0, 2, 0).getType() == Material.AIR

                && curr.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
            Location east2 = east.clone().add(0, 1, 0);
            neighbors.add(east2);
        }

        if ((west.getBlock().getRelative(0, 1, 0).getType() == Material.STONE
                || west.getBlock().getRelative(0, 1, 0).getType() == Material.RED_WOOL)
                && west.getBlock().getRelative(0, 2, 0).getType() == Material.AIR
                && curr.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
            Location west2 = west.clone().add(0, 1, 0);
            neighbors.add(west2);
        }

        if ((south.getBlock().getRelative(0, 1, 0).getType() == Material.STONE
                || south.getBlock().getRelative(0, 1, 0).getType() == Material.RED_WOOL)
                && south.getBlock().getRelative(0, 2, 0).getType() == Material.AIR
                && curr.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
            Location south2 = south.clone().add(0, 1, 0);
            neighbors.add(south2);
        }

        if ((north.getBlock().getRelative(0, 1, 0).getType() == Material.STONE
                || north.getBlock().getRelative(0, 1, 0).getType() == Material.RED_WOOL)
                && north.getBlock().getRelative(0, 2, 0).getType() == Material.AIR
                && curr.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
            Location north2 = north.clone().add(0, 1, 0);
            neighbors.add(north2);
        }
        // 下坠
        if (east.getBlock().getType() == Material.AIR
                && east.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
            Location eastBelow = fall(east);
            if (eastBelow != null) {
                neighbors.add(eastBelow);
            }
        }

        if (west.getBlock().getType() == Material.AIR
                && west.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
            Location westBelow = fall(west);
            if (westBelow != null) {
                neighbors.add(westBelow);
            }
        }

        if (south.getBlock().getType() == Material.AIR
                && south.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
            Location southBelow = fall(south);
            if (southBelow != null) {
                neighbors.add(southBelow);
            }
        }

        if (north.getBlock().getType() == Material.AIR
                && north.getBlock().getRelative(0, 1, 0).getType() == Material.AIR) {
            Location northBelow = fall(north);
            if (northBelow != null) {
                neighbors.add(northBelow);
            }
        }
        return neighbors;
    }

    private Location fall(Location curr) {
        Location below = curr.clone();
        while ((below.getBlock().getType() != Material.STONE && below.getBlock().getType() != Material.RED_WOOL)
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
}
