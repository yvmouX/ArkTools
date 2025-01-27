package cn.yvmou.arktools;

import cn.yvmou.arktools.listeners.PlayerDeathListener;
import cn.yvmou.arktools.listeners.PlayerOffHandListener;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public final class ArkTools extends JavaPlugin implements Listener {
    public final static String VERSION = "1.0.0";
    public FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        // 如果配置文件不存在，Bukkit 会保存默认的配置
        saveDefaultConfig();
        // 注册命令执行器和补全器
        CommandManager commandManager = new CommandManager(this);
        Objects.requireNonNull(getCommand("arktools")).setExecutor(commandManager);
        Objects.requireNonNull(getCommand("arktools")).setTabCompleter(commandManager);
        // 注册事件监听器
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerOffHandListener(this), this);

        Bukkit.getConsoleSender().sendMessage("ArkTools 已启用");
    }



    @Override
    public void onDisable() {

        Bukkit.getConsoleSender().sendMessage("ArkTools 已关闭");
    }

    // 玩家加入事件
    @EventHandler
    public void onAdminLogin(PlayerJoinEvent event) {
        var player = event.getPlayer();
        if (player.isOp()) {
            player.sendMessage("");
            player.sendMessage("[ArkTools] §a当前插件版本为" + VERSION);
            player.sendMessage("[ArkTools] §a配置文件版本为" + config.getString("version"));
            player.sendMessage("[ArkTools] §a若二者版本不同请手动删除配置文件后重启服务器");
            player.sendMessage("");
        }
    }
}


