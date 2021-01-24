package changerls.exampleplugin;

import java.io.File;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	String[] arrsymb = "0123456789abcdefghijklmnopqrstuvwoyzABCDEFGHIJKLMNOPQRSTUVWOYZ".split("");
	Ocelot ocelot;
	
	public void onEnable() {
		File dir = new File(getDataFolder().getAbsolutePath());
		if (!dir.exists()) {
			dir.mkdir();
		}
		Bukkit.getPluginManager().registerEvents(this, this);
		new db(this);
	}
	
	@EventHandler
	public void DeathEntity(EntityDeathEvent e) {
		Player p = (Player) e.getEntity().getKiller();
		if (p == null) return;
		
		if (e.getEntity() instanceof Zombie) {
			Location loc = e.getEntity().getLocation();
			ocelot = (Ocelot) loc.getWorld().spawnEntity(loc, EntityType.OCELOT);
			
			String name = "";
			for (int i = 0; i < 5; i++) name = name + arrsymb[(int) (Math.random() * arrsymb.length)];
			ocelot.setCustomName(name);
			ocelot.setCustomNameVisible(true);
			
			ocelot.setTarget(p);
		} else if (e.getEntity() == ocelot) {
			String killer = p.getName();
			String name = ocelot.getCustomName();
			long date = new Date().getTime();
			
			db.insert(killer, name, date);
			
			e.getDrops().clear();
			ItemStack is = new ItemStack(Material.LEATHER);
			DropItemWithCustomName(p, is, ocelot.getLocation());
		}
	}

	private void DropItemWithCustomName(Player p, ItemStack is, Location loc) {
		Item i = loc.getWorld().dropItem(loc, is);
		//stop coding
	}
}
