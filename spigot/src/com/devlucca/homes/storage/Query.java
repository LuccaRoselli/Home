package com.devlucca.homes.storage;

import java.sql.SQLException;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.devlucca.homes.Main;

public class Query
{
    private HashSet<String> update;
    private HashSet<String> normal;
    private boolean async;
    
    public Query() {
        this.update = new HashSet<String>();
        this.normal = new HashSet<String>();
        this.async = false;
    }
    
    public void addUpdate(final String querycommand) {
        this.update.add(querycommand);
    }
    
    public void add(final String querycommand) {
        this.normal.add(querycommand);
    }
    
    public void setAsync(final boolean arg) {
        this.async = arg;
    }
    
    public boolean isAsync() {
        return this.async;
    }
    
    public HashSet<String> getUpdates() {
        return this.update;
    }
    
    public HashSet<String> getNormal() {
        return this.normal;
    }
    
    public static void create(){
        final Query c = new Query();
        c.add("CREATE TABLE IF NOT EXISTS ganckhomes (player varchar(16), home TEXT, x DOUBLE, y DOUBLE, z DOUBLE, yaw DOUBLE, pitch DOUBLE, world TEXT, publica TEXT, icone TEXT)");
        c.setAsync(false);
        try {
            Main.getStorage().execute(c);
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("yHomes - Nao foi possivel gerar a tabela de armazenamento.");
            Bukkit.getPluginManager().disablePlugin((Plugin)Main.getInstance());
        }
    }
}
