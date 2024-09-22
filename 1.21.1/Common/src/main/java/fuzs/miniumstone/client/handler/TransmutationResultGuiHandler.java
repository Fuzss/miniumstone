package fuzs.miniumstone.client.handler;

import fuzs.miniumstone.util.MiniumStoneHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class TransmutationResultGuiHandler {
    protected static final ResourceLocation HOTBAR_OFFHAND_LEFT_SPRITE = new ResourceLocation("hud/hotbar_offhand_left");
    protected static final ResourceLocation HOTBAR_OFFHAND_RIGHT_SPRITE = new ResourceLocation(
            "hud/hotbar_offhand_right");

    private static int blockPopTime;

    public static void onClientTick$End(Minecraft minecraft) {
        if (blockPopTime > 0) blockPopTime--;
    }

    public static void onRenderGui(Minecraft minecraft, GuiGraphics guiGraphics, float tickDelta, int screenWidth, int screenHeight) {
        if (MiniumStoneHelper.getMiniumStoneHand(minecraft.player) != null) {
            BlockWalker blockWalker = TransmutateShapeRenderingHandler.getBlockWalker();
            if (blockWalker != null) {
                Block block = blockWalker.getResult(minecraft.player.isShiftKeyDown());
                if (block != null) {
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().translate(0.0F, 0.0F, -90.0F);
                    ItemStack itemStack = new ItemStack(block);
                    itemStack.setPopTime(blockPopTime);
                    HumanoidArm humanoidArm = minecraft.player.getMainArm().getOpposite();
                    if (humanoidArm == HumanoidArm.LEFT) {
                        guiGraphics.blitSprite(HOTBAR_OFFHAND_LEFT_SPRITE,
                                screenWidth / 2 - 91 - 29 * 2,
                                screenHeight - 23,
                                29,
                                24
                        );
                        renderItemWithPopTime(minecraft.player,
                                guiGraphics,
                                tickDelta,
                                itemStack,
                                screenWidth / 2 - 91 - 29 * 2 + 3,
                                screenHeight - 23 + 4
                        );
                    } else {
                        guiGraphics.blitSprite(HOTBAR_OFFHAND_RIGHT_SPRITE,
                                screenWidth / 2 + 91 + 29,
                                screenHeight - 23,
                                29,
                                24
                        );
                        renderItemWithPopTime(minecraft.player,
                                guiGraphics,
                                tickDelta,
                                itemStack,
                                screenWidth / 2 + 91 + 29 + 10,
                                screenHeight - 23 + 4
                        );
                    }
                    guiGraphics.pose().popPose();
                    ;
                }
            }
        }
    }

    private static void renderItemWithPopTime(Player player, GuiGraphics guiGraphics, float tickDelta, ItemStack itemStack, int posX, int posY) {

        float popTime = (float) itemStack.getPopTime() - tickDelta;

        if (popTime > 0.0F) {
            float h = 1.0F + popTime / 5.0F;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate((float) (posX + 8), (float) (posY + 12), 0.0F);
            guiGraphics.pose().scale(1.0F / h, (h + 1.0F) / 2.0F, 1.0F);
            guiGraphics.pose().translate((float) (-(posX + 8)), (float) (-(posY + 12)), 0.0F);
        }

        guiGraphics.renderItem(player, itemStack, posX, posY, 0);

        if (popTime > 0.0F) {
            guiGraphics.pose().popPose();
        }
    }

    public static void setBlockPopTime(int blockPopTime) {
        TransmutationResultGuiHandler.blockPopTime = blockPopTime;
    }
}
