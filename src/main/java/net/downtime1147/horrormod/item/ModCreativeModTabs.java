package net.downtime1147.horrormod.item;

import net.downtime1147.horrormod.HorrorMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HorrorMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> HorrorMod_TAB = CREATIVE_MODE_TABS.register("horrormod_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.DRILL_BIT.get()))
                    .title(Component.translatable("creativetab.horrormod_tab"))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ModItems.DRILL_BIT.get());
                    }))
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
