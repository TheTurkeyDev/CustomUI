package com.theprogrammingturkey.customUI.command;

import com.theprogrammingturkey.customUI.CustomUICore;
import com.theprogrammingturkey.customUI.config.CustomUIConfigLoader;
import com.theprogrammingturkey.gobblecore.commands.BaseCommandHandler;
import com.theprogrammingturkey.gobblecore.commands.CommandManager;
import com.theprogrammingturkey.gobblecore.commands.SimpleSubCommand;
import com.theprogrammingturkey.gobblecore.config.QueuedMessageReporter;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CustomUICommands
{

	public static void loadCommands()
	{
		BaseCommandHandler commandHandler = new BaseCommandHandler(CustomUICore.instance, "CustomUI");
		commandHandler.addCommandAliases("customUI", "cui", "CUI", "Customui", "customui");

		commandHandler.registerSubCommand("reload", new SimpleSubCommand("Refreshes the mod with any changes made in the mod's config", false)
		{
			@Override
			public boolean execute(MinecraftServer server, ICommandSender sender, String[] args)
			{
				CustomUIConfigLoader.refreshSettings();
				QueuedMessageReporter.outputErrors((EntityPlayer) sender);
				sender.addChatMessage(new TextComponentString(TextFormatting.GREEN + "Reloaded"));
				return true;
			}
		});

		CommandManager.registerCommandHandler(commandHandler);
	}
}