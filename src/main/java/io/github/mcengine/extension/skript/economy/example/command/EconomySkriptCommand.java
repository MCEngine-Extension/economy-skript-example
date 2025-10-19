package io.github.mcengine.extension.skript.economy.example.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Handles the {@code /economyskriptexample} command logic.
 */
public class EconomySkriptCommand implements CommandExecutor {

    /**
     * Executes the {@code /economyskriptexample} command.
     *
     * @param sender  The source of the command.
     * @param command The command that was executed.
     * @param label   The alias used.
     * @param args    The command arguments.
     * @return {@code true} if the command was handled successfully.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("§aEconomySkript example command executed!");
        return true;
    }
}
