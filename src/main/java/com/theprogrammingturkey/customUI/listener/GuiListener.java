package com.theprogrammingturkey.customUI.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.theprogrammingturkey.customUI.client.gui.ConfigGui.ButtonAnimationType;
import com.theprogrammingturkey.customUI.config.CustomUISettings;
import com.theprogrammingturkey.customUI.util.CustomUIRenderer;
import com.theprogrammingturkey.gobblecore.client.gui.GuiToggleButton;
import com.theprogrammingturkey.gobblecore.util.CustomEntry;
import com.theprogrammingturkey.gobblecore.util.MathUtil;
import com.theprogrammingturkey.gobblecore.util.RenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent.BackgroundDrawnEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiListener
{
	protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");

	private CustomEntry<GuiButton, Float> animationProgress;

	@SubscribeEvent
	public void onBackgroundDraw(BackgroundDrawnEvent e)
	{
		if(CustomUISettings.guiHighlight && e.getGui() instanceof GuiContainer)
		{
			GuiContainer gui = (GuiContainer) e.getGui();
			Slot s = gui.getSlotUnderMouse();
			if(s == null)
				return;
			int left = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, "guiLeft", "field_147003_i");
			int top = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, "guiTop", "field_147009_r");

			GlStateManager.translate(left, top, 50.0F);
			int j1 = s.xDisplayPosition;
			int k1 = s.yDisplayPosition;
			GlStateManager.colorMask(true, true, true, true);
			int argb = ((int) 255 << 24) | ((int) (CustomUISettings.guihighlightColorR * 255) << 16) | ((int) (CustomUISettings.guihighlightColorG * 255) << 8) | (int) (CustomUISettings.guihighlightColorB * 255);
			RenderUtil.drawGradientRect(j1, k1, j1 + 16, k1 + 16, argb, argb);
			GlStateManager.colorMask(true, true, true, true);
			GlStateManager.translate(-left, -top, -50.0F);
		}
	}

	@SubscribeEvent
	public void onGuiDraw(DrawScreenEvent.Pre e)
	{
	}

	@SubscribeEvent
	public void onGuiDraw(DrawScreenEvent.Post e)
	{
		if(CustomUISettings.buttonAnimation)
		{
			List<GuiButton> buttons = ObfuscationReflectionHelper.getPrivateValue(GuiScreen.class, e.getGui(), "buttonList", "field_146292_n");

			if(animationProgress != null && (!animationProgress.getKey().isMouseOver() || !animationProgress.getKey().visible))
				animationProgress = null;

			for(GuiButton b : buttons)
			{
				if(!b.getClass().equals(GuiButton.class) && !b.getClass().equals(GuiToggleButton.class))
					continue;
				if(b.isMouseOver() && b.visible && b.enabled)
					if(animationProgress == null || !animationProgress.getKey().equals(b))
						animationProgress = new CustomEntry<GuiButton, Float>(b, CustomUISettings.buttonAnimationSpeed);
			}

			if(animationProgress != null && animationProgress.getValue() >= 0 && buttons.contains(animationProgress.getKey()))
			{
				animationProgress.setValue(animationProgress.getValue() - (20.0f / Minecraft.getDebugFPS()));

				GuiButton b = animationProgress.getKey();
				Minecraft mc = Minecraft.getMinecraft();
				FontRenderer fontrenderer = mc.fontRendererObj;
				GlStateManager.pushMatrix();
				mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
				GlStateManager.enableBlend();
				GlStateManager.disableAlpha();
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

				float alpha = 1;
				float x1 = b.xPosition;
				float x2 = b.xPosition + (b.width / 2);
				float y1 = b.yPosition;
				int uCord2 = b.width / 2;
				int uCord4 = b.width / 2;
				int uCord3 = 200 - uCord2;
				int uCord1 = 0;
				int vCord0 = 86;
				int vCord1 = 66;
				int vCord2 = b.height;

				if(CustomUISettings.buttonAnimationType == ButtonAnimationType.FadeIn)
				{
					alpha = animationProgress.getValue() / CustomUISettings.buttonAnimationSpeed;
				}
				else if(CustomUISettings.buttonAnimationType == ButtonAnimationType.SlideUp)
				{
					vCord2 = (int) ((b.height / CustomUISettings.buttonAnimationSpeed) * animationProgress.getValue());
				}
				else if(CustomUISettings.buttonAnimationType == ButtonAnimationType.SlideLeft)
				{
					if(animationProgress.getValue() > CustomUISettings.buttonAnimationSpeed / 2)
					{
						uCord4 = (int) (b.width * ((animationProgress.getValue() - (CustomUISettings.buttonAnimationSpeed / 2)) / CustomUISettings.buttonAnimationSpeed));
					}
					else
					{
						uCord4 = 0;
						uCord2 = (int) (b.width * (animationProgress.getValue() / CustomUISettings.buttonAnimationSpeed));
					}
				}
				else if(CustomUISettings.buttonAnimationType == ButtonAnimationType.SlideRight)
				{
					vCord0 = 66;
					vCord1 = 86;
					if(animationProgress.getValue() > CustomUISettings.buttonAnimationSpeed / 2)
					{
						uCord4 = 0;
						uCord2 = (int) (b.width * ((CustomUISettings.buttonAnimationSpeed - animationProgress.getValue()) / CustomUISettings.buttonAnimationSpeed));
					}
					else
					{
						uCord4 = (int) (b.width * (((CustomUISettings.buttonAnimationSpeed / 2) - animationProgress.getValue()) / CustomUISettings.buttonAnimationSpeed));
					}
				}
				else if(CustomUISettings.buttonAnimationType == ButtonAnimationType.SlideIn)
				{
					boolean right = b.xPosition < e.getGui().width / 2;
					if(right)
					{
						vCord0 = 66;
						vCord1 = 86;
					}
					if(animationProgress.getValue() > CustomUISettings.buttonAnimationSpeed / 2)
					{
						if(right)
						{
							uCord4 = 0;
							uCord2 = (int) (b.width * ((CustomUISettings.buttonAnimationSpeed - animationProgress.getValue()) / CustomUISettings.buttonAnimationSpeed));
						}
						else
						{
							uCord4 = (int) (b.width * ((animationProgress.getValue() - (CustomUISettings.buttonAnimationSpeed / 2)) / CustomUISettings.buttonAnimationSpeed));
						}
					}
					else
					{
						if(right)
						{
							uCord4 = (int) (b.width * (((CustomUISettings.buttonAnimationSpeed / 2) - animationProgress.getValue()) / CustomUISettings.buttonAnimationSpeed));
						}
						else
						{
							uCord4 = 0;
							uCord2 = (int) (b.width * (animationProgress.getValue() / CustomUISettings.buttonAnimationSpeed));
						}

					}
				}
				else if(CustomUISettings.buttonAnimationType == ButtonAnimationType.None)
				{
					uCord2 = 0;
					uCord4 = 0;
				}

				uCord2 = MathUtil.clamp(0, (b.width / 2), uCord2);
				uCord4 = MathUtil.clamp(0, (b.width / 2), uCord4);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				b.drawTexturedModalRect(x1, y1, 0, vCord0, b.width / 2, vCord2);
				b.drawTexturedModalRect(x2, y1, uCord3, vCord0, b.width / 2, vCord2);
				GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
				b.drawTexturedModalRect(x1, y1, uCord1, vCord1, uCord2, vCord2);
				b.drawTexturedModalRect(x2, y1, uCord3, vCord1, uCord4, vCord2);

				int j = 14737632;

				if(b.packedFGColour != 0)
				{
					j = b.packedFGColour;
				}
				else if(!b.enabled)
				{
					j = 10526880;
				}
				else if(b.isMouseOver())
				{
					j = 16777120;
				}

				b.drawCenteredString(fontrenderer, b.displayString, b.xPosition + b.width / 2, b.yPosition + (b.height - 8) / 2, j);
				GlStateManager.disableBlend();
				GlStateManager.disableAlpha();
				GlStateManager.popMatrix();
			}
		}
	}

	@SubscribeEvent
	public void onGameRender(RenderGameOverlayEvent.Post e)
	{
		if(CustomUISettings.armorGuiHud)
		{
			Minecraft mc = Minecraft.getMinecraft();
			if(mc.getRenderViewEntity() instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) mc.getRenderViewEntity();
				List<ItemStack> stacks = new ArrayList<ItemStack>();
				stacks.addAll(Arrays.asList(player.inventory.armorInventory));
				stacks.addAll(Arrays.asList(player.inventory.offHandInventory));
				stacks.add(player.inventory.mainInventory[player.inventory.currentItem]);
				CustomUIRenderer.renderDurabilityHUD(mc, stacks);
			}
		}
	}
}
