package com.theprogrammingturkey.customUI.util;

import java.util.List;

import com.theprogrammingturkey.customUI.config.CustomUISettings;
import com.theprogrammingturkey.gobblecore.util.RenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class CustomUIRenderer
{
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
					RenderUtil.drawGradientRect(left, bottom - 100, left + 90, bottom, 169416985, 169416985);
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
}