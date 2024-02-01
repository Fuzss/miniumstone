package fuzs.miniumstone.client;

import fuzs.miniumstone.client.handler.MiniumStoneKeyHandler;
import fuzs.miniumstone.client.handler.StoneTransmuteHandler;
import fuzs.miniumstone.client.handler.TransmutateShapeRenderingHandler;
import fuzs.miniumstone.client.handler.TransmutationResultGuiHandler;
import fuzs.miniumstone.init.ModRegistry;
import fuzs.miniumstone.world.item.MiniumStoneItem;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.ItemDecorationContext;
import fuzs.puzzleslib.api.client.core.v1.context.KeyMappingsContext;
import fuzs.puzzleslib.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.api.client.event.v1.renderer.RenderGuiCallback;
import fuzs.puzzleslib.api.client.event.v1.renderer.RenderLevelEvents;
import fuzs.puzzleslib.api.client.key.v1.KeyActivationContext;
import fuzs.puzzleslib.api.event.v1.entity.player.PlayerInteractEvents;
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
        ClientTickEvents.START.register(MiniumStoneKeyHandler::onClientTick$Start);
        ClientTickEvents.END.register(TransmutateShapeRenderingHandler::onClientTick$End);
        RenderLevelEvents.AFTER_ENTITIES.register(TransmutateShapeRenderingHandler::onRenderLevelAfterTranslucent);
        RenderGuiCallback.EVENT.register(TransmutationResultGuiHandler::onRenderGui);
        ClientTickEvents.END.register(TransmutationResultGuiHandler::onClientTick$End);
        PlayerInteractEvents.USE_BLOCK.register(StoneTransmuteHandler::onUseBlock);
    }

    @Override
    public void onRegisterKeyMappings(KeyMappingsContext context) {
        context.registerKeyMapping(MiniumStoneKeyHandler.CHARGE_MINIUM_STONE_KEY_MAPPING, KeyActivationContext.GAME);
        context.registerKeyMapping(MiniumStoneKeyHandler.OPEN_CRAFTING_GRID_KEY_MAPPING, KeyActivationContext.GAME);
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
        }, ModRegistry.MINIUM_STONE_ITEM.value());
    }
}
