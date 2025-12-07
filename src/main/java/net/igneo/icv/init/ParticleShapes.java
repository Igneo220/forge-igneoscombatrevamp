package net.igneo.icv.init;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.function.Consumer;

import static net.igneo.icv.init.TrigMath.radiansToInternalAngle;
import static net.igneo.icv.init.TrigMath.sincos;

public class ParticleShapes {
    public static void renderLine(ServerLevel level, Vec3 start, Vec3 end, ParticleOptions particle, int points) {
        processLine(start, end, points, point ->
                level.sendParticles(particle, point.x, point.y, point.z, 1, 0, 0, 0, 0)
        );
    }
    
    public static ArrayList<Vec3> renderLineList(Level level, Vec3 start, Vec3 end, int points) {
        ArrayList<Vec3> posList = new ArrayList<>(points + 1);
        processLine(start, end, points, posList::add);
        return posList;
    }
    
    public static void renderRing(ServerLevel level, Vec3 center, ParticleOptions particle, int points, float radius) {
        processRing(center, points, radius, point ->
                level.sendParticles(particle, point.x, point.y, point.z, 1, 0, 0, 0, 0)
        );
    }
    
    public static ArrayList<Vec3> renderRingList(Level level, Vec3 center, int points, float radius) {
        ArrayList<Vec3> posList = new ArrayList<>(points * 2);
        processRing(center, points, radius, posList::add);
        return posList;
    }
    
    public static void renderSphere(ServerLevel level, Vec3 center, ParticleOptions particle, int latitudeSteps, int longitudeSteps, float radius) {
        processSphere(center, latitudeSteps, longitudeSteps, radius, point ->
                level.sendParticles(particle, point.x, point.y, point.z, 1, 0, 0, 0, 0)
        );
    }
    
    public static ArrayList<Vec3> renderSphereList(Level level, Vec3 center, int latitudeSteps, int longitudeSteps, float radius) {
        ArrayList<Vec3> posList = new ArrayList<>();
        processSphere(center, latitudeSteps, longitudeSteps, radius, posList::add);
        return posList;
    }
    
    public static void renderCone(ServerLevel level, Player player, Vec3 direction, ParticleOptions particle, double radius, double halfAngleDegrees, int particlesPerRing, int numRings) {
        processCone(player, direction, radius, halfAngleDegrees, particlesPerRing, numRings, point ->
                level.sendParticles(particle, point.x, point.y, point.z, 1, 0, 0, 0, 0)
        );
    }
    
    public static ArrayList<Vec3> renderConeList(Level level, Player player, Vec3 direction, double radius, double halfAngleDegrees, int particlesPerRing, int numRings) {
        int estimatedSize = particlesPerRing * numRings;
        ArrayList<Vec3> posList = new ArrayList<>(estimatedSize);
        processCone(player, direction, radius, halfAngleDegrees, particlesPerRing, numRings, posList::add);
        return posList;
    }
    
    // Math stuff below
    
    private static void processLine(Vec3 start, Vec3 end, int points, Consumer<Vec3> pointConsumer) {
        double dx = (end.x - start.x) / points;
        double dy = (end.y - start.y) / points;
        double dz = (end.z - start.z) / points;
        
        for (int i = 0; i <= points; i++) {
            double x = start.x + dx * i;
            double y = start.y + dy * i;
            double z = start.z + dz * i;
            
            pointConsumer.accept(new Vec3(x, y, z));
        }
    }
    
    private static void processRing(Vec3 center, int points, float radius, Consumer<Vec3> pointConsumer) {
        double angleStep = Math.PI / points;
        
        for (int i = 0; i < points; i++) {
            int angleInt = radiansToInternalAngle(2 * i * angleStep);
            
            double[] trigValues = sincos(angleInt);
            double sinValue = trigValues[0];
            double cosValue = trigValues[1];
            
            float x = (float) (center.x + radius * cosValue);
            float z = (float) (center.z + radius * sinValue);
            float inv_x = (float) (center.x - radius * cosValue);
            float inv_z = (float) (center.z - radius * sinValue);
            
            pointConsumer.accept(new Vec3(x, center.y, z));
            pointConsumer.accept(new Vec3(inv_x, center.y, inv_z));
        }
    }
    
    private static void processSphere(Vec3 center, int latitudeSteps, int longitudeSteps, float radius, Consumer<Vec3> pointConsumer) {
        double latStep = Math.PI / (latitudeSteps * 0.25d);
        double lonStep = Math.PI / (longitudeSteps * 0.25d);
        
        for (int i = 0; i <= latitudeSteps; i++) {
            int latAngleInt = radiansToInternalAngle(i * latStep);
            double[] latTrig = sincos(latAngleInt);
            double sinLat = latTrig[0];
            double cosLat = latTrig[1];
            
            for (int j = 0; j < longitudeSteps; j++) {
                int lonAngleInt = radiansToInternalAngle(j * lonStep);
                double[] lonTrig = sincos(lonAngleInt);
                double sinLon = lonTrig[0];
                double cosLon = lonTrig[1];
                
                float x = (float) (center.x + radius * sinLat * cosLon);
                float baseLat = (float) (radius * cosLat);
                float baseLon = (float) (radius * sinLat * sinLon);
                
                pointConsumer.accept(new Vec3(x, center.y + baseLat, center.z + baseLon));
                pointConsumer.accept(new Vec3(x, center.y - baseLat, center.z + baseLon));
                pointConsumer.accept(new Vec3(x, center.y - baseLat, center.z - baseLon));
                pointConsumer.accept(new Vec3(x, center.y + baseLat, center.z - baseLon));
            }
        }
    }
    
    // (WIP) Still need to convert to custom sincos function
    
    private static void processCone(Player player, Vec3 direction, double radius, double halfAngleDegrees, int particlesPerRing, int numRings, Consumer<Vec3> pointConsumer) {
        Vec3 playerPos = player.position().add(ICVUtils.UP_VECTOR);
        double tanHalfAngle = Math.tan(Math.toRadians(halfAngleDegrees));
        double angleStep = 2 * Math.PI / particlesPerRing;
        
        Vec3 up = new Vec3(0, 1, 0);
        Vec3 right = direction.cross(up).normalize();
        if (right.length() < 0.001f) {
            right = new Vec3(1, 0, 0);
        }
        Vec3 newUp = right.cross(direction).normalize();
        
        for (int ring = 1; ring <= numRings; ring++) {
            double ringDistance = (radius * ring) / numRings;
            double circleRadius = tanHalfAngle * ringDistance;
            Vec3 ringCenter = playerPos.add(direction.scale(ringDistance));
            
            for (int i = 0; i < particlesPerRing; i++) {
                double angle = i * angleStep;
                double cosAngle = Math.cos(angle);
                double sinAngle = Math.sin(angle);
                
                Vec3 offset = right.scale(cosAngle * circleRadius)
                        .add(newUp.scale(sinAngle * circleRadius));
                
                Vec3 particlePos = ringCenter.add(offset);
                pointConsumer.accept(particlePos);
            }
        }
    }
}