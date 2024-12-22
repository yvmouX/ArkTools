package cn.yvmou.arktools.commands;

import cn.yvmou.arktools.ArkTools;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WorldBorderCommand implements CommandExecutor, TabCompleter {

    private final ArkTools plugin;

    public WorldBorderCommand(ArkTools plugin) {
        this.plugin = plugin;
    }

    // 执行命令
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1) {
            String subcommand = args[0];
            return switch (subcommand) {
                case "help" -> {
                    if (sender.hasPermission("arktools.help")) {
                        sendHelpMessage(sender);
                    } else {
                        sender.sendMessage(Component.text("你没有权限执行此命令.", NamedTextColor.RED));
                    }
                    yield true;
                }
                case "reload" -> {
                    if (sender.hasPermission("arktools.reload")) {
                        plugin.reloadConfig();
                        reloadWorldBorders();
                        sender.sendMessage(Component.text("配置文件已重新加载.", NamedTextColor.GREEN));
                    } else {
                        sender.sendMessage(Component.text("你没有权限执行此命令.", NamedTextColor.RED));
                    }
                    yield true;
                }
                case "setworldborder" -> {
                    if (sender.hasPermission("arktools.setworldborder")) {
                        setWorldBorderCommand(sender, args);
                    } else {
                        sender.sendMessage(Component.text("你没有权限执行此命令.", NamedTextColor.RED));
                    }
                    yield true;
                }
                default -> {
                    sender.sendMessage(Component.text("未知子命令.", NamedTextColor.RED));
                    yield false;
                }
            };
        } else {
            sendHelpMessage(sender);
        }
        return false;
    }

    // 重载所有世界边界
    private void reloadWorldBorders() {
        for (World world : Bukkit.getWorlds()) {
            double centerX = plugin.getConfig().getDouble("world-border." + world.getName() + ".centerX");
            double centerZ = plugin.getConfig().getDouble("world-border." + world.getName() + ".centerZ");
            double size = plugin.getConfig().getDouble("world-border." + world.getName() + ".size");
            world.getWorldBorder().setCenter(centerX, centerZ);
            world.getWorldBorder().setSize(size);
        }
    }

    // 调整世界边界
    private void setWorldBorderCommand(CommandSender sender, String[] args) {
        if (args.length != 5) {
            sender.sendMessage(Component.text("参数错误", NamedTextColor.RED));
            return;
        }
        try {
            String worldName = args[1];
            double centerX = Double.parseDouble(args[2]);
            double centerZ = Double.parseDouble(args[3]);
            double size = Double.parseDouble(args[4]);

            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                world.getWorldBorder().setCenter(centerX, centerZ);
                world.getWorldBorder().setSize(size);
                sender.sendMessage(Component.text("世界边界已更新", NamedTextColor.GREEN));
            } else {
                sender.sendMessage(Component.text("未找到世界", NamedTextColor.RED));
            }

            plugin.getConfig().set("world-border." + worldName + ".centerX", centerX);
            plugin.getConfig().set("world-border." + worldName + ".centerZ", centerZ);
            plugin.getConfig().set("world-border." + worldName + ".size", size);
            plugin.saveConfig();

        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("参数错误", NamedTextColor.RED));
        }
    }

    // 命令补全
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("arktools")) {
            if (args.length == 1) {
                return List.of("setworldborder", "reload", "help");
            } else if (args[0].equalsIgnoreCase("setworldborder")) {
                return switch (args.length) {
                    case 2 -> Bukkit.getWorlds().stream().map(World::getName).toList();
                    case 3 -> List.of("<centerX>");
                    case 4 -> List.of("<centerZ>");
                    case 5 -> List.of("<size>");
                    default -> List.of();
                };
            }
        }
        return List.of();
    }

    // 帮助信息
    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(Component.text("ArkTools 帮助", NamedTextColor.GOLD));
        sender.sendMessage(Component.text("-------------------", NamedTextColor.WHITE));
        sender.sendMessage(Component.text("/arktools setworldborder", NamedTextColor.WHITE).append(Component.text(" 设置世界边界", NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("/arktools reload", NamedTextColor.WHITE).append(Component.text(" 重载插件", NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("/arktools help", NamedTextColor.WHITE).append(Component.text(" 查看帮助", NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("-------------------", NamedTextColor.WHITE));
    }
}