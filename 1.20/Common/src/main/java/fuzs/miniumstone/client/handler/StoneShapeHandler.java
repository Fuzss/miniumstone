package fuzs.miniumstone.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import fuzs.miniumstone.client.renderer.ModRenderType;
import fuzs.miniumstone.mixin.client.accessor.LevelRendererAccessor;
import fuzs.miniumstone.world.item.MiniumStoneItem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class StoneShapeHandler {
    @Nullable
    private static BlockWalker blockWalker;

    public static void onRenderLevelAfterTranslucent(LevelRenderer levelRenderer, Camera camera, GameRenderer gameRenderer, float tickDelta, PoseStack poseStack, Matrix4f projectionMatrix, Frustum frustum, ClientLevel level) {

        if (camera.getEntity() instanceof Player player) {

            InteractionHand interactionHand = StoneChargeHandler.getMiniumStoneHand(player);
            if (interactionHand != null) {

                HitResult hitResult = gameRenderer.getMinecraft().hitResult;
                if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {

                    int miniumStoneCharge = MiniumStoneItem.getCharge(player.getItemInHand(interactionHand));
                    if (blockWalker == null || !blockWalker.stillValid(miniumStoneCharge, hitResult, level)) {

                        blockWalker = BlockWalker.fromHitResult(miniumStoneCharge, (BlockHitResult) hitResult, level);
                    }

                    MultiBufferSource.BufferSource bufferSource = gameRenderer.getMinecraft().renderBuffers().bufferSource();
                    VoxelShape voxelShape = blockWalker.getVoxelShape(level);
                    renderLines(camera.getPosition(), bufferSource, poseStack, voxelShape);
                }
            }

        }
    }

    private static void renderLines(Vec3 cameraPosition, MultiBufferSource.BufferSource bufferSource, PoseStack poseStack, VoxelShape voxelShape) {

        RenderSystem.disableDepthTest();
        VertexConsumer buffer = bufferSource.getBuffer(ModRenderType.LINES_SEE_THROUGH);
        LevelRendererAccessor.miniumstone$callRenderShape(poseStack, buffer, voxelShape, -cameraPosition.x, -cameraPosition.y, -cameraPosition.z, 1.0F, 1.0F, 1.0F, 0.05F);
        bufferSource.endBatch(ModRenderType.LINES_SEE_THROUGH);
        RenderSystem.enableDepthTest();
    }

    public static void onClientTick$End(Minecraft minecraft) {
        if (blockWalker != null) blockWalker.testAllBlocks(minecraft.hitResult, minecraft.level);
    }
}
