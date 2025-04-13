package fuzs.miniumstone.network;

import fuzs.miniumstone.client.handler.BlockWalker;
import fuzs.miniumstone.world.item.crafting.TransmutationInWorldRecipe;
import fuzs.puzzleslib.api.network.v4.message.MessageListener;
import fuzs.puzzleslib.api.network.v4.message.play.ClientboundPlayMessage;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SelectableRecipe;
import net.minecraft.world.item.crafting.display.SlotDisplay;

public record ClientboundTransmutationInWorldRecipesMessage(SelectableRecipe.SingleInputSet<TransmutationInWorldRecipe> recipes) implements ClientboundPlayMessage {
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundTransmutationInWorldRecipesMessage> STREAM_CODEC = ClientboundTransmutationInWorldRecipesMessage.<TransmutationInWorldRecipe>singleInputSetCodec()
            .map(ClientboundTransmutationInWorldRecipesMessage::new,
                    ClientboundTransmutationInWorldRecipesMessage::recipes);

    public static SelectableRecipe.SingleInputSet<TransmutationInWorldRecipe> transmutationInWorldRecipes = SelectableRecipe.SingleInputSet.empty();

    static <T extends Recipe<?>> StreamCodec<RegistryFriendlyByteBuf, SelectableRecipe.SingleInputSet<T>> singleInputSetCodec() {
        return ClientboundTransmutationInWorldRecipesMessage.<T>singleInputEntryCodec()
                .apply(ByteBufCodecs.list())
                .map(SelectableRecipe.SingleInputSet::new, SelectableRecipe.SingleInputSet::entries);
    }

    static <T extends Recipe<?>> StreamCodec<RegistryFriendlyByteBuf, SelectableRecipe.SingleInputEntry<T>> singleInputEntryCodec() {
        return StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC,
                SelectableRecipe.SingleInputEntry::input,
                selectableRecipeCodec(),
                SelectableRecipe.SingleInputEntry::recipe,
                SelectableRecipe.SingleInputEntry::new);
    }

    @SuppressWarnings("unchecked")
    static <T extends Recipe<?>> StreamCodec<RegistryFriendlyByteBuf, SelectableRecipe<T>> selectableRecipeCodec() {
        return StreamCodec.composite(SlotDisplay.STREAM_CODEC,
                SelectableRecipe::optionDisplay,
                ((StreamCodec<RegistryFriendlyByteBuf, RecipeHolder<T>>) (StreamCodec<?, ?>) RecipeHolder.STREAM_CODEC).apply(
                        ByteBufCodecs::optional),
                SelectableRecipe::recipe,
                SelectableRecipe::new);
    }

    @Override
    public MessageListener<Context> getListener() {
        return new MessageListener<Context>() {
            @Override
            public void accept(Context context) {
                BlockWalker.transmutationInWorldRecipes = ClientboundTransmutationInWorldRecipesMessage.this.recipes();
            }
        };
    }
}
