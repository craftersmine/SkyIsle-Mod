package craftersmine.skyisle;

import craftersmine.skyisle.commands.CommandIsland;
import craftersmine.skyisle.events.PlayerEventHandlers;
import craftersmine.skyisle.generation.IslandsWorldType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION, useMetadata = true)
public class Main
{
    public static final String MODID = "skyisle";
    public static final String NAME = "SkyIsle";
    public static final String VERSION = "1.0";

    public static Logger logger;

    public static IslandsWorldType islandsWorldType;
    public static Configuration islandsConfig;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        logger.info("Initializing SkyIsle mod...");
        logger.info("Registering Islands world type...");
        islandsWorldType = new IslandsWorldType();

        logger.debug("Registering Player event handlers");
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandlers());

    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent evt)
    {
        evt.registerServerCommand(new CommandIsland());
    }
}
