package org.zonex.commands;

import org.bukkit.plugin.java.JavaPlugin;
import org.zonex.commands.multipartcommand.MultiPartCommandManager;
import org.zonex.commands.watch.UnwatchCommandHandler;
import org.zonex.commands.watch.WatchCommandHandler;
import org.zonex.commands.zone.ZoneHandler;
import org.zonex.commands.zx.ZxCommandHandler;
import org.zonex.particles.ParticleManager;

/**
 * Registers commands. Used to de-clutter the onEnable main method.
 */
public class RegisterCommands {

    public RegisterCommands() {
        new ZxCommandHandler();
        new ZoneHandler();
        new UnwatchCommandHandler();
        new WatchCommandHandler();
    }

    // Manages multi-part commands like building an area
    public static MultiPartCommandManager mutiPartManager = new MultiPartCommandManager();

    // Manages particle systems
    public static ParticleManager particleManager = new ParticleManager();

}
