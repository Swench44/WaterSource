package dev.swench.watersource.mixin;

import dev.swench.watersource.Config;
import dev.swench.watersource.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.ObjectAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(ObjectAllocator allocator, RenderTickCounter tickCounter, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, org.joml.Matrix4f matrix4f, org.joml.Matrix4f matrix4f2, CallbackInfo ci) {
        if (!Config.waterEnabled && !Config.lavaEnabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;

        MatrixStack matrices = new MatrixStack();
        matrices.multiplyPositionMatrix(matrix4f);

        Vec3d cameraPos = camera.getPos();
        matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

        VertexConsumerProvider.Immediate vertexConsumers = client.getBufferBuilders().getEntityVertexConsumers();

        BlockPos playerPos = client.player.getBlockPos();
        int waterRadiusSq = Config.waterRadius * Config.waterRadius;
        int lavaRadiusSq = Config.lavaRadius * Config.lavaRadius;
        int maxRadius = Math.max(Config.waterRadius, Config.lavaRadius);

        float waterR = (Config.waterColor.getValue() >> 16 & 0xFF) / 255f;
        float waterG = (Config.waterColor.getValue() >> 8 & 0xFF) / 255f;
        float waterB = (Config.waterColor.getValue() & 0xFF) / 255f;
        float waterA = (100 - Config.waterAlpha) / 100f;

        float lavaR = (Config.lavaColor.getValue() >> 16 & 0xFF) / 255f;
        float lavaG = (Config.lavaColor.getValue() >> 8 & 0xFF) / 255f;
        float lavaB = (Config.lavaColor.getValue() & 0xFF) / 255f;
        float lavaA = (100 - Config.lavaAlpha) / 100f;

        for (BlockPos pos : BlockPos.iterate(playerPos.add(-maxRadius, -maxRadius, -maxRadius), playerPos.add(maxRadius, maxRadius, maxRadius))) {
            double distSq = pos.getSquaredDistance(playerPos);
            FluidState fluidState = client.world.getFluidState(pos);
            if (fluidState.isEmpty()) continue;

            if (Config.waterEnabled && distSq <= waterRadiusSq) {
                if (fluidState.isOf(Fluids.WATER) && fluidState.isStill() && fluidState.getLevel() == 8) {
                    RenderUtils.drawBox(matrices, vertexConsumers, pos, waterR, waterG, waterB, waterA, Config.waterFill, true);
                }
            }

            if (Config.lavaEnabled && distSq <= lavaRadiusSq) {
                if (fluidState.isOf(Fluids.LAVA) && fluidState.isStill() && fluidState.getLevel() == 8) {
                    RenderUtils.drawBox(matrices, vertexConsumers, pos, lavaR, lavaG, lavaB, lavaA, Config.lavaFill, true);
                }
            }
        }

        vertexConsumers.draw();
    }
}
