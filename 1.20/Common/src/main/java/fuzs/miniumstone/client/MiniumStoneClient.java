package fuzs.miniumstone.client;

import fuzs.miniumstone.client.handler.StoneChargeHandler;
import fuzs.miniumstone.client.handler.StoneShapeHandler;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.world.item.MiniumStoneItem;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.ItemDecorationContext;
import fuzs.puzzleslib.api.client.core.v1.context.KeyMappingsContext;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.RenderLevelEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;

public class MiniumStoneClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerHandlers();
    }

    private static void registerHandlers() {
        ClientTickEvents.START.register(StoneChargeHandler::onClientTick$Start);
        ClientTickEvents.END.register(StoneShapeHandler::onClientTick$End);
        RenderLevelEvents.AFTER_TRANSLUCENT.register(StoneShapeHandler::onRenderLevelAfterTranslucent);
    }

    @Override
    public void onRegisterKeyMappings(KeyMappingsContext context) {
        context.registerKeyMapping(StoneChargeHandler.CHARGE_MINIUM_STONE_KEY_MAPPING, StoneChargeHandler.OPEN_CRAFTING_GRID_KEY_MAPPING);
    }

    @Override
    public void onRegisterItemDecorations(ItemDecorationContext context) {
        context.registerItemDecorator((GuiGraphics guiGraphics, Font font, ItemStack stack, int itemPosX, int itemPosY) -> {
            int charge = MiniumStoneItem.getCharge(stack) + 1;
            int startX = itemPosX + 2;
            int startY = itemPosY + 13;
            guiGraphics.pose().pushPose();
            for (int i = 0; i < charge; i++) {
                guiGraphics.fill(RenderType.guiOverlay(), startX + 1 + 3 * i, startY + 1, startX + 1 + 3 * i + 2, startY + 2, ChatFormatting.AQUA.getColor() | 0xFF000000);
            }
            guiGraphics.pose().popPose();
            return true;
        }, ModRegistry.MINIUM_STONE_ITEM.get());
    }
}
