package fuzs.miniumstone.world.item.crafting;

import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public interface PlayerRecipeInput {

    void miniumstone$setPlayer(Player player);

    @Nullable
    Player miniumstone$getPlayer();
}
