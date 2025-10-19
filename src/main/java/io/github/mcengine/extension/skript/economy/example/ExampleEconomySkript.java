package io.github.mcengine.extension.skript.economy.example;

import io.github.mcengine.api.core.MCEngineCoreApi;
import io.github.mcengine.api.core.extension.logger.MCEngineExtensionLogger;
import io.github.mcengine.api.economy.extension.api.IMCEngineEconomyAPI;

import io.github.mcengine.extension.skript.economy.example.command.EconomySkriptCommand;
import io.github.mcengine.extension.skript.economy.example.listener.EconomySkriptListener;
import io.github.mcengine.extension.skript.economy.example.tabcompleter.EconomySkriptTabCompleter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;

/**
 * Main class for the Economy <b>Skript</b> example module.
 * <p>
 * Registers the {@code /economyskriptexample} command and related event listeners.
 * <p>
 * Migrated from the previous “API” naming to “Skript” while preserving
 * {@link IMCEngineEconomyAPI} integration for compatibility.
 */
public class ExampleEconomySkript implements IMCEngineEconomyAPI {

    /** Custom extension logger for this module, with contextual labeling. */
    private MCEngineExtensionLogger logger;

    /**
     * Initializes the Economy Skript example module.
     * Called automatically by the MCEngine core plugin.
     *
     * @param plugin The Bukkit plugin instance.
     */
    @Override
    public void onLoad(Plugin plugin) {
        // Initialize contextual logger once and keep it for later use.
        this.logger = new MCEngineExtensionLogger(plugin, "Skript", "EconomyExampleSkript");

        try {
            // Register event listener
            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.registerEvents(new EconomySkriptListener(plugin, this.logger), plugin);

            // Reflectively access Bukkit's CommandMap
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            // Define the /economyskriptexample command
            Command economySkriptExampleCommand = new Command("economyskriptexample") {

                /** Handles command execution for /economyskriptexample. */
                private final EconomySkriptCommand handler = new EconomySkriptCommand();

                /** Handles tab-completion for /economyskriptexample. */
                private final EconomySkriptTabCompleter completer = new EconomySkriptTabCompleter();

                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    return handler.onCommand(sender, this, label, args);
                }

                @Override
                public java.util.List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                    return completer.onTabComplete(sender, this, alias, args);
                }
            };

            economySkriptExampleCommand.setDescription("Economy Skript example command.");
            economySkriptExampleCommand.setUsage("/economyskriptexample");

            // Dynamically register the /economyskriptexample command
            commandMap.register(plugin.getName().toLowerCase(), economySkriptExampleCommand);

            this.logger.info("Enabled successfully.");
        } catch (Exception e) {
            this.logger.warning("Failed to initialize ExampleEconomySkript: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Called when the Economy Skript example module is disabled/unloaded.
     *
     * @param plugin The Bukkit plugin instance.
     */
    @Override
    public void onDisload(Plugin plugin) {
        if (this.logger != null) {
            this.logger.info("Disabled.");
        }
    }

    /**
     * Sets the unique ID for this module.
     *
     * @param id the assigned identifier (ignored; a fixed ID is used for consistency)
     */
    @Override
    public void setId(String id) {
        MCEngineCoreApi.setId("mcengine-economy-skript-example");
    }
}
