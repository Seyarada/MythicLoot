package net.seyarada.mythicloot;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.drops.Drop;
import io.lumine.xikage.mythicmobs.drops.DropMetadata;
import io.lumine.xikage.mythicmobs.drops.DropTable;
import io.lumine.xikage.mythicmobs.drops.IItemDrop;
import io.lumine.xikage.mythicmobs.drops.LootBag;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import net.seyarada.mythicloot.rank.Util;

public class DropItem {
	
	Util u = new Util();
	ISchedulers s = new ISchedulers();
	
	private String msg;
	private String dmg;
	private String color;
	private String title;
	private String subtitle;
	private String sound;
	private String broadcast;
	private String item;
	private String command;
	private boolean glow;
	private boolean stop;
	private boolean explode;
	private int amount;
	private int top;
	private double chance;
	private double expheight;
	private double expoffset;
	DropTable dropTable;

	public void prepareDrop(MythicLineConfig mlc, Data data, double HP, Entity e, LinkedList<Map.Entry<String, Double>> topDamagers) {
		item = mlc.getKey();
		
		dropTable = null;
		
		Comparator<Map.Entry<String, Double>> comparator = Comparator.comparing(Map.Entry::getValue);
		Collections.sort(topDamagers, comparator.reversed());
		
		Optional<DropTable> maybeTable = MythicMobs.inst().getDropManager().getDropTable(item);
	     if (maybeTable.isPresent()) {
	    	 dropTable = (DropTable)maybeTable.get();
	     }
		
		Iterator<Entry<String, Double>> players = data.get().entrySet().iterator();
	    while (players.hasNext()) {
	    	
	    	msg = mlc.getString(new String[] { "message", "msg", "m"}, null);
			dmg = mlc.getString(new String[] { "damage", "dmg", "d"}, "0");
			color = mlc.getString(new String[] { "color", "cl"}, "display");
			title = mlc.getString(new String[] { "title", "t"}, null);
			subtitle = mlc.getString(new String[] { "subtitle", "st"}, null);
			command = mlc.getString(new String[] { "command", "cmd"}, null);
			sound = mlc.getString(new String[] { "sound", "sd"}, null);
			broadcast = mlc.getString(new String[] { "broadcast", "bc", "b"}, null);
			if(sound!=null) sound=sound.replace(".", "_").toUpperCase();
	    	glow = mlc.getBoolean(new String[] { "glow", "g"}, true);
			stop = mlc.getBoolean(new String[] { "stop", "s"}, false);
			explode = mlc.getBoolean(new String[] { "explode", "ex", "e"}, true);
			amount = mlc.getInteger(new String[] { "amount", "a"}, 1);
			top = mlc.getInteger(new String[] { "top", "T"}, -1);
			chance = mlc.getDouble(new String[] { "chance", "c"}, 1.0);
			expheight = mlc.getDouble(new String[] { "expheight", "exh"}, 0.6);
			expoffset = mlc.getDouble(new String[] { "expoffset", "exo"}, 0.2);
			
			
	    	
	        Entry<String, Double> pair = players.next();
	        String player = (String) pair.getKey();
	        double value = (double) pair.getValue();
	        
	        //System.out.println("Running drops for "+player+" with "+ value+" damage:");
	        
	        // Checks if the target meets the requirements ( chance, damage dealt & top )
	        boolean skip = true;
	    	double r = Math.random();
	    	if (r>=chance) continue;
	    	if(dmg.contains("%")){
	    		if (dmg.contains("to")) {
	    			String[] vals = dmg.replace("%", "").split("to");
	    			if (Double.valueOf(vals[0]) <= value/HP*100 && Double.valueOf(vals[1]) >= value/HP*100) {
	    				//System.out.println(player+" Condition for % damage met!");
	    				skip=false;
	    			}
	    		}
	    		else if (value/HP*100 >= Double.valueOf(dmg.replace("%", ""))) skip=false;
	    	}
	    	else if (dmg.contains("to")) {
    			String[] vals = dmg.split("to");
    			if (Double.valueOf(vals[0]) <= value && Double.valueOf(vals[1]) >= value) {
    				//System.out.println(player+" Condition for ranged damage met!");
    				skip=false;
    			}
    		}
	    	else if (value >= Double.valueOf(dmg)) {
	    		//System.out.println(player+" Condition for damage met!");
	    		skip=false;
	    	}
	    	if(top>0) {
	    		if(topDamagers.size()<top) {
	    			//System.out.println(player+" Player is not the top, skipping!");
	    			skip=true;
	    		} else {
	    			Entry<String, Double> a = topDamagers.get(top-1);
		    		if (player.equals(a.getKey())) {
		    			//System.out.println(player+" Player being top is met!");
		    			skip=false;
		    		} else {
		    			//System.out.println(player+" Player is not the top, skipping!");
		    			skip=true;
		    		}
	    		}
	    	}
	    	
	    	if(skip) continue;
	    	
	    	//System.out.println(player+" Everything ok! Going to drop the item");
	    	
	    	// Drops the item
	    	if(dropTable!=null) {
	    		Player p = Bukkit.getPlayer(player);
	    		LootBag loot = dropTable.generate(new DropMetadata(null, BukkitAdapter.adapt(p)));
	    		Iterator<Drop> toDrop = loot.getDrops().iterator();
	    		while(toDrop.hasNext()) {
	    	         Drop type = (Drop)toDrop.next();
	    	         MythicLineConfig subDrop = new MythicLineConfig((String) type.getLine());
	    	         String tempSound = subDrop.getString(new String[] { "sound", "sd"}, null);
	    	         if(tempSound!=null) sound = tempSound;
	    	         String tempBroadcast = subDrop.getString(new String[] { "broadcast", "bc", "b"}, null);
	    	         if(tempBroadcast!=null) broadcast = tempBroadcast;	 
	    	         
	    	         String tempTitle = subDrop.getString(new String[] { "title", "t"}, null);
	    	         if(tempTitle!=null) title = tempTitle;	 
	    	         String tempSubtitle = subDrop.getString(new String[] { "subtitle", "st"}, null);
	    	         if(tempSubtitle!=null) subtitle = tempSubtitle;	 
	    	         if(command!=null) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("<player.name>", p.getName()));
	    	         
	    	         if (type instanceof IItemDrop) {
	    	        	ItemStack mythicItem =  BukkitAdapter.adapt(((IItemDrop)type).getDrop(new DropMetadata(null, BukkitAdapter.adapt(p))));
	    	            drop(player, e, mythicItem);
	    	         }
	    		}
	    	}
	    	else {
	    		Optional<MythicItem> t = MythicMobs.inst().getItemManager().getItem(item);
	            ItemStack mythicItem = BukkitAdapter.adapt(t.get().generateItemStack(amount));
	    		drop(player, e, mythicItem);
	    	}
	    	if(stop) players.remove();
	    }
	}
	
	public void drop(String player, Entity e, ItemStack mythicItem) {
        Map<String, String> tags = new HashMap<String, String>();
	    tags.put("mythicloot", player);
        mythicItem = MythicItem.addItemNBT(mythicItem, "Base", tags);
    	Player p = Bukkit.getPlayer(player);
    	//System.out.println(player+" Message is "+msg);
    	if(msg!=null) {
    		//System.out.println(player+" Executing message");
    		p.sendMessage(cct(msg));
    	}
    	//System.out.println(player+" broadcast is "+broadcast);
    	if(broadcast!=null) {
    		//System.out.println(player+" Executing broadcast");
    		for (Player players : Bukkit.getOnlinePlayers()) {
    		    players.sendMessage(cct(broadcast.replace("<player>", player)));
    		}
    	}
    	Item dropped = e.getWorld().dropItemNaturally(e.getLocation(), mythicItem);
    	//System.out.println(player+" got "+dropped.getItemStack());
    	//System.out.println(player+" the droptable was "+dropTable);
    	if(glow) u.ColorHandler(dropped, color);
    	//System.out.println(player+" color is "+color);
    	if(explode) s.explodeParticles(dropped,p,color,expheight,expoffset);
    	if(sound!=null) p.playSound(p.getLocation(), Sound.valueOf(sound), 1, 1);
    	
    	//System.out.println(player+" title is "+title);
    	//System.out.println(player+" subtitle is "+subtitle);
    	if(title==null&&subtitle!=null)p.sendTitle("", cct(subtitle), 1, 40, 1);
    	else if(subtitle==null&&title!=null)p.sendTitle(cct(title), "", 1, 40, 1);
    	else if(title!=null||subtitle!=null) p.sendTitle(cct(title), cct(subtitle), 1, 40, 1);
    	s.keepInvisible(dropped,player);
	}
	
	public String cct(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
}
