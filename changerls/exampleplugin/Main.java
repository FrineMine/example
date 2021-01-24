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

	String[] arrsymb = "0123456789абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwoyzABCDEFGHIJKLMNOPQRSTUVWOYZ".split("");
	Ocelot ocelot;
	
	public void onEnable() {
		File dir = new File(getDataFolder().getAbsolutePath());
		if (!dir.exists()) {
			dir.mkdir();
		}
		//Регистрация нового обработчика событий
		Bukkit.getPluginManager().registerEvents(this, this);
		new db(this);
	}
	
	@EventHandler
	public void DeathEntity(EntityDeathEvent e) {
		//проверка на убийство игроком
		Player p = (Player) e.getEntity().getKiller();
		if (p == null) return;
		
		if (e.getEntity() instanceof Zombie) {
		//проверка на убийство зомби
			
			//1. Спавн оцелота при убийстве зомби игроком
			Location loc = e.getEntity().getLocation();
			ocelot = (Ocelot) loc.getWorld().spawnEntity(loc, EntityType.OCELOT);
			
			//2. Случайное имя оцелота
			String name = "";
			for (int i = 0; i < 5; i++) name = name + arrsymb[(int) (Math.random() * arrsymb.length)];
			ocelot.setCustomName(name);
			ocelot.setCustomNameVisible(true);
			
			//4. Атака игрока оцелотом
			ocelot.setTarget(p);
			
		  //ocelot = null, если до этого не убивали, однако e.getEntity()
		  //не может быть null, поэтому я не стал заморачиваться над проверкой сущности на оцелота
		} else if (e.getEntity() == ocelot) {
		  //проверка на убийство данного оцелота
			String killer = p.getName();
			String name = ocelot.getCustomName();
			long date = new Date().getTime();
			
			//Заносим в базу данных ник игрока, имя оцелота и время совершения убийства.
			db.insert(killer, name, date);
			
			//Дроп только кожи с индивидуальным отображением ника
			e.getDrops().clear();
			ItemStack is = new ItemStack(Material.LEATHER);
			DropItemWithCustomName(p, is, ocelot.getLocation());
		}
	}

	private void DropItemWithCustomName(Player p, ItemStack is, Location loc) {
		Item i = loc.getWorld().dropItem(loc, is);
		//обработка пакета
		//...
		//...
		//...
	}
}
