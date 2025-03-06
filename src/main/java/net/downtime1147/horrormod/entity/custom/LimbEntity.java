package net.downtime1147.horrormod.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class LimbEntity extends Animal {
    private static final EntityDataAccessor<Boolean> SEEN =
            SynchedEntityData.defineId(LimbEntity.class, EntityDataSerializers.BOOLEAN);

    public LimbEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {super(pEntityType, pLevel);}

    public boolean retracting = false;

    public final AnimationState idleAnimationState = new AnimationState();
    public int idleAnimationTimeout = 0;

    public final AnimationState retractAnimationState = new AnimationState();
    public int retractAnimationTimeout = 0;

    @Override
    public void tick() {
        super.tick();

        if(this.level().isClientSide){
            setupAnimationStates();
            if(isSeen() && !retracting){
                retract();
            }
        }
    }

    private void setupAnimationStates(){
        if(this.idleAnimationTimeout <= 0){
            this.idleAnimationTimeout = 120;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }

        if(retracting && this.retractAnimationTimeout >= 0){
            if(this.retractAnimationTimeout <= 0){
                discard(); // Not actually despawning it, they just invisible
            } else {
                --this.retractAnimationTimeout;
            }
        }
    }

    public void retract(){
        if(!retracting){
            retracting = true;

            this.idleAnimationState.stop();
            this.retractAnimationTimeout = 20;
            this.retractAnimationState.start(this.tickCount);
        }
    }

    public void setSeen(boolean seen){
        this.entityData.set(SEEN, seen);
    }

    public boolean isSeen(){
        return this.entityData.get(SEEN);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SEEN,false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    public static AttributeSupplier.Builder createAttributes(){
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1000D)
                .add(Attributes.MOVEMENT_SPEED, 0D)
                .add(Attributes.FOLLOW_RANGE, 0D);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }
}
