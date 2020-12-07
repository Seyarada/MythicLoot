package net.seyarada.mythicloot.nms;

import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;

import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityDestroy;

public class V1_13_R2 {
	
	public void removeNBT(EntityPickupItemEvent e) {
		ItemStack removeNBT = CraftItemStack.asNMSCopy(e.getItem().getItemStack());
	    removeNBT.c("mythicloot");
	    e.getItem().remove();
	    e.setCancelled(true);
	    ((Player)e.getEntity()).getInventory().addItem(CraftItemStack.asBukkitCopy(removeNBT));
	}
	
	public void destroyEntity(Item item, Entity entity) {
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(item.getEntityId());
		((CraftPlayer) ((Player)entity)).getHandle().playerConnection.sendPacket(packet);
	}
	
}
