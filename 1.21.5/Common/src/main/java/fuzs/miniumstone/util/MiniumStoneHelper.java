package fuzs.miniumstone.util;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.network.ClientboundTransmutationParticleMessage;
import fuzs.miniumstone.world.item.MiniumStoneItem;
import fuzs.miniumstone.world.item.crafting.PlayerRecipeInput;
import fuzs.puzzleslib.api.item.v2.ItemHelper;
import fuzs.puzzleslib.api.network.v4.MessageSender;
import fuzs.puzzleslib.api.network.v4.PlayerSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MiniumStoneHelper {

    @Nullable
    public static InteractionHand getMiniumStoneHand(Player player) {
        for (InteractionHand interactionHand : InteractionHand.values()) {
            ItemStack itemInHand = player.getItemInHand(interactionHand);
            if (itemInHand.is(ModRegistry.MINIUM_STONE_ITEM.value())) {
                return interactionHand;
            }
        }
        return null;
    }

    public static void transmuteBlocks(BlockPos blockPosition, List<BlockPos> blocks, Level level, Block ingredient, Block result, ItemStack itemInHand) {
        int miniumStoneCharge = MiniumStoneItem.getCharge(itemInHand) * 3;
        boolean transformedAny = false;
        for (BlockPos blockPos : blocks) {
            if (blockPosition.distManhattan(blockPos) <= miniumStoneCharge) {
                BlockState oldState = level.getBlockState(blockPos);
                if (oldState.is(ingredient)) {
                    BlockState newState = result.defaultBlockState();
                    newState = copyBlockStateValues(oldState, newState);
                    level.setBlockAndUpdate(blockPos, newState);
                    transformedAny = true;
                    if (level instanceof ServerLevel serverLevel) {
                        blockPos = blockPos.above();
                        if (serverLevel.getBlockState(blockPos).isAir()) {
                            for (int i = 0; i < 2; i++) {
                                if (serverLevel.random.nextBoolean()) {
                                    MessageSender.broadcast(PlayerSet.nearPosition(blockPos, serverLevel),
                                            new ClientboundTransmutationParticleMessage(blockPos, i == 0));
                                }
                            }
                        }
                    }
                }
            }
        }
        if (transformedAny) {
            level.playSound(null,
                    blockPosition,
                    ModRegistry.ITEM_MINIUM_STONE_TRANSMUTE_SOUND_EVENT.value(),
                    SoundSource.BLOCKS,
                    1.0F,
                    1.0F);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>, V extends T> BlockState copyBlockStateValues(BlockState oldState, BlockState newState) {
        for (Map.Entry<Property<?>, Comparable<?>> entry : oldState.getValues().entrySet()) {
            newState = newState.trySetValue((Property<T>) entry.getKey(), (V) entry.getValue());
        }
        return newState;
    }

    public static NonNullList<ItemStack> damageMiniumStoneClearRest(CraftingInput craftingInput) {
        NonNullList<ItemStack> items = NonNullList.withSize(craftingInput.size(), ItemStack.EMPTY);
        for (int i = 0; i < items.size(); ++i) {
            ItemStack itemStack = craftingInput.getItem(i);
            if (itemStack.is(ModRegistry.MINIUM_STONE_ITEM.value())) {
                itemStack = itemStack.copy();
                Player player = ((PlayerRecipeInput) craftingInput).miniumstone$getPlayer();
                if (player instanceof ServerPlayer serverPlayer) {
                    ItemHelper.hurtAndBreak(itemStack,
                            1,
                            serverPlayer.serverLevel(),
                            serverPlayer,
                            Function.identity()::apply);
                } else {
                    hurtAndBreak(itemStack, 1);
                }
                items.set(i, itemStack.isEmpty() ? ItemStack.EMPTY : itemStack);
                break;
            } else if (!itemStack.getItem().getCraftingRemainder().isEmpty()) {
                items.set(i, itemStack.getItem().getCraftingRemainder());
            }
        }
        return items;
    }

    public static void hurtAndBreak(ItemStack itemStack, int damageValue) {
        if (itemStack.isDamageableItem()) {
            int newDamageValue = itemStack.getDamageValue() + damageValue;
            itemStack.setDamageValue(newDamageValue);
            if (newDamageValue >= itemStack.getMaxDamage()) {
                itemStack.shrink(1);
            }
        }
    }
}
