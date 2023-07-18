package fuzs.miniumstone.client.handler;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.networking.client.ServerboundChargeStoneMessage;
import fuzs.miniumstone.networking.client.ServerboundOpenCraftingGridMessage;
import fuzs.miniumstone.world.item.MiniumStoneItem;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class StoneChargeHandler {
    public static final KeyMapping CHARGE_MINIUM_STONE_KEY_MAPPING = new KeyMapping("key.chargeMiniumStone", InputConstants.KEY_V, "key.categories." + MiniumStone.MOD_ID);
    public static final KeyMapping OPEN_CRAFTING_GRID_KEY_MAPPING = new KeyMapping("key.openCraftingGrid", InputConstants.KEY_G, "key.categories." + MiniumStone.MOD_ID);

    public static void onClientTick$Start(Minecraft minecraft) {
        Player player = minecraft.player;
        if (player != null && !player.isSpectator()) {
            handleModKeybinds(player);
        }
    }

    private static void handleModKeybinds(Player player) {
        while (CHARGE_MINIUM_STONE_KEY_MAPPING.consumeClick()) {
            InteractionHand interactionHand = getMiniumStoneHand(player);
            if (interactionHand != null) {
                ItemStack itemInHand = player.getItemInHand(interactionHand);
                if (player.isShiftKeyDown()) {
                    if (MiniumStoneItem.decreaseCharge(itemInHand)) {
                        chargeStone(player, interactionHand, false);
                        break;
                    }
                } else {
                    if (MiniumStoneItem.increaseCharge(itemInHand)) {
                        chargeStone(player, interactionHand, true);
                        break;
                    }
                }
            }
        }
        while (OPEN_CRAFTING_GRID_KEY_MAPPING.consumeClick()) {
            InteractionHand interactionHand = getMiniumStoneHand(player);
            if (interactionHand != null) {
                MiniumStone.NETWORK.sendToServer(new ServerboundOpenCraftingGridMessage(player.getInventory().selected, interactionHand));
            }
        }
    }

    @Nullable
    public static InteractionHand getMiniumStoneHand(Player player) {
        for (InteractionHand interactionHand : InteractionHand.values()) {
            ItemStack itemInHand = player.getItemInHand(interactionHand);
            if (itemInHand.is(ModRegistry.MINIUM_STONE_ITEM.get())) {
                return interactionHand;
            }
        }
        return null;
    }

    private static void chargeStone(Player player, InteractionHand interactionHand, boolean increaseCharge) {
        player.playSound(increaseCharge ? SoundEvents.BUNDLE_INSERT : SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + player.getRandom().nextFloat() * 0.4F);
        MiniumStone.NETWORK.sendToServer(new ServerboundChargeStoneMessage(player.getInventory().selected, interactionHand, increaseCharge));
    }
}
