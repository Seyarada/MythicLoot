package net.seyarada.mythicloot;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.drops.*;
import io.lumine.xikage.mythicmobs.io.IOHandler;
import io.lumine.xikage.mythicmobs.io.IOLoader;
import io.lumine.xikage.mythicmobs.io.MythicConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.RandomDouble;
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
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import net.seyarada.mythicloot.rank.Util;

public class DropItem extends DropTable {
	
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
	private boolean shared;
	private int amount;
	private float chance;
	private double expheight;
	private double expoffset;
	private DropTable dropTable;

	public DropItem(String file, String name, List<String> drops) {
		super(file, name, drops);
	}

	public void prepareDrop(MythicLineConfig mlc, Data data, double HP, Entity e, LinkedList<Map.Entry<String, Double>> topDamagers) {
	    // Gets the item/part before the {}
		item = mlc.getKey();

		// Gets the players / damage arranged in order
		Comparator<Map.Entry<String, Double>> comparator = Entry.comparingByValue();
		topDamagers.sort(comparator.reversed());

		// Gets if the item is a dropTable
		Optional<DropTable> maybeTable = MythicMobs.inst().getDropManager().getDropTable(item);
		maybeTable.ifPresent(table -> dropTable = table);

		// Gets the iterator of all players in the fight
		Iterator<Entry<String, Double>> players = data.get().entrySet().iterator();
		int k = 0;

        // Iterates trough the players
	    while (players.hasNext()) {

            Entry<String, Double> pair = players.next();
            String player = pair.getKey();  // Player
            double value = pair.getValue(); // Damage dealt
	    	k++;

	    	// Gets the attributes and runs the conditions
	    	getAttributes(mlc);
	    	if(!runConditions(value, HP, player, topDamagers, k))
	    	    continue;

	    	if(dropTable!=null) {
                AbstractPlayer p = BukkitAdapter.adapt(Bukkit.getPlayer(player));
                AbstractEntity entity = BukkitAdapter.adapt(e);
                ActiveMob caster = MythicMobs.inst().getMobManager().getMythicMobInstance(entity);
	    		LootBag loot = dropTable.generate(new DropMetadata(caster, p));
	    		System.out.println(dropTable.getFileName());

	    		String file = dropTable.getFileName();
	    		String name = dropTable.getInternalName();

				IOLoader<MythicMobs> defaultDroptables = new IOLoader(MythicMobs.inst(), "ExampleDropTables.yml", "DropTables");
				List<File> droptableFiles = IOHandler.getAllFiles(defaultDroptables.getFile().getParent());
				List<IOLoader<MythicMobs>> droptableLoaders = IOHandler.getSaveLoad(MythicMobs.inst(), droptableFiles, "DropTables");
				DropTable newTable = null;

				for (IOLoader<MythicMobs> sl : droptableLoaders) {
					if(sl.getFile().getName().equals(file)) {
						Set<String> lS = sl.getCustomConfig().getConfigurationSection("."+name).getKeys(false);
						int ct = 0;
						MythicConfig mc = new MythicConfig(name, sl.getCustomConfig());
						List<String> myList = mc.getStringList("Drops");
						for (String s : myList) {
							System.out.println("S: "+s);
							MythicLineConfig imlc = new MythicLineConfig(file, s);
							double weight = new tableHander(s, imlc, file).getWeight();
							double chancha = imlc.getDouble(new String[]{"multiplier", "ml"}, 1d);
							if(chancha>1d) {
								String replace = s.replace(String.valueOf(weight), String.valueOf(weight * chancha));
								myList.set(ct, replace);
								mc.set("Drops", myList);
								this.to
							}
							ct++;
						}

						System.out.println(mc.getStringList("Drops"));
						newTable = new DropTable(file, name+"temp", mc);
						System.out.println(newTable.hasDrops());

					}
				}

				for (Drop type : dropTable.generate(new DropMetadata(caster, p)).getDrops()) {
					System.out.println("Droptable: " +type);
				}

				for (Drop type : newTable.generate(new DropMetadata(caster, p)).getDrops()) {
					System.out.println("Newtable " +type);
				}

				for (Drop type : newTable.generate(new DropMetadata(caster, p)).getDrops()) {
					MythicLineConfig subDrop = new MythicLineConfig(type.getLine());

                    getAttributes(subDrop);

					if (type instanceof IItemDrop) {
						ItemStack mythicItem = BukkitAdapter.adapt(((IItemDrop) type)
                                .getDrop(new DropMetadata(null, p)));
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
					} catch (Exception exc) {
						String error = "§c[ERROR] §eMythicLoot isn't able to create an item for " +
								item + "! Verify that you can get this item with in-game commands" +
								" like §b/mm i get "+item+"§e, as ML needs MM items to work for them to be dropped";
						Bukkit.getServer().getConsoleSender().sendMessage(error);
						Bukkit.getServer().getPlayer(player).sendMessage(error);
						return;
					}
	    		}
	    		drop(player, e, mythicItem);
	    	}
	    	if(stop) players.remove();
	    }
	}
	
	public void drop(String player, Entity e, ItemStack mythicItem) {

	    if(player==null||mythicItem==null) return;

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

    public void getAttributes(MythicLineConfig mlc) {
	    // Message that is displayed to the player
        msg = mlc.getString(new String[] { "message", "msg", "m"}, null);

        // Damage required for the damage to get the item
        dmg = mlc.getString(new String[] { "damage", "dmg", "d"}, "0");

        // Color of the drop if glow is active
        color = mlc.getString(new String[] { "color", "cl"}, "display");

        // Title to display to the player
        title = mlc.getString(new String[] { "title", "t"}, null);

        // Subtitle to display to the player
        subtitle = mlc.getString(new String[] { "subtitle", "st"}, null);

        // Command to execute
        command = mlc.getString(new String[] { "command", "cmd"}, null);

        // Sound to play
        sound = mlc.getString(new String[] { "sound", "sd"}, null);
        if(sound!=null) sound=sound.replace(".", "_").toUpperCase();

        // Message to broadcast
        broadcast = mlc.getString(new String[] { "broadcast", "bc", "b"}, null);

        // If the item should glow
        glow = mlc.getBoolean(new String[] { "glow", "g"}, true);

        // If the player should stop getting drops after this one
        stop = mlc.getBoolean(new String[] { "stop", "s"}, false);

        // If the item should do the explode effect
        explode = mlc.getBoolean(new String[] { "explode", "ex", "e"}, true);

        // If the item should go directly to the inventory
        toInv = mlc.getBoolean(new String[] { "toinv", "ti"}, false);

        // If the reward should be shared among all players
        shared = mlc.getBoolean(new String[] { "shared", "share", "sr"}, false);

        // Amount of the item to drop
        amount = mlc.getInteger(new String[] { "amount", "a"}, 1);

        // Position required to get the reward
        top = mlc.getString(new String[] { "top", "T"}, null);

        // The chance
        chance = mlc.getFloat(new String[] { "chance", "c"}, 1.0f);

        // Explosion height/offset
        expheight = mlc.getDouble(new String[] { "expheight", "exh"}, 0.6);
        expoffset = mlc.getDouble(new String[] { "expoffset", "exo"}, 0.2);
    }

    public boolean runConditions(double value, double HP, String player, LinkedList<Entry<String, Double>> topDamagers, int k) {

        if(shared) {
            // Applies the chance to shared, this means that the first time this runs, it will
            // throw the dice and if successful, it will mark all the shared as false
            if(chanceCondition(chance)) {
                shared = false;
                return false;
            }

            // Rolls the chance per player
            chance = 1.0f/topDamagers.size();
            if(k==topDamagers.size()) {
                chance = 1.0f;
            }
            if(chanceCondition(chance) && topCondition(player, topDamagers)) {
                shared = false;
                return true;
            }
            return false;
        }

        else {
            return damageCondition(value, HP) &&
            topCondition(player, topDamagers) &&
            chanceCondition(chance);
        }
    }

    public boolean damageCondition(double value, double HP) {
        if(dmg.contains("%")){
            if (dmg.contains("to")) {
                String[] values = dmg.replace("%", "").split("to");
                return Double.parseDouble(values[0]) <= value / HP * 100 &&
                        Double.parseDouble(values[1]) >= value / HP * 100;
            }
            else return value / HP * 100 >= Double.parseDouble(dmg.replace("%", ""));
        }
        else if (dmg.contains("to")) {
            String[] values = dmg.split("to");
            return Double.parseDouble(values[0]) <= value && Double.parseDouble(values[1]) >= value;
        }
        else return value >= Double.parseDouble(dmg);
    }

    public boolean topCondition(String player, LinkedList<Entry<String, Double>> topDamagers) {
        if(top!=null) {

            if (top.contains("to")) {
                String[] values = top.split("to");
                int i1 = Integer.parseInt(values[0]);
                int i2 = Integer.parseInt(values[1])+1;

                AtomicBoolean j = new AtomicBoolean(false);
                IntStream.range(i1, i2).forEachOrdered(n -> {
                        Entry<String, Double> a = topDamagers.get(n-1);
                        if(player.equals(a.getKey())) j.set(true);
                });
                return j.get();

            } else {
                int topR = Integer.parseInt(top);
                if(topDamagers.size()<topR) {
                    return false;
                } else {
                    Entry<String, Double> a = topDamagers.get(topR-1);
                    return player.equals(a.getKey());
                }
            }
        }
        return true;
    }

    public boolean chanceCondition(float chance) {
        double r = Math.random();
        return chance>=r;
    }

    public class tableHander extends Drop {

		String fileName;
		String line;

		public tableHander(String line, MythicLineConfig config, String fileName) {
			super(line, config);
			this.fileName = fileName;
			this.line = line;

		}

		public double getWeight() {
			return tableHander.getDrop(fileName, line).getWeight();
		}
	}


	public String cct(String msg, Player p) {
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
			return PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', msg));
		else
			return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
}
