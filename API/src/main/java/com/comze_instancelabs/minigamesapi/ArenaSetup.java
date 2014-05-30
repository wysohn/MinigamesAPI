package com.comze_instancelabs.minigamesapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class ArenaSetup {

	// actually the most basic arena just needs a spawn and a lobby

	/**
	 * Sets the spawn for a single-spawn arena
	 * 
	 * @param arenaname
	 * @param l
	 *            Location of the spawn
	 */
	public static void setSpawn(JavaPlugin plugin, String arenaname, Location l) {
		Util.saveComponentForArena(plugin, arenaname, "spawns.spawn0", l);
	}

	/**
	 * Sets one of multiple spawns for a multi-spawn arena
	 * 
	 * @param arenaname
	 * @param l
	 *            Location of the spawn
	 * @param count
	 *            Index of the spawn; if the given index is already set, the
	 *            spawn location will be overwritten
	 */
	public static void setSpawn(JavaPlugin plugin, String arenaname, Location l, int count) {
		Util.saveComponentForArena(plugin, arenaname, "spawns.spawn" + Integer.toString(count), l);
	}

	/**
	 * Sets the waiting lobby for an arena
	 * 
	 * @param arenaname
	 * @param l
	 *            Location of the lobby
	 */
	public static void setLobby(JavaPlugin plugin, String arenaname, Location l) {
		Util.saveComponentForArena(plugin, arenaname, "lobby", l);
	}

	/**
	 * Sets the main lobby
	 * 
	 * @param l
	 *            Location of the lobby
	 */
	public static void setMainLobby(JavaPlugin plugin, Location l) {
		Util.saveMainLobby(plugin, l);
	}

	/**
	 * Sets low and high boundaries for later blocks resetting
	 * 
	 * @param plugin
	 * @param arenaname
	 * @param l
	 *            Location to save
	 * @param low
	 *            True if it's the low boundary, false if it's the high boundary
	 */
	public static void setBoundaries(JavaPlugin plugin, String arenaname, Location l, boolean low) {
		if (low) {
			Util.saveComponentForArena(plugin, arenaname, "bounds.low", l);
		} else {
			Util.saveComponentForArena(plugin, arenaname, "bounds.high", l);
		}
	}

	/**
	 * Saves a given arena if it was set up properly.
	 * 
	 * @return Arena or null if setup failed
	 */
	public static Arena saveArena(JavaPlugin plugin, String arenaname) {
		if (!Validator.isArenaValid(plugin, arenaname)) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Arena " + arenaname + " appears to be invalid.");
			return null;
		}
		// TODO arena saving (to file too)
		Arena a = Util.initArena(plugin, arenaname);
		ArenaSetup.setArenaVIP(plugin, arenaname, false);
		MinigamesAPI.getAPI().pinstances.get(plugin).addArena(a);
		return a;
	}

	public static void setPlayerCount(JavaPlugin plugin, String arena, int count, boolean max) {
		String component = "max_players";
		if (!max) {
			component = "min_players";
		}
		String base = "arenas." + arena + "." + component;
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set(base, count);
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().saveConfig();
	}

	public static int getPlayerCount(JavaPlugin plugin, String arena, boolean max) {
		if (!max) {
			if (!MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().isSet("arenas." + arena + ".min_players")) {
				setPlayerCount(plugin, arena, plugin.getConfig().getInt("config.default_min_players"), max);
				return plugin.getConfig().getInt("config.default_min_players");
			}
			return MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getInt("arenas." + arena + ".min_players");
		}
		if (!MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().isSet("arenas." + arena + ".max_players")) {
			setPlayerCount(plugin, arena, plugin.getConfig().getInt("config.default_max_players"), max);
			return plugin.getConfig().getInt("config.default_max_players");
		}
		return MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getInt("arenas." + arena + ".max_players");
	}

	public static void setArenaVIP(JavaPlugin plugin, String arena, boolean vip) {
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().set("arenas." + arena + ".is_vip", vip);
		MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().saveConfig();
	}

	public static boolean getArenaVIP(JavaPlugin plugin, String arena) {
		return MinigamesAPI.getAPI().pinstances.get(plugin).getArenasConfig().getConfig().getBoolean("arenas." + arena + ".is_vip");
	}
}