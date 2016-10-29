package cn.mccraft.nukkit.JoinMessage;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.level.Level;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.plugin.PluginBase;

import java.util.*;

public final class JoinMessage extends PluginBase implements Listener {
    public static Random rand = new Random();
    private List<String> JOINMESSAGE = new ArrayList<String>();
    private List<String> FISTJOINMESSAGE = new ArrayList<String>();
    private List<String> LEAVEMESSAGE = new ArrayList<String>();
    private HashMap<String, String> SPECALJOINMESSAGE = new HashMap<String, String>();
    private HashMap<String, String> SPECALLEAVEMESSAGE = new HashMap<String, String>();
    private HashMap<String, Integer> EFFECT = new HashMap<String, Integer>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        load();
        getServer().getPluginManager().registerEvents(this, this);
        if (getServer().getLanguage().getLang().equalsIgnoreCase("chs"))
            getLogger().info("加载成功");
        else
            getLogger().info("Loaded Successfully");
    }

    public void load() {
        if (!getConfig().exists("version")) {
            getConfig().set("Version", 1);
        }
        JOINMESSAGE = getConfig().getList("JoinMessage");
        FISTJOINMESSAGE = getConfig().getList("FirstJoinMessage");
        LEAVEMESSAGE = getConfig().getList("LeaveMessage");
        SPECALJOINMESSAGE = (HashMap<String, String>) getConfig().get("SpecialJoinMessage");
        SPECALLEAVEMESSAGE = (HashMap<String, String>) getConfig().get("SpecialLeaveMessage");

        getConfig().getMapList("Effect");

//        List<String> a = getConfig().getStringList("EffectAble");
//        for (String s : a) {
//            if (getConfig().exists("Effect." + s)) {
//                if (getConfig().exists("Effect." + s)) {
//                    Object par = getConfig().get("Effect." + s);
//                    if (par instanceof Integer) {
//                        EFFECT.put(s)
//                    } else if (par instanceof String) {
//
//                    }
//                }
//            }
//        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((sender.isOp() || sender.hasPermission("joinmessage.reload")) && (label.equalsIgnoreCase("jm") || label.equalsIgnoreCase("joinmessage"))) {
            load();
        } else {
            if (getServer().getLanguage().getLang().equalsIgnoreCase("chs")) {
                sender.sendMessage("&3你没有权限进行该操作");
            }
            sender.sendMessage("&3You haven't permission to do that");
        }
        return true;
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        for (String permission : SPECALJOINMESSAGE.keySet()) {
            if (event.getPlayer().hasPermission(permission)) {
                event.setJoinMessage(SPECALJOINMESSAGE.get(permission).replaceAll("<player>", event.getPlayer().getName()).replaceAll("&", "§"));
                return;
            }
        }
        if (!event.getPlayer().hasPlayedBefore()) {
            String f = FISTJOINMESSAGE.get(rand.nextInt(FISTJOINMESSAGE.size())).replaceAll("<player>", event.getPlayer().getName()).replaceAll("&", "§");
            event.setJoinMessage(f);

            return;
        }
        String j = JOINMESSAGE.get(rand.nextInt(JOINMESSAGE.size())).replaceAll("<player>", event.getPlayer().getName()).replaceAll("&", "§");
        event.setJoinMessage(j);
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        for (String permission : SPECALLEAVEMESSAGE.keySet()) {
            if (event.getPlayer().hasPermission(permission)) {
                event.setQuitMessage(SPECALLEAVEMESSAGE.get(permission).replaceAll("<player>", event.getPlayer().getName()).replaceAll("&", "§"));
                return;
            }
        }
        String l = LEAVEMESSAGE.get(rand.nextInt(LEAVEMESSAGE.size())).replaceAll("<player>", event.getPlayer().getName()).replaceAll("&", "§");
        event.setQuitMessage(l);
    }
}
