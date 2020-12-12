package net.seyarada.mythicloot;

import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

import io.lumine.xikage.mythicmobs.MythicMobs;

public class Config {

	String message = "Configuration.Compatibility.MythicLoot.Message";
	String rank = "Configuration.Compatibility.MythicLoot.Rank";
	String score = "Configuration.Compatibility.MythicLoot.Score";
	FileConfiguration config = 	MythicMobs.inst().getConfig();
	
	public Config() {
		if(config.contains(message)) {
			List<String> old = config.getStringList(message);
			config.set(message, null);
			config.set(rank, old);

		} else if(!config.contains(rank)) {
			List<String> listOfStrings = Arrays.asList(
					/*
					Placeholders:
					<mob.name> Returns the mob display name
					<mob.hp> Returns the mob health
					<player.rank> Returns the player rank
					<player.dmg>
					<player.damage>
					<1.name> Returns the name of the top damager
					<1.dmg> Returns the damage % of the top damager
					<1.damage> Returns the true damage of the top damager
					*/
					"&8&m========================================",
					"<mob.name> &7- &a<mob.hp>",
					"&e#1 &7|&b &f<1.name> &7|&b <1.dmg>%",
					"&6#2 &7|&b &f<2.name> &7|&b <2.dmg>%",
					"&3#3 &7|&b &f<3.name> &7|&b <3.dmg>%",
					"",
					"Your rank: &a#<player.rank>",
					"&8&m========================================"
			);
			config.set(rank, listOfStrings);
		}

		List<String> listOfStrings = Arrays.asList(
					"&8&m======================",
					"<mob.name> &7- &a<mob.hp>",
					"&e#1 &7|&b &f<1.name> &7|&b <1.dmg>%",
					"&6#2 &7|&b &f<2.name> &7|&b <2.dmg>%",
					"&3#3 &7|&b &f<3.name> &7|&b <3.dmg>%",
					"",
					"Your rank: &a#<player.rank>",
					"&8&m======================"
		);
		config.set(score, listOfStrings);

		config.set("Configuration.Compatibility.MythicLoot.RankersToAnnounce", 3);
		MythicMobs.inst().saveConfig();
	}
	
	public List<?> getRankConfig() {
		return config.getStringList(rank);
	}

	public List<?> getScoreConfig() {
		return config.getStringList(score);
	}
	
	public int getRankers() {
		return config.getInt("Configuration.Compatibility.MythicLoot.RankersToAnnounce");
	}
	
}
