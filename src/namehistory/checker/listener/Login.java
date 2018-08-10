package namehistory.checker.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import namehistory.checker.Checker;

public class Login implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if(Checker.isPremium(player.getName()) /*&& Checker.isPremium(player.getUniqueId().toString())*/) {
			player.sendMessage("§aVocê é um jogador original!");
		} else {
			player.sendMessage("§cVocê é um jogador pirata!");
		}
	}

}
