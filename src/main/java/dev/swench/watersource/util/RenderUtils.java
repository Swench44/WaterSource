package dev.swench.watersource.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Matrix4f;

public class RenderUtils {

    public static void drawBox(MatrixStack matrixStack, BlockPos pos, int color, boolean fill, int alphaPercent) {
        float r = (float) (color >> 16 & 255) / 255.0F;
        float g = (float) (color >> 8 & 255) / 255.0F;
        float b = (float) (color & 255) / 255.0F;
        float a = (float) alphaPercent / 100.0F;

        Matrix4f matrix = matrixStack.peek().getModel();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableDepthTest();

        Box box = new Box(pos);
        
        if (fill) {
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            drawFilledBox(matrixStack, bufferBuilder, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, a);
            Tessellator.getInstance().draw();
        }

        bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        WorldRenderer.drawBox(matrixStack, bufferBuilder, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, r, g, b, a);
        Tessellator.getInstance().draw();
        
        RenderSystem.enableDepthTest();
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    public static void drawFilledBox(MatrixStack matrixStack, BufferBuilder bufferBuilder, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float r, float g, float b, float a) {
        Matrix4f matrix = matrixStack.peek().getModel();
        bufferBuilder.vertex(matrix, (float)minX, (float)minY, (float)minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)minY, (float)minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)minY, (float)maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)minX, (float)minY, (float)maxZ).color(r, g, b, a).next();

        bufferBuilder.vertex(matrix, (float)minX, (float)maxY, (float)minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)minX, (float)maxY, (float)maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)maxY, (float)maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)maxY, (float)minZ).color(r, g, b, a).next();

        bufferBuilder.vertex(matrix, (float)minX, (float)minY, (float)minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)minX, (float)maxY, (float)minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)maxY, (float)minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)minY, (float)minZ).color(r, g, b, a).next();

        bufferBuilder.vertex(matrix, (float)maxX, (float)minY, (float)minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)maxY, (float)minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)maxY, (float)maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)minY, (float)maxZ).color(r, g, b, a).next();

        bufferBuilder.vertex(matrix, (float)maxX, (float)minY, (float)maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)maxX, (float)maxY, (float)maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)minX, (float)maxY, (float)maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)minX, (float)minY, (float)maxZ).color(r, g, b, a).next();

        bufferBuilder.vertex(matrix, (float)minX, (float)minY, (float)maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)minX, (float)maxY, (float)maxZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)minX, (float)maxY, (float)minZ).color(r, g, b, a).next();
        bufferBuilder.vertex(matrix, (float)minX, (float)minY, (float)minZ).color(r, g, b, a).next();
    }
}
