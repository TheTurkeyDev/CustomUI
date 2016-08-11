package com.theprogrammingturkey.customUI.config;

import com.theprogrammingturkey.customUI.client.gui.ConfigGui.ButtonAnimationType;

public class CustomUISettings
{
	public static boolean includeDefaultHighlight = false;
	public static float highlightColorR = 0.0F;
	public static float highlightColorG = 0.0F;
	public static float highlightColorB = 0.0F;
	public static float highlightColorA = 0.0F;
	public static float highlightLineThickness = 2.0F;
	public static boolean highlightAffectedByLight = true;

	public static boolean guiHighlight = false;
	public static float guihighlightColorR = 0.0F;
	public static float guihighlightColorG = 0.0F;
	public static float guihighlightColorB = 0.0F;

	public static boolean buttonAnimation = false;
	public static ButtonAnimationType buttonAnimationType = ButtonAnimationType.SlideUp;
	public static float buttonAnimationSpeed = 5f;
	
	public static boolean armorGuiHud = false;
}