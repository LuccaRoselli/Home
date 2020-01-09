package com.devlucca.homes.events;

import java.util.Arrays;
import java.util.WeakHashMap;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import com.devlucca.homes.Main;
import com.devlucca.homes.core.Home;
import com.devlucca.homes.core.HomePlayer;
import com.devlucca.homes.gui.PagedHomes;
import com.devlucca.homes.gui.ScrollerInventory;
import com.devlucca.homes.utils.StringManager;

public class DataLoad implements Listener {
	@EventHandler
	public void onJoin(final PlayerJoinEvent e) {
		Main.getHomeManager().loadData(e.getPlayer().getName());
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player))
			return;
		Player p = (Player) event.getWhoClicked();
		// Get the current scroller inventory the player is looking at, if the
		// player is looking at one.
		if (!ScrollerInventory.users.containsKey(p.getUniqueId()))
			return;
		ScrollerInventory inv = ScrollerInventory.users.get(p.getUniqueId());
		if (event.getCurrentItem() == null)
			return;
		if (event.getCurrentItem().getItemMeta() == null)
			return;
		if (event.getCurrentItem().getItemMeta().getDisplayName() == null)
			return;
		// If the pressed item was a nextpage button
		if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ScrollerInventory.nextPageName)) {
			event.setCancelled(true);
			// If there is no next page, don't do anything
			if (inv.currpage >= inv.pages.size() - 1) {
				inv.pages.get(inv.currpage).setItem(26, new ItemStack(Material.AIR));
				return;
			} else {
				// Next page exists, flip the page
				inv.currpage += 1;
				p.openInventory(inv.pages.get(inv.currpage));
				ScrollerInventory.removerVidro(inv.pages.get(inv.currpage));
			}
			// if the pressed item was a previous page button
		} else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ScrollerInventory.previousPageName)) {
			event.setCancelled(true);
			// If the page number is more than 0 (So a previous page exists)
			if (inv.currpage > 0) {
				// Flip to previous page
				inv.currpage -= 1;
				p.openInventory(inv.pages.get(inv.currpage));
				ScrollerInventory.removerVidro(inv.pages.get(inv.currpage));
			}
		}
	}
	
	public static WeakHashMap<Player, Home> gambiarra = new WeakHashMap<>();

	@SuppressWarnings("deprecation")
	@EventHandler
	public void clickInventory(final InventoryClickEvent e) {
		if (e.getInventory().getTitle().startsWith("Homes de") && e.getCurrentItem() != null) {
			final Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
				p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
				if (e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§eHome")) {
					final String id = e.getCurrentItem().getItemMeta().getDisplayName().replaceAll("§eHome - ", "");
					if (e.getClick() == ClickType.SHIFT_LEFT) {
						final HomePlayer hp = Main.getHomeManager().getHP(p.getName());
						final String home = id;
						boolean has = false;
						Home ho = null;
						for (final Home h : hp.getHomes()) {
							if (h.getName().equalsIgnoreCase(home)) {
								has = true;
								ho = h;
								break;
							}
						}
						if (!has) {
							p.sendMessage(StringManager.getPrefix() + "§cEsta home n\u00e3o existe!");
						} else {
							hp.deleteHome(ho);
	                        hp.getHomes().remove(ho);
	                        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 10.0f, 1.0f);
	                        p.sendMessage(StringManager.getPrefix() + "§aHome §f\"" + ho.getName() + "\"§a deletada com sucesso.");
	                        p.closeInventory();
						}
					} else if (e.getClick() == ClickType.RIGHT) {
						final HomePlayer hp = Main.getHomeManager().getHP(p.getName());
						final String home = id;
						boolean has = false;
						Home ho = null;
						for (final Home h : hp.getHomes()) {
							if (h.getName().equalsIgnoreCase(home)) {
								has = true;
								ho = h;
								break;
							}
						}
						if (!has) {
							p.sendMessage(StringManager.getPrefix() + "§cEsta home n\u00e3o existe!");
						} else {
							PagedHomes.getHomePanel(p, ho);
						}
					} else if (e.getClick() == ClickType.LEFT){
						p.performCommand("home " + id);
					}
				}
			}
		}
		if (e.getInventory().getTitle().startsWith("Home:") && e.getCurrentItem() != null) {
			final Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			final String id = e.getInventory().getTitle().replaceAll("Home: ", "");
			final HomePlayer hp = Main.getHomeManager().getHP(p.getName());
			final String home = id;
			boolean has = false;
			Home ho = null;
			for (final Home h : hp.getHomes()) {
				if (h.getName().equalsIgnoreCase(home)) {
					has = true;
					ho = h;
					break;
				}
			}
			if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
				p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
				if (e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§eMudar íco")) {
					if (!has) {
						p.sendMessage(StringManager.getPrefix() + "§cEsta home n\u00e3o existe!");
					} else {
						p.openInventory(Main.icones);
						Main.icon_change.put(p, ho);
						gambiarra.put(p, ho);
					}
				}
			}
			if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
				p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
				if (e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§eHome p")) {
					if (e.getCurrentItem().getDurability() == 5){
                        ho.setPublica(false);
                        hp.publica(ho, false);
                        PagedHomes.getHomePanel(p, ho);
                        p.sendMessage(StringManager.getPrefix() + "§aEsta home n\u00e3o \u00e9 mais p\u00fablica.");
					} else if (e.getCurrentItem().getDurability() == 7){
                        ho.setPublica(true);
                        hp.publica(ho, true);
                        PagedHomes.getHomePanel(p, ho);
                        p.sendMessage(StringManager.getPrefix() + "§aEsta home se tornou acess\u00edvel para qualquer jogador.");
					}
				}
			}
			if (e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
				p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.0f);
				if (e.getCurrentItem().getItemMeta().getDisplayName().startsWith("§cVolta")) {
					if (ScrollerInventory.users.containsKey(p.getUniqueId())) {
						ScrollerInventory inv = ScrollerInventory.users.get(p.getUniqueId());
						int page = inv.currpage;
						inv = null;
						PagedHomes.getPagedInventory(p);
						inv = ScrollerInventory.users.get(p.getUniqueId());
						p.openInventory(inv.pages.get(page));
						inv.currpage = page;
						ScrollerInventory.removerVidro(inv.pages.get(page));
					}
				}
			}
		}
		if (e.getInventory().getName().equalsIgnoreCase("Selecione um icone")) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
				return;
			}
			final ItemStack item = e.getCurrentItem();
			final Player p = (Player) e.getWhoClicked();
			if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() &&
					item.getItemMeta().getDisplayName().contains("Voltar") &&
						ScrollerInventory.users.containsKey(p.getUniqueId()) && gambiarra.containsKey(p)) {
				PagedHomes.getHomePanel(p, gambiarra.get(p));
				
				return;
			}
			if (Main.icon_change.containsKey(p)) {
				final Home c2 = Main.icon_change.get(p);
				if (Arrays.asList(Main.materials).contains(item.getType())) {
					c2.setIcone(item.getTypeId(), item.getDurability());
					final HomePlayer hp = Main.getHomeManager().getHP(p.getName());
					hp.icone(c2, HomePlayer.fromItemStack(e.getCurrentItem()));
					
					if (ScrollerInventory.users.containsKey(p.getUniqueId())) {
						
						ScrollerInventory inv = ScrollerInventory.users.get(p.getUniqueId());
						int page = inv.currpage;
						inv = null;
						PagedHomes.getPagedInventory(p);
						inv = ScrollerInventory.users.get(p.getUniqueId());
						p.openInventory(inv.pages.get(page));
						inv.currpage = page;
						ScrollerInventory.removerVidro(inv.pages.get(page));
					}
					p.sendMessage(StringManager.getPrefix() + "§aIcone alterado.");
					p.playSound(p.getLocation(), Sound.ORB_PICKUP, 10.0f, 2.0f);
					Main.icon_change.remove(p);
				}
			} else {
				p.closeInventory();
			}
		}
	}
}
