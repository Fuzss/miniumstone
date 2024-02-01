package fuzs.miniumstone.client.handler;

import com.mojang.blaze3d.platform.InputConstants;
import fuzs.miniumstone.MiniumStone;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.network.client.ServerboundChargeStoneMessage;
import fuzs.miniumstone.network.client.ServerboundOpenCraftingGridMessage;
import fuzs.miniumstone.util.MiniumStoneHelper;
import fuzs.miniumstone.world.item.MiniumStoneItem;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class MiniumStoneKeyHandler {
    public static final String MINIUM_STONE_KEY_CATEGORY = "key.categories." + MiniumStone.MOD_ID;
    public static final KeyMapping CHARGE_MINIUM_STONE_KEY_MAPPING = new KeyMapping("key.chargeMiniumStone", InputConstants.KEY_V,
            MINIUM_STONE_KEY_CATEGORY
    );
    public static final KeyMapping OPEN_CRAFTING_GRID_KEY_MAPPING = new KeyMapping("key.openCraftingGrid", InputConstants.KEY_G,
            MINIUM_STONE_KEY_CATEGORY
    );

    public static void onClientTick$Start(Minecraft minecraft) {
        Player player = minecraft.player;
        if (player != null && !player.isSpectator()) {
            handleModKeybinds(player);
        }
    }

    private static void handleModKeybinds(Player player) {
        while (CHARGE_MINIUM_STONE_KEY_MAPPING.consumeClick()) {
            InteractionHand interactionHand = MiniumStoneHelper.getMiniumStoneHand(player);
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
            InteractionHand interactionHand = MiniumStoneHelper.getMiniumStoneHand(player);
            if (interactionHand != null) {
                MiniumStone.NETWORK.sendToServer(new ServerboundOpenCraftingGridMessage(player.getInventory().selected, interactionHand));
            }
        }
    }

    private static void chargeStone(Player player, InteractionHand interactionHand, boolean increaseCharge) {
        SoundEvent soundEvent = increaseCharge ?
                ModRegistry.ITEM_MINIUM_STONE_CHARGE_SOUND_EVENT.value() :
                ModRegistry.ITEM_MINIUM_STONE_UNCHARGE_SOUND_EVENT.value();
        player.playSound(soundEvent, 0.8F, 0.8F + player.getRandom().nextFloat() * 0.4F);
        MiniumStone.NETWORK.sendToServer(new ServerboundChargeStoneMessage(player.getInventory().selected, interactionHand, increaseCharge));
    }
}
