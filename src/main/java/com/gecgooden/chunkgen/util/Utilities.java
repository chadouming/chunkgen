package com.gecgooden.chunkgen.util;

import com.gecgooden.chunkgen.reference.Reference;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.storage.RegionFileCache;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.DimensionManager;

public class Utilities {

	public static void generateChunks(int x, int z, int width, int height, int dimensionID) {
		for(int i = (x - width/2); i < (x + width/2); i++) {
			for(int j = (z - height/2); j < (z + height/2); j++) {
				generateChunk(i, j, dimensionID);
			}
		}
	}

	private static boolean chunksExist(int x, int z, int dimensionID) {
		WorldServer world = null;

		world = DimensionManager.getWorld(dimensionID);

		return RegionFileCache.createOrLoadRegionFile(world.getChunkSaveLocation(), x, z).chunkExists(x & 0x1F, z & 0x1F);
	}

	public static void generateChunk(int x, int z, int dimensionID) {
		ChunkProviderServer cps = MinecraftServer.getServer().worldServerForDimension(dimensionID).theChunkProviderServer;

		if(cps.getLoadedChunkCount() > 1000) {
			Reference.logger.info("Too many chunks are loaded, unloading some of them.");
			Reference.logger.info("Chunks loaded : " + cps.getLoadedChunkCount());
			
			//Unload all chunks that are marked to be unloaded
			cps.unloadQueuedChunks();

			Reference.logger.info("Chunks left : " + cps.getLoadedChunkCount());
		}

		if(!chunksExist(x, z, dimensionID)) {
			// Load the desired chunk
			cps.loadChunk(x, z);
			// Mark the chunk for unload
			cps.dropChunk(x, z);
			
			// Display info about the created chunk
			if(Reference.debug)
				Reference.logger.info("Chunk created at x=" + x + " and z=" + z);
		}
		
		//For % calculations
		Reference.totalGenerated++;
		Reference.totalToGen--;
		
		//show the % of completion to the requester.
		if((Reference.totalGenerated % Reference.perCentDelay) == 0){
			Reference.logger.info("Generation "+((Reference.totalGenerated/Reference.totalToGen)*100)+"% completed");
			Reference.icomSender.addChatMessage(new ChatComponentText("Chunkgen: "+(float)((Reference.totalGenerated/Reference.totalToGen)*100)+"% completed"));
		}
	}
	
	public static void queueChunkGen(int x0, int z0, int height, int width, int dimID, ICommandSender icommandsender){
		Reference.x = x0;
		Reference.z = z0;
		Reference.height = height;
		Reference.width = width;
		Reference.dimID = dimID;
		
		Reference.totalToGen = (height*width);
		Reference.totalGenerated = 0;
		
		Reference.startX = (Reference.x - Reference.width/2);
		Reference.startZ = (Reference.z - Reference.height/2);
		Reference.stopX = (Reference.x + Reference.width/2);
		Reference.stopZ = (Reference.z + Reference.height/2);
		
		Reference.icomSender = icommandsender;
		
		Reference.toGenerate = true;
		
		Reference.logger.info("Chunk generation queued");
	}
}
