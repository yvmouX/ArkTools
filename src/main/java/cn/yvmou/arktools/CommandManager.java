package cn.yvmou.arktools;

import cn.yvmou.arktools.commands.*;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager implements CommandExecutor, TabCompleter {
    private final ArkTools plugin;
    private final Map<String, CommandExecutor> commands = new HashMap<>();

    public CommandManager(ArkTools plugin) {
        this.plugin = plugin;
        registerCommands();
    }

    private void registerCommands() {
        commands.put("help", new HelpCommand());
        commands.put("reload", new ReloadCommand(plugin));
        commands.put("setworldborder", new WorldBorderCommand(plugin));
        commands.put("deathpunish", new DeathPunishCommand(plugin));
        commands.put("hideonmap", new GiveItemCommand(plugin));
    }

    // 执行命令
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            CommandExecutor executor = commands.get(args[0].toLowerCase());
            if (executor != null) {
                return executor.onCommand(sender, command, label, args);
            } else {
                sender.sendMessage("未知子命令.");
                return false;
            }
        } else {
            new HelpCommand().onCommand(sender, command, label, args);
        }
        return false;
    }

    // Tab补全
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("setworldborder", "deathpunish", "reload", "help", "hideonmap");
        } else if (args[0].equalsIgnoreCase("setworldborder")) {
            return getWorldBorderTabComplete(args);
        } else if (args[0].equalsIgnoreCase("deathpunish")) {
            return getDeathPunishTabComplete(args);
        } else if (args[0].equalsIgnoreCase("hideonmap")) {
            return List.of("get");
        }
        return List.of();
    }

    // 获取世界边界Tab补全
    private List<String> getWorldBorderTabComplete(String[] args) {
        return switch (args.length) {
            case 2 -> plugin.getServer().getWorlds().stream().map(World::getName).toList();
            case 3 -> List.of("<centerX>");
            case 4 -> List.of("<centerZ>");
            case 5 -> List.of("<size>");
            default -> List.of();
        };
    }
    // 获取死亡惩罚Tab补全
    private List<String> getDeathPunishTabComplete(String[] args) {
        return switch (args.length) {
            case 2 -> List.of("setmaxhealth", "add", "get");
            case 3 -> plugin.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
            case 4 -> List.of(args[1].equalsIgnoreCase("setmaxhealth") ? "<maxHealth>" : "<addHealth>");
            default -> List.of();
        };
    }
}