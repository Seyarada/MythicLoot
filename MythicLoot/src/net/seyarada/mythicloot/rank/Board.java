package net.seyarada.mythicloot.rank;

import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import net.seyarada.mythicloot.nms.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.seyarada.mythicloot.Config;

public class Board {

	Util u = new Util();
	Config c = new Config();
	
	public Board(LinkedList<Map.Entry<String, Double>> list, UUID uuid,
				 double HP, boolean announceRank, boolean announceScore, boolean relative) {
		
		Comparator<Map.Entry<String, Double>> comparator = Entry.comparingByValue();
		list.sort(comparator.reversed());

		int rankers = c.getRankers();
		int position = 0;
		ActiveMob mythicMob = MythicMobs.inst().getMobManager().getMythicMobInstance(BukkitAdapter.adapt(Bukkit.getEntity(uuid)));
		String mobName = mythicMob.getDisplayName();
		String mobHP = new DecimalFormat("#.##").format(HP) +"HP";
		
		for(Entry<String, Double> abc:list) {
			
			position++;
			Player target = Bukkit.getPlayer(abc.getKey());
			List<String> messages = new ArrayList<>();

			if(announceRank) cycle(c.getRankConfig(), true, false, rankers, position, mobName, mobHP,
					list, abc, HP, target, messages, uuid, relative, mythicMob);
			if(announceScore)  cycle(c.getScoreConfig(), false, true, rankers, position, mobName, mobHP,
					list, abc, HP, target, messages, uuid, relative, mythicMob);

		}
	}

	public void cycle(List<?> config, boolean announceRank, boolean announceScore, int rankers,
					  int position, String mobName, String mobHP, LinkedList<Entry<String, Double>> list,
					  Entry<String, Double> abc, double HP, Player target, List<String> messages, UUID uuid,
					  boolean relative, ActiveMob mythicMob) {

		for(Object j:config) {
			boolean skip = false;
			if(j==null) continue;
			String line = j.toString();
			if(mobName==null) {
				mobName = mythicMob.getEntity().getBukkitEntity().getName();
			}
			if (line.contains("<mob.name>")) line = line.replace("<mob.name>", mobName);
			if (line.contains("<player.rank>")) line = line.replace("<player.rank>", String.valueOf(position));


			// TODO
			// This is ugly
			if(!relative) {
				if (line.contains("<mob.hp>")) line = line.replace("<mob.hp>", mobHP);
				if (line.contains("<player.dmg>"))
					line = line.replace("<player.dmg>",
							new DecimalFormat("#.##").format(abc.getValue()/HP*100));
				if (line.contains("<player.damage>"))
					line = line.replace("<player.damage>",
							new DecimalFormat("#.##").format(abc.getValue()));
			} else {
				double Health = mythicMob.getEntity().getMaxHealth();
				double percentDMG = abc.getValue() / HP*100;
				double betterPercent = abc.getValue() / HP;
				double relativeDamage = Health*betterPercent;

				if (line.contains("<mob.hp>"))
					line = line.replace("<mob.hp>", String.valueOf(Health));
				if (line.contains("<player.dmg>"))
					line = line.replace("<player.dmg>",
							new DecimalFormat("#.##").format(percentDMG));
				if (line.contains("<player.damage>"))
					line = line.replace("<player.damage>",
							new DecimalFormat("#.##").format(relativeDamage));
			}

			String pattern = "<([0-9]*).name>";
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(line);
			if (m.find( )) {
				for (int i = 0; i < m.groupCount(); i++) {
					int index = Integer.parseInt(m.group(i).replace(".name>", "").replace("<", ""));
					if (index<=rankers && list.size() >= index) {
						line = line.replace("<"+index+".name>", list.get(index-1).getKey());
					} else {
						skip = true;
					}

				}
			}

			pattern = "<([0-9]*).dmg>";
			r = Pattern.compile(pattern);
			m = r.matcher(line);
			if (m.find( )) {
				for (int i = 0; i < m.groupCount(); i++) {
					int index = Integer.parseInt(m.group(i).replace(".dmg>", "").replace("<", ""));
					if (index<=rankers && list.size() >= index) {
						line = line.replace("<"+index+".dmg>", String.valueOf(new DecimalFormat("#.##").format(
								list.get(index-1).getValue() /HP*100)));
					} else {
						skip = true;
					}

				}
			}

			pattern = "<([0-9]*).damage>";
			r = Pattern.compile(pattern);
			m = r.matcher(line);
			if (m.find( )) {
				for (int i = 0; i < m.groupCount(); i++) {
					int index = Integer.parseInt(m.group(i).replace(".damage>", "").replace("<", ""));
					if (index<=rankers && list.size() >= index) {
						if(!relative) {
							line = line.replace("<"+index+".damage>", new DecimalFormat("#.##").format(
									list.get(index-1).getValue()));
						} else {
							double oldDMG = list.get(index - 1).getValue();
							double percentOldDMG = oldDMG / HP;
							double newPercentDMG = mythicMob.getEntity().getMaxHealth() / percentOldDMG;
							line = line.replace("<"+index+".damage>", new DecimalFormat("#.##").format(
									newPercentDMG));
						}
					} else {
						skip = true;
					}

				}
			}

			if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
				line = PlaceholderAPI.setPlaceholders(target, line).trim();
			else
				line = cct(line, target).trim();
			if(!skip) {

				if(announceRank)
					u.msg(target, line);
				if(announceScore)
					messages.add(line);

			}
		}

		if(announceScore) {
			Collections.reverse(messages);
			spawnHologram(uuid, target, messages);
		}
	}

	public void spawnHologram(UUID uuid, Player player, List<String> messages) {

		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

		switch (version) {
			case "v1_16_R3":
				new V1_16_R3().spawnHologram(uuid, player, messages);
				break;

			case "v1_16_R2":
				new V1_16_R2().spawnHologram(uuid, player, messages);
				break;

			case "v1_16_R1":
				new V1_16_R1().spawnHologram(uuid, player, messages);
				break;

			case "v1_15_R1":
				new V1_15_R1().spawnHologram(uuid, player, messages);
				break;

			case "v1_14_R1":
				new V1_14_R1().spawnHologram(uuid, player, messages);
				break;

			case "v1_13_R2":
				new V1_13_R2().spawnHologram(uuid, player, messages);
				break;

			case "v1_13_R1":
				new V1_13_R1().spawnHologram(uuid, player, messages);
				break;

			case "v1_12_R1":
				new V1_12_R1().spawnHologram(uuid, player, messages);
				break;
		}

	}

	public String cct(String msg, Player p) {
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
			return PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', msg));
		else
			return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
}
