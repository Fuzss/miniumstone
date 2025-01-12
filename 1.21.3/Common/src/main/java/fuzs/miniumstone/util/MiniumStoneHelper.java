package fuzs.miniumstone.util;

import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.network.ClientboundTransmutationParticleMessage;
import fuzs.miniumstone.world.item.MiniumStoneItem;
import fuzs.miniumstone.world.item.crafting.PlayerRecipeInput;
import fuzs.puzzleslib.api.item.v2.ItemHelper;
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
                                    MiniumStone.NETWORK.sendToAllNear(pos, (ServerLevel) level,
                                            new ClientboundTransmutationParticleMessage(pos, i == 0)
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }
        if (transformedAny) {
            level.playSound(null, blockPosition, ModRegistry.ITEM_MINIUM_STONE_TRANSMUTE_SOUND_EVENT.value(),
                    SoundSource.BLOCKS, 1.0F, 1.0F
            );
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
            ItemStack itemstack = craftingInput.getItem(i);
            if (itemstack.getItem().hasCraftingRemainingItem()) {
                items.set(i, new ItemStack(itemstack.getItem().getCraftingRemainingItem()));
            } else if (itemstack.is(ModRegistry.MINIUM_STONE_ITEM.value())) {
                itemstack = itemstack.copy();
                Player player = ((PlayerRecipeInput) craftingInput).miniumstone$getPlayer();
                if (player instanceof ServerPlayer serverPlayer) {
                    ItemHelper.hurtAndBreak(itemstack, 1, serverPlayer.serverLevel(), serverPlayer,
                            Function.identity()::apply
                    );
                } else {
                    hurtAndBreak(itemstack, 1);
                }
                items.set(i, itemstack.isEmpty() ? ItemStack.EMPTY : itemstack);
                break;
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
