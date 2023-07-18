package fuzs.miniumstone.client.handler;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.miniumstone.client.renderer.CachedEdge;
import fuzs.miniumstone.client.renderer.UltimineRenderTypes;
import fuzs.miniumstone.client.shape.ShapeContext;
import fuzs.miniumstone.client.shape.ShapeMerger;
import fuzs.miniumstone.init.ModRegistry;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StoneShapeHandler {
    @Nullable
    private static BlockWalker blockWalker;

    public static void onRenderLevelAfterTranslucent(LevelRenderer levelRenderer, Camera camera, GameRenderer gameRenderer, float tickDelta, PoseStack poseStack, Matrix4f projectionMatrix, Frustum frustum, ClientLevel level) {
        if (camera.getEntity() instanceof Player player && (player.getMainHandItem().is(ModRegistry.MINIUM_STONE_ITEM.get()) || player.getOffhandItem().is(ModRegistry.MINIUM_STONE_ITEM.get()))) {
            HitResult hitResult = gameRenderer.getMinecraft().hitResult;
            if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
                if (blockWalker == null || !blockWalker.stillValid(hitResult, level)) {
                    blockWalker = BlockWalker.fromHitResult((BlockHitResult) hitResult, level);
                }

                poseStack.pushPose();
                Vec3 projectedView = camera.getPosition().reverse();
                poseStack.translate(projectedView.x, projectedView.y, projectedView.z);
                Matrix4f matrix = poseStack.last().pose();

                MultiBufferSource.BufferSource bufferSource = gameRenderer.getMinecraft().renderBuffers().bufferSource();

                VertexConsumer buffer = bufferSource.getBuffer(UltimineRenderTypes.LINES_NORMAL);

                for (VoxelEdge edge : blockWalker.getLines(level)) {
                    buffer.vertex(matrix, edge.x1(), edge.y1(), edge.z1()).color(255, 255, 255, 255).endVertex();
                    buffer.vertex(matrix, edge.x2(), edge.y2(), edge.z2()).color(255, 255, 255, 255).endVertex();
                }

                bufferSource.endBatch(UltimineRenderTypes.LINES_NORMAL);

                VertexConsumer buffer2 = bufferSource.getBuffer(UltimineRenderTypes.LINES_TRANSPARENT);

                for (VoxelEdge edge : blockWalker.getLines(level)) {
                    buffer2.vertex(matrix, edge.x1(), edge.y1(), edge.z1()).color(255, 255, 255, 10).endVertex();
                    buffer2.vertex(matrix, edge.x2(), edge.y2(), edge.z2()).color(255, 255, 255, 10).endVertex();
                }

                bufferSource.endBatch(UltimineRenderTypes.LINES_TRANSPARENT);

                poseStack.popPose();
            }
        }
    }

    public static void onClientTick$End(Minecraft minecraft) {
        if (blockWalker != null) blockWalker.testAllBlocks(minecraft.hitResult, minecraft.level);
    }

    public static class BlockWalker {
        // all blocks in 3x3x3 cube around the block
        private static final Object2IntMap<BlockPos> NEIGHBOR_POSITIONS = BlockPos.betweenClosedStream(-1, -1, -1, 1, 1, 1)
                .filter(Predicate.not(BlockPos.ZERO::equals))
                .map(BlockPos::immutable)
                .collect(Collectors.toMap(Function.identity(), t -> t.distManhattan(BlockPos.ZERO), (o1, o2) -> o1, Object2IntOpenHashMap::new));
        // all blocks in 5x5 square around block's Y-level, plus blocks directly above & below
        private static final Object2IntMap<BlockPos> NEIGHBOR_POSITIONS_BUSH = BlockPos.betweenClosedStream(-2, -1, -2, 2, 1, 2)
                .filter(t -> !BlockPos.ZERO.equals(t) && (t.getY() == 0 || t.distManhattan(BlockPos.ZERO) == 0))
                .map(BlockPos::immutable)
                .collect(Collectors.toMap(Function.identity(), t -> t.distManhattan(BlockPos.ZERO), (o1, o2) -> o1, Object2IntOpenHashMap::new));

        private final BlockPos blockPos;
        private final Direction blockDirection;
        private final BlockState blockState;
        private final Object2IntMap<BlockPos> neighborPositions;
        private final int maxBlocks = 49;
        private final int maxDepth = 3;
        private int blockTicks;
        @Nullable
        private List<BlockPos> blocks;
        private List<VoxelEdge> lines;

        private BlockWalker(BlockPos blockPos, Direction blockDirection, BlockState blockState) {
            this.blockPos = blockPos;
            this.blockDirection = blockDirection;
            this.blockState = blockState;
            this.neighborPositions = blockState.getBlock() instanceof BushBlock ? NEIGHBOR_POSITIONS_BUSH : NEIGHBOR_POSITIONS;
        }

        public static BlockWalker fromHitResult(BlockHitResult hitResult, BlockGetter blockGetter) {
            return new BlockWalker(hitResult.getBlockPos(), hitResult.getDirection(), blockGetter.getBlockState(hitResult.getBlockPos()));
        }

        public boolean stillValid(@Nullable HitResult hitResult, @Nullable BlockGetter blockGetter) {
            if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK && blockGetter != null) {
                BlockHitResult blockHitResult = (BlockHitResult) hitResult;
                return this.blockPos.equals(blockHitResult.getBlockPos()) && this.blockDirection.equals(blockHitResult.getDirection()) && this.blockState.equals(blockGetter.getBlockState(blockHitResult.getBlockPos()));
            }
            return false;
        }

        public void testAllBlocks(@Nullable HitResult hitResult, BlockGetter blockGetter) {
            if (this.stillValid(hitResult, blockGetter)) {
                if (this.blocks != null && --this.blockTicks <= 0) {
                    this.blockTicks = 20;
                    if (this.blocks.stream().anyMatch(t -> !this.isSame(blockGetter, t))) {
                        this.blocks = null;
                        this.lines = null;
                    }
                }
            }
        }

        public List<VoxelEdge> getLines(BlockGetter blockGetter) {
            if (this.lines == null) {
                return this.lines = this.createLines(blockGetter);
            }
            return this.lines;
        }

        private List<VoxelEdge> createLines(BlockGetter blockGetter) {
            List<VoxelEdge> lines = Lists.newArrayList();
            VoxelShape voxelShape = Shapes.empty();
            for (BlockPos pos : this.getBlocks(blockGetter)) {
                voxelShape = Shapes.joinUnoptimized(voxelShape, Shapes.create(new AABB(pos).inflate(0.005)), BooleanOp.OR);
            }
            voxelShape.forAllEdges((x1, y1, z1, x2, y2, z2) -> lines.add(new VoxelEdge(x1, y1, z1, x2, y2, z2)));
            return lines;
        }

        public List<BlockPos> getBlocks(BlockGetter blockGetter) {
            if (this.blocks == null) {
                return this.blocks = this.findBlocks(blockGetter);
            }
            return this.blocks;
        }

        private List<BlockPos> findBlocks(BlockGetter blockGetter) {
            Set<BlockPos> known = Sets.newHashSet();
            if (blockGetter != null) this.walk(blockGetter, known);

            List<BlockPos> list = new ArrayList<>(known);
            list.sort(Comparator.comparingInt(t -> t.distManhattan(this.blockPos)));

            if (list.size() > this.maxBlocks) {
                list.subList(this.maxBlocks, list.size()).clear();
            }

            return list;
        }

        private void walk(BlockGetter blockGetter, Set<BlockPos> known) {
            Set<BlockPos> traversed = Sets.newHashSet();
            Deque<BlockPos> openSet = Queues.newArrayDeque();
            openSet.add(this.blockPos);
            traversed.add(this.blockPos);

            while (!openSet.isEmpty()) {
                BlockPos pos = openSet.pop();

                if (this.isSame(blockGetter, pos) && known.add(pos)) {
                    if (known.size() >= this.maxBlocks) {
                        return;
                    }

                    for (BlockPos side : this.neighborPositions.keySet()) {
                        BlockPos offset = pos.offset(side);

                        if (traversed.add(offset)) {
                            openSet.add(offset);
                        }
                    }
                }
            }
        }

        private boolean isSame(BlockGetter blockGetter, BlockPos pos) {
            return this.blockState.is(blockGetter.getBlockState(pos).getBlock());
        }
    }

    private record VoxelEdge(float x1, float y1, float z1, float x2, float y2, float z2) {

        public VoxelEdge(double x1, double y1, double z1, double x2, double y2, double z2) {
            this((float) x1, (float) y1, (float) z1, (float) x2, (float) y2, (float) z2);
        }
    }

    private List<BlockPos> shapeBlocks = Collections.emptyList();
    private int actualBlocks = 0;
    private List<CachedEdge> cachedEdges = null;
    private BlockPos cachedPos = null;

    public void renderInGame(PoseStack poseStack) {

        Minecraft mc = Minecraft.getInstance();

        // Rewrite this to use shader that does outline instead

        Camera activeRenderInfo = mc.getEntityRenderDispatcher().camera;
        Vec3 projectedView = activeRenderInfo.getPosition();

        poseStack.pushPose();
        poseStack.translate(cachedPos.getX() - projectedView.x, cachedPos.getY() - projectedView.y, cachedPos.getZ() - projectedView.z);
        Matrix4f matrix = poseStack.last().pose();

        VertexConsumer buffer = mc.renderBuffers().bufferSource().getBuffer(UltimineRenderTypes.LINES_NORMAL);

        for (CachedEdge edge : cachedEdges) {
            buffer.vertex(matrix, edge.x1, edge.y1, edge.z1).color(255, 255, 255, 255).endVertex();
            buffer.vertex(matrix, edge.x2, edge.y2, edge.z2).color(255, 255, 255, 255).endVertex();
        }

        mc.renderBuffers().bufferSource().endBatch(UltimineRenderTypes.LINES_NORMAL);

        VertexConsumer buffer2 = mc.renderBuffers().bufferSource().getBuffer(UltimineRenderTypes.LINES_TRANSPARENT);

        for (CachedEdge edge : cachedEdges) {
            buffer2.vertex(matrix, edge.x1, edge.y1, edge.z1).color(255, 255, 255, 10).endVertex();
            buffer2.vertex(matrix, edge.x2, edge.y2, edge.z2).color(255, 255, 255, 10).endVertex();
        }

        mc.renderBuffers().bufferSource().endBatch(UltimineRenderTypes.LINES_TRANSPARENT);

        poseStack.popPose();
    }

    private void updateEdges() {
        if (cachedEdges != null) {
            return;
        }
        if (shapeBlocks.isEmpty()) {
            cachedEdges = Collections.emptyList();
            return;
        }

        cachedPos = shapeBlocks.get(0);

        double d = 0.005D;

        cachedEdges = new ArrayList<>();

        Collection<VoxelShape> shapes = new HashSet<>();
        for (AABB aabb : ShapeMerger.merge(shapeBlocks, cachedPos)) {
            shapes.add(Shapes.create(aabb.inflate(d)));
        }

        orShapes(shapes).forAllEdges((x1, y1, z1, x2, y2, z2) -> {
            CachedEdge edge = new CachedEdge();
            edge.x1 = (float) x1;
            edge.y1 = (float) y1;
            edge.z1 = (float) z1;
            edge.x2 = (float) x2;
            edge.y2 = (float) y2;
            edge.z2 = (float) z2;
            cachedEdges.add(edge);
        });
    }

    static VoxelShape orShapes(Collection<VoxelShape> shapes) {
        VoxelShape combinedShape = Shapes.empty();
        for (VoxelShape shape : shapes) {
            combinedShape = Shapes.joinUnoptimized(combinedShape, shape, BooleanOp.OR);
        }
        return combinedShape;
    }
}
