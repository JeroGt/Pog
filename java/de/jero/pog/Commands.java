package de.jero.pog;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player p = (Player) sender;
        if(command.getName().equalsIgnoreCase("gm")) {
            if(p.hasPermission("gm.*")) {
                if(args.length > 0) {
                    if(Integer.valueOf(args[0]) == 0) {
                        p.setGameMode(GameMode.SURVIVAL);
                    }
                    if(Integer.valueOf(args[0]) == 1) {
                        p.setGameMode(GameMode.CREATIVE);
                    }
                    if(Integer.valueOf(args[0]) == 2) {
                        p.setGameMode(GameMode.ADVENTURE);
                    }
                    if(Integer.valueOf(args[0]) == 3) {
                        p.setGameMode(GameMode.SPECTATOR);
                    }
                }
            }
        }

        return false;
    }
}
