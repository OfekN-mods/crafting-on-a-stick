package com.ofekn.crafting_on_a_stick.client;

import org.joml.Math;
import org.joml.Vector2f;

import java.util.function.IntFunction;

public enum RoundWheelLayout implements WheelLayoutSupplier {
    INSTANCE;

    private static final float R0 = 28;
    private static final float R1 = 32;
    private static final float R2 = 48;
    private static final float R3 = 64;
    private static final int N_CENTRAL_POINTS = 360;
    private static final int N_HALF_AROUND_POINTS = 360;

    @Override
    public WheelPolygon[] apply(int numOptions) {
        if (numOptions == 0) {
            return new WheelPolygon[0];
        }

        int numAround = numOptions - 1;

        WheelPolygon[] result = new WheelPolygon[numOptions];
        result[0] = createCenter();
        for (int index = 0; index < numAround; index++) {
            result[index + 1] = createAround(index, numAround);
        }

        return result;
    }

    private static WheelPolygon createCenter() {
        Vector2f[] firstPoints = new Vector2f[N_CENTRAL_POINTS];
        for (int i = 0; i < N_CENTRAL_POINTS; i++) {
            firstPoints[i] = angleDistance(-i / (float) N_CENTRAL_POINTS, R0);
        }
        return new WheelPolygon(
                firstPoints,
                new Vector2f(0, 0)
        );
    }

    private static WheelPolygon createAround(int index, int numAround) {
        float gap1 = numAround == 1 ? 0 : 0.3f / R1;
        float gap3 = numAround == 1 ? 0 : 0.3f / R3;
        float alpha1 = (index - 0.5f) / numAround + gap1;
        float beta1 = (index + 0.5f) / numAround - gap1;
        float alpha3 = (index - 0.5f) / numAround + gap3;
        float beta3 = (index + 0.5f) / numAround - gap3;
        Vector2f[] polygonPoints = new Vector2f[N_HALF_AROUND_POINTS * 2];
        for (int j = 0; j < N_HALF_AROUND_POINTS; j++) {
            float t = (float)j / (N_HALF_AROUND_POINTS - 1);
            polygonPoints[j] = angleDistance(Math.lerp(alpha1, beta1, t), R1);
            polygonPoints[polygonPoints.length - j - 1] = angleDistance(Math.lerp(alpha3, beta3, t), R3);
        }

        Vector2f center = angleDistance((float)index / numAround, R2);

        return new WheelPolygon(polygonPoints, center);
    }

    private static Vector2f angleDistance(float angleRatio, float distance) {
        float rad = 2 * (float) Math.PI * angleRatio;
        float dx = Math.sin(rad);
        float dy = -Math.cos(rad);
        return new Vector2f(distance * dx, distance * dy);
    }

    @Override
    public String getSerializedName() {
        return "round";
    }
}
