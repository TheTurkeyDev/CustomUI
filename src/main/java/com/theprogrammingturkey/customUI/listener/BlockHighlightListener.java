package com.theprogrammingturkey.customUI.listener;

import com.theprogrammingturkey.customUI.config.CustomUISettings;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockHighlightListener
{

	@SubscribeEvent
	public void onBlockOutlineRender(DrawBlockHighlightEvent e)
	{
		this.drawSelectionBox(e.getPlayer(), e.getTarget(), e.getSubID(), e.getPartialTicks());
		if(!CustomUISettings.includeDefaultHighlight)
			e.setCanceled(true);
	}

	public void drawSelectionBox(EntityPlayer player, RayTraceResult movingObjectPositionIn, int execute, float partialTicks)
	{
		World theWorld = player.getEntityWorld();
		if(execute == 0 && movingObjectPositionIn.typeOfHit == RayTraceResult.Type.BLOCK)
		{
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			GlStateManager.glLineWidth(CustomUISettings.highlightLineThickness);
			GlStateManager.disableTexture2D();
			GlStateManager.depthMask(false);
			BlockPos blockpos = movingObjectPositionIn.getBlockPos();
			IBlockState iblockstate = theWorld.getBlockState(blockpos);

			if(iblockstate.getMaterial() != Material.AIR && theWorld.getWorldBorder().contains(blockpos))
			{
				double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
				double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
				double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;

				int redAmount = (int) (CustomUISettings.highlightColorR * 255);
				int greenAmount = (int) (CustomUISettings.highlightColorG * 255);
				int blueAmount = (int) (CustomUISettings.highlightColorB * 255);
				int alphaAmount = (int) (CustomUISettings.highlightColorA * 255);

				if(CustomUISettings.highlightAffectedByLight)
				{
					BlockPos lightCheckPos;

					switch(movingObjectPositionIn.sideHit)
					{
						case DOWN:
							lightCheckPos = blockpos.add(0, -1, 0);
							break;
						case EAST:
							lightCheckPos = blockpos.add(1, 0, 0);
							break;
						case NORTH:
							lightCheckPos = blockpos.add(0, 0, -1);
							break;
						case SOUTH:
							lightCheckPos = blockpos.add(0, 0, 1);
							break;
						case UP:
							lightCheckPos = blockpos.add(0, 1, 0);
							break;
						case WEST:
							lightCheckPos = blockpos.add(-1, 0, 0);
							break;
						default:
							lightCheckPos = blockpos;
							break;
					}

					int light = (int)((15.0f - (float)theWorld.getLight(lightCheckPos)) / 6.0f);

					if(light > 0)
					{
						redAmount /= light;
						greenAmount /= light;
						blueAmount /= light;
						alphaAmount /= light;
					}
				}

				

				RenderGlobal.drawOutlinedBoundingBox(iblockstate.getSelectedBoundingBox(theWorld, blockpos).expandXyz(0.0020000000949949026D).offset(-d0, -d1, -d2), redAmount, greenAmount, blueAmount, alphaAmount);
			}

			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		}
	}
}
