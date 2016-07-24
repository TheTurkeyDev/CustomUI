package com.theprogrammingturkey.customUI.command;

import java.util.ArrayList;
import java.util.List;

import com.theprogrammingturkey.customUI.config.CustomUIConfigLoader;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CustomUICommands implements ICommand
{
	private List<String> aliases;
	List<String> tab;

	public CustomUICommands()
	{
		this.aliases = new ArrayList<String>();
		this.aliases.add("customUI");
		this.aliases.add("cui");
		this.aliases.add("CUI");
		this.aliases.add("Customui");
		this.aliases.add("customui");

		tab = new ArrayList<String>();
		tab.add("reload");
		tab.add("version");
		tab.add("handNBT");
	}

	@Override
	public String getCommandName()
	{
		return "CustomUI";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "/CustomUI <reload>";
	}

	@Override
	public List<String> getCommandAliases()
	{
		return this.aliases;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args)
	{
		if(args.length > 0 && args[0].equalsIgnoreCase("reload"))
		{
			CustomUIConfigLoader.refreshSettings();
		}
	}
	
	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
	{
		if(args.length == 0)
			return tab;
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] astring, int i)
	{
		return false;
	}
	@Override
	public int compareTo(ICommand arg0)
	{
		return 0;
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		return true;
	}


}