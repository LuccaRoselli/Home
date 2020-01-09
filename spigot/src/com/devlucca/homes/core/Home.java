package com.devlucca.homes.core;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class Home
{
    private Location loc;
    private boolean invalid;
    private String name;
    private boolean publica;
    private ItemStack icone;
    
    public Home(final String name, final double x, final double y, final double z, final double yaw, final double pitch, final String world, ItemStack icone) {
        this.invalid = false;
        this.name = "";
        this.publica = false;
        this.name = name;
        if (Bukkit.getWorld(world) == null) {
            this.invalid = true;
        }
        else {
            final Location l = new Location(Bukkit.getWorld(world), x, y, z, (float)(short)yaw, (float)(short)pitch);
            this.loc = l;
        }
        this.icone = icone;
    }
    
    public ItemStack getIcone(){
    	return this.icone;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Location getLoc() {
        return this.loc;
    }
    
    public boolean isInvalid() {
        return this.invalid;
    }
    
    public boolean isPublica() {
        return this.publica;
    }
    
	@SuppressWarnings("deprecation")
	public void setIcone(final int id, final int data) {
        this.icone = new ItemStack(id, 1, (short)data);
    }
    
    public void setPublica(final boolean publica) {
        this.publica = publica;
    }
}
