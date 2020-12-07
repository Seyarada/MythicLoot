package net.seyarada.mythicloot;

import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import io.lumine.xikage.mythicmobs.MythicMobs;

public class Config {

	String PATH = "Configuration.Compatibility.MythicLoot.Message";
	FileConfiguration CONFIG = 	MythicMobs.inst().getConfig();
	
	public Config() {
		if(!CONFIG.contains(PATH)) {
			List<String> listOfStrings = Arrays.asList(
					/*
					"# Placeholders:",
					"# <mob.name> Returns the mob display name",
					"# <mob.hp> Returns the mob health",
					"# <player.rank> Returns the player rank",
					"# <1.name> Returns the name of the top damager",
					"# <1.dmg> Returns the damage of the top damager",
					"",*/
					"&8&m========================================",
					"<mob.name> &7- &a<mob.hp>", 
					"&e#1 &7|&b &f<1.name> &7|&b <1.dmg>%",
					"&6#2 &7|&b &f<2.name> &7|&b <2.dmg>%",
					"&3#3 &7|&b &f<3.name> &7|&b <3.dmg>%",
					"",
					"Your rank: &a#<player.rank>",
					"&8&m========================================"
					);
			CONFIG.set(PATH, listOfStrings);
			CONFIG.set("Configuration.Compatibility.MythicLoot.RankersToAnnounce", 3);
			MythicMobs.inst().saveConfig();
		}
	}
	
	public List<?> getConfig() {
		return CONFIG.getList(PATH);
	}
	
	public int getRankers() {
		return CONFIG.getInt("Configuration.Compatibility.MythicLoot.RankersToAnnounce");
	}
	
}
