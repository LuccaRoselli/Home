package com.devlucca.homes;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.devlucca.homes.core.Home;
import com.devlucca.homes.core.HomeCmds;
import com.devlucca.homes.core.HomeManager;
import com.devlucca.homes.events.DataLoad;
import com.devlucca.homes.license.License;
import com.devlucca.homes.storage.Query;
import com.devlucca.homes.storage.Storage;

public class Main extends JavaPlugin {
	private static HomeManager manager;
	private static Main plugin;
	private static Storage storage;
	public static Material[] materials;
	public static Inventory icones;
	public static HashMap<Player, Home> icon_change;

	public void onEnable() {
		(Main.plugin = this).saveDefaultConfig();
		License l = new License();
		if (!l.check()) {
			Bukkit.getConsoleSender().sendMessage("§6[LICENCA - " + License.plugin_name + "] §fACEITA");
			Bukkit.getConsoleSender().sendMessage("§6[LICENCA - " + License.plugin_name + "] §fCarregando plugin...");
			Main.materials = new Material[] { Material.APPLE, Material.SPONGE, Material.PACKED_ICE, Material.COAL_BLOCK,
					Material.DRAGON_EGG, Material.WOOL, Material.COAL_ORE, Material.DIAMOND_LEGGINGS,
					Material.DIAMOND_BOOTS, Material.CHEST, Material.MELON, Material.HAY_BLOCK, Material.OBSIDIAN,
					Material.BEDROCK, Material.TNT, Material.DIAMOND, Material.EMERALD, Material.GOLD_ORE,
					Material.GLASS_BOTTLE, Material.IRON_PICKAXE, Material.EGG, Material.IRON_SPADE,
					Material.DIAMOND_SWORD, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.IRON_SWORD,
					Material.COAL, Material.ENCHANTED_BOOK, Material.BREAD, Material.BONE, Material.BOOK,
					Material.ANVIL, Material.EXP_BOTTLE, Material.ENDER_CHEST, Material.ENDER_PEARL, Material.PAPER,
					Material.SLIME_BALL, Material.ARROW, Material.DIAMOND_ORE, Material.SKULL, Material.IRON_AXE,
					Material.GOLDEN_APPLE, Material.SIGN, Material.COOKED_BEEF, Material.MOB_SPAWNER, Material.DIRT};
			Main.icones = Bukkit.createInventory((InventoryHolder) null, 54, "Selecione um icone");
			Main.icon_change = new HashMap<Player, Home>();
			Main.manager = new HomeManager();
			Main.storage = new Storage((Plugin) this);
			this.loadStorage();
			this.loadIcons();
			this.getServer().getPluginManager().registerEvents((Listener) new DataLoad(), (Plugin) this);
			this.getCommand("home").setExecutor((CommandExecutor) new HomeCmds());
			this.getCommand("publica").setExecutor((CommandExecutor) new HomeCmds());
			this.getCommand("sethome").setExecutor((CommandExecutor) new HomeCmds());
			this.getCommand("delhome").setExecutor((CommandExecutor) new HomeCmds());
			for (Player p : Bukkit.getOnlinePlayers())
				Main.getHomeManager().loadData(p.getName());
		}
	}

	public static HomeManager getHomeManager() {
		return Main.manager;
	}

	public void loadIcons() {
		Material[] materials;
		for (int length = (materials = Main.materials).length, i = 0; i < length; ++i) {
			final Material m = materials[i];
			final ItemStack item = new ItemStack(m);
			Main.icones.addItem(new ItemStack[] { item });
		}
		ItemStack item2 = null;
		if (Bukkit.getServer().getBukkitVersion().getClass().getPackage().toString().contains("v1_8")) {
			item2 = new ItemStack(Material.BARRIER);
		} else {
			item2 = new ItemStack(Material.ARROW);
		}
		final ItemMeta meta = item2.getItemMeta();
		meta.setDisplayName("§cVoltar");
		item2.setItemMeta(meta);
		Main.icones.setItem(49, item2);
	}

	private void loadStorage() {
		if (this.getConfig().getBoolean("MySQL.enable")) {
			final String user = this.getConfig().getString("MySQL.user");
			final String senha = this.getConfig().getString("MySQL.senha");
			final String host = this.getConfig().getString("MySQL.host");
			final int porta = this.getConfig().getInt("MySQL.porta");
			final String database = this.getConfig().getString("MySQL.database");
			getStorage().initMySQL(user, senha, host, database, porta);
		} else {
			getStorage().initSQLite("storage.db");
		}
		Query.create();
	}

	public static Storage getStorage() {
		return Main.storage;
	}

	public static Main getInstance() {
		return Main.plugin;
	}
}
