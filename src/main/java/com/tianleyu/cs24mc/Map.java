package com.tianleyu.cs24mc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

public class Map {
    private List<String> lines;
    private ArrayList<ArrayList<ArrayList<Integer>>> dim;
    private int sizeX, sizeY, sizeZ;

    public Map(String file) {
        String cwd = System.getProperty("user.dir");
        try {
            lines = Utils.RF(cwd + "/plugins/data/" + file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.dim = new ArrayList<ArrayList<ArrayList<Integer>>>();
    }

    public String parse() {
        if (lines == null) {
            return "Input file not found";
        }
        String dimLine = lines.get(0);
        String[] dims = dimLine.trim().split("\\s+");
        if (dims.length != 3) {
            return "Invalid dimension";
        }
        sizeX = Integer.valueOf(dims[0]);
        sizeY = Integer.valueOf(dims[1]);
        sizeZ = Integer.valueOf(dims[2]);
        int lnPtr = 2;
        int clPtr = 0;
        for (int z = 0; z < sizeZ; z++) {
            ArrayList<ArrayList<Integer>> layer = new ArrayList<ArrayList<Integer>>();
            for (int y = 0; y < sizeY; y++) {
                ArrayList<Integer> line = new ArrayList<Integer>();
                for (int x = 0; x < sizeX; x++) {
                    if (lnPtr >= lines.size()) {
                        return "Failed to parse: Line string shorter than expected in line " + lnPtr;
                    }
                    char c = lines.get(lnPtr).charAt(clPtr);
                    int t = Utils.C2I(c);
                    line.add(((t & 0b1000) != 0) ? 1 : 0);
                    x++;
                    line.add(((t & 0b0100) != 0) ? 1 : 0);
                    x++;
                    line.add(((t & 0b0010) != 0) ? 1 : 0);
                    x++;
                    line.add(((t & 0b0001) != 0) ? 1 : 0);
                    clPtr++;
                }
                layer.add(line);
                lnPtr++;
                clPtr = 0;
            }
            dim.add(layer);
            if (lnPtr < lines.size() && lines.get(lnPtr).length() != 0) {
                return "Failed to parse: Expecting an empty string as the end of the layer, but got \""
                        + lines.get(lnPtr) + "\", at layer"
                        + dim.size();
            }
            lnPtr++;
        }
        return "OK";
    }

    public ArrayList<Location> toLocations(World world) {
        ArrayList<Location> output = new ArrayList<Location>();
        for (int z = 0; z < sizeZ; z++) {
            for (int y = 0; y < sizeY; y++) {
                for (int x = 0; x < sizeX; x++) {
                    if (dim.get(z).get(y).get(x) == 1)
                        output.add(new Location(world, x, z, y));
                }
            }
        }
        return output;
    }
}
