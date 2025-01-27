package cn.yvmou.arktools.listeners;

import cn.yvmou.arktools.ArkTools;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import static cn.yvmou.arktools.ArkTools.VERSION;


public class PlayerDeathListener implements Listener {

    // 插件实例
    private final ArkTools plugin;

    public PlayerDeathListener(ArkTools plugin) {
        this.plugin = plugin;
    }


    // 玩家死亡事件
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        var player = event.getEntity();


        AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH); // 获取玩家的最大生命值属性实例

        if (!player.hasPermission("arktools.deathpunish.bypass")) {

            player.sendMessage(Objects.requireNonNull(plugin.getConfig().getString("punishments.deathMsg")));

            if (plugin.getConfig().getBoolean("punishments.enable")) {

                if (maxHealthAttribute != null) {
                    double nowMaxHealth = maxHealthAttribute.getValue(); // 获取当前值的最大生命值
                    //player.sendMessage("当前最大生命值: " + nowMaxHealth); // 调试信息

                    // 如果玩家生命上限小于6，不扣除生命上限
                    // 减少玩家生命上限
                    if (plugin.getConfig().getBoolean("punishments.reduceMaxHealth")) {
                        // 获取配置文件玩家最小生命值
                        double minHealth = plugin.getConfig().getDouble("punishments.minHealth");
                        double reduceHealthAmount = plugin.getConfig().getDouble("punishments.reduceHealthAmount");
                        double newMaxHealth = Math.max(nowMaxHealth - reduceHealthAmount, minHealth); // 最小值为1.0
                        maxHealthAttribute.setBaseValue(newMaxHealth); // 设置玩家的新最大生命值
                        //player.sendMessage("新的最大生命值: " + newMaxHealth); // 调试信息

                        // 玩家生命值上限为1时封禁
                        if (plugin.getConfig().getBoolean("punishments.banOnDeath") && newMaxHealth == 1) {
                            player.ban(plugin.getConfig().getString("banReason"), (Date) null, "null");
                            player.sendMessage("你已被封禁"); // 调试信息
                        }
                    }
                    try {
                        // 清除玩家背包
                        if (plugin.getConfig().getBoolean("punishments.clearInventory")) {
                            player.getInventory().clear();
                        }
                        // 重置玩家经验
                        if (plugin.getConfig().getBoolean("punishments.resetExp")) {
                            player.setLevel(0);
                            player.setTotalExperience(0);
                        }
                    } catch (Exception e) {
                        Bukkit.getConsoleSender().sendMessage("清除失败");
                    }

                }
            } else {
                player.sendMessage(Objects.requireNonNull(plugin.getConfig().getString("punishments.bypassPunishMsg")));
            }
        }
    }










}
