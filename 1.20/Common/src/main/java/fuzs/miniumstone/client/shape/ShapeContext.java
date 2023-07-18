package fuzs.miniumstone.client.shape;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author LatvianModder
 */
public record ShapeContext(Player player, BlockPos pos, Direction face, BlockState original, int maxBlocks) {

    public boolean check(BlockState state) {
        return this.original.is(state.getBlock());
    }

    public BlockState block(BlockPos pos) {
        return this.player.level().getBlockState(pos);
    }

    public boolean check(BlockPos pos) {
        return this.check(this.block(pos));
    }
}
