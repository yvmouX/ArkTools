package cn.yvmou.arktools.commands;

import cn.yvmou.arktools.ArkTools;
import cn.yvmou.arktools.utils.MessageUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GiveItemCommand implements CommandExecutor {
    private final ArkTools plugin;

    public GiveItemCommand(ArkTools plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            if (args[0].equals("hideonmap")) {
                if (sender.hasPermission("arktools.hideonmap")) {
                    getItemCommang(sender, args);
                    return true;
                } else {
                    sender.sendMessage("你没有权限执行此命令.");
                    return false;
                }
            } else {
                sender.sendMessage("未知子命令.");
                return false;
            }
        } else {
            MessageUtil.sendHelpMessage(sender);
            return false;
        }
    }

    // 获取物品命令
    private void getItemCommang(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            switch (args[1]) {
                case "get" -> {
                    if (args.length == 2) {
                        getCompass(sender, args);
                    } else {
                        sender.sendMessage("参数错误");
                    }
                }
                default -> sender.sendMessage("未知子命令.");
            }
        } else {
            MessageUtil.sendHelpMessage(sender);
        }
    }

    private void getCompass(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack item = new ItemStack(Material.valueOf(plugin.getConfig().getString("hideOnMap.Item.material")));
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(plugin.getConfig().getString("hideOnMap.Item.name"));
            item.setItemMeta(meta);

            player.getInventory().addItem(item);
        } else {
            sender.sendMessage("这个命令只能玩家使用");
        }

    }
}