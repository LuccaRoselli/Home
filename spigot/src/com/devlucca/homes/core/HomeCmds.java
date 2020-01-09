package com.devlucca.homes.core;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.devlucca.homes.Main;
import com.devlucca.homes.gui.PagedHomes;
import com.devlucca.homes.utils.StringManager;

public class HomeCmds implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command arg1, final String label, final String[] args) {
        if (label.equalsIgnoreCase("publica") && sender instanceof Player) {
            final Player p = (Player)sender;
            if (Main.getHomeManager().getHP(p.getName()) == null) {
                p.sendMessage(StringManager.getPrefix() + "§cOcorreu um problema, tente relogar no servidor e tentar novamente.");
            }
            else {
                final HomePlayer hp = Main.getHomeManager().getHP(p.getName());
                if (args.length == 0) {
                    p.sendMessage(StringManager.getPrefix() + "§cUtilize §f\"/publica <home>\"§c para tornar uma home p\u00fablica.");
                }
                else {
                    final String home = args[0];
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
                    }
                    else if (ho.isPublica()) {
                        ho.setPublica(false);
                        hp.publica(ho, false);
                        p.sendMessage(StringManager.getPrefix() + "§aEsta home n\u00e3o \u00e9 mais p\u00fablica.");
                    }
                    else {
                        ho.setPublica(true);
                        hp.publica(ho, true);
                        p.sendMessage(StringManager.getPrefix() + "§aEsta home se tornou acess\u00edvel para qualquer jogador.");
                    }
                }
            }
        }
        if (label.equalsIgnoreCase("delhome") && sender instanceof Player) {
            final Player p = (Player)sender;
            if (Main.getHomeManager().getHP(p.getName()) == null) {
                p.sendMessage(StringManager.getPrefix() + "§cOcorreu um problema, tente relogar no servidor e tentar novamente.");
            }
            else {
                final HomePlayer hp = Main.getHomeManager().getHP(p.getName());
                if (args.length == 0) {
                    p.sendMessage(StringManager.getPrefix() + "§cUtilize §f\"/delhome <home>\"§c para deletar uma home.");
                }
                else {
                    final String home = args[0];
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
                        p.sendMessage(StringManager.getPrefix() + "§cVoc\u00ea n\u00e3o possui uma home com este nome!");
                    }
                    else {
                        hp.deleteHome(ho);
                        hp.getHomes().remove(ho);
                        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 10.0f, 1.0f);
                        p.sendMessage(StringManager.getPrefix() + "§aHome §f\"" + ho.getName() + "\"§a deletada com sucesso.");
                    }
                }
            }
        }
        if (label.equalsIgnoreCase("sethome") && sender instanceof Player) {
            final Player p = (Player)sender;
            if (Main.getHomeManager().getHP(p.getName()) == null) {
                p.sendMessage(StringManager.getPrefix() + "§cOcorreu um problema, tente relogar no servidor e tentar novamente.");
            }
            else {
                final HomePlayer hp = Main.getHomeManager().getHP(p.getName());
                if (args.length == 0) {
                    p.sendMessage(StringManager.getPrefix() + "§cUtilize §f\"/sethome <home>\"§c para definir uma nova home.");
                }
                else {
                    final String world = p.getWorld().getName();
                    if (Main.getInstance().getConfig().getStringList("blacklist_worlds").contains(world)) {
                        p.sendMessage(StringManager.getPrefix() + "§cVoc\u00ea n\u00e3o pode setar home neste world.");
                        return true;
                    }
                    final String home2 = args[0];
                    boolean has2 = false;
                    for (final Home h : hp.getHomes()) {
                        if (h.getName().equalsIgnoreCase(home2)) {
                            p.sendMessage(StringManager.getPrefix() + "§cVoc\u00ea j\u00e1 possui uma home com este nome!");
                            has2 = true;
                            break;
                        }
                    }
                    if (!has2) {
                        if (home2.matches("^[a-zA-Z0-9]*$")) {
                            int homes = 5;
                            for (int i = 5; i < 60; ++i) {
                                if (p.hasPermission("homes." + i) && i > homes) {
                                    homes = i;
                                }
                            }
                            final int index = hp.getHomes().size();
                            if (index >= homes) {
                                p.sendMessage(StringManager.getPrefix() + "§cVoc\u00ea j\u00e1 alcan\u00e7ou seu limite de §f" + homes + "§c homes.");
                                return true;
                            }
                            if (home2.length() > 10) {
                                p.sendMessage(StringManager.getPrefix() + "§cSua home deve ter no m\u00e1ximo 10 caracteres.");
                            }
                            else {
                                final Home h2 = new Home(home2, p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch(), p.getWorld().getName(), new ItemStack(Material.DIRT));
                                h2.setPublica(false);
                                hp.getHomes().add(h2);
                                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 10.0f, 1.0f);
                                p.sendMessage(StringManager.getPrefix() + "§aHome §f\"" + home2 + "\"§a setada com sucesso.");
                                hp.insertHome(h2);
                                hp.getHomes().add(h2);
                            }
                        }
                        else {
                            p.sendMessage(StringManager.getPrefix() + "§cO nome da sua home n\u00e3o deve conter caracteres especiais.");
                        }
                    }
                }
            }
        }
        if ((label.equalsIgnoreCase("home") || label.equalsIgnoreCase("homes")) && sender instanceof Player) {
            final Player p = (Player)sender;
            if (Main.getHomeManager().getHP(p.getName()) == null) {
                p.sendMessage(StringManager.getPrefix() + "§cOcorreu um problema, tente relogar no servidor e tentar novamente.");
            }
            else if (args.length == 0) {
//                final HomePlayer hp = Main.getHomeManager().getHP(p.getName());
                PagedHomes.getPagedInventory(p);
//                if (hp.getHomes().isEmpty()) {
//                    p.sendMessage(StringManager.getPrefix() + "");
//                    p.sendMessage(StringManager.getPrefix() + "§7P\u00fablicas: §7Nenhuma");
//                    p.sendMessage(StringManager.getPrefix() + "§7Privadas: §7Nenhuma");
//                    p.sendMessage(StringManager.getPrefix() + "");
//                }
//                else {
//                    final StringBuilder publicas = new StringBuilder();
//                    final StringBuilder normal = new StringBuilder();
//                    int index2 = 0;
//                    int index3 = 0;
//                    for (final Home h3 : hp.getHomes()) {
//                        if (h3.isPublica()) {
//                            publicas.append(h3.getName());
//                            if (index2 >= hp.getHomes().size()) {
//                                continue;
//                            }
//                            ++index2;
//                            publicas.append(", ");
//                        }
//                        else {
//                            normal.append(h3.getName());
//                            if (index3 >= hp.getHomes().size()) {
//                                continue;
//                            }
//                            ++index3;
//                            normal.append(", ");
//                        }
//                    }
//                    p.sendMessage(StringManager.getPrefix() + "");
//                    if (publicas.length() == 0) {
//                        p.sendMessage(StringManager.getPrefix() + "§7P\u00fablicas: §7Nenhuma");
//                    }
//                    else {
//                        p.sendMessage(StringManager.getPrefix() + "§7P\u00fablicas: §f" + publicas.toString().trim());
//                    }
//                    if (normal.length() == 0) {
//                        p.sendMessage(StringManager.getPrefix() + "§7Privadas: §7Nenhuma");
//                    }
//                    else {
//                        p.sendMessage(StringManager.getPrefix() + "§7hs: §f" + normal.toString().trim());
//                    }
//                    p.sendMessage(StringManager.getPrefix() + "");
//                }
            }
            else {
                final String home3 = args[0];
                HomePlayer hp2 = null;
                if (home3.contains(":")) {
                	hp2 = Main.getHomeManager().getHP(home3.split(":")[0]);
                    boolean a = false;
                    for (final Home h5 : hp2.getHomes()) {
                        if (h5.getName().equalsIgnoreCase(home3.split(":")[1])) {
                            if (h5.isInvalid()) {
                                p.sendMessage(StringManager.getPrefix() + "§cEsta home \u00e9 inv\u00e1lida.");
                            }
                            else if (!h5.isPublica() && !p.getName().equalsIgnoreCase(home3.split(":")[0])){
                            	p.sendMessage(StringManager.getPrefix() + "§cEsta home é privada.");
                            }
                            else if (p.hasPermission("home.bypass")) {
                                p.teleport(h5.getLoc());
                                p.setAllowFlight(false);
                                p.sendMessage(StringManager.getPrefix() + "§7Voc\u00ea foi teleportado para a home §f\"" + h5.getName() + "\" §7de §f" + home3.split(":")[0] + "§7.");
                                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 10.0f, 1.0f);
                            }
                            else {
                                new BukkitRunnable() {
                                    int timer = 3;
                                    double x = p.getLocation().getX();
                                    double y = p.getLocation().getY();
                                    double z = p.getLocation().getZ();
                                    
                                    public void run() {
                                        final double x = p.getLocation().getX();
                                        final double y = p.getLocation().getY();
                                        final double z = p.getLocation().getZ();
                                        if (x == this.x && y == this.y && z == this.z) {
                                            p.sendMessage(StringManager.getPrefix() + "§7Aguarde §f" + this.timer + " segundo(s)");
                                            --this.timer;
                                            if (this.timer == 0) {
                                                p.teleport(h5.getLoc());
                                                p.setAllowFlight(false);
                                                p.sendMessage(StringManager.getPrefix() + "§7Voc\u00ea foi teleportado para a home §f\"" + h5.getName() + "\" §7de §f" + home3.split(":")[0] + "§7.");
                                                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 10.0f, 1.0f);
                                                this.cancel();
                                            }
                                        }
                                        else {
                                            p.playSound(p.getLocation(), Sound.NOTE_BASS, 10.0f, 1.0f);
                                            p.sendMessage(StringManager.getPrefix() + "§cVoc\u00ea se moveu, teleporte cancelado.");
                                            this.cancel();
                                        }
                                    }
                                }.runTaskTimer((Plugin)Main.getInstance(), 0L, 20L);
                            }
                            a = true;
                            break;
                        }
                    }
                    if (!a) {
                        p.sendMessage(StringManager.getPrefix() + "§cVoc\u00ea n\u00e3o possui uma home chamada \"" + home3 + "\".");
                    }
                }
                else {
                	hp2 = Main.getHomeManager().getHP(p.getName());
                    boolean a = false;
                    for (final Home h5 : hp2.getHomes()) {
                        if (h5.getName().equalsIgnoreCase(home3)) {
                            if (h5.isInvalid()) {
                                p.sendMessage(StringManager.getPrefix() + "§cEsta home \u00e9 inv\u00e1lida.");
                            }
                            else if (p.hasPermission("home.bypass")) {
                                p.teleport(h5.getLoc());
                                p.setAllowFlight(false);
                                p.sendMessage(StringManager.getPrefix() + "§7Voc\u00ea foi teleportado para a home §f\"" + h5.getName() + "\"");
                                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 10.0f, 1.0f);
                            }
                            else {
                                new BukkitRunnable() {
                                    int timer = 3;
                                    double x = p.getLocation().getX();
                                    double y = p.getLocation().getY();
                                    double z = p.getLocation().getZ();
                                    
                                    public void run() {
                                        final double x = p.getLocation().getX();
                                        final double y = p.getLocation().getY();
                                        final double z = p.getLocation().getZ();
                                        if (x == this.x && y == this.y && z == this.z) {
                                            p.sendMessage(StringManager.getPrefix() + "§7Aguarde §f" + this.timer + " segundo(s)");
                                            --this.timer;
                                            if (this.timer == 0) {
                                                p.teleport(h5.getLoc());
                                                p.setAllowFlight(false);
                                                p.sendMessage(StringManager.getPrefix() + "§7Voc\u00ea foi teleportado para a home §f\"" + h5.getName() + "\"");
                                                p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 10.0f, 1.0f);
                                                this.cancel();
                                            }
                                        }
                                        else {
                                            p.playSound(p.getLocation(), Sound.NOTE_BASS, 10.0f, 1.0f);
                                            p.sendMessage(StringManager.getPrefix() + "§cVoc\u00ea se moveu, teleporte cancelado.");
                                            this.cancel();
                                        }
                                    }
                                }.runTaskTimer((Plugin)Main.getInstance(), 0L, 20L);
                            }
                            a = true;
                            break;
                        }
                    }
                    if (!a) {
                        p.sendMessage(StringManager.getPrefix() + "§cVoc\u00ea n\u00e3o possui uma home chamada \"" + home3 + "\"");
                    }
                }
            }
        }
        return false;
    }
}
