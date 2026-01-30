package dev.swench.watersource.util;

import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.joml.Matrix4f;

public class RenderUtils {
    public static void drawBox(MatrixStack matrices, VertexConsumerProvider vertexConsumers, BlockPos pos, float red, float green, float blue, float alpha, boolean fill, boolean outline) {
        Box box = new Box(pos);
        renderBox(matrices, vertexConsumers, box, red, green, blue, alpha, fill, outline);
    }

    public static void renderBox(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Box box, float red, float green, float blue, float alpha, boolean fill, boolean outline) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        if (fill) {
            VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getGui());
            drawFilledBox(matrix, consumer, box, red, green, blue, alpha);
        }

        if (outline) {
            VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getLines());
            drawOutlineBox(matrix, consumer, box, red, green, blue, alpha);
        }
    }

    private static void drawFilledBox(Matrix4f matrix, VertexConsumer consumer, Box box, float r, float g, float b, float a) {
        float x1 = (float) box.minX;
        float y1 = (float) box.minY;
        float z1 = (float) box.minZ;
        float x2 = (float) box.maxX;
        float y2 = (float) box.maxY;
        float z2 = (float) box.maxZ;

        consumer.vertex(matrix, x1, y1, z1).color(r, g, b, a);
        consumer.vertex(matrix, x2, y1, z1).color(r, g, b, a);
        consumer.vertex(matrix, x2, y1, z2).color(r, g, b, a);
        consumer.vertex(matrix, x1, y1, z2).color(r, g, b, a);

        consumer.vertex(matrix, x1, y2, z1).color(r, g, b, a);
        consumer.vertex(matrix, x1, y2, z2).color(r, g, b, a);
        consumer.vertex(matrix, x2, y2, z2).color(r, g, b, a);
        consumer.vertex(matrix, x2, y2, z1).color(r, g, b, a);

        consumer.vertex(matrix, x1, y1, z1).color(r, g, b, a);
        consumer.vertex(matrix, x1, y1, z2).color(r, g, b, a);
        consumer.vertex(matrix, x1, y2, z2).color(r, g, b, a);
        consumer.vertex(matrix, x1, y2, z1).color(r, g, b, a);

        consumer.vertex(matrix, x2, y1, z1).color(r, g, b, a);
        consumer.vertex(matrix, x2, y2, z1).color(r, g, b, a);
        consumer.vertex(matrix, x2, y2, z2).color(r, g, b, a);
        consumer.vertex(matrix, x2, y1, z2).color(r, g, b, a);

        consumer.vertex(matrix, x1, y1, z1).color(r, g, b, a);
        consumer.vertex(matrix, x1, y2, z1).color(r, g, b, a);
        consumer.vertex(matrix, x2, y2, z1).color(r, g, b, a);
        consumer.vertex(matrix, x2, y1, z1).color(r, g, b, a);

        consumer.vertex(matrix, x1, y1, z2).color(r, g, b, a);
        consumer.vertex(matrix, x2, y1, z2).color(r, g, b, a);
        consumer.vertex(matrix, x2, y2, z2).color(r, g, b, a);
        consumer.vertex(matrix, x1, y2, z2).color(r, g, b, a);
    }

    private static void drawOutlineBox(Matrix4f matrix, VertexConsumer consumer, Box box, float r, float g, float b, float a) {
        float x1 = (float) box.minX;
        float y1 = (float) box.minY;
        float z1 = (float) box.minZ;
        float x2 = (float) box.maxX;
        float y2 = (float) box.maxY;
        float z2 = (float) box.maxZ;

        line(matrix, consumer, x1, y1, z1, x2, y1, z1, r, g, b, a);
        line(matrix, consumer, x2, y1, z1, x2, y1, z2, r, g, b, a);
        line(matrix, consumer, x2, y1, z2, x1, y1, z2, r, g, b, a);
        line(matrix, consumer, x1, y1, z2, x1, y1, z1, r, g, b, a);

        line(matrix, consumer, x1, y2, z1, x2, y2, z1, r, g, b, a);
        line(matrix, consumer, x2, y2, z1, x2, y2, z2, r, g, b, a);
        line(matrix, consumer, x2, y2, z2, x1, y2, z2, r, g, b, a);
        line(matrix, consumer, x1, y2, z2, x1, y2, z1, r, g, b, a);

        line(matrix, consumer, x1, y1, z1, x1, y2, z1, r, g, b, a);
        line(matrix, consumer, x2, y1, z1, x2, y2, z1, r, g, b, a);
        line(matrix, consumer, x1, y1, z2, x1, y2, z2, r, g, b, a);
        line(matrix, consumer, x2, y1, z2, x2, y2, z2, r, g, b, a);
    }

    private static void line(Matrix4f matrix, VertexConsumer consumer, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float dz = z2 - z1;
        float len = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (len > 0) {
            dx /= len;
            dy /= len;
            dz /= len;
        }

        consumer.vertex(matrix, x1, y1, z1).color(r, g, b, a).normal(dx, dy, dz);
        consumer.vertex(matrix, x2, y2, z2).color(r, g, b, a).normal(dx, dy, dz);
    }
}
