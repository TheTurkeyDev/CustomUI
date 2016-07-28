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
	private GuiButton buttonAnimationSettings;
	private GuiButton save;

	private GuiScreen parentScreen;
	private GuiSlider redSlider;
	private GuiSlider greenSlider;
	private GuiSlider blueSlider;
	private GuiSlider alphaSlider;
	private GuiSlider thicknessSlider;
	private GuiButton useDefaultBox;
	private GuiButton useGuiHighlight;

	private GuiButton useButtonAnimation;
	private GuiButton buttonAnimationType;
	private GuiSlider animationSpeedSlider;

	private int gradientX = this.width / 2 + 155;
	private int gradientY = 80;

	private float savedDelay = 0;
	public static boolean buttonPressedThisUpdate = false;

	public ConfigGui(GuiScreen parent)
	{
		parentScreen = parent;
	}

	public void initGui()
	{
		this.buttonList.clear();
		this.buttonList.add(hitBoxSettings = new GuiButton(1000, this.width / 2 - 50, 25, 100, 20, "Block Selection"));
		this.buttonList.add(guiOverlaySettings = new GuiButton(1001, this.width / 2 - 50, 50, 100, 20, "Gui Slot Highlight"));
		this.buttonList.add(buttonAnimationSettings = new GuiButton(1002, this.width / 2 - 50, 75, 100, 20, "Button Animations"));

		this.buttonList.add(save = new GuiButton(0, this.width / 2 - 100, this.height - 25, 200, 20, "Save"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 50, 200, 20, "Back"));

		this.buttonList.add(redSlider = new GuiSlider(10, "Red", this.width / 2 - 100, 30, 0F, 1F, CustomUISettings.highlightColorR, 0.01F));
		this.buttonList.add(greenSlider = new GuiSlider(11, "Green", this.width / 2 - 100, 55, 0F, 1F, CustomUISettings.highlightColorG, 0.01F));
		this.buttonList.add(blueSlider = new GuiSlider(12, "Blue", this.width / 2 - 100, 80, 0F, 1F, CustomUISettings.highlightColorB, 0.01F));
		this.buttonList.add(alphaSlider = new GuiSlider(13, "Alpha", this.width / 2 - 100, 105, 0F, 1F, CustomUISettings.highlightColorA, 0.01F));
		this.buttonList.add(thicknessSlider = new GuiSlider(14, "Thickness", this.width / 2 - 100, 130, 1F, 10F, CustomUISettings.highlightLineThickness, 0.5F));
		this.buttonList.add(useDefaultBox = new GuiButton(15, this.width / 2 - 100, 155, 150, 20, "Default selection box: " + (CustomUISettings.includeDefaultHighlight ? "On" : "Off")));
		this.buttonList.add(useGuiHighlight = new GuiButton(16, this.width / 2 - 100, 130, 150, 20, "Gui Highlight: " + (CustomUISettings.guiHighlight ? "On" : "Off")));

		this.buttonList.add(useButtonAnimation = new GuiButton(20, this.width / 2 - 100, 30, 200, 20, "Button Animations: " + (CustomUISettings.buttonAnimation ? "On" : "Off")));
		this.buttonList.add(buttonAnimationType = new GuiButton(21, this.width / 2 - 100, 55, 200, 20, "Button Animation Type: " + CustomUISettings.buttonAnimationType.getTypeName()));
		this.buttonList.add(animationSpeedSlider = new GuiSlider(22, "Speed", this.width / 2 - 75, 80, 0F, 40F, CustomUISettings.buttonAnimationSpeed, 1F));

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

		if(savedDelay > 0)
		{
			savedDelay -= partialTicks;
			this.drawString(fr, "Saved!", this.width / 2 - 15, this.height - 75, -1);
		}
	}

	public void updateScreen()
	{
		buttonPressedThisUpdate = false;
	}

	protected void actionPerformed(GuiButton button)
	{
		if(buttonPressedThisUpdate)
			return;

		buttonPressedThisUpdate = true;

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
					CustomUIConfigLoader.saveGuiHighlightSettings(CustomUISettings.guiHighlight, redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue());
				}
				else if(this.editing == SettingEditing.ButtonAnimation)
				{
					CustomUIConfigLoader.saveButtonAnimationSettings(CustomUISettings.buttonAnimation, this.animationSpeedSlider.getValueAdjusted(40.0F), CustomUISettings.buttonAnimationType);
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
			else if(button.id == 16)
			{
				CustomUISettings.guiHighlight = !CustomUISettings.guiHighlight;
				useGuiHighlight.displayString = "Gui Highlight: " + (CustomUISettings.guiHighlight ? "On" : "Off");
			}
			else if(button.id == 20)
			{
				CustomUISettings.buttonAnimation = !CustomUISettings.buttonAnimation;
				useButtonAnimation.displayString = "Button Animations: " + (CustomUISettings.buttonAnimation ? "On" : "Off");
			}
			else if(button.id == 21)
			{
				CustomUISettings.buttonAnimationType = CustomUISettings.buttonAnimationType.getNext();
				this.buttonAnimationType.displayString = "Button Animation Type: " + CustomUISettings.buttonAnimationType.getTypeName();
			}
			else if(button.id == 1000)
			{
				this.setEditState(SettingEditing.BlockHighlight);
			}
			else if(button.id == 1001)
			{
				this.setEditState(SettingEditing.GuiHighlight);
			}
			else if(button.id == 1002)
			{
				this.setEditState(SettingEditing.ButtonAnimation);
			}
		}
	}

	public void setEditState(SettingEditing setting)
	{
		this.redSlider.visible = (setting == SettingEditing.BlockHighlight || setting == SettingEditing.GuiHighlight);
		this.greenSlider.visible = (setting == SettingEditing.BlockHighlight || setting == SettingEditing.GuiHighlight);
		this.blueSlider.visible = (setting == SettingEditing.BlockHighlight || setting == SettingEditing.GuiHighlight);
		this.alphaSlider.visible = setting == SettingEditing.BlockHighlight;
		this.thicknessSlider.visible = setting == SettingEditing.BlockHighlight;
		this.useDefaultBox.visible = setting == SettingEditing.BlockHighlight;
		this.useGuiHighlight.visible = setting == SettingEditing.GuiHighlight;
		this.hitBoxSettings.visible = setting == SettingEditing.None;
		this.guiOverlaySettings.visible = setting == SettingEditing.None;
		this.buttonAnimationSettings.visible = setting == SettingEditing.None;
		this.save.visible = setting != SettingEditing.None;
		this.useButtonAnimation.visible = setting == SettingEditing.ButtonAnimation;
		this.buttonAnimationType.visible = setting == SettingEditing.ButtonAnimation;
		this.animationSpeedSlider.visible = setting == SettingEditing.ButtonAnimation;

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
		}
		this.editing = setting;
	}

	public enum SettingEditing
	{
		None, BlockHighlight, GuiHighlight, ButtonAnimation;
	}

	public enum ButtonAnimationType
	{
		None("None"), SlideUp("Slide Up"), FadeIn("Fade In");

		private String typeName;

		ButtonAnimationType(String name)
		{
			this.typeName = name;
		}

		public String getTypeName()
		{
			return this.typeName;
		}

		public ButtonAnimationType getNext()
		{
			switch(this)
			{
				case None:
					return SlideUp;
				case SlideUp:
					return FadeIn;
				case FadeIn:
					return None;
			}
			return None;
		}

		public static ButtonAnimationType getTypeFromName(String name)
		{
			for(ButtonAnimationType type : values())
				if(type.getTypeName().equalsIgnoreCase(name))
					return type;
			return None;
		}
	}
}