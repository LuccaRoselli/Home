package com.devlucca.homes.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import com.devlucca.homes.Main;
import com.devlucca.homes.core.Home;
import com.devlucca.homes.core.HomePlayer;
import com.devlucca.homes.utils.ItemBuilder;

public class PagedHomes {
	
	private static final List<Integer> ints = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28,
			29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);

	public static int getNextSlot(Inventory inventory) {
		int i = 0;
		while (i != 53) {
			if (ints.contains(i) && (inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR)) {
				return i;
			}
			++i;
		}
		return 0;
	}
    
    
	public static void getPagedInventory(Player p) {
		final HomePlayer hp = Main.getHomeManager().getHP(p.getName());
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for (Home c : hp.getHomes()){
			String[] lore = null;
        	if (c.isPublica()){
        		lore = new String[]{"\u00a77Use \u00a7f\u00a7lESQUERDO \u00a77para teleportar", "\u00a77Use \u00a7f\u00a7lDIREITO \u00a77para gerenciar", "\u00a77Use \u00a7f\u00a7lSHIFT + ESQUERDO \u00a77para deletar", " ", "§e* §7Esta home é pública."};
        		items.add(new ItemBuilder(c.getIcone().getType()).durability(c.getIcone().getDurability()).name("\u00a7eHome - " + c.getName()).lore(lore).glow(true).build());
        	} else {
        		lore = new String[]{"\u00a77Use \u00a7f\u00a7lESQUERDO \u00a77para teleportar", "\u00a77Use \u00a7f\u00a7lDIREITO \u00a77para gerenciar", "\u00a77Use \u00a7f\u00a7lSHIFT + ESQUERDO \u00a77para deletar"};
        		items.add(new ItemBuilder(c.getIcone().getType()).durability(c.getIcone().getDurability()).name("\u00a7eHome - " + c.getName()).lore(lore).build());
        	}
        	
		}
		//Add your items to the items list.
		if (items.isEmpty()){
			Inventory inv = Bukkit.createInventory(null, 27, "Homes de " + p.getName());
			inv.setItem(13, new ItemBuilder(Material.PAPER).name("§cVocê não possui uma home").lore("§8/sethome <nome>").build());
			p.openInventory(inv);
		} else {
			new ScrollerInventory(items, "Homes de " + p.getName(), p);
		}
    
    }
	
	public static void getHomePanel(Player p, Home h){
		Inventory inventory = Bukkit.createInventory((InventoryHolder)null, (int)36, (String)("Home: " + h.getName()));
		inventory.setItem(10, new ItemBuilder(Material.PAPER).name("§eEstado da home").lore("§8Clique no vidro abaixo", "§8para alterar o estado").build());
		inventory.setItem(16, new ItemBuilder(h.getIcone().getType()).durability(h.getIcone().getDurability()).name("§eMudar ícone").lore("§8Clique aqui para mudar", "§8o ícone de sua home").build());
		if (h.isPublica())
			inventory.setItem(19, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(5).name("§eHome pública").lore("§8Clique para alterar").build());
		else
			inventory.setItem(19, new ItemBuilder(Material.STAINED_GLASS_PANE).durability(7).name("§eHome privada").lore("§8Clique para alterar").build());
		inventory.setItem(31, new ItemBuilder(Material.BARRIER).name("§cVoltar").build());
		p.openInventory(inventory);
	}
	
//    public static void getPanel(Player p) {
//        Inventory inventory = Bukkit.createInventory((InventoryHolder)null, (int)27, (String)("Homes de " + p.getName()));
//            ArrayList<ItemStack> itens = new ArrayList<ItemStack>();
//            final HomePlayer hp = Main.getHomeManager().getHP(p.getName());
//            for (Home t : hp.getHomes()) {
//            	String[] lore = null;
//            	if (t.isPublica())
//            		lore = new String[]{"\u00a77Use bot\u00e3o \u00a7f\u00a7lESQUERDO \u00a77para teleportar", "\u00a77Use bot\u00e3o \u00a7f\u00a7lDIREITO \u00a77para gerenciar", " ", "§e* §7Esta home é pública."};
//            	else
//            		lore = new String[]{"\u00a77Use bot\u00e3o \u00a7f\u00a7lESQUERDO \u00a77para teleportar", "\u00a77Use bot\u00e3o \u00a7f\u00a7lDIREITO \u00a77para gerenciar"};
//                itens.add(new ItemBuilder(t.getIcone().getType()).durability(t.getIcone().getDurability()).name("\u00a7eHome - " + t.getName()).lore(lore).build());
//                int i = itens.size() / 7;
//                inventory = Bukkit.createInventory((InventoryHolder)null, (int)(i < 0 ? 27 : (9 * (3 + i) + 9 > 54 ? 54 : 9 * (3 + i) + 9)), (String)("Homes de: " + p.getName()));
//                for (ItemStack is : itens) {
//                    if (!(inventory.getSize() == 54 ? inventory.getItem(34) == null : inventory.getItem(0) == null)) continue;
//                    inventory.setItem(getNextSlot(inventory), is);
//                }
//                if (hp.getHomes().isEmpty()) continue;
//                inventory.setItem(9 * (3 + i) + 4, new ItemBuilder(Material.ARROW).name("§cFechar menu").build());
//            }
//            if (hp.getHomes().isEmpty())
//            	inventory.setItem(13, new ItemBuilder(Material.PAPER).name("\u00a7cVocê não possui homes").build());
//        p.openInventory(inventory);
//    }


}
