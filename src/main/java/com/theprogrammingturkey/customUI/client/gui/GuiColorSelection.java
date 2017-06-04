package com.theprogrammingturkey.customUI.client.gui;

import java.util.List;

import com.theprogrammingturkey.gobblecore.client.gui.GuiSlider;
import com.theprogrammingturkey.gobblecore.util.RenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;

public class GuiColorSelection
{
	private String dispLabel;

	private GuiSlider redSlider;
	private GuiSlider greenSlider;
	private GuiSlider blueSlider;
	private GuiSlider alphaSlider;

	private int x;
	private int y;

	public GuiColorSelection(String disp, List<GuiButton> buttonList, int startID, int x, int y)
	{
		this.dispLabel = disp;
		this.x = x;
		this.y = y;
		buttonList.add(redSlider = new GuiSlider(startID, "Red", x, y, 0F, 1F, 0, 0.01F));
		buttonList.add(greenSlider = new GuiSlider(startID++, "Green", x, y + 25, 0F, 1F, 0, 0.01F));
		buttonList.add(blueSlider = new GuiSlider(startID++, "Blue", x, y + 50, 0F, 1F, 0, 0.01F));
		buttonList.add(alphaSlider = new GuiSlider(startID++, "Alpha", x, y + 75, 0F, 1F, 0, 0.01F));
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
		fr.drawStringWithShadow(dispLabel + " Color", x + 160, y + 20, -1);
		int argb = getIntColor();
		int gradientX = x + 175;
		int gradientY = y + 40;
		RenderUtil.drawGradientRect(gradientX - 5, gradientY - 5, gradientX + 37, gradientY + 37, -1, -1);
		RenderUtil.drawGradientRect(gradientX, gradientY, gradientX + 32, gradientY + 32, argb, argb);
	}

	public void setCurrentValues(float r, float g, float b, float a)
	{
		redSlider.setValue(r);
		greenSlider.setValue(g);
		blueSlider.setValue(b);
		alphaSlider.setValue(a);
	}

	public void setVisible(boolean visible)
	{
		redSlider.visible = visible;
		greenSlider.visible = visible;
		blueSlider.visible = visible;
		alphaSlider.visible = visible;
	}

	public int getIntColor()
	{
		return (alphaSlider.getValueAdjusted(255) << 24) | (redSlider.getValueAdjusted(255) << 16) | (greenSlider.getValueAdjusted(255) << 8) | blueSlider.getValueAdjusted(255);
	}

	public float getFloatRed()
	{
		return this.redSlider.getValue();
	}

	public float getFloatGreen()
	{
		return this.greenSlider.getValue();
	}

	public float getFloatBlue()
	{
		return this.blueSlider.getValue();
	}

	public float getFloatAlpha()
	{
		return this.alphaSlider.getValue();
	}

	public float getIntRed()
	{
		return this.redSlider.getValueAdjusted(255);
	}

	public float getIntGreen()
	{
		return this.greenSlider.getValueAdjusted(255);
	}

	public float getIntBlue()
	{
		return this.blueSlider.getValueAdjusted(255);
	}

	public float getIntAlpha()
	{
		return this.alphaSlider.getValueAdjusted(255);
	}
}
