package net.seyarada.mythicloot.rank;

import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.seyarada.mythicloot.Config;

public class Board {

	private org.bukkit.Location Location;
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
			    
			    
			    line = PlaceholderAPI.setPlaceholders(target, line);
			    if(!skip) {

			    	u.msg(target, line);
			    	messages.add(line);

				}
			}

			Collections.reverse(messages);
			spawnHologram(uuid, target, messages);
		}
	}

	public void spawnHologram(UUID uuid, Player player, List<String> messages) {

		Location location = Bukkit.getEntity(uuid).getLocation();
		WorldServer wS = ((CraftWorld)location.getWorld()).getHandle();
		double lX = location.getX();
		double lY = location.getY()+1.5;
		double lZ = location.getZ();

		for(String msg : messages) {
			lY += 0.2;
			if(!msg.isEmpty()) {
				final EntityArmorStand armorStand = new EntityArmorStand(EntityTypes.ARMOR_STAND, wS);
				armorStand.setPosition(lX, lY, lZ);
				armorStand.setCustomName(CraftChatMessage.fromStringOrNull(msg));
				armorStand.setCustomNameVisible(true);
				armorStand.setInvisible(true);
				armorStand.setMarker(true);
				PacketPlayOutSpawnEntity packetPlayOutSpawnEntity = new PacketPlayOutSpawnEntity(armorStand);
				PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true);

				final PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
				connection.sendPacket(packetPlayOutSpawnEntity);
				connection.sendPacket(metadata);
			}
		}
	}
	
}
