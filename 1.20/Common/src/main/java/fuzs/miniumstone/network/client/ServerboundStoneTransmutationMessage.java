package fuzs.miniumstone.network.client;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.util.MiniumStoneHelper;
import fuzs.miniumstone.world.item.crafting.TransmutationInWorldRecipe;
import fuzs.puzzleslib.api.network.v3.ServerMessageListener;
import fuzs.puzzleslib.api.network.v3.ServerboundMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.List;

public record ServerboundStoneTransmutationMessage(int selectedItem, InteractionHand interactionHand, BlockPos pos, List<BlockPos> blocks, boolean reverse, ResourceLocation recipe) implements ServerboundMessage<ServerboundStoneTransmutationMessage> {

    @Override
    public ServerMessageListener<ServerboundStoneTransmutationMessage> getHandler() {
        return new ServerMessageListener<>() {

            @Override
            public void handle(ServerboundStoneTransmutationMessage message, MinecraftServer server, ServerGamePacketListenerImpl handler, ServerPlayer player, ServerLevel level) {
                handler.handleSetCarriedItem(new ServerboundSetCarriedItemPacket(message.selectedItem));
                ItemStack itemInHand = player.getItemInHand(message.interactionHand);
                if (itemInHand.is(ModRegistry.MINIUM_STONE_ITEM.get())) {
                    TransmutationInWorldRecipe recipe = level.getRecipeManager().byKey(message.recipe).map(TransmutationInWorldRecipe.class::cast).orElse(null);
                    if (recipe != null) {
                        Block ingredient = message.reverse ? recipe.result() : recipe.ingredient();
                        Block result = message.reverse ? recipe.ingredient() : recipe.result();
                        MiniumStoneHelper.transmuteBlocks(message.pos, message.blocks, level, ingredient, result, itemInHand);
                        itemInHand.hurtAndBreak(1, player, player1 -> {
                            player1.broadcastBreakEvent(message.interactionHand);
                        });
                    }
                }
            }
        };
    }
}
