package fuzs.miniumstone.util;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.network.ClientboundTransmutationParticleMessage;
import fuzs.miniumstone.world.item.MiniumStoneItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

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
        for (BlockPos pos : blocks) {
            if (blockPosition.distManhattan(pos) <= miniumStoneCharge) {
                BlockState oldState = level.getBlockState(pos);
                if (oldState.is(ingredient)) {
                    BlockState newState = result.defaultBlockState();
                    newState = copyBlockStateValues(oldState, newState);
                    level.setBlockAndUpdate(pos, newState);
                    transformedAny = true;
                    if (!level.isClientSide) {
                        pos = pos.above();
                        if (level.getBlockState(pos).isAir()) {
                            for (int i = 0; i < 2; i++) {
                                if (level.random.nextBoolean()) {
                                    MiniumStone.NETWORK.sendToAllNear(pos,
                                            (ServerLevel) level, new ClientboundTransmutationParticleMessage(pos, i == 0));
                                }
                            }
                        }
                    }
                }
            }
        }
        if (transformedAny) level.playSound(null, blockPosition, ModRegistry.ITEM_MINIUM_STONE_TRANSMUTE_SOUND_EVENT.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>, V extends T> BlockState copyBlockStateValues(BlockState oldState, BlockState newState) {
        for (Map.Entry<Property<?>, Comparable<?>> entry : oldState.getValues().entrySet()) {
            newState = newState.trySetValue((Property<T>) entry.getKey(), (V) entry.getValue());
        }
        return newState;
    }

    public static NonNullList<ItemStack> damageMiniumStoneClearRest(CraftingContainer container) {
        NonNullList<ItemStack> items = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < items.size(); ++i) {
            ItemStack itemstack = container.getItem(i);
            if (itemstack.getItem().hasCraftingRemainingItem()) {
                items.set(i, new ItemStack(itemstack.getItem().getCraftingRemainingItem()));
            } else if (itemstack.is(ModRegistry.MINIUM_STONE_ITEM.value())) {
                itemstack = itemstack.copy();
                if (itemstack.hurt(1, RandomSource.create(), null)) {
                    itemstack = ItemStack.EMPTY;
                }
                items.set(i, itemstack);
                break;
            }
        }
        return items;
    }
}
