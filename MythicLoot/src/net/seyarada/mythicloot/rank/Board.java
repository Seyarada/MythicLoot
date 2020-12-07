package net.seyarada.mythicloot.rank;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import net.seyarada.mythicloot.Config;

public class Board {

	Util u = new Util();
	Config c = new Config();
	
	public Board(LinkedList<Map.Entry<String, Double>> list, UUID uuid, double HP) {
		
		Comparator<Map.Entry<String, Double>> comparator = Comparator.comparing(Map.Entry::getValue);
		Collections.sort(list, comparator.reversed());

		int rankers = c.getRankers();
		int position = 0;
		String mobName = MythicMobs.inst().getMobManager().getMythicMobInstance(BukkitAdapter.adapt(Bukkit.getEntity(uuid))).getDisplayName();
		String mobHP = String.valueOf(new DecimalFormat("#.##").format(HP))+"HP";
		
		for(Entry<String, Double> abc:list) {
			
			position++;
			Player target = Bukkit.getPlayer(abc.getKey());	
			
			for(Object j:c.getConfig()) {
				boolean skip = false;
				String line = j.toString();
				if (line.contains("<mob.name>")) line = line.replace("<mob.name>", mobName);
				if (line.contains("<mob.hp>")) line = line.replace("<mob.hp>", mobHP);
				if (line.contains("<player.rank>")) line = line.replace("<player.rank>", String.valueOf(position));
				
			    String pattern = "<([0-9]*).name>";
			    Pattern r = Pattern.compile(pattern);
			    Matcher m = r.matcher(line);
			    if (m.find( )) {
			    	for (int i = 0; i < m.groupCount(); i++) {
			    		Integer index = Integer.valueOf(m.group(i).replace(".name>", "").replace("<", ""));
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
			    		Integer index = Integer.valueOf(m.group(i).replace(".dmg>", "").replace("<", ""));
			    		if (index<=rankers && list.size() >= index) {
			    			line = line.replace("<"+index+".dmg>", String.valueOf(new DecimalFormat("#.##").format(Double.valueOf(list.get(index-1).getValue())/HP*100)));
			    		} else {
			    			skip = true;
			    		}
			    		
			    	}
			    }
			    
			    if(!skip) u.msg(target, line);
			}
		}
		
	}
	
}
