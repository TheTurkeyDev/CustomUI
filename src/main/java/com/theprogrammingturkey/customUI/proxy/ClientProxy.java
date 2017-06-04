package com.theprogrammingturkey.customUI.proxy;

import com.theprogrammingturkey.customUI.listener.BlockHighlightListener;
import com.theprogrammingturkey.customUI.listener.GuiListener;
import com.theprogrammingturkey.gobblecore.events.EventManager;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

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
		EventManager.registerListener(new BlockHighlightListener());
		EventManager.registerListener(new GuiListener());
	}

	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}
}