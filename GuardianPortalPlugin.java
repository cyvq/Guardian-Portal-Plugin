package com.cyvertray.guardianportal;

import org.bukkit.command.TabCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Guardian;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class GuardianPortalPlugin extends JavaPlugin implements Listener, TabCompleter {

    private boolean enabled = true;
    private boolean debug = false;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getLogger().info("GuardianPortalPlugin enabled!");
        saveDefaultConfig();

        this.enabled = getConfig().getBoolean("enabled", true);
        this.debug = getConfig().getBoolean("debug", false);
        this.getCommand("guardian").setTabCompleter(this);
    }
//commands and perms
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("guardian")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("guardian.reload")) {
                    sender.sendMessage("§cYᴏᴜ ᴅᴏɴ’ᴛ ʜᴀᴠᴇ ᴘᴇʀᴍɪssɪᴏɴ ᴛᴏ ʀᴇʟᴏᴀᴅ ᴛʜɪs ᴘʟᴜɢɪɴ!");
                    return true;
                }
                reloadConfig();

                enabled = getConfig().getBoolean("enabled", true);

                sender.sendMessage("§eGᴜᴀʀᴅɪᴀɴPᴏʀᴛᴀʟPʟᴜɢɪɴ ᴄᴏɴꜰɪɢ ʀᴇʟᴏᴀᴅᴇᴅ!");
                getLogger().info("Cᴏɴꜰɪɢ ʀᴇʟᴏᴀᴅᴇᴅ ʙʏ " + sender.getName());
                return true;
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
                if (!sender.hasPermission("guardian.toggle")) {
                    sender.sendMessage("§cYᴏᴜ ᴅᴏɴ’ᴛ ʜᴀᴠᴇ ᴘᴇʀᴍɪssɪᴏɴ ᴛᴏ ᴛᴏɢɢʟᴇ ᴛʜɪs ᴘʟᴜɢɪɴ!");
                    return true;
                }
                enabled = !enabled;
                sender.sendMessage("§eGᴜᴀʀᴅɪᴀɴPᴏʀᴛᴀʟPʟᴜɢɪɴ ɪs ɴᴏᴡ " + (enabled ? "§2ᴇɴᴀʙʟᴇᴅ" : "§4ᴅɪsᴀʙʟᴇᴅ") + "!");
                getLogger().info("Pʟᴜɢɪɴ ᴛᴏɢɢʟᴇ ᴄʜᴀɴɢᴇᴅ ʙʏ " + sender.getName() + ": " + (enabled ? "ᴏɴ" : "ᴏꜰꜰ"));
                return true;
            }
            if (args.length == 1 && args[0].equalsIgnoreCase("debug")) {
                if (!sender.hasPermission("guardian.debug")) {
                    sender.sendMessage("§cYᴏᴜ ᴅᴏɴ’ᴛ ʜᴀᴠᴇ ᴘᴇʀᴍɪssɪᴏɴ ᴛᴏ ᴛᴏɢɢʟᴇ ᴅᴇʙᴜɢ!");
                    return true;
                }
                debug = !debug;
                sender.sendMessage("§aGᴜᴀʀᴅɪᴀɴPᴏʀᴛᴀʟPʟᴜɢɪɴ ᴅᴇʙᴜɢ ᴍᴏᴅᴇ ɪs ɴᴏᴡ " + (debug ? "§2ᴇɴᴀʙʟᴇᴅ" : "§4ᴅɪsᴀʙʟᴇᴅ") + "!");
                getLogger().info("Debug Mode changed by " + sender.getName() + ": " + (debug ? "on" : "off"));
                return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onGuardianPortal(EntityPortalEvent event) {

    if (!enabled) return;

    Entity entity = event.getEntity();
    if (!(entity instanceof Guardian)) return;
// sets it to persistent and removes some goals for performance, not needed when for kill chambers
    Guardian guardian = (Guardian) entity;
    guardian.setPersistent(true);
    guardian.setRemoveWhenFarAway(false);
    guardian.setTarget(null);
    guardian.setAware(false);
    guardian.setHealth(15.0);

    Location to = event.getTo();
    if (to != null) {
        World world = to.getWorld();
        if (world != null) {
            int chunkX = to.getBlockX() >> 4;
            int chunkZ = to.getBlockZ() >> 4;

            if (!world.isChunkLoaded(chunkX, chunkZ)) {
                world.getChunkAtAsync(chunkX, chunkZ).thenAccept(chunk -> {
                    if (debug) getLogger().info("Chunk loaded at " + chunkX + "," + chunkZ);
                    Bukkit.getScheduler().runTask(this, () -> {

                    });
                });
            } else {
                // Already loaded, normal tp event now
                if (debug) getLogger().info("Chunk already loaded at " + chunkX + "," + chunkZ + " (no loading needed)");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        // vanilla tp mechanics
                    }
                }.runTask(this);
            }
        }
    }

        if (debug) {
            this.getLogger().info("Guardian optimized and chunks preloaded!");
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("guardian")) {
            if (args.length == 1) {
                List<String> subCommands = new ArrayList<>();
                if (sender.hasPermission("guardian.reload")) subCommands.add("reload");
                if (sender.hasPermission("guardian.toggle")) subCommands.add("toggle");
                if (sender.hasPermission("guardian.debug")) subCommands.add("debug");
                return subCommands.stream()
                        .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                        .toList();
            }
        }
        return Collections.emptyList();
    }
}
