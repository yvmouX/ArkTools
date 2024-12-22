package cn.yvmou.arktools;

import cn.yvmou.arktools.commands.WorldBorderCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;


public final class ArkTools extends JavaPlugin {

    @Override
    public void onEnable() {

        // 如果配置文件不存在，Bukkit 会保存默认的配置
        saveDefaultConfig();
        // 注册命令执行器
        Objects.requireNonNull(getCommand("arktools")).setExecutor(new WorldBorderCommand(this));
        // 注册命令补全器
        Objects.requireNonNull(getCommand("arktools")).setTabCompleter(new WorldBorderCommand(this));

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

