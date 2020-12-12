package net.seyarada.mythicloot;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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
	private String top;
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
	private boolean toInv;
	private int amount;
	private double chance;
	private double expheight;
	private double expoffset;
	DropTable dropTable;

	public void prepareDrop(MythicLineConfig mlc, Data data, double HP, Entity e, LinkedList<Map.Entry<String, Double>> topDamagers) {
		item = mlc.getKey();
		
		dropTable = null;
		
		Comparator<Map.Entry<String, Double>> comparator = Entry.comparingByValue();
		topDamagers.sort(comparator.reversed());
		
		Optional<DropTable> maybeTable = MythicMobs.inst().getDropManager().getDropTable(item);
		maybeTable.ifPresent(table -> dropTable = table);
		
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
			toInv = mlc.getBoolean(new String[] { "toinv", "ti"}, false);
			amount = mlc.getInteger(new String[] { "amount", "a"}, 1);
			top = mlc.getString(new String[] { "top", "T"}, null);
			chance = mlc.getDouble(new String[] { "chance", "c"}, 1.0);
			expheight = mlc.getDouble(new String[] { "expheight", "exh"}, 0.6);
			expoffset = mlc.getDouble(new String[] { "expoffset", "exo"}, 0.2);
			
			
	    	
	        Entry<String, Double> pair = players.next();
	        String player = pair.getKey();
	        double value = pair.getValue();


	        AtomicBoolean skip = new AtomicBoolean(true);
	    	double r = Math.random();
	    	if (r>=chance) continue;
	    	if(dmg.contains("%")){
	    		if (dmg.contains("to")) {
	    			String[] values = dmg.replace("%", "").split("to");
	    			if (Double.parseDouble(values[0]) <= value/HP*100 && Double.parseDouble(values[1]) >= value/HP*100) {
	    				skip.set(false);
	    			}
	    		}
	    		else if (value/HP*100 >= Double.parseDouble(dmg.replace("%", ""))) skip.set(false);
	    	}
	    	else if (dmg.contains("to")) {
    			String[] vals = dmg.split("to");
    			if (Double.parseDouble(vals[0]) <= value && Double.parseDouble(vals[1]) >= value) {
    				skip.set(false);
    			}
    		}
	    	else if (value >= Double.parseDouble(dmg)) {
	    		skip.set(false);
	    	}

	    	if(top!=null) {

	    		if (top.contains("to")) {
					String[] values = top.split("to");
					int i1 = Integer.parseInt(values[0]);
					int i2 = Integer.parseInt(values[0])+1;

					IntStream.range(i1, i2).forEachOrdered(n -> {
						Entry<String, Double> a = topDamagers.get(n-1);
						skip.set(!player.equals(a.getKey()));
					});
				} else {
	    			int topR = Integer.parseInt(top);
					if(topDamagers.size()<topR) {
						skip.set(true);
					} else {
						Entry<String, Double> a = topDamagers.get(topR-1);
						skip.set(!player.equals(a.getKey()));
					}
				}
	    	}
	    	
	    	if(skip.get()) continue;

	    	if(dropTable!=null) {
	    		Player p = Bukkit.getPlayer(player);
	    		LootBag loot = dropTable.generate(new DropMetadata(MythicMobs.inst().getMobManager().
						getMythicMobInstance(BukkitAdapter.adapt(e))
								, BukkitAdapter.adapt(p)));

				for (Drop type : loot.getDrops()) {
					MythicLineConfig subDrop = new MythicLineConfig(type.getLine());

					String tempSound = subDrop.getString(new String[]{"sound", "sd"}, null);
					if (tempSound != null) sound = tempSound;

					String tempBroadcast = subDrop.getString(new String[]{"broadcast", "bc", "b"}, null);
					if (tempBroadcast != null) broadcast = tempBroadcast;

					String tempTitle = subDrop.getString(new String[]{"title", "t"}, null);
					if (tempTitle != null) title = tempTitle;

					String tempSubtitle = subDrop.getString(new String[]{"subtitle", "st"}, null);
					if (tempSubtitle != null) subtitle = tempSubtitle;

					String tempCommand = subDrop.getString(new String[]{"command", "cmd"}, null);
					if (tempCommand != null) command = tempCommand;

					boolean tempInv = subDrop.getBoolean(new String[] { "toinv", "ti"}, false);
					if (tempInv) toInv = true;

					if (type instanceof IItemDrop) {
						ItemStack mythicItem = BukkitAdapter.adapt(((IItemDrop) type).getDrop(new DropMetadata(null, BukkitAdapter.adapt(p))));
						drop(player, e, mythicItem);
					}
				}
	    	}
	    	else {
				Optional<MythicItem> mI = MythicMobs.inst().getItemManager().getItem(item);
				ItemStack mythicItem;
				if (mI.isPresent()) {
					mythicItem = BukkitAdapter.adapt(mI.get().generateItemStack(amount));
				} else {
	    			try {
						mythicItem = new ItemStack(Material.valueOf(item.toUpperCase()), amount);
					} catch (Exception k) {
						Bukkit.getServer().getConsoleSender().sendMessage(
								"§c[ERROR] §eMythicLoot isn't able to create an item for "+
								item+"! Verify that you can get this item with in-game commands" +
								" like §b/mm i get <item> §e, as ML needs MM items to work for them to be dropped");
						return;
					}
	    		}
	    		drop(player, e, mythicItem);
	    	}
	    	if(stop) players.remove();
	    }
	}
	
	public void drop(String player, Entity e, ItemStack mythicItem) {

		Player p = Bukkit.getServer().getPlayer(player);

        Map<String, String> tags = new HashMap<>();
	    tags.put("mythicloot", player);
        mythicItem = MythicItem.addItemNBT(mythicItem, "Base", tags);
    	if(msg!=null&&p!=null) {
    		p.sendMessage(cct(msg, p));
    	}
    	if(broadcast!=null) {
    		for (Player players : Bukkit.getOnlinePlayers()) {
    		    players.sendMessage(cct(broadcast.replace("<player>", player), p));
    		}
    	}

    	if(p!=null) {
			if(sound!=null) p.playSound(p.getLocation(), Sound.valueOf(sound), 1, 1);
			if(title==null&&subtitle!=null)p.sendTitle("", cct(subtitle, p), 1, 40, 1);
			else if(subtitle==null&&title!=null)p.sendTitle(cct(title, p), "", 1, 40, 1);
			else if(title != null) p.sendTitle(cct(title, p), cct(subtitle, p), 1, 40, 1);
			if(command!=null) Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
					command.replace("<player.name>", p.getName()));
		}

		if(toInv&&p!=null && p.getInventory().firstEmpty()>=0) {
			p.getInventory().addItem(mythicItem);
			return;
		}

		if(mythicItem!=null&&mythicItem.getType()!=Material.AIR) {
			Item dropped = e.getWorld().dropItemNaturally(e.getLocation(), mythicItem);
			if(glow) u.ColorHandler(dropped, color);
			if(explode) s.explodeParticles(dropped,p,color,expheight,expoffset);
			s.keepInvisible(dropped,player);
		}
	}
	
	public String cct(String msg, Player p) {
		return PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', msg));
	}
	
}
