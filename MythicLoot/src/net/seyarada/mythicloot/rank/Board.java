package net.seyarada.mythicloot.rank;

import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.seyarada.mythicloot.nms.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.seyarada.mythicloot.Config;

public class Board {

	Util u = new Util();
	Config c = new Config();
	
	public Board(LinkedList<Map.Entry<String, Double>> list, UUID uuid, double HP) {
		
		Comparator<Map.Entry<String, Double>> comparator = Entry.comparingByValue();
		list.sort(comparator.reversed());

		int rankers = c.getRankers();
		int position = 0;
		String mobName = MythicMobs.inst().getMobManager().getMythicMobInstance(BukkitAdapter.adapt(Bukkit.getEntity(uuid))).getDisplayName();
		String mobHP = new DecimalFormat("#.##").format(HP) +"HP";
		
		for(Entry<String, Double> abc:list) {
			
			position++;
			Player target = Bukkit.getPlayer(abc.getKey());
			List<String> messages = new ArrayList<>();

			for(Object j:c.getConfig()) {
				boolean skip = false;
				if(j==null) continue;
				String line = j.toString();
				if (line.contains("<mob.name>")) line = line.replace("<mob.name>", mobName);
				if (line.contains("<mob.hp>")) line = line.replace("<mob.hp>", mobHP);
				if (line.contains("<player.rank>")) line = line.replace("<player.rank>", String.valueOf(position));
				
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
			    			line = line.replace("<"+index+".dmg>", String.valueOf(new DecimalFormat("#.##").format(list.get(index-1).getValue() /HP*100)));
			    		} else {
			    			skip = true;
			    		}
			    		
			    	}
			    }
			    
			    
			    line = PlaceholderAPI.setPlaceholders(target, line);
			    if(!skip) {

			    	u.msg(target, line.trim());
			    	messages.add(line);

				}
			}

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
	
}
