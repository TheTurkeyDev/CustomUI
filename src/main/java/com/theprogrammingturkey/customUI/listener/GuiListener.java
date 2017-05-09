package com.theprogrammingturkey.customUI.listener;

import java.util.ArrayList;
import java.util.List;

import com.theprogrammingturkey.customUI.client.gui.ConfigGui.ButtonAnimationType;
import com.theprogrammingturkey.customUI.config.CustomUISettings;
import com.theprogrammingturkey.customUI.util.CustomEntry;
import com.theprogrammingturkey.customUI.util.RenderUtil;

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
		if(!CustomUISettings.guiHighlight)
			return;

		if(e.getGui() instanceof GuiContainer)
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
	public void onGuiDraw(DrawScreenEvent.Post e)
	{
		if(CustomUISettings.buttonAnimation)
		{
			List<GuiButton> buttons = ObfuscationReflectionHelper.getPrivateValue(GuiScreen.class, e.getGui(), "buttonList", "field_146292_n");

			if(animationProgress != null && (!animationProgress.getKey().isMouseOver() || !animationProgress.getKey().visible))
				animationProgress = null;

			for(GuiButton b : buttons)
			{
				if(b.getClass() != GuiButton.class)
					continue;
				if(b.isMouseOver() && b.visible)
					if(animationProgress == null || !animationProgress.getKey().equals(b))
						animationProgress = new CustomEntry<GuiButton, Float>(b, CustomUISettings.buttonAnimationSpeed);
			}

			if(animationProgress != null && animationProgress.getValue() >= 0)
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

				if(CustomUISettings.buttonAnimationType == ButtonAnimationType.FadeIn)
				{
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1 - (animationProgress.getValue() / CustomUISettings.buttonAnimationSpeed));
					b.drawTexturedModalRect(b.xPosition, b.yPosition, 0, 86, b.width, b.height);
					b.drawTexturedModalRect(b.xPosition + b.width / 2, b.yPosition, 200 - b.width / 2, 86, b.width / 2, b.height);
					GlStateManager.color(1.0F, 1.0F, 1.0F, animationProgress.getValue() / CustomUISettings.buttonAnimationSpeed);
					b.drawTexturedModalRect(b.xPosition, b.yPosition, 0, 66, b.width, b.height);
					b.drawTexturedModalRect(b.xPosition + b.width / 2, b.yPosition, 200 - b.width / 2, 66, b.width / 2, b.height);
				}
				else if(CustomUISettings.buttonAnimationType == ButtonAnimationType.SlideUp)
				{
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
					b.drawTexturedModalRect(b.xPosition, b.yPosition, 0, 86, b.width, b.height);
					b.drawTexturedModalRect(b.xPosition + b.width / 2, b.yPosition, 200 - b.width / 2, 86, b.width / 2, b.height);
					b.drawTexturedModalRect(b.xPosition, b.yPosition, 0, 66, b.width, (int) ((b.height / CustomUISettings.buttonAnimationSpeed) * animationProgress.getValue()));
					b.drawTexturedModalRect(b.xPosition + b.width / 2, b.yPosition, 200 - b.width / 2, 66, b.width / 2, (int) ((b.height / CustomUISettings.buttonAnimationSpeed) * animationProgress.getValue()));
				}

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
				stacks.addAll(player.inventory.armorInventory);
				stacks.addAll(player.inventory.offHandInventory);
				stacks.add(player.inventory.mainInventory.get(player.inventory.currentItem));
				RenderUtil.renderDurabilityHUD(mc, stacks);
			}
		}
	}
}
