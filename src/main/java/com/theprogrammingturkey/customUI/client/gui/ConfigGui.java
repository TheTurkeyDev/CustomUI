package com.theprogrammingturkey.customUI.client.gui;

import java.util.ArrayList;

import com.theprogrammingturkey.customUI.config.CustomUIConfigLoader;
import com.theprogrammingturkey.customUI.config.CustomUISettings;
import com.theprogrammingturkey.customUI.util.RenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;

public class ConfigGui extends GuiScreen
{
	private SettingEditing editing = SettingEditing.None;

	private GuiButton hitBoxSettings;
	private GuiButton guiOverlaySettings;
	private GuiButton buttonAnimationSettings;
	private GuiButton armorInfoSettings;

	private GuiScreen parentScreen;
	private GuiSlider redSlider;
	private GuiSlider greenSlider;
	private GuiSlider blueSlider;
	private GuiSlider alphaSlider;
	private GuiSlider thicknessSlider;
	private GuiButton useDefaultBox;
	private GuiButton useGuiHighlight;
	private GuiButton highlightAffectedByLight;
	private GuiButton highlightBlockFaces;

	private GuiButton useButtonAnimation;
	private GuiButton buttonAnimationType;
	private GuiSlider animationSpeedSlider;

	private GuiButton useArmorHUD;
	private GuiButton armorHUDPosition;
	private ItemStack[] testItems = { new ItemStack(Items.DIAMOND_BOOTS, 1), new ItemStack(Items.GOLDEN_LEGGINGS, 1), new ItemStack(Items.LEATHER_CHESTPLATE, 1), new ItemStack(Items.IRON_HELMET, 1), new ItemStack(Items.BOW, 1), new ItemStack(Items.STONE_SWORD, 1) };
	private boolean movingHUD = false;

	private int gradientX = this.width / 2 + 155;
	private int gradientY = 80;

	public static boolean buttonPressedThisUpdate = false;
	private GuiSlider trackedSliderSelected = null;

	public ConfigGui(GuiScreen parent)
	{
		parentScreen = parent;
	}

	public void initGui()
	{
		this.buttonList.clear();
		this.buttonList.add(hitBoxSettings = new GuiButton(1000, this.width / 2 - 125, 25, 100, 20, "Block Selection"));
		this.buttonList.add(guiOverlaySettings = new GuiButton(1001, this.width / 2 + 25, 25, 100, 20, "Gui Slot Highlight"));
		this.buttonList.add(buttonAnimationSettings = new GuiButton(1002, this.width / 2 - 125, 50, 100, 20, "Button Animations"));
		this.buttonList.add(armorInfoSettings = new GuiButton(1003, this.width / 2 + 25, 50, 100, 20, "Armor Info"));

		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 25, 200, 20, "Back"));

		this.buttonList.add(redSlider = new GuiSlider(10, "Red", this.width / 2 - 100, 30, 0F, 1F, CustomUISettings.highlightColorR, 0.01F));
		this.buttonList.add(greenSlider = new GuiSlider(11, "Green", this.width / 2 - 100, 55, 0F, 1F, CustomUISettings.highlightColorG, 0.01F));
		this.buttonList.add(blueSlider = new GuiSlider(12, "Blue", this.width / 2 - 100, 80, 0F, 1F, CustomUISettings.highlightColorB, 0.01F));
		this.buttonList.add(alphaSlider = new GuiSlider(13, "Alpha", this.width / 2 - 100, 105, 0F, 1F, CustomUISettings.highlightColorA, 0.01F));
		this.buttonList.add(thicknessSlider = new GuiSlider(14, "Thickness", this.width / 2 - 100, 130, 1F, 10F, CustomUISettings.highlightLineThickness, 0.5F));
		this.buttonList.add(useDefaultBox = new GuiButton(15, this.width / 2 - 100, 155, 150, 20, "Default selection box: " + (CustomUISettings.includeDefaultHighlight ? "On" : "Off")));
		this.buttonList.add(useGuiHighlight = new GuiButton(16, this.width / 2 - 100, 130, 150, 20, "Gui Highlight: " + (CustomUISettings.guiHighlight ? "On" : "Off")));
		this.buttonList.add(highlightAffectedByLight = new GuiButton(17, this.width / 2 - 100, 180, 150, 20, "Highlight Dim: " + (CustomUISettings.highlightAffectedByLight ? "On" : "Off")));
		this.buttonList.add(highlightBlockFaces = new GuiButton(18, this.width / 2 - 100, 205, 150, 20, "Highlight Block Faces: " + (CustomUISettings.highlightBlockFaces ? "On" : "Off")));

		this.buttonList.add(useButtonAnimation = new GuiButton(20, this.width / 2 - 100, 30, 200, 20, "Button Animations: " + (CustomUISettings.buttonAnimation ? "On" : "Off")));
		this.buttonList.add(buttonAnimationType = new GuiButton(21, this.width / 2 - 100, 55, 200, 20, "Button Animation Type: " + CustomUISettings.buttonAnimationType.getTypeName()));
		this.buttonList.add(animationSpeedSlider = new GuiSlider(22, "Speed", this.width / 2 - 75, 80, 0F, 40F, CustomUISettings.buttonAnimationSpeed, 1F));

		this.buttonList.add(useArmorHUD = new GuiButton(30, this.width / 2 - 100, 30, 200, 20, "Armor Gui Hud: " + (CustomUISettings.armorGuiHud ? "On" : "Off")));
		this.buttonList.add(armorHUDPosition = new GuiButton(31, this.width / 2 - 100, 55, 200, 20, "Change HUD Position"));

		this.setEditState(SettingEditing.None);
	}

	@SuppressWarnings("unchecked")
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
			int argb = (255 << 24) | (redSlider.getValueAdjusted(255) << 16) | (greenSlider.getValueAdjusted(255) << 8) | blueSlider.getValueAdjusted(255);
			gradientX = this.width / 2 + 120;
			this.drawGradientRect(gradientX - 5, gradientY - 5, gradientX + 37, gradientY + 37, -1, -1);
			this.drawGradientRect(gradientX, gradientY, gradientX + 32, gradientY + 32, argb, argb);
		}

		if(movingHUD)
		{
			RenderUtil.renderDurabilityHUD(mc, new ArrayList<ItemStack>(Arrays.asList(this.testItems)));
		}
	}

	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
	{
		if(movingHUD)
		{
			ScaledResolution scaledresolution = new ScaledResolution(mc);
			CustomUISettings.armorGuiHudX = (float) mouseX / (float) scaledresolution.getScaledWidth();
			CustomUISettings.armorGuiHudY = (float) (scaledresolution.getScaledHeight() - mouseY) / (float) scaledresolution.getScaledHeight();
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

				boolean goBack = false;
				if(this.editing == SettingEditing.None)
				{
					this.mc.displayGuiScreen(this.parentScreen);
				}
				else if(this.editing == SettingEditing.BlockHighlight)
				{
					CustomUIConfigLoader.saveBlockHighlightSettings(alphaSlider.getValue(), redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue(), thicknessSlider.getValueAdjusted(10.0F));
					goBack = true;
				}
				else if(this.editing == SettingEditing.GuiHighlight)
				{
					CustomUIConfigLoader.saveGuiHighlightSettings(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue());
					goBack = true;
				}
				else if(this.editing == SettingEditing.ButtonAnimation)
				{
					CustomUIConfigLoader.saveButtonAnimationSettings(this.animationSpeedSlider.getValueAdjusted(40.0F), CustomUISettings.buttonAnimationType);
					goBack = true;
				}
				else if(this.editing == SettingEditing.ArmorInfo)
				{
					if(this.movingHUD)
					{
						this.useArmorHUD.visible = true;
						this.armorHUDPosition.visible = true;
						this.movingHUD = false;
					}
					else
					{
						CustomUIConfigLoader.saveArmorInfoSettings();
						goBack = true;
					}
				}

				if(goBack)
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
			else if(button.id == 17)
			{
				CustomUISettings.highlightAffectedByLight = !CustomUISettings.highlightAffectedByLight;
				highlightAffectedByLight.displayString = "Highlight Dim: " + (CustomUISettings.highlightAffectedByLight ? "On" : "Off");
			}
			else if(button.id == 18)
			{
				CustomUISettings.highlightBlockFaces = !CustomUISettings.highlightBlockFaces;
				this.highlightBlockFaces.displayString = "Highlight Block Faces: " + (CustomUISettings.highlightBlockFaces ? "On" : "Off");
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
			else if(button.id == 22)
			{
				trackedSliderSelected = this.animationSpeedSlider;
			}
			else if(button.id == 30)
			{
				CustomUISettings.armorGuiHud = !CustomUISettings.armorGuiHud;
				this.useArmorHUD.displayString = "Armor Gui Hud: " + (CustomUISettings.armorGuiHud ? "On" : "Off");
			}
			else if(button.id == 31)
			{
				this.useArmorHUD.visible = false;
				this.armorHUDPosition.visible = false;
				this.movingHUD = true;
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
			else if(button.id == 1003)
			{
				this.setEditState(SettingEditing.ArmorInfo);
			}
		}
	}

	protected void mouseReleased(int mouseX, int mouseY, int state)
	{
		super.mouseReleased(mouseX, mouseY, state);
		if(trackedSliderSelected != null)
		{
			if(trackedSliderSelected.id == 22)
			{
				CustomUISettings.buttonAnimationSpeed = this.animationSpeedSlider.getValueAdjusted(40.0F);
				trackedSliderSelected = null;
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
		this.highlightAffectedByLight.visible = setting == SettingEditing.BlockHighlight;
		this.highlightBlockFaces.visible = setting == SettingEditing.BlockHighlight;
		this.useGuiHighlight.visible = setting == SettingEditing.GuiHighlight;
		this.hitBoxSettings.visible = setting == SettingEditing.None;
		this.guiOverlaySettings.visible = setting == SettingEditing.None;
		this.buttonAnimationSettings.visible = setting == SettingEditing.None;
		this.armorInfoSettings.visible = setting == SettingEditing.None;
		this.useButtonAnimation.visible = setting == SettingEditing.ButtonAnimation;
		this.buttonAnimationType.visible = setting == SettingEditing.ButtonAnimation;
		this.animationSpeedSlider.visible = setting == SettingEditing.ButtonAnimation;
		this.useArmorHUD.visible = setting == SettingEditing.ArmorInfo;
		this.armorHUDPosition.visible = setting == SettingEditing.ArmorInfo;

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
		None, BlockHighlight, GuiHighlight, ButtonAnimation, ArmorInfo;
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