package com.ofekn.crafting_on_a_stick.client;

import org.joml.Math;
import org.joml.Vector2f;

import java.util.function.IntFunction;

public enum PolygonalWheelLayout implements WheelLayoutSupplier {
    INSTANCE;

    @Override
    public WheelPolygon[] apply(int numOptions) {
        if (numOptions == 0) {
            return new WheelPolygon[0];
        }
        // TODO fix
        int numAround = numOptions - 1;
        Vector2f[] points = new Vector2f[numAround * 2];
        float anglePerSection = 2 * (float) org.joml.Math.PI / numAround;
        float r1 = 32;
        float r2 = 64;
        for (int i = 0; i < numAround; i++) {
            float rad = (i - 0.5f) * anglePerSection;
            float dx = org.joml.Math.sin(rad);
            float dy = -org.joml.Math.cos(rad);
            points[i] = new Vector2f(r1 * dx, r1 * dy);
            points[i + numAround] = new Vector2f(r2 * dx, r2 * dy);
        }

        WheelPolygon[] result = new WheelPolygon[numOptions];
        Vector2f[] innerPoints = new Vector2f[numAround];
        for (int i = 0; i < numAround; i++) {
            innerPoints[i] = points[numAround - i - 1];
        }
        result[0] = new WheelPolygon(
                innerPoints,
                new Vector2f(0, 0)
        );

        float r = 48;
        for (int i = 0; i < numAround; i++) {
            float rad = i * anglePerSection;
            float dx = org.joml.Math.sin(rad);
            float dy = -Math.cos(rad);
            Vector2f center = new Vector2f(r * dx, r * dy);

            int i2 = (i + 1) % numAround;
            result[i + 1] = new WheelPolygon(new Vector2f[] {
                    points[i + numAround],
                    points[i],
                    points[i2],
                    points[i2 + numAround],
            }, center);
        }

        return result;
    }

    @Override
    public String getSerializedName() {
        return "polygonal";
    }
}
