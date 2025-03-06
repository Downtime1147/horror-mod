package net.downtime1147.horrormod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.downtime1147.horrormod.HorrorMod;
import net.downtime1147.horrormod.entity.custom.LimbEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, HorrorMod.MOD_ID);

    public static final RegistryObject<EntityType<LimbEntity>> LIMB =
            ENTITY_TYPES.register("limb", () -> EntityType.Builder.of(LimbEntity::new, MobCategory.CREATURE)
                    .sized(1.5f, 1.5f).build("limb"));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
