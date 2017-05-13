package com.theprogrammingturkey.customUI.listener;

import com.theprogrammingturkey.customUI.config.CustomUISettings;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumSkyBlock;
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
				float redAmountHighlight = CustomUISettings.highlightColorR;
				float greenAmountHighlight = CustomUISettings.highlightColorG;
				float blueAmountHighlight = CustomUISettings.highlightColorB;
				float alphaAmountHighlight = CustomUISettings.highlightColorA;
				float redAmountFill = CustomUISettings.fillColorR;
				float greenAmountFill = CustomUISettings.fillColorG;
				float blueAmountFill = CustomUISettings.fillColorB;
				float alphaAmountFill = CustomUISettings.fillColorA;

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

					int blockLight = theWorld.getLightFor(EnumSkyBlock.BLOCK, lightCheckPos);
					int skyLight = theWorld.getLightFor(EnumSkyBlock.SKY, lightCheckPos) - theWorld.getSkylightSubtracted();
					float light = (float) (blockLight > skyLight ? blockLight : skyLight) / 15f;
					light = light < 0.5f ? 0.5f : light;
					if(light > 0)
					{
						redAmountHighlight *= light;
						greenAmountHighlight *= light;
						blueAmountHighlight *= light;
						alphaAmountHighlight *= light;
						redAmountFill *= light;
						greenAmountFill *= light;
						blueAmountFill *= light;
						alphaAmountFill *= light;
					}
				}

				if(CustomUISettings.highlightBlockFaces)
					RenderGlobal.func_189696_b(iblockstate.getSelectedBoundingBox(theWorld, blockpos).expandXyz(0.0020000000949949026D).offset(-d0, -d1, -d2), redAmountFill, greenAmountFill, blueAmountFill, alphaAmountFill);

				RenderGlobal.func_189697_a(iblockstate.getSelectedBoundingBox(theWorld, blockpos).expandXyz(0.0020000000949949026D).offset(-d0, -d1, -d2), redAmountHighlight, greenAmountHighlight, blueAmountHighlight, alphaAmountHighlight);
			}

			GlStateManager.depthMask(true);
			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
		}
	}
}
