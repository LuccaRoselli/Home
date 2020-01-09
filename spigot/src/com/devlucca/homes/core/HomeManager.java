package com.devlucca.homes.core;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.devlucca.homes.Main;

public class HomeManager
{
    private HashMap<String, HomePlayer> cache;
    private HashMap<String, Home> publics;
    
    public HomeManager() {
        this.cache = new HashMap<String, HomePlayer>();
        this.publics = new HashMap<String, Home>();
    }
    
    public HomePlayer getHP(final String player) {
        if (this.cache.containsKey(player)) {
            return this.cache.get(player);
        }
        return null;
    }
    
    @SuppressWarnings("deprecation")
	public ItemStack fromString(String s){
    	return new ItemStack(Integer.parseInt(s.split("-")[0]), 1, (short)Short.valueOf(s.split("-")[1]));
    }
    
    public void loadPublicHomes() {
        new BukkitRunnable() {
            public void run() {
                try {
                    final Connection c = Main.getStorage().getConnection();
                    final Statement stmt = c.createStatement();
                    final ResultSet rs = stmt.executeQuery("SELECT * FROM ganckhomes WHERE publica='true'");
                    while (rs.next()) {
                        final String player = rs.getString("player");
                        final String name = rs.getString("home");
                        final double x = rs.getDouble("x");
                        final double y = rs.getDouble("y");
                        final double z = rs.getDouble("z");
                        final double yaw = rs.getDouble("yaw");
                        final double pitch = rs.getDouble("pitch");
                        final String world = rs.getString("world");
                        final Home h = new Home(name, x, y, z, yaw, pitch, world, fromString(rs.getString("icone")));
                        h.setPublica(Boolean.parseBoolean(rs.getString("publica")));
                        HomeManager.this.getPublics().put((String.valueOf(player) + ":" + name).toLowerCase(), h);
                    }
                    stmt.close();
                    rs.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously((Plugin)Main.getInstance());
    }
    
    public void loadData(final String player) {
        new BukkitRunnable() {
            public void run() {
                try {
                    HomePlayer hp = null;
                    if (HomeManager.this.cache.containsKey(player)) {
                        hp = HomeManager.this.cache.get(player);
                    }
                    else {
                        hp = new HomePlayer(player);
                    }
                    hp.getHomes().clear();
                    final Connection c = Main.getStorage().getConnection();
                    final Statement stmt = c.createStatement();
                    final ResultSet rs = stmt.executeQuery("SELECT * FROM ganckhomes WHERE player='" + player + "'");
                    while (rs.next()) {
                        final String name = rs.getString("home");
                        final double x = rs.getDouble("x");
                        final double y = rs.getDouble("y");
                        final double z = rs.getDouble("z");
                        final double yaw = rs.getDouble("yaw");
                        final double pitch = rs.getDouble("pitch");
                        final String world = rs.getString("world");
                        final Home h = new Home(name, x, y, z, yaw, pitch, world, fromString(rs.getString("icone")));
                        h.setPublica(Boolean.parseBoolean(rs.getString("publica")));
                        hp.getHomes().add(h);
                    }
                    HomeManager.this.cache.put(player, hp);
                    stmt.close();
                    rs.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously((Plugin)Main.getInstance());
    }
    
    public HashMap<String, HomePlayer> getCache() {
        return this.cache;
    }
    
    public HashMap<String, Home> getPublics() {
        return this.publics;
    }
    
    public void setPublics(final HashMap<String, Home> publics) {
        this.publics = publics;
    }
}
