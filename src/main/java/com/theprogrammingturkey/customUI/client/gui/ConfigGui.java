package com.theprogrammingturkey.customUI.client.gui;

import com.theprogrammingturkey.customUI.config.CustomUIConfigLoader;
import com.theprogrammingturkey.customUI.config.CustomUISettings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class ConfigGui extends GuiScreen
{
	private SettingEditing editing = SettingEditing.None;

	private GuiButton hitBoxSettings;
	private GuiButton guiOverlaySettings;
	private GuiButton save;

	private GuiScreen parentScreen;
	private GuiSlider redSlider;
	private GuiSlider greenSlider;
	private GuiSlider blueSlider;
	private GuiSlider alphaSlider;
	private GuiSlider thicknessSlider;
	private GuiButton useDefaultBox;

	private int gradientX = this.width / 2 + 155;
	private int gradientY = 80;
	
	
	private float savedDelay = 0;

	public ConfigGui(GuiScreen parent)
	{
		parentScreen = parent;
	}

	public void initGui()
	{
		this.buttonList.clear();
		this.buttonList.add(hitBoxSettings = new GuiButton(1000, this.width / 2 - 50, 25, 100, 20, "Block Selection"));
		this.buttonList.add(guiOverlaySettings = new GuiButton(1001, this.width / 2 - 50, 50, 100, 20, "Gui Slot Highlight"));

		this.buttonList.add(save = new GuiButton(0, this.width / 2 - 100, this.height - 25, 200, 20, "Save"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 50, 200, 20, "Back"));

		this.buttonList.add(redSlider = new GuiSlider(10, "Red", this.width / 2 - 100, 40, 0F, 1F, CustomUISettings.highlightColorR, 0.01F));
		this.buttonList.add(greenSlider = new GuiSlider(11, "Green", this.width / 2 - 100, 70, 0F, 1F, CustomUISettings.highlightColorG, 0.01F));
		this.buttonList.add(blueSlider = new GuiSlider(12, "Blue", this.width / 2 - 100, 100, 0F, 1F, CustomUISettings.highlightColorB, 0.01F));
		this.buttonList.add(alphaSlider = new GuiSlider(13, "Alpha", this.width / 2 - 100, 130, 0F, 1F, CustomUISettings.highlightColorA, 0.01F));
		this.buttonList.add(thicknessSlider = new GuiSlider(14, "Thickness", this.width / 2 - 100, 160, 1F, 10F, CustomUISettings.highlightLineThickness / 10.0F, 0.5F));
		this.buttonList.add(useDefaultBox = new GuiButton(15, this.width / 2 - 100, 190, 150, 20, "Default selection box: " + (CustomUISettings.includeDefaultHighlight ? "On" : "Off")));

		this.setEditState(SettingEditing.None);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		if(this.editing == SettingEditing.BlockHighlight)
		{
			this.drawString(fr, "Block Highlight color", this.width / 2 - 75, 10, -1);
			int argb = (alphaSlider.getValueAdjusted(255) << 24) | (redSlider.getValueAdjusted(255) << 16) | (greenSlider.getValueAdjusted(255) << 8) | blueSlider.getValueAdjusted(255);
			gradientX = this.width / 2 + 120;
			this.drawGradientRect(gradientX - 5, gradientY - 5, gradientX + 37, gradientY + 37, -1, -1);
			this.drawGradientRect(gradientX, gradientY, gradientX + 32, gradientY + 32, argb, argb);
		}
		else if(this.editing == SettingEditing.GuiHighlight)
		{
			this.drawString(fr, "Gui Highlight color", this.width / 2 - 75, 10, -1);
			int argb = (alphaSlider.getValueAdjusted(255) << 24) | (redSlider.getValueAdjusted(255) << 16) | (greenSlider.getValueAdjusted(255) << 8) | blueSlider.getValueAdjusted(255);
			gradientX = this.width / 2 + 120;
			this.drawGradientRect(gradientX - 5, gradientY - 5, gradientX + 37, gradientY + 37, -1, -1);
			this.drawGradientRect(gradientX, gradientY, gradientX + 32, gradientY + 32, argb, argb);
		}
		else if(this.editing == SettingEditing.None)
		{
			
		}
		
		if(savedDelay > 0)
		{
			savedDelay -= partialTicks;
			this.drawString(fr, "Saved!", this.width / 2 - 15, this.height - 75, -1);
		}
	}

	protected void actionPerformed(GuiButton button)
	{
		if(button.enabled)
		{
			if(button.id == 0)
			{
				if(this.editing == SettingEditing.BlockHighlight)
				{
					CustomUIConfigLoader.saveBlockHighlightSettings(alphaSlider.getValue(), redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue(), thicknessSlider.getValueAdjusted(10.0F), CustomUISettings.includeDefaultHighlight);
				}
				else if(this.editing == SettingEditing.GuiHighlight)
				{
					CustomUIConfigLoader.saveGuiHighlightSettings(alphaSlider.getValue(), redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue());
				}
				
				savedDelay = 40.0f;
			}
			else if(button.id == 1)
			{
				if(this.editing == SettingEditing.None)
					this.mc.displayGuiScreen(this.parentScreen);
				else
					this.setEditState(SettingEditing.None);
			}
			else if(button.id == 15)
			{
				CustomUISettings.includeDefaultHighlight = !CustomUISettings.includeDefaultHighlight;
				useDefaultBox.displayString = "Default selection box: " + (CustomUISettings.includeDefaultHighlight ? "On" : "Off");
			}
			else if(button.id == 1000)
			{
				this.setEditState(SettingEditing.BlockHighlight);
			}
			else if(button.id == 1001)
			{
				this.setEditState(SettingEditing.GuiHighlight);
			}
		}
	}

	public void setEditState(SettingEditing setting)
	{
		this.redSlider.visible = (setting == SettingEditing.BlockHighlight || setting == SettingEditing.GuiHighlight);
		this.greenSlider.visible = (setting == SettingEditing.BlockHighlight || setting == SettingEditing.GuiHighlight);
		this.blueSlider.visible = (setting == SettingEditing.BlockHighlight || setting == SettingEditing.GuiHighlight);
		this.alphaSlider.visible = (setting == SettingEditing.BlockHighlight || setting == SettingEditing.GuiHighlight);
		this.thicknessSlider.visible = setting == SettingEditing.BlockHighlight;
		this.useDefaultBox.visible = setting == SettingEditing.BlockHighlight;
		this.hitBoxSettings.visible = setting == SettingEditing.None;
		this.guiOverlaySettings.visible = setting == SettingEditing.None;
		this.save.visible = setting != SettingEditing.None;
		
		if(setting == SettingEditing.BlockHighlight)
		{
			redSlider.setValue(CustomUISettings.highlightColorR);
			greenSlider.setValue(CustomUISettings.highlightColorG);
			blueSlider.setValue(CustomUISettings.highlightColorB);
			alphaSlider.setValue(CustomUISettings.highlightColorA);
		}
		else if(setting == SettingEditing.GuiHighlight)
		{
			redSlider.setValue(CustomUISettings.guihighlightColorR);
			greenSlider.setValue(CustomUISettings.guihighlightColorG);
			blueSlider.setValue(CustomUISettings.guihighlightColorB);
			alphaSlider.setValue(CustomUISettings.guihighlightColorA);
		}
		this.editing = setting;
	}

	public enum SettingEditing
	{
		None, BlockHighlight, GuiHighlight;
	}
}