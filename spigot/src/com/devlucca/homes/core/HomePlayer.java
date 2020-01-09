package com.devlucca.homes.core;

import java.util.*;
import org.bukkit.scheduler.*;

import com.devlucca.homes.*;

import java.sql.*;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.*;

public class HomePlayer
{
    private String player;
    private HashSet<Home> homes;
    
    public HomePlayer(final String player) {
        this.homes = new HashSet<Home>();
        this.setPlayer(player);
    }
    
    public HashSet<Home> getHomes() {
        return this.homes;
    }
    
    public String getPlayer() {
        return this.player;
    }
    
    public void setPlayer(final String player) {
        this.player = player;
    }
    
    @SuppressWarnings("deprecation")
	public static String fromItemStack(ItemStack s){
    	return s.getTypeId() + "-" + s.getDurability();
    }
    
    public void icone(final Home h, final String icone){
        new BukkitRunnable() {
            public void run() {
                try {
                    final Connection c = Main.getStorage().getConnection();
                    final Statement stmt = c.createStatement();
                    stmt.executeUpdate("UPDATE ganckhomes SET icone='" + icone + "' WHERE player='" + HomePlayer.this.player + "' AND home='" + h.getName() + "'");
                    stmt.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously((Plugin)Main.getInstance());
    }
    
    public void insertHome(final Home h) {
        new BukkitRunnable() {
            public void run() {
                try {
                    final Connection c = Main.getStorage().getConnection();
                    final Statement stmt = c.createStatement();
                    stmt.execute("INSERT INTO ganckhomes (player, home, x, y, z, yaw, pitch, world, publica, icone) VALUES ('" + HomePlayer.this.player + "','" + h.getName() + "','" + h.getLoc().getX() + "','" + h.getLoc().getY() + "','" + h.getLoc().getZ() + "','" + h.getLoc().getYaw() + "','" + h.getLoc().getPitch() + "','" + h.getLoc().getWorld().getName() + "','" + String.valueOf(h.isPublica()) + "','" + fromItemStack(h.getIcone()) + "')");
                    stmt.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously((Plugin)Main.getInstance());
    }
    
    public void publica(final Home h, final boolean b) {
        new BukkitRunnable() {
            public void run() {
                try {
                    final Connection c = Main.getStorage().getConnection();
                    final Statement stmt = c.createStatement();
                    stmt.executeUpdate("UPDATE ganckhomes SET publica='" + b + "' WHERE player='" + HomePlayer.this.player + "' AND home='" + h.getName() + "'");
                    stmt.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously((Plugin)Main.getInstance());
    }
    
    public void deleteHome(final Home h) {
        new BukkitRunnable() {
            public void run() {
                try {
                    final Connection c = Main.getStorage().getConnection();
                    final Statement stmt = c.createStatement();
                    stmt.execute("DELETE FROM ganckhomes WHERE player='" + HomePlayer.this.player + "' AND home='" + h.getName() + "'");
                    stmt.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously((Plugin)Main.getInstance());
    }
}
