package fuzs.miniumstone.client.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.miniumstone.client.renderer.ModRenderType;
import fuzs.miniumstone.util.MiniumStoneHelper;
import fuzs.miniumstone.world.item.MiniumStoneItem;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.ARGB;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class TransmutateShapeRenderingHandler {
    @Nullable
    private static BlockWalker blockWalker;

    public static void onRenderLevelAfterEntities(LevelRenderer levelRenderer, Camera camera, GameRenderer gameRenderer, DeltaTracker deltaTracker, PoseStack poseStack, Matrix4f projectionMatrix, Frustum frustum, ClientLevel level) {

        if (camera.getEntity() instanceof Player player) {

            InteractionHand interactionHand = MiniumStoneHelper.getMiniumStoneHand(player);
            if (interactionHand != null) {

                Minecraft minecraft = gameRenderer.getMinecraft();
                HitResult hitResult = minecraft.hitResult;
                if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {

                    ItemStack itemInHand = player.getItemInHand(interactionHand);
                    int charge = MiniumStoneItem.getCharge(itemInHand);
                    MiniumStoneItem.SelectionMode selectionMode = MiniumStoneItem.getSelectionMode(itemInHand);
                    BlockWalker blockWalker = TransmutateShapeRenderingHandler.blockWalker;
                    if (blockWalker == null || !blockWalker.stillValid(charge, selectionMode, hitResult, level)) {

                        TransmutateShapeRenderingHandler.blockWalker = blockWalker = BlockWalker.fromHitResult(charge,
                                selectionMode,
                                (BlockHitResult) hitResult,
                                level);
                    }

                    MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
                    VoxelShape voxelShape = blockWalker.getVoxelShape(level, player.isShiftKeyDown());
                    renderLines(camera.getPosition(), bufferSource, poseStack, voxelShape);
                } else {

                    TransmutateShapeRenderingHandler.blockWalker = null;
                }
            }

        }
    }

    private static void renderLines(Vec3 cameraPosition, MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, VoxelShape voxelShape) {

        VertexConsumer vertexConsumer = bufferSource.getBuffer(ModRenderType.LINES_SEE_THROUGH);
        ShapeRenderer.renderShape(poseStack,
                vertexConsumer,
                voxelShape,
                -cameraPosition.x,
                -cameraPosition.y,
                -cameraPosition.z,
                ARGB.white(0.65F));
        bufferSource.endBatch(ModRenderType.LINES_SEE_THROUGH);
        vertexConsumer = bufferSource.getBuffer(RenderType.LINES);
        ShapeRenderer.renderShape(poseStack,
                vertexConsumer,
                voxelShape,
                -cameraPosition.x,
                -cameraPosition.y,
                -cameraPosition.z,
                -1);
        bufferSource.endBatch(RenderType.LINES);
    }

    public static void onEndClientTick(Minecraft minecraft) {
        if (blockWalker != null) blockWalker.testAllBlocks(minecraft.hitResult, minecraft.level);
    }

    @Nullable
    public static BlockWalker getBlockWalker() {
        return blockWalker;
    }

    public static void clearBlockWalker() {
        blockWalker = null;
    }
}
