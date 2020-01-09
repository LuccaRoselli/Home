package com.devlucca.homes.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.devlucca.homes.utils.ItemBuilder;

public class ScrollerInventory {

	public ArrayList<Inventory> pages = new ArrayList<Inventory>();
	public UUID id;
	public int currpage = 0;
	public static HashMap<UUID, ScrollerInventory> users = new HashMap<UUID, ScrollerInventory>();

	// Running this will open a paged inventory for the specified player, with
	// the items in the arraylist specified.
	public ScrollerInventory(ArrayList<ItemStack> items, String name, Player p) {
		this.id = UUID.randomUUID();
		// create new blank page
		Inventory page = getBlankPage(name, items);
		// According to the items in the arraylist, add items to the
		// ScrollerInventory
		for (int i = 0; i < items.size(); i++) {
			// If the current page is full, add the page to the inventory's
			// pages arraylist, and create a new page to add the items.
			if (page.firstEmpty() == 44) {
				pages.add(page);
				page = getBlankPage(name, items);
				page.addItem(items.get(i));
			} else {
				// Add the item to the current page as per normal
				page.addItem(items.get(i));
			}
		}
		pages.add(page);
		// open page 0 for the specified player
		p.openInventory(pages.get(currpage));
		removerVidro(pages.get(currpage));
		users.put(p.getUniqueId(), this);
		if  (currpage == 0)
			pages.get(currpage).setItem(18, new ItemStack(Material.AIR));
		pages.get(pages.size() - 1).setItem(26, new ItemStack(Material.AIR));
	}

	public static final String nextPageName = ChatColor.AQUA + "§aPróxima página";
	public static final String previousPageName = ChatColor.AQUA + "§cPágina anterior";

	// This creates a blank page with the next and prev buttons
	private Inventory getBlankPage(String name, ArrayList<ItemStack> itens) {
		int i = itens.size() / 7;
		Inventory page = Bukkit.createInventory(null, (int)(i < 0 ? 27 : (9 * (3 + i) + 9 > 54 ? 54 : 9 * (3 + i) + 9)), name);

		ItemStack nextpage = new ItemBuilder().head(ItemBuilder.HeadsENUM.ARROW_RIGHT).name(nextPageName).build();

		ItemStack prevpage = new ItemBuilder().head(ItemBuilder.HeadsENUM.ARROW_LEFT).name(previousPageName).build();
		
		ItemStack itemStack = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(8).build();
		tryToSet(page, 0, itemStack);
		tryToSet(page, 1, itemStack);
		tryToSet(page, 2, itemStack);
		tryToSet(page, 3, itemStack);
		tryToSet(page, 4, itemStack);
		tryToSet(page, 5, itemStack);
		tryToSet(page, 6, itemStack);
		tryToSet(page, 7, itemStack);
		tryToSet(page, 8, itemStack);
		tryToSet(page, 9, itemStack);
		tryToSet(page, 17, itemStack);
		tryToSet(page, 18, prevpage);
		tryToSet(page, 26, nextpage);
		tryToSet(page, 27, itemStack);
		tryToSet(page, 35, itemStack);
		tryToSet(page, 36, itemStack);
		tryToSet(page, 45, itemStack);
		tryToSet(page, 46, itemStack);
		tryToSet(page, 47, itemStack);
		tryToSet(page, 48, itemStack);
		tryToSet(page, 49, itemStack);
		tryToSet(page, 50, itemStack);
		tryToSet(page, 51, itemStack);
		tryToSet(page, 52, itemStack);
		tryToSet(page, 53, itemStack);
		return page;
	}
	
	public void tryToSet(Inventory inv, int slot, ItemStack stack){
		try {
			inv.setItem(slot, stack);
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO: handle exception
		}
	}
	
	public static void removerVidro(Inventory inv){
		for(int i = 0; i < inv.getSize(); i++){
			if(inv.getItem(i) != null){
				if(inv.getItem(i).getType() == Material.STAINED_GLASS_PANE){
					if (inv.getItem(i).getDurability() == 8)
						inv.setItem(i, new ItemStack(Material.AIR));
				}
			}
		}
	}
}