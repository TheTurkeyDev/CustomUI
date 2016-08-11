package com.theprogrammingturkey.customUI.config;

import java.io.File;

import com.theprogrammingturkey.customUI.client.gui.ConfigGui.ButtonAnimationType;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class CustomUIConfigLoader
{
	public static Configuration config;
	public static final String genCat = "General Settings";

	public static final String bhCat = "Block Highlight Settings";
	public static final String ghCat = "Gui Highlight Settings";
	public static final String baCat = "Button Animation Settings";
	public static final String aiCat = "Armor Info Settings";

	public static final String thicknessAmount = "Highlight Line Thickness";
	public static final String defaultHighlight = "includeDefaultHighlight";
	public static final String dimHighlight = "highlightAffectedByLight";

	public static final String guiHighlight = "Gui Slot Highlighting";

	public static final String redAmount = "Highlight red amount";
	public static final String greenAmount = "Highlight green amount";
	public static final String blueAmount = "Highlight blue amount";
	public static final String alphaAmount = "Highlight alpha amount";

	public static final String buttonAnimation = "Button Animations";
	public static final String buttonAnimationType = "Button Animation Type";
	public static final String buttonAnimationSpeed = "Button Animation Speed";
	
	public static final String armorHUD = "Armor Gui Hud";

	public static void loadConfigSettings(File file)
	{
		config = new Configuration(file);

		config.load();
		config.setCategoryComment(genCat, "General overall settings");
		config.setCategoryComment(bhCat, "Settings related to block highlighting");
		config.setCategoryComment(ghCat, "Settings related to gui slot highlighting when moused over");
		config.setCategoryComment(baCat, "Settings related to button animations");
		config.setCategoryComment(aiCat, "Settings related to armor info");
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
		CustomUISettings.highlightAffectedByLight = config.getBoolean(dimHighlight, bhCat, false, "Set to true for the block highlight to dim to match the blocks light level");

		CustomUISettings.guiHighlight = config.getBoolean(guiHighlight, ghCat, false, "Set to true to enable highlighting the currently hovered slot.");
		CustomUISettings.guihighlightColorR = config.getFloat(redAmount, ghCat, 0F, 0F, 1F, "Red color value to be mixed into the the gui hightlight overall color");
		CustomUISettings.guihighlightColorG = config.getFloat(greenAmount, ghCat, 0F, 0F, 1F, "Green color value to be mixed into the the gui hightlight overall color");
		CustomUISettings.guihighlightColorB = config.getFloat(blueAmount, ghCat, 0F, 0F, 1F, "Blue color value to be mixed into the the gui hightlight overall color");

		CustomUISettings.buttonAnimation = config.getBoolean(buttonAnimation, baCat, false, "Set to true to enable button animations");
		CustomUISettings.buttonAnimationType = ButtonAnimationType.getTypeFromName(config.getString(buttonAnimationType, baCat, "None", "How fast the animation should occur. (lower is faster)"));
		CustomUISettings.buttonAnimationSpeed = config.getFloat(buttonAnimationSpeed, baCat, 5F, 1F, 40F, "How fast the animation should occur. (lower is faster)");

		CustomUISettings.armorGuiHud = config.getBoolean(armorHUD, aiCat, false, "Set to true to enable the gui hud for armor info ingame");
		config.save();
	}

	public static void saveBlockHighlightSettings(float a, float r, float g, float b, float thickness, boolean override, boolean dim)
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
		
		prop = config.get(bhCat, dimHighlight, false);
		prop.set(dim);
		CustomUISettings.highlightAffectedByLight = dim;

		config.save();
	}

	public static void saveGuiHighlightSettings(boolean useGuiHighlgiht, float r, float g, float b)
	{
		config.load();

		Property prop = config.get(ghCat, guiHighlight, false);
		prop.set(useGuiHighlgiht);
		CustomUISettings.guiHighlight = useGuiHighlgiht;

		prop = config.get(ghCat, redAmount, 0F);
		prop.set(r);
		CustomUISettings.guihighlightColorR = r;

		prop = config.get(ghCat, greenAmount, 0F);
		prop.set(g);
		CustomUISettings.guihighlightColorG = g;

		prop = config.get(ghCat, blueAmount, 0F);
		prop.set(b);
		CustomUISettings.guihighlightColorB = b;

		config.save();
	}

	public static void saveButtonAnimationSettings(boolean enabled, float speed, ButtonAnimationType animationType)
	{
		config.load();

		Property prop = config.get(baCat, buttonAnimation, false);
		prop.set(enabled);
		CustomUISettings.buttonAnimation = enabled;

		prop = config.get(baCat, buttonAnimationSpeed, 5F);
		prop.set(speed);
		CustomUISettings.buttonAnimationSpeed = speed;

		prop = config.get(baCat, buttonAnimationType, "None");
		prop.set(animationType.getTypeName());
		CustomUISettings.buttonAnimationType = animationType;

		config.save();
	}
	
	public static void saveArmorInfoSettings(boolean enabled)
	{
		config.load();

		Property prop = config.get(aiCat, armorHUD, false);
		prop.set(enabled);
		CustomUISettings.armorGuiHud = enabled;
		
		config.save();
	}
}
