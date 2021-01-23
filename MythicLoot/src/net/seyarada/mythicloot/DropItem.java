package net.seyarada.mythicloot;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractPlayer;
import io.lumine.xikage.mythicmobs.drops.*;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import net.mystipvp.holobroadcast.holograms.HologramPlayer;
import net.mystipvp.holobroadcast.holograms.HologramPlayersManager;
import net.seyarada.mythicloot.events.Trackers;
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

public class DropItem  {
	
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
	private String command;
	private String holobroadcast;
	private String actionbar;
	private boolean glow;
	private boolean stop;
	private boolean explode;
	private boolean toInv;
	private boolean shared;
	private boolean legacy;
	private int amount;
	private int titleDuration;
	private int titleFade;
	private int XP;
	private int skip;
	private float chance;
	private float multiplier;
	private double expheight;
	private double expoffset;
	private double money;
	private DropTable dropTable;

	private final boolean debug = true;

	public void prepareDrop(MythicLineConfig mlc, Data data, double HP, Entity e, LinkedList<Entry<String, Double>> topDamagers) {
	    // Gets the item/part before the {}
		String item = mlc.getKey();

		// Gets the players / damage arranged in order
		Comparator<Entry<String, Double>> comparator = Entry.comparingByValue();
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

			if(debug) {
				System.out.println("#####");
				System.out.println("Running drop "+item+" for "+player+" with "+value+" damage.");
			}

			Integer toSkip = new Trackers().getSkip(Bukkit.getPlayer(player));
			if(toSkip!=null) {
				new Trackers().addSkip(Bukkit.getPlayer(player), toSkip-1);
				if(toSkip>0) {
					continue;
				}
			}

	    	k++;

	    	// Gets the attributes and runs the conditions
	    	getAttributes(mlc);
	    	boolean passedConditions = runConditions(value, HP, player, topDamagers, k);
			if(debug) System.out.println("Conditions result is "+passedConditions);
	    	if(!passedConditions)
	    	    continue;


			AbstractPlayer p = BukkitAdapter.adapt(Bukkit.getPlayer(player));
			AbstractEntity entity = BukkitAdapter.adapt(e);
			ActiveMob caster = MythicMobs.inst().getMobManager().getMythicMobInstance(entity);
	    	if(dropTable!=null) {
	    		LootBag loot = dropTable.generate(new DropMetadata(caster, p));

				for (Drop type : loot.getDrops()) {

					MythicLineConfig subDrop = new MythicLineConfig(type.getLine());

					if(!legacy)
                   		getAttributes(subDrop);
					else {
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
					}

					if (type instanceof IItemDrop) {
						ItemStack mythicItem = BukkitAdapter.adapt(((IItemDrop) type)
								.getDrop(new DropMetadata(null, p)));
						drop(player, e, mythicItem, null);
					}

					else if (type instanceof IIntangibleDrop) {
						((IIntangibleDrop) type).giveDrop(p, new DropMetadata(null, p));
					}

				}
	    	}
	    	else {
				Optional<MythicItem> mI = MythicMobs.inst().getItemManager().getItem(item);
				ItemStack mythicItem;
				if (mI.isPresent()) {
					mythicItem = BukkitAdapter.adapt(mI.get().generateItemStack(amount));
					drop(player, e, mythicItem, null);
				}

				else {
					try {
						mythicItem = new ItemStack(Material.valueOf(item.toUpperCase()), amount);
						drop(player, e, mythicItem, null);
					} catch (Exception exc) {
						String error = "§c[ERROR] §eMythicLoot isn't able to create an item for " +
								item + "! Verify that you can get this item with the in-game command" +
								" §b/mm i get " + item + "§e, as ML needs MM items to work for them to be dropped";
						Bukkit.getServer().getConsoleSender().sendMessage(error);
						Objects.requireNonNull(Bukkit.getServer().getPlayer(player)).sendMessage(error);
						return;
					}
				}
	    	}


			if(stop) {
				if(debug) System.out.println("Stop is true, removing "+player+" from getting more drops.");if(debug) System.out.println("Stop is true, removing "+player+" from getting more drops.");
				players.remove();
			}
	    }
	}
	
	public void drop(String player, Entity e, ItemStack mythicItem, String commandDrop) {


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

    		if(skip>0) {
				new Trackers().addSkip(p, skip);
			}

			if(commandDrop!=null) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandDrop);
			if(XP>0) p.giveExp(XP);
			if(sound!=null) p.playSound(p.getLocation(), Sound.valueOf(sound), 1, 1);
			if(title==null&&subtitle!=null)p.sendTitle("", cct(subtitle, p), titleFade, titleDuration, titleFade);
			else if(subtitle==null&&title!=null)p.sendTitle(cct(title, p), "", titleFade, titleDuration, titleFade);
			else if(title != null) p.sendTitle(cct(title, p), cct(subtitle, p), titleFade, titleDuration, titleFade);
			if(command!=null) Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
					command.replace("<player.name>", p.getName()));
			if(actionbar!=null)
				p.sendActionBar(TextComponent.fromLegacyText(cct(actionbar, p)));
		}

		if (holobroadcast != null && Bukkit.getServer().getPluginManager().getPlugin("HoloBroadcast") != null) {
			HologramPlayersManager manager = HologramPlayersManager.getInstance();
			HologramPlayer holoPlayer = manager.getHologramPlayerFromUUID(p.getUniqueId());
			holoPlayer.showHUD(holobroadcast, -1);
		}

    	if(money>0 && Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
			Economy economy = MythicLoot.getEconomy();
			economy.depositPlayer(p, money);
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
        color = mlc.getString(new String[] { "color", "colour", "cl"}, "display");

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

		// Message to broadcast
		holobroadcast = mlc.getString(new String[] { "holobroadcast", "holobc", "holob"}, null);

		// Message to display in the actionbar2
		actionbar = mlc.getString(new String[] { "actionbar", "ab"}, null);

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

		// If the legacy dropTable attributes should be used
		legacy = mlc.getBoolean("legacy", false);

        // Amount of the item to drop
        amount = ranged(mlc.getString(new String[] { "amount", "a"}, "1"));

		// Amount of money to give
		money = ranged(mlc.getString(new String[] { "money", "eco"}, "0"));

		// The amount of ticks the title is going to last
		titleDuration = mlc.getInteger(new String[] { "titleduration", "td"}, 40);

		// The amount of ticks for the title to fade in/out
		titleFade = mlc.getInteger(new String[] { "titlefade", "tf"}, 1);

		// The amount of XP to give
		XP = ranged(mlc.getString("xp", "0"));

		// The amount of lines to skip
		skip = mlc.getInteger("skip", 0);

        // Position required to get the reward
        top = mlc.getString(new String[] { "top", "T"}, null);

        // The chance
        chance = mlc.getFloat(new String[] { "chance", "c"}, 1.0f);

        // Multiplier, increases the chance depending of the damage done
		multiplier = mlc.getFloat(new String[]{"multiplier", "ml"}, -1f);

        // Explosion height/offset
        expheight = mlc.getDouble(new String[] { "expheight", "exh"}, 0.6);
        expoffset = mlc.getDouble(new String[] { "expoffset", "exo"}, 0.2);
    }

    public boolean runConditions(double value, double HP, String player, LinkedList<Entry<String, Double>> topDamagers, int k) {

        if(shared) {
            // Applies the chance to shared, this means that the first time this runs, it will
            // throw the dice and if successful, it will mark all the shared as false
            if(chanceCondition(chance, value, HP)) {
                shared = false;
                return false;
            }

            // Rolls the chance per player
            chance = 1.0f/topDamagers.size();
            if(k==topDamagers.size()) {
                chance = 1.0f;
            }
            if(chanceCondition(chance, value, HP) && topCondition(player, topDamagers)) {
                shared = false;
                return true;
            }
            return false;
        }

        else {
            return damageCondition(value, HP) &&
            topCondition(player, topDamagers) &&
            chanceCondition(chance, value, HP);
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
                	if(topDamagers.size()>=n) {
						Entry<String, Double> a = topDamagers.get(n - 1);
						if (player.equals(a.getKey())) j.set(true);
					}
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

    public boolean chanceCondition(float chance, double value, double HP) {
		if(multiplier>0) {
			float percentDamage = (float) value / (float) HP;
			chance = chance + multiplier * percentDamage;
		}

        double r = Math.random();
        return chance>=r;
    }

	public String cct(String msg, Player p) {
		msg = msg.replace("<money>", String.valueOf(money));
		if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
			return PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', msg));
		else
			return ChatColor.translateAlternateColorCodes('&', msg);
	}


	public int ranged(String i) {
		if(i.contains("to")) {

			int min = Integer.parseInt(i.split("to")[0]);
			int max = Integer.parseInt(i.split("to")[1]);

			return new Random().nextInt((max - min) + 1) + min;

		} else return Integer.parseInt(i);
	}

	/*
	# This is to be implemented for when MM fixes creating new dropTables with a mythicConfig

	public class tableHander extends Drop {

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
					}
					ct++;
				}

				System.out.println(mc.getStringList("Drops"));
				newTable = new DropTable(file, name+"temp", mc);
				System.out.println(newTable.hasDrops());

			}
		}

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
	*/

}
