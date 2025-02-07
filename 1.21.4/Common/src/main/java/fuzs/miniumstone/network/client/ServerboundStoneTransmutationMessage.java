package fuzs.miniumstone.network.client;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.util.MiniumStoneHelper;
import fuzs.miniumstone.world.item.crafting.TransmutationInWorldRecipe;
import fuzs.puzzleslib.api.item.v2.ItemHelper;
import fuzs.puzzleslib.api.network.v3.ServerMessageListener;
import fuzs.puzzleslib.api.network.v3.ServerboundMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;

import java.util.List;

public record ServerboundStoneTransmutationMessage(int selectedItem,
                                                   InteractionHand interactionHand,
                                                   BlockPos pos,
                                                   List<BlockPos> blocks,
                                                   boolean reverse,
                                                   ResourceKey<Recipe<?>> recipe) implements ServerboundMessage<ServerboundStoneTransmutationMessage> {

    @Override
    public ServerMessageListener<ServerboundStoneTransmutationMessage> getHandler() {
        return new ServerMessageListener<>() {

            @Override
            public void handle(ServerboundStoneTransmutationMessage message, MinecraftServer server, ServerGamePacketListenerImpl handler, ServerPlayer player, ServerLevel level) {
                handler.handleSetCarriedItem(new ServerboundSetCarriedItemPacket(message.selectedItem));
                ItemStack itemInHand = player.getItemInHand(message.interactionHand);
                if (itemInHand.is(ModRegistry.MINIUM_STONE_ITEM.value())) {
                    TransmutationInWorldRecipe recipe = level.recipeAccess().byKey(message.recipe).map(
                            RecipeHolder::value).map(TransmutationInWorldRecipe.class::cast).orElse(null);
                    if (recipe != null) {
                        Block ingredient = message.reverse ? recipe.getBlockResult() : recipe.getBlockIngredient();
                        Block result = message.reverse ? recipe.getBlockIngredient() : recipe.getBlockResult();
                        MiniumStoneHelper.transmuteBlocks(message.pos, message.blocks, level, ingredient, result,
                                itemInHand
                        );
                        ItemHelper.hurtAndBreak(itemInHand, 1, player, message.interactionHand);
                    }
                }
            }
        };
    }
}
