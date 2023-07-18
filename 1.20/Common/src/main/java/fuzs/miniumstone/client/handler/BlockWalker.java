package fuzs.miniumstone.client.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.compress.utils.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BlockWalker {
    private static final List<BlockPos> NEIGHBOR_POSITIONS = BlockPos.betweenClosedStream(-1, -1, -1, 1, 1, 1).filter(Predicate.not(BlockPos.ZERO::equals)).map(BlockPos::immutable).toList();
    private static final List<BlockPos> NEIGHBOR_POSITIONS_BUSH = BlockPos.betweenClosedStream(-2, -1, -2, 2, 1, 2).filter(t -> !BlockPos.ZERO.equals(t) && (t.getY() == 0 || t.distManhattan(BlockPos.ZERO) == 1)).map(BlockPos::immutable).toList();

    private final BlockPos blockPos;
    private final BlockState blockState;
    private final List<BlockPos> neighborPositions;
    private final int maxDepth;
    private int blockTicks;
    @Nullable
    private List<BlockPos> blocks;
    private VoxelShape voxelShape;

    private BlockWalker(int maxDepth, BlockPos blockPos, BlockState blockState) {
        this.maxDepth = maxDepth;
        this.blockPos = blockPos;
        this.blockState = blockState;
        this.neighborPositions = blockState.getBlock() instanceof BushBlock ? NEIGHBOR_POSITIONS_BUSH : NEIGHBOR_POSITIONS;
    }

    public static BlockWalker fromHitResult(int maxDepth, BlockHitResult hitResult, BlockGetter blockGetter) {
        return new BlockWalker(maxDepth, hitResult.getBlockPos(), blockGetter.getBlockState(hitResult.getBlockPos()));
    }

    public boolean stillValid(int maxDepth, @Nullable HitResult hitResult, @Nullable BlockGetter blockGetter) {
        if (maxDepth == -1 || maxDepth == this.maxDepth) {
            if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK && blockGetter != null) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                return this.blockPos.equals(blockHitResult.getBlockPos()) && this.blockState.equals(blockGetter.getBlockState(blockHitResult.getBlockPos()));
            }
        }
        return false;
    }

    public void testAllBlocks(@Nullable HitResult hitResult, BlockGetter blockGetter) {
        if (this.stillValid(-1, hitResult, blockGetter)) {
            if (this.blocks != null && --this.blockTicks <= 0) {
                this.blockTicks = 20;
                if (this.blocks.stream().anyMatch(t -> !this.isSame(blockGetter, t))) {
                    this.blocks = null;
                    this.voxelShape = null;
                }
            }
        }
    }

    public VoxelShape getVoxelShape(BlockGetter blockGetter) {
        if (this.voxelShape == null) {
            return this.voxelShape = this.createVoxelShape(blockGetter);
        }
        return this.voxelShape;
    }

    private VoxelShape createVoxelShape(BlockGetter blockGetter) {
        VoxelShape voxelShape = Shapes.empty();
        for (BlockPos pos : this.getBlocks(blockGetter)) {
            voxelShape = Shapes.joinUnoptimized(voxelShape, Shapes.create(new AABB(pos).inflate(0.005)), BooleanOp.OR);
        }
        return voxelShape;
    }

    public List<BlockPos> getBlocks(BlockGetter blockGetter) {
        if (this.blocks == null) {
            return this.blocks = this.findBlocks(blockGetter);
        }
        return this.blocks;
    }

    private List<BlockPos> findBlocks(BlockGetter blockGetter) {

        List<BlockPos> blocks = Lists.newArrayList();
        BlockPos.breadthFirstTraversal(this.blockPos, this.maxDepth, 1024, (BlockPos pos, Consumer<BlockPos> blockPosConsumer) -> {
            for (BlockPos side : this.neighborPositions) {
                blockPosConsumer.accept(pos.offset(side));
            }
        }, (BlockPos pos) -> {
            if (this.isSame(blockGetter, pos)) blocks.add(pos);
            return true;
        });

        return blocks;
    }

    private boolean isSame(BlockGetter blockGetter, BlockPos pos) {
        return this.blockState.is(blockGetter.getBlockState(pos).getBlock());
    }
}
