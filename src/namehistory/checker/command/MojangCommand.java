package namehistory.checker.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.util.com.google.gson.JsonArray;
import net.minecraft.util.com.google.gson.JsonElement;
import net.minecraft.util.com.google.gson.JsonObject;
import net.minecraft.util.com.google.gson.JsonParser;

public class MojangCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player)sender;
		if(sender instanceof Player) {
			if(args.length == 0) {
				player.sendMessage("§aVerificando serviços online da §eMOJANG");
				try {
					HttpURLConnection connection = (HttpURLConnection)new URL("https://status.mojang.com/check").openConnection();
					JsonParser parser = new JsonParser();
					JsonElement element = parser.parse(new InputStreamReader((InputStream)connection.getContent()));
					JsonArray array = element.getAsJsonArray();
					for(int i = 0; i < array.size(); i++) {
						JsonObject object = array.get(i).getAsJsonObject();
						String formatted = object.toString().replace("{", "").replace("}", "").replace("\"", "");
						sender.sendMessage(ChatColor.GREEN + "Serviço: " + ChatColor.YELLOW + formatted.split(":")[0] + ChatColor.GRAY + " | " + ChatColor.GREEN + "Status: " + ChatColor.valueOf(String.valueOf(formatted.split(":")[1].equals("yellow") ? "gold" : formatted.split(":")[1]).toUpperCase()) + ChatColor.BOLD + formatted.split(":")[1].toUpperCase().replace("GREEN", "ATIVADO"));
					}
				} catch(IOException exception) {
					exception.printStackTrace();
					sender.sendMessage(ChatColor.RED + "Não foi possível se comunicar com a Mojang.");
				}
				return true;
			}
		}
		return false;
	}

}
