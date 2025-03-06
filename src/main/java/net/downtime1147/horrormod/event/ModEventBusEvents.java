package net.downtime1147.horrormod.event;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.downtime1147.horrormod.HorrorMod;
import net.downtime1147.horrormod.entity.ModEntities;
import net.downtime1147.horrormod.entity.custom.LimbEntity;

@Mod.EventBusSubscriber(modid = HorrorMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(ModEntities.LIMB.get(), LimbEntity.createAttributes().build());
    }
}
