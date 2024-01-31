package fuzs.miniumstone.client.handler;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.network.client.ServerboundStoneTransmutationMessage;
import fuzs.miniumstone.util.MiniumStoneHelper;
import fuzs.miniumstone.world.item.crafting.TransmutationInWorldRecipe;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class StoneTransmuteHandler {

    public static EventResultHolder<InteractionResult> onUseBlock(Player player, Level level, InteractionHand interactionHand, BlockHitResult hitResult) {
        ItemStack itemInHand = player.getItemInHand(interactionHand);
        if (itemInHand.is(ModRegistry.MINIUM_STONE_ITEM.get()) && level.isClientSide) {
            BlockWalker blockWalker = TransmutateShapeRenderingHandler.getBlockWalker();
            if (blockWalker != null) {
                boolean shiftKeyDown = player.isShiftKeyDown();
                TransmutationInWorldRecipe recipe = blockWalker.getRecipe(shiftKeyDown);
                if (recipe != null) {
                    BlockPos blockPos = blockWalker.getBlockPos();
                    List<BlockPos> blocks = blockWalker.getBlocks(level, shiftKeyDown);
                    MiniumStoneHelper.transmuteBlocks(blockPos, blocks, level, shiftKeyDown ? recipe.result() : recipe.ingredient(), shiftKeyDown ? recipe.ingredient() : recipe.result(), itemInHand);
                    int selectedSlot = player.getInventory().selected;
                    MiniumStone.NETWORK.sendToServer(new ServerboundStoneTransmutationMessage(selectedSlot, interactionHand, blockPos, blocks, shiftKeyDown, recipe.getId()));
                    TransmutateShapeRenderingHandler.clearBlockWalker();
                    TransmutationResultGuiHandler.setBlockPopTime(5);
                    return EventResultHolder.interrupt(InteractionResult.SUCCESS);
                }
            }
            return EventResultHolder.interrupt(InteractionResult.PASS);
        }
        return EventResultHolder.pass();
    }
}
