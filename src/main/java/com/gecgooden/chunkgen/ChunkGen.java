package com.gecgooden.chunkgen;

import net.minecraft.network.INetworkManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod.VersionCheckHandler;

@Mod(modid = ChunkGen.MODID, version = ChunkGen.VERSION)
public class ChunkGen
{
    public static final String MODID = "chunkgen";
    public static final String VERSION = "1.7.10-1.0";
    
    /**
     * Makes the mod server side only
     * @param map
     * @param side
     * @return Always true.
     */
    @VersionCheckHandler
    public boolean networkCheckHandler(String string, INetworkManager networkManager) { 
    	return true;
    }
    
    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
      event.registerServerCommand(new ChunkGenCommand());
    }
}
