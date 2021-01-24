package changerls.exampleplugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.plugin.Plugin;

public class db {
	
	public static String DB_URL = null;
	public db(Plugin pl) {
		DB_URL = "jdbc:sqlite:/" + pl.getDataFolder().getAbsolutePath() + "/kills.db";
	}
	
	public static Connection connection;
	public static void connect() {
		try {
			Class.forName("org.sqlite.JDBC").newInstance();
			connection = DriverManager.getConnection(DB_URL);
			connection.createStatement().execute(
				  "CREATE TABLE IF NOT EXISTS `ocelot`("
				  + "`id` INTEGER PRIMARY KEY AUTOINCREMENT,"
				  + "`player` text(16) NOT NULL,"
				  + "`name` text(16) NOT NULL,"
				  + "`date` LONG NOT NULL"
				  + ")"
			);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean hasConnected() {
		try {
			return !connection.isClosed();
		}
		catch (Exception var1) {
			return false;
		}
	}

	public static void insert(String killer, String name, long date) {
		if (!hasConnected()) {
			connect();
		}
		try {
			connection.createStatement().execute("INSERT INTO `ocelot`(`player`,`name`,`date`) VALUES ('" + killer + "','" + name + "','" + date + "')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}