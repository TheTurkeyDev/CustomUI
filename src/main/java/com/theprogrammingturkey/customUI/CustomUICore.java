package com.theprogrammingturkey.customUI;

import org.apache.logging.log4j.Logger;

import com.theprogrammingturkey.customUI.command.CustomUICommands;
import com.theprogrammingturkey.customUI.config.CustomUIConfigLoader;
import com.theprogrammingturkey.customUI.proxy.CommonProxy;
import com.theprogrammingturkey.gobblecore.IModCore;
import com.theprogrammingturkey.gobblecore.proxy.ProxyManager;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = CustomUICore.MODID, version = CustomUICore.VERSION, name = CustomUICore.NAME, dependencies = "after:gobblecore[0.1.4.13,)", guiFactory = CustomUICore.GUIFACTORY)
public class CustomUICore implements IModCore
{
	public static final String MODID = "customui";
	public static final String VERSION = "@VERSION@";
	public static final String NAME = "CustomUI";
	public static final String GUIFACTORY = "com.theprogrammingturkey.customUI.config.ConfigGuiFactory";

	@Instance(value = MODID)
	public static CustomUICore instance;
	@SidedProxy(clientSide = "com.theprogrammingturkey.customUI.proxy.ClientProxy", serverSide = "com.theprogrammingturkey.customUI.proxy.CommonProxy")
	public static CommonProxy proxy;
	public static Logger logger;

	@EventHandler
	public void init(FMLInitializationEvent event)
	{

	}

	@EventHandler
	public void load(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();

		CustomUIConfigLoader.loadConfigSettings(event.getSuggestedConfigurationFile());

		ProxyManager.registerModProxy(proxy);

		CustomUICommands.loadCommands();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{

	}

	@Override
	public String getModID()
	{
		return MODID;
	}

	@Override
	public String getName()
	{
		return NAME;
	}

	@Override
	public String getVersion()
	{
		return VERSION;
	}
}
