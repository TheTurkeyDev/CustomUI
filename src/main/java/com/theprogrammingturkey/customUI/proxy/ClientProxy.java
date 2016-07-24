package com.theprogrammingturkey.customUI.proxy;

import com.theprogrammingturkey.customUI.command.CustomUICommands;
import com.theprogrammingturkey.customUI.listener.BlockHighlightListener;
import com.theprogrammingturkey.customUI.listener.GuiListener;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;


public class ClientProxy extends CommonProxy
{

	@Override
	public boolean isClient()
	{
		return true;
	}

	public void registerRenderings()
	{
		
	}
	
	public void registerEvents()
	{
		MinecraftForge.EVENT_BUS.register(new BlockHighlightListener());
		MinecraftForge.EVENT_BUS.register(new GuiListener());
		ClientCommandHandler.instance.registerCommand(new CustomUICommands());
	}
	
	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}
}
