package fuzs.miniumstone.network.client;

import fuzs.miniumstone.init.ModRegistry;
import fuzs.puzzleslib.api.network.v4.codec.ExtraStreamCodecs;
import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ServerboundPlayMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;

public record ServerboundOpenCraftingGridMessage(int carriedItemStack,
                                                 InteractionHand interactionHand) implements ServerboundPlayMessage {
    public static final StreamCodec<ByteBuf, ServerboundOpenCraftingGridMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.SHORT.map(Short::intValue, Integer::shortValue),
            ServerboundOpenCraftingGridMessage::carriedItemStack,
            ExtraStreamCodecs.fromEnum(InteractionHand.class),
            ServerboundOpenCraftingGridMessage::interactionHand,
            ServerboundOpenCraftingGridMessage::new);

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            static final Component CONTAINER_TITLE = Component.translatable("container.crafting");

            @Override
            public void accept(Context context) {
                context.packetListener()
                        .handleSetCarriedItem(new ServerboundSetCarriedItemPacket(ServerboundOpenCraftingGridMessage.this.carriedItemStack));
                ItemStack itemInHand = context.player()
                        .getItemInHand(ServerboundOpenCraftingGridMessage.this.interactionHand);
                if (itemInHand.is(ModRegistry.MINIUM_STONE_ITEM.value())) {
                    context.player()
                            .openMenu(new SimpleMenuProvider((int containerId, Inventory inventory, Player playerX) -> {
                                return new CraftingMenu(containerId,
                                        inventory,
                                        ContainerLevelAccess.create(playerX.level(), playerX.blockPosition())) {

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
