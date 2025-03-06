package net.downtime1147.horrormod.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.downtime1147.horrormod.HorrorMod;
import net.downtime1147.horrormod.entity.client.LimbModel;
import net.downtime1147.horrormod.entity.client.ModModelLayers;

@Mod.EventBusSubscriber(modid = HorrorMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ModModelLayers.LIMB_LOCATION, LimbModel::createBodyLayer);
    }
}
