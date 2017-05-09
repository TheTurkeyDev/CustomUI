package com.theprogrammingturkey.customUI.util;

import java.util.List;

import com.theprogrammingturkey.customUI.config.CustomUISettings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;

public class RenderUtil
{

	public static int colorIntFromRGBA(int r, int g, int b, int a)
	{
		return (a << 24) | (r << 16) | (g << 8) | (b);
	}

	public static int colorIntFromRGBA(float r, float g, float b, float a)
	{
		return RenderUtil.colorIntFromRGBA((int) (r * 255), (int) (g * 255), (int) (b * 255), (int) (a * 255));
	}

	public static void renderDurabilityHUD(Minecraft mc, List<ItemStack> items)
	{
		RenderItem irender = mc.getRenderItem();
		FontRenderer frender = mc.fontRendererObj;
		ScaledResolution scaledresolution = new ScaledResolution(mc);

		int left = (int) (CustomUISettings.armorGuiHudX * scaledresolution.getScaledWidth());
		int bottom = (int) (scaledresolution.getScaledHeight() - (CustomUISettings.armorGuiHudY * scaledresolution.getScaledHeight()));

		int i = 0;
		int offset = 0;
		float percent = 1;
		int color = 0xFFFFFF;
		GlStateManager.pushMatrix();
		boolean drawn = false;

		for(ItemStack stack : items)
		{
			if(stack != null && stack.isItemStackDamageable())
			{
				if(!drawn)
				{
					drawGradientRect(left, bottom - 100, left + 90, bottom, 169416985, 169416985);
					drawn = true;
				}
				offset = (16 * i) + 17;
				percent = ((float) stack.getMaxDamage() - (float) stack.getItemDamage()) / (float) stack.getMaxDamage();
				irender.renderItemAndEffectIntoGUI(stack, left + 5, bottom - offset);
				color = RenderUtil.colorIntFromRGBA(1 - percent, percent, 0f, 1f);
				frender.drawString("" + (stack.getMaxDamage() - stack.getItemDamage()) + "/" + stack.getMaxDamage(), left + 25, bottom - (offset - 4), color);
			}
			i++;
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
		mc.getTextureManager().bindTexture(Gui.ICONS);
	}

	public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
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
		vertexbuffer.pos((double) right, (double) top, 0.0).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos((double) left, (double) top, 0.0).color(f1, f2, f3, f).endVertex();
		vertexbuffer.pos((double) left, (double) bottom, 0.0).color(f5, f6, f7, f4).endVertex();
		vertexbuffer.pos((double) right, (double) bottom, 0.0).color(f5, f6, f7, f4).endVertex();
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}
}