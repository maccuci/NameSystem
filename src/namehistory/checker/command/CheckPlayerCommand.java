package namehistory.checker.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import namehistory.checker.Checker;

public class CheckPlayerCommand implements CommandExecutor {

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		if (sender instanceof Player) {
			if (args.length == 0) {
				player.sendMessage("§cUsage: /checkplayer <player>");
				return true;
			}
			if (args.length == 1) {
				Player target = Bukkit.getPlayer(args[0]);
				if (target == null) {
					OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
					if (!Checker.isPremium(offlinePlayer.getName())) {
						player.sendMessage("§cO jogador '" + args[0] + "' não é original!");
					} else {
						player.sendMessage("§aO jogador '" + args[0] +"' é original");
						return true;
					}
				}
				if (target != null) {
					if (!Checker.isPremium(target.getName())) {
						player.sendMessage("§cO jogador '" + args[0] + "' não é original!");
					} else {
						player.sendMessage("§aO jogador '" + args[0] +"' é original");
						return true;
					}
				}
			}
		}
		return false;
	}

}
