package com.gecgooden.chunkgen.handlers;

import com.gecgooden.chunkgen.reference.Reference;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class ConfigurationHandler {
	
	public static Configuration configuration;
		
	public static void init(File configFile) {
		if(configuration == null) { 
			configuration = new Configuration(configFile);
			loadConfiguration();
		}
	}
	
	@SubscribeEvent
	public void onConfigurationChangedEvent(OnConfigChangedEvent event) {
		if(event.modID.equalsIgnoreCase("chunkgen")) {
			loadConfiguration();
		}
	}
	
	private static void loadConfiguration() {
		Reference.x = configuration.get(Configuration.CATEGORY_GENERAL, "x", 0, "X starting value").getInt();
		Reference.z = configuration.get(Configuration.CATEGORY_GENERAL, "z", 0, "Z starting value").getInt();
		Reference.height = configuration.get(Configuration.CATEGORY_GENERAL, "height", 0, "Height starting value").getInt();
		Reference.width = configuration.get(Configuration.CATEGORY_GENERAL, "width", 0, "Width starting value").getInt();
		Reference.pauseForPlayers = configuration.get(Configuration.CATEGORY_GENERAL, "pauseForPlayers", true, "Pause chunk generation when players are logged on").getBoolean();
		Reference.numChunksPerTick = configuration.get(Configuration.CATEGORY_GENERAL, "numChunksPerTick", 1.0, "Number of chunks loaded per tick").getDouble();
		Reference.perCentDelay = configuration.get(Configuration.CATEGORY_GENERAL, "percentDelay", 50, "Number of chunks to generate before showing the % done").getInt();
		Reference.debug = configuration.get(Configuration.CATEGORY_GENERAL, "debug", false, "Show debug infos, keep at false if you don't want to spam your console").getBoolean();
		
		Reference.startX = configuration.get(Configuration.CATEGORY_GENERAL, "startx", 0, "for resuming generation, AUTOMATIC, DO NOT MODIFY").getInt();
		Reference.startZ = configuration.get(Configuration.CATEGORY_GENERAL, "startz", 0, "for resuming generation, AUTOMATIC, DO NOT MODIFY").getInt();
		Reference.stopX = configuration.get(Configuration.CATEGORY_GENERAL, "stopx", 0, "for resuming generation, AUTOMATIC, DO NOT MODIFY").getInt();
		Reference.stopZ = configuration.get(Configuration.CATEGORY_GENERAL, "stopz", 0, "for resuming generation, AUTOMATIC, DO NOT MODIFY").getInt();
		Reference.toGenerate = configuration.get(Configuration.CATEGORY_GENERAL, "generating", false, "Was the server generating stuff when it stopped? AUTOMATIC, DO NOT MODIFY").getBoolean();
		
		//Failed attempt at saving the iCommandSender to a string in the configs, needs more research.
		//Reference.icomSender = new configuration.get(Configuration.CATEGORY_GENERAL, "icomSender", "--", "Command Sender Name").getString();
		
		if(configuration.hasChanged()) {
			configuration.save();
		}
	}

	public static void updateConfigs() {
		configuration.get(Configuration.CATEGORY_GENERAL, "startx", 0, "for resuming generation, AUTOMATIC, DO NOT MODIFY").setValue(Reference.startX);
		configuration.get(Configuration.CATEGORY_GENERAL, "startz", 0, "for resuming generation, AUTOMATIC, DO NOT MODIFY").setValue(Reference.startZ);
		configuration.get(Configuration.CATEGORY_GENERAL, "stopx", 0, "for resuming generation, AUTOMATIC, DO NOT MODIFY").setValue(Reference.stopX);
		configuration.get(Configuration.CATEGORY_GENERAL, "stopz", 0, "for resuming generation, AUTOMATIC, DO NOT MODIFY").setValue(Reference.stopZ);
		configuration.get(Configuration.CATEGORY_GENERAL, "generating", false, "Was the server generating stuff when it stopped? AUTOMATIC, DO NOT MODIFY").setValue(Reference.toGenerate);
		configuration.save();
	}
}
