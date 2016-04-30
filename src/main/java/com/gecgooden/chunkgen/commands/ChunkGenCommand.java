package com.gecgooden.chunkgen.commands;

import com.gecgooden.chunkgen.handlers.ConfigurationHandler;
import com.gecgooden.chunkgen.reference.Reference;
import com.gecgooden.chunkgen.util.Utilities;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;

import java.util.ArrayList;
import java.util.List;
//import net.minecraft.util.ChunkCoordinates;

public class ChunkGenCommand implements ICommand
{	
	private List aliases;
	public ChunkGenCommand()
	{
		this.aliases = new ArrayList();
		this.aliases.add("chunkgen");
	}

	@Override
	public String getCommandName()
	{
		return "chunkgen";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "chunkgen <x> <y> <height> <width> [dimension]";
	}

	@Override
	public List getCommandAliases()
	{
		return this.aliases;
	}

	/**
	 * Return the required permission level for this command.
	 */
	public int getRequiredPermissionLevel()
	{
		return 4;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring)
	{
		if(!icommandsender.canCommandSenderUseCommand(getRequiredPermissionLevel(), this.getCommandName()) && !MinecraftServer.getServer().isSinglePlayer()) {
			ChatComponentTranslation chatTranslation = new ChatComponentTranslation("commands.generic.permission", new Object[0]);
			MinecraftServer.getServer().addChatMessage(chatTranslation);
			icommandsender.addChatMessage(new ChatComponentText(chatTranslation.getUnformattedTextForChat()));
		} else {
			int playerX = 0;
			int playerZ = 0;
			if(!icommandsender.getName().equalsIgnoreCase("Rcon")) {
				MinecraftServer.getServer().worldServerForDimension(0).getPlayerEntityByName(icommandsender.getName());
				BlockPos blockPos = icommandsender.getPosition();
				playerX = blockPos.getX();
				playerZ = blockPos.getZ();
			}
			if(astring.length == 0 || astring[0].equalsIgnoreCase("help")) {
				ChatComponentTranslation chatTranslation = new ChatComponentTranslation(getCommandUsage(icommandsender), new Object[0]);
				MinecraftServer.getServer().addChatMessage(chatTranslation);
				icommandsender.addChatMessage(new ChatComponentText(chatTranslation.getUnformattedTextForChat()));
			}
			else if(astring[0].equalsIgnoreCase("stop")) {
				Reference.toGenerate = false;
				ConfigurationHandler.updateConfigs();
				ChatComponentTranslation chatTranslation = new ChatComponentTranslation("commands.stopped");
				MinecraftServer.getServer().addChatMessage(chatTranslation);
				icommandsender.addChatMessage(new ChatComponentText(chatTranslation.getUnformattedTextForChat()));
			} else {
				try {
					int x = 0;
					int z = 0;
					if(astring[0].equalsIgnoreCase("~")) {
						x = playerX/16;
					} else {
						x = Integer.parseInt(astring[0]);
					}
					if(astring[1].equalsIgnoreCase("~")) {
						z = playerZ/16;
					} else {
						z = Integer.parseInt(astring[1]);
					}
					int height = Integer.parseInt(astring[2]);
					int width = Integer.parseInt(astring[3]);
					int dimensionID = icommandsender.getEntityWorld().provider.getDimensionId();

					if(astring.length == 5) {
						dimensionID = Integer.parseInt(astring[4]);
					}
					Reference.dimID = dimensionID;
					
					Utilities.queueChunkGen( x, z, height, width, dimensionID, icommandsender);
					
					ConfigurationHandler.updateConfigs();
					ChatComponentTranslation chatTranslation = new ChatComponentTranslation("commands.start");
					MinecraftServer.getServer().addChatMessage(chatTranslation);
					icommandsender.addChatMessage(new ChatComponentText(chatTranslation.getUnformattedTextForChat()));
					
					//informs the user that generation will start when everyone is offline.
					if(Reference.pauseForPlayers){
						ChatComponentTranslation chatTranslation2 = new ChatComponentTranslation("commands.wait");
						MinecraftServer.getServer().addChatMessage(chatTranslation2);
						icommandsender.addChatMessage(new ChatComponentText(chatTranslation2.getUnformattedTextForChat()));
					}
					
				} catch (NumberFormatException e) {
					e.printStackTrace();
					ChatComponentTranslation chatTranslation = new ChatComponentTranslation("commands.numberFormatException");
					MinecraftServer.getServer().addChatMessage(chatTranslation);
					icommandsender.addChatMessage(new ChatComponentText(chatTranslation.getUnformattedTextForChat()));
				} catch (Exception e) {
					e.printStackTrace();
					ChatComponentTranslation chatTranslation = new ChatComponentTranslation("commands.failed");
					MinecraftServer.getServer().addChatMessage(chatTranslation);
					icommandsender.addChatMessage(new ChatComponentText(chatTranslation.getUnformattedTextForChat()));
				}
			}
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender icommandsender)
	{	
		return true;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i)
	{
		return false;
	}

	@Override
	public int compareTo(ICommand o) {
		return 0;
	}
}