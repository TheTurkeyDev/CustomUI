package com.theprogrammingturkey.customUI.config;

import java.io.File;

import com.theprogrammingturkey.customUI.client.gui.GuiColorSelection;
import com.theprogrammingturkey.customUI.client.gui.ConfigGui.ButtonAnimationType;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class CustomUIConfigLoader
{
	public static Configuration config;

	public static final String bhCat = "Block Highlight Settings";
	public static final String ghCat = "Gui Highlight Settings";
	public static final String baCat = "Button Animation Settings";
	public static final String aiCat = "Armor Info Settings";

	public static final String thicknessAmount = "Highlight Line Thickness";
	public static final String defaultHighlight = "include Default Highlight";
	public static final String dimHighlight = "highlight Affected By Light";
	public static final String faceHighlight = "Highlight Block Faces";
	public static final String customHighlight = "Highlight Block Edges With Custom Color";

	public static final String guiHighlight = "Gui Slot Highlighting";

	public static final String redAmount = "Red amount";
	public static final String greenAmount = "Green amount";
	public static final String blueAmount = "Blue amount";
	public static final String alphaAmount = "Alpha amount";

	public static final String buttonAnimation = "Button Animations";
	public static final String buttonAnimationType = "Button Animation Type";
	public static final String buttonAnimationSpeed = "Button Animation Speed";

	public static final String armorHUD = "Armor Gui Hud";
	public static final String armorHUDX = "Armor Gui Hud X Position";
	public static final String armorHUDY = "Armor Gui Hud Y Position";

	public static void loadConfigSettings(File file)
	{
		config = new Configuration(file);

		config.load();
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

		CustomUISettings.includeDefaultHighlight = config.getBoolean(defaultHighlight, bhCat, true, "Set to true to include the default thin black outline with the custom outline highlight");
		CustomUISettings.highlightColorR = config.getFloat("Outline " + redAmount, bhCat, 0F, 0F, 1F, "Red color value to be mixed into the the block hightlight overall color");
		CustomUISettings.highlightColorG = config.getFloat("Outline " + greenAmount, bhCat, 0F, 0F, 1F, "Green color value to be mixed into the the block hightlight overall color");
		CustomUISettings.highlightColorB = config.getFloat("Outline " + blueAmount, bhCat, 0F, 0F, 1F, "Blue color value to be mixed into the the block hightlight overall color");
		CustomUISettings.highlightColorA = config.getFloat("Outline " + alphaAmount, bhCat, 1F, 0F, 1F, "Alpha amount for block face hightlight overall");
		CustomUISettings.fillColorR = config.getFloat("Fill " + redAmount, bhCat, 0F, 0F, 1F, "Red color value to be mixed into the the block hightlight overall color");
		CustomUISettings.fillColorG = config.getFloat("Fill " + greenAmount, bhCat, 0F, 0F, 1F, "Green color value to be mixed into the the block hightlight overall color");
		CustomUISettings.fillColorB = config.getFloat("Fill " + blueAmount, bhCat, 0F, 0F, 1F, "Blue color value to be mixed into the the block hightlight overall color");
		CustomUISettings.fillColorA = config.getFloat("Fill " + alphaAmount, bhCat, 1F, 0F, 1F, "Alpha amount for block face hightlight overall");
		CustomUISettings.highlightLineThickness = config.getFloat(thicknessAmount, bhCat, 2F, 1F, 10F, "How thick the highlight line should be.");
		CustomUISettings.highlightAffectedByLight = config.getBoolean(dimHighlight, bhCat, false, "Set to true for the block highlight to dim to match the blocks light level");
		CustomUISettings.highlightBlockFaces = config.getBoolean(faceHighlight, bhCat, false, "Set to true for block faces to be highlighted aswell");
		CustomUISettings.customHighlight = config.getBoolean(customHighlight, bhCat, false, "Set to true for block edges to be highlighted with a custom color");

		CustomUISettings.guiHighlight = config.getBoolean(guiHighlight, ghCat, false, "Set to true to enable highlighting the currently hovered slot.");
		CustomUISettings.guihighlightColorR = config.getFloat(redAmount, ghCat, 0F, 0F, 1F, "Red color value to be mixed into the the gui hightlight overall color");
		CustomUISettings.guihighlightColorG = config.getFloat(greenAmount, ghCat, 0F, 0F, 1F, "Green color value to be mixed into the the gui hightlight overall color");
		CustomUISettings.guihighlightColorB = config.getFloat(blueAmount, ghCat, 0F, 0F, 1F, "Blue color value to be mixed into the the gui hightlight overall color");
		CustomUISettings.guihighlightColorA = config.getFloat(alphaAmount, ghCat, 1F, 0F, 1F, "Alpha amount to be mixed into the the gui hightlight overall color");

		CustomUISettings.buttonAnimation = config.getBoolean(buttonAnimation, baCat, true, "Set to true to enable button animations");
		CustomUISettings.buttonAnimationType = ButtonAnimationType.getTypeFromName(config.getString(buttonAnimationType, baCat, "None", "How fast the animation should occur. (lower is faster)"));
		CustomUISettings.buttonAnimationSpeed = config.getFloat(buttonAnimationSpeed, baCat, 5F, 1F, 40F, "How fast the animation should occur. (lower is faster)");

		CustomUISettings.armorGuiHud = config.getBoolean(armorHUD, aiCat, false, "Set to true to enable the gui hud for armor info ingame");
		CustomUISettings.armorGuiHudX = config.getFloat(armorHUDX, aiCat, 0F, 0F, 1F, "X position of the hud for armor info ingame");
		CustomUISettings.armorGuiHudY = config.getFloat(armorHUDY, aiCat, 0.1F, 0F, 1F, "Y position of the hud for armor info ingame");

		config.save();
	}

	public static void saveBlockHighlightSettings(GuiColorSelection outline, GuiColorSelection fill, float thickness)
	{
		config.load();

		Property prop = config.get(bhCat, defaultHighlight, true);
		prop.set(CustomUISettings.includeDefaultHighlight);

		prop = config.get(bhCat, "Outline " + redAmount, 0F);
		prop.set(outline.getFloatRed());
		CustomUISettings.highlightColorR = outline.getFloatRed();

		prop = config.get(bhCat, "Outline " + greenAmount, 0F);
		prop.set(outline.getFloatGreen());
		CustomUISettings.highlightColorG = outline.getFloatGreen();

		prop = config.get(bhCat, "Outline " + blueAmount, 0F);
		prop.set(outline.getFloatBlue());
		CustomUISettings.highlightColorB = outline.getFloatBlue();

		prop = config.get(bhCat, "Outline " + alphaAmount, 1F);
		prop.set(outline.getFloatAlpha());
		CustomUISettings.highlightColorA = outline.getFloatAlpha();

		prop = config.get(bhCat, "Fill " + redAmount, 0F);
		prop.set(fill.getFloatRed());
		CustomUISettings.fillColorR = fill.getFloatRed();

		prop = config.get(bhCat, "Fill " + greenAmount, 0F);
		prop.set(fill.getFloatGreen());
		CustomUISettings.fillColorG = fill.getFloatGreen();

		prop = config.get(bhCat, "Fill " + blueAmount, 0F);
		prop.set(fill.getFloatBlue());
		CustomUISettings.fillColorB = fill.getFloatBlue();

		prop = config.get(bhCat, "Fill " + alphaAmount, 1F);
		prop.set(fill.getFloatAlpha());
		CustomUISettings.fillColorA = fill.getFloatAlpha();

		prop = config.get(bhCat, thicknessAmount, 2F);
		prop.set(thickness);
		CustomUISettings.highlightLineThickness = thickness;

		prop = config.get(bhCat, dimHighlight, false);
		prop.set(CustomUISettings.highlightAffectedByLight);

		prop = config.get(bhCat, faceHighlight, false);
		prop.set(CustomUISettings.highlightBlockFaces);
		
		prop = config.get(bhCat, customHighlight, false);
		prop.set(CustomUISettings.customHighlight);

		config.save();
	}

	public static void saveGuiHighlightSettings(GuiColorSelection color)
	{
		config.load();

		Property prop = config.get(ghCat, guiHighlight, false);
		prop.set(CustomUISettings.guiHighlight);

		prop = config.get(ghCat, redAmount, 0F);
		prop.set(color.getFloatRed());
		CustomUISettings.guihighlightColorR = color.getFloatRed();

		prop = config.get(ghCat, greenAmount, 0F);
		prop.set(color.getFloatGreen());
		CustomUISettings.guihighlightColorG = color.getFloatGreen();

		prop = config.get(ghCat, blueAmount, 0F);
		prop.set(color.getFloatBlue());
		CustomUISettings.guihighlightColorB = color.getFloatBlue();

		prop = config.get(ghCat, alphaAmount, 0F);
		prop.set(color.getFloatAlpha());
		CustomUISettings.guihighlightColorA = color.getFloatAlpha();

		config.save();
	}

	public static void saveButtonAnimationSettings(float speed, ButtonAnimationType animationType)
	{
		config.load();

		Property prop = config.get(baCat, buttonAnimation, false);
		prop.set(CustomUISettings.buttonAnimation);

		prop = config.get(baCat, buttonAnimationSpeed, 5F);
		prop.set(speed);
		CustomUISettings.buttonAnimationSpeed = speed;

		prop = config.get(baCat, buttonAnimationType, "None");
		prop.set(animationType.getTypeName());
		CustomUISettings.buttonAnimationType = animationType;

		config.save();
	}

	public static void saveArmorInfoSettings()
	{
		config.load();

		Property prop = config.get(aiCat, armorHUD, false);
		prop.set(CustomUISettings.armorGuiHud);

		prop = config.get(aiCat, armorHUDX, 0f);
		prop.set(CustomUISettings.armorGuiHudX);

		prop = config.get(aiCat, armorHUDY, 0.1f);
		prop.set(CustomUISettings.armorGuiHudY);

		config.save();
	}
}