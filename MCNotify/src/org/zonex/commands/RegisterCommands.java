package org.zonex.commands;

import org.bukkit.plugin.java.JavaPlugin;
import org.zonex.commands.multipartcommand.MultiPartCommandManager;
import org.zonex.commands.watch.UnwatchCommandHandler;
import org.zonex.commands.watch.WatchCommandHandler;
import org.zonex.commands.zone.ZoneHandler;
import org.zonex.commands.zx.ZxCommandHandler;
import org.zonex.particles.ParticleManager;

public class RegisterCommands {

    public RegisterCommands() {
        new ZxCommandHandler();
        new ZoneHandler();
        new UnwatchCommandHandler();
        new WatchCommandHandler();
    }

    public static MultiPartCommandManager mutiPartManager = new MultiPartCommandManager();
    public static ParticleManager particleManager = new ParticleManager();

}
