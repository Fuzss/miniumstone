package fuzs.miniumstone.network.client;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.puzzleslib.api.network.v3.ServerMessageListener;
import fuzs.puzzleslib.api.network.v3.ServerboundMessage;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;

public record ServerboundOpenCraftingGridMessage(int carriedItem, InteractionHand interactionHand) implements ServerboundMessage<ServerboundOpenCraftingGridMessage> {

    @Override
    public ServerMessageListener<ServerboundOpenCraftingGridMessage> getHandler() {
        return new ServerMessageListener<>() {
            private static final Component CONTAINER_TITLE = Component.translatable("container.crafting");

            @Override
            public void handle(ServerboundOpenCraftingGridMessage message, MinecraftServer server, ServerGamePacketListenerImpl handler, ServerPlayer player, ServerLevel level) {
                handler.handleSetCarriedItem(new ServerboundSetCarriedItemPacket(message.carriedItem));
                ItemStack itemInHand = player.getItemInHand(message.interactionHand);
                if (itemInHand.is(ModRegistry.MINIUM_STONE_ITEM.get())) {
                    player.openMenu(new SimpleMenuProvider((i, inventory, player1) -> {
                        return new CraftingMenu(i, inventory, ContainerLevelAccess.create(level, player1.blockPosition())) {

                            @Override
                            public boolean stillValid(Player player) {
                                return player.isAlive();
                            }
                        };
                    }, CONTAINER_TITLE));
                }
            }
        };
    }
}
