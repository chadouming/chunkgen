package com.gecgooden.chunkgen.handlers;

import com.gecgooden.chunkgen.reference.Reference;
import com.gecgooden.chunkgen.util.Utilities;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickHandler {

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event) {

		final World world = MinecraftServer.getServer().getEntityWorld();
		if (Reference.pauseForPlayers && world.playerEntities.size() > 0) return;
		
		if(Reference.toGenerate){
			for(char i = 0; i < Reference.numChunksPerTick; i++){
				if(Reference.startX < Reference.stopX){
					if(Reference.startZ < Reference.stopZ){
						Utilities.generateChunk(Reference.startX, Reference.startZ, Reference.dimID);
						Reference.startX++;
					} else 
						Reference.toGenerate = false;
				} else {
					Reference.startZ++;
					Reference.startX = (Reference.x - Reference.width/2); 
				}
			}
			Reference.logger.info("Generated chunk batch");
			ConfigurationHandler.updateConfigs();
			//reimplement % of completion
		}
	}
}
