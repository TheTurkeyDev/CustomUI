package com.theprogrammingturkey.customUI.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class CustomUIConfigLoader
{
	public static Configuration config;
	public static final String genCat = "General Settings";
	
	public static final String bhCat = "Block Highlight Settings";
	public static final String ghCat = "Gui Highlight Settings";
	
	public static final String thicknessAmount = "Highlight Line Thickness";
	public static final String defaultHighlight = "includeDefaultHighlight";
	
	public static final String redAmount = "Highlight red amount";
	public static final String greenAmount = "Highlight green amount";
	public static final String blueAmount = "Highlight blue amount";
	public static final String alphaAmount = "Highlight alpha amount";
	
	public static void loadConfigSettings(File file)
	{
		config = new Configuration(file);
		
		config.load();
		config.setCategoryComment(genCat, "General overall settings");
		config.setCategoryComment(bhCat, "Settings related to block highlighting");
		config.setCategoryComment(ghCat, "Settings related to gui slot highlighting when moused over");
		config.save();
		
		refreshSettings();
	}
	
	public static void refreshSettings()
	{
		config.load();
		
		CustomUISettings.includeDefaultHighlight = config.getBoolean(defaultHighlight, bhCat, false, "Set to true to include the default thin black outline with the custom outline highlight");
		CustomUISettings.highlightColorR = config.getFloat(redAmount, bhCat, 0F, 0F, 1F, "Red color value to be mixed into the the block hightlight overall color");
		CustomUISettings.highlightColorG = config.getFloat(greenAmount, bhCat, 0F, 0F, 1F, "Green color value to be mixed into the the block hightlight overall color");
		CustomUISettings.highlightColorB = config.getFloat(blueAmount, bhCat, 0F, 0F, 1F, "Blue color value to be mixed into the the block hightlight overall color");
		CustomUISettings.highlightColorA = config.getFloat(alphaAmount, bhCat, 0.4F, 0F, 1F, "Alpha amount for block hightlight overall. I.e. line clearness");
		CustomUISettings.highlightLineThickness = config.getFloat(thicknessAmount, bhCat, 2F, 1F, 10F, "How thick the highlight line should be.");
		
		CustomUISettings.guihighlightColorR = config.getFloat(redAmount, ghCat, 0F, 0F, 1F, "Red color value to be mixed into the the gui hightlight overall color");
		CustomUISettings.guihighlightColorG = config.getFloat(greenAmount, ghCat, 0F, 0F, 1F, "Green color value to be mixed into the the gui hightlight overall color");
		CustomUISettings.guihighlightColorB = config.getFloat(blueAmount, ghCat, 0F, 0F, 1F, "Blue color value to be mixed into the the gui hightlight overall color");
		CustomUISettings.guihighlightColorA = config.getFloat(alphaAmount, ghCat, 0F, 0F, 1F, "Alpha amount for gui hightlight overall (opacity)");
		
		config.save();
	}
	
	public static void saveBlockHighlightSettings(float a, float r, float g, float b, float thickness, boolean override)
	{
		config.load();
		
		Property prop = config.get(bhCat, defaultHighlight, false);
		prop.set(override);
		CustomUISettings.includeDefaultHighlight = override;
		
		prop = config.get(bhCat, redAmount, 0F);
		prop.set(r);
		CustomUISettings.highlightColorR = r;
		
		prop = config.get(bhCat, greenAmount, 0F);
		prop.set(g);
		CustomUISettings.highlightColorG = g;
		
		prop = config.get(bhCat, blueAmount, 0F);
		prop.set(b);
		CustomUISettings.highlightColorB = b;
		
		prop = config.get(bhCat, alphaAmount, 1F);
		prop.set(a);
		CustomUISettings.highlightColorA = a;
		
		prop = config.get(bhCat, thicknessAmount, 2F);
		prop.set(thickness);
		CustomUISettings.highlightLineThickness = thickness;
		
		config.save();
	}
	
	public static void saveGuiHighlightSettings(float a, float r, float g, float b)
	{
		config.load();
		
		Property prop = config.get(ghCat, redAmount, 0F);
		prop.set(r);
		CustomUISettings.guihighlightColorR = r;
		
		prop = config.get(ghCat, greenAmount, 0F);
		prop.set(g);
		CustomUISettings.guihighlightColorG = g;
		
		prop = config.get(ghCat, blueAmount, 0F);
		prop.set(b);
		CustomUISettings.guihighlightColorB = b;
		
		prop = config.get(ghCat, alphaAmount, 1F);
		prop.set(a);
		CustomUISettings.guihighlightColorA = a;
		
		config.save();
	}
}
