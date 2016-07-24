package com.theprogrammingturkey.customUI.listener;

import com.theprogrammingturkey.customUI.CustomUICore;
import com.theprogrammingturkey.customUI.config.CustomUISettings;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.Slot;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiListener
{

	private double zLevel = 0.0;

	@SubscribeEvent
	public void onGuiDraw(DrawScreenEvent.Post e)
	{
		if(e.getGui() instanceof GuiContainer)
		{
			GuiContainer gui = (GuiContainer) e.getGui();
			Slot s = gui.getSlotUnderMouse();
			if(s == null)
				return;
			int left;
			int top;
			if(CustomUICore.VERSION.equalsIgnoreCase("@Version@"))
			{
				left = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, "guiLeft");
				top = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, "guiTop");
			}
			else
			{
				left = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, "field_147003_i");
				top = ObfuscationReflectionHelper.getPrivateValue(GuiContainer.class, gui, "field_147009_r");
			}
			GlStateManager.translate(left, top, 0.0F);
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			int j1 = s.xDisplayPosition;
			int k1 = s.yDisplayPosition;
			GlStateManager.colorMask(true, true, true, false);
			int argb = ((int) (CustomUISettings.guihighlightColorA * 255) << 24) | ((int) (CustomUISettings.guihighlightColorR * 255) << 16) | ((int) (CustomUISettings.guihighlightColorG * 255) << 8) | (int) (CustomUISettings.guihighlightColorB * 255);
			this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, argb, argb);
			GlStateManager.colorMask(true, true, true, true);
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
		}
	}

	protected void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
	{
		float f = (float) (startColor >> 24 & 255) / 255.0F;
		float f1 = (float) (startColor >> 16 & 255) / 255.0F;
		float f2 = (float) (startColor >> 8 & 255) / 255.0F;
		float f3 = (float) (startColor & 255) / 255.0F;
		float f4 = (float) (endColor >> 24 & 255) / 255.0F;
		float f5 = (float) (endColor >> 16 & 255) / 255.0F;
		float f6 = (float) (endColor >> 8 & 255) / 255.0F;
		float f7 = (float) (endColor & 255) / 255.0F;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		vertexbuffer.pos((double) right, (double) top, this.zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos((double) left, (double) top, this.zLevel).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos((double) left, (double) bottom, this.zLevel).color(f5, f6, f7, f4).endVertex();
		vertexbuffer.pos((double) right, (double) bottom, this.zLevel).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}
}
