package namehistory.checker.command;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.util.com.google.gson.JsonArray;
import net.minecraft.util.com.google.gson.JsonElement;
import net.minecraft.util.com.google.gson.JsonObject;
import net.minecraft.util.com.google.gson.JsonParser;

import namehistory.checker.Checker;

public class NameHistoryCommand implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player)sender;
		if(sender instanceof Player) {
			if(args.length == 0) {
				player.sendMessage("§cUsage: /namehistory <player>");
				return true;
			}
			if(args.length == 1) {
				if(!Checker.isPremium(args[0])) {
					player.sendMessage("§cEste jogador não é original!");
					return true;
				}
				UUID uuid = null;
				Player target = Bukkit.getPlayer(args[0]);
				if(target != null) {
					uuid = target.getUniqueId();
				} else {
					uuid = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
				}
				sender.sendMessage("§aCarregando informações de §e" + args[0]);
				try {
					HttpURLConnection connection = (HttpURLConnection)new URL("https://api.mojang.com/user/profiles/" + uuid.toString().replaceAll("-", "") + "/names").openConnection();
					JsonParser parser = new JsonParser();
					JsonElement element = parser.parse(new InputStreamReader((InputStream)connection.getContent()));
					JsonArray array = element.getAsJsonArray();
					for(int i = 0; i < array.size(); i++) {
						JsonObject object = array.get(i).getAsJsonObject();
						if(i == 0) {
							sender.sendMessage(ChatColor.GREEN + "Nickname original: " + ChatColor.YELLOW + object.get("name").getAsString());
							if(array.size() == 1) {
								sender.sendMessage(ChatColor.RED + "Este usuário nunca alterou o seu nickname.");
								return true;
							}
						} else {
							String date = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(new Date(new Timestamp(Long.valueOf(object.get("changedToAt").getAsString())).getTime()));
							sender.sendMessage(ChatColor.GREEN + "Variação: " + ChatColor.YELLOW + object.get("name").getAsString() + ChatColor.GRAY + " | " + ChatColor.GREEN + "Data: " + ChatColor.YELLOW + date);
						}
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
