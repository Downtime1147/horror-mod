package net.downtime1147.horrormod.api;

import net.downtime1147.horrormod.entity.custom.LimbEntity;

import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.*;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

public class Raycast {
    private static final double MAX_RAYCAST_DISTANCE = 100.0;
    private static final double ENTITY_INTERSECTION_TOLERANCE = 0.5; // How close an entity must be to the ray

    private static final int TICK_DELAY = 10; // Process every 10 ticks
    private int tickCounter = 0;

    private Map<UUID, Boolean> detectedEntities = new HashMap<>();

    // Singleton pattern to ensure only one instance
    private static Raycast INSTANCE;

    private Raycast() {
        // Register to Forge event bus
        MinecraftForge.EVENT_BUS.register(this);
    }

    // Singleton getter
    public static synchronized Raycast getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Raycast();
        }
        return INSTANCE;
    }

    private HitResult performDirectRaycast(ServerPlayer player) {
        Level world = player.level();
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 lookVec = player.getLookAngle();
        Vec3 endPos = eyePos.add(
                lookVec.x * MAX_RAYCAST_DISTANCE,
                lookVec.y * MAX_RAYCAST_DISTANCE,
                lookVec.z * MAX_RAYCAST_DISTANCE
        );

        // First, do a block raycast
        HitResult blockHitResult = world.clip(new ClipContext(
                eyePos,
                endPos,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
        ));

        // Find entities along the ray
        List<Entity> entitiesInWorld = world.getEntities(player,
                new AABB(eyePos, endPos).inflate(1.0),
                entity -> entity instanceof LivingEntity && entity != player
        );

        // Check for direct intersection with ray
        for (Entity entity : entitiesInWorld) {
            if (isEntityDirectlyInRay(eyePos, lookVec, entity)) {
                return new EntityHitResult(entity);
            }
        }

        // Return original block hit or miss
        return blockHitResult;
    }

    // Precise method to check if an entity is directly in the ray's path
    private boolean isEntityDirectlyInRay(Vec3 rayOrigin, Vec3 rayDirection, Entity entity) {
        // Get the entity's bounding box
        AABB entityBox = entity.getBoundingBox();

        // Calculate the closest point on the ray to the entity's center
        Vec3 entityCenter = entityBox.getCenter();
        Vec3 originToCenter = entityCenter.subtract(rayOrigin);

        // Project the origin-to-center vector onto the ray direction
        double t = originToCenter.dot(rayDirection);

        // Calculate the closest point on the ray
        Vec3 closestPointOnRay = rayOrigin.add(rayDirection.scale(t));

        // Calculate distance between closest point and entity center
        double distanceToRay = closestPointOnRay.distanceTo(entityCenter);

        // Check if the closest point is within the ray's max distance
        // and the distance to the ray is within the tolerance
        return t > 0 && t <= MAX_RAYCAST_DISTANCE &&
                distanceToRay <= ENTITY_INTERSECTION_TOLERANCE;
    }

    // Modify your existing performContinuousRaycast method
    private void performContinuousRaycast(ServerPlayer player) {
        HitResult hitResult = performDirectRaycast(player);

        switch (hitResult.getType()) {
            case BLOCK:
                BlockHitResult blockHit = (BlockHitResult) hitResult;
                handleBlockHit(player, blockHit);
                break;

            case ENTITY:
                EntityHitResult entityHit = (EntityHitResult) hitResult;
                handleEntityHit(player, entityHit);
                break;

            case MISS:
                handleMiss(player);
                break;
        }
    }

    protected void handleBlockHit(ServerPlayer player, BlockHitResult blockHit) {
        // Override this method in your specific implementation
        // Current implementation is a placeholder
        System.out.println("Block hit: " + blockHit.getBlockPos());
    }

    protected void handleEntityHit(ServerPlayer player, EntityHitResult entityHit) {
        // Override this method in your specific implementation
        // Current implementation is a placeholder

        Entity entity = entityHit.getEntity();

        System.out.println("Entity hit: " + entity.getName());

        if(entity instanceof LimbEntity limb){
            UUID entityId = limb.getUUID();

            // Only set the flag if we haven't detected this entity before
            if (!detectedEntities.containsKey(entityId)){
                // Mark this entity as detected
                detectedEntities.put(entityId, true);

                // Instead of directly triggering the animation,
                // set a flag or property on the entity that its own animation system can check
                limb.setSeen(true);  // Assuming this method exists

                // If your entity has a method to check if it should start its animation, call it
                // This allows the entity's own system to decide when to play the animation
                if (!limb.retracting) {
                    limb.retract();
                }

                System.out.println("Entity detected: " + entityId);
            }
        }
        detectedEntities.clear();
    }

    protected void handleMiss(ServerPlayer player) {
        // Override this method in your specific implementation
        // Current implementation is a placeholder
        System.out.println("No hit");
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        // Only process on the server side and at the end of each server tick
        if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END) {
            tickCounter++;

            // Only process every TICK_DELAY ticks
            if (tickCounter >= TICK_DELAY) {
                // Reset tick counter
                tickCounter = 0;

                // Iterate through all players on the server
                for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
                    performContinuousRaycast(player);
                }
            }
        }
    }
}