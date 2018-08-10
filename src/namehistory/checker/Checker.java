package namehistory.checker;

import org.bukkit.plugin.java.JavaPlugin;

import namehistory.checker.command.CheckPlayerCommand;
import namehistory.checker.command.MojangCommand;
import namehistory.checker.command.NameHistoryCommand;
import namehistory.checker.listener.Login;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;

import net.minecraft.util.com.google.gson.JsonElement;
import net.minecraft.util.com.google.gson.JsonObject;
import net.minecraft.util.com.google.gson.JsonParser;

public class Checker extends JavaPlugin {
	
	@Override
	public void onEnable() {
		getCommand("namehistory").setExecutor(new NameHistoryCommand());
		getCommand("mojang").setExecutor(new MojangCommand());
		getCommand("checkplayer").setExecutor(new CheckPlayerCommand());
		Bukkit.getPluginManager().registerEvents(new Login(), this);
	}
	
	public static boolean isPremium(String nickname) {
		try {
			HttpURLConnection connection = (HttpURLConnection)new URL("https://api.mojang.com/users/profiles/minecraft/" + nickname).openConnection();
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(new InputStreamReader((InputStream)connection.getContent()));
			JsonObject object = element.getAsJsonObject();
			String result = object.toString();
			if(result.contains("\"error\"")) {
				return false;
			} else if(result.contains("\"id\"")) {
				return true;
			}
		} catch(IOException exception) {}
		return false;
	}
}
