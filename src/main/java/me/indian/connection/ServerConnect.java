package me.indian.connection;

import cn.nukkit.plugin.PluginBase;

public class ServerConnect extends PluginBase {


    @Override
    public void onEnable() {

        getServer().getCommandMap().register("", new CommandT("Test"));

    }

    @Override
    public void onDisable() {

    }
}