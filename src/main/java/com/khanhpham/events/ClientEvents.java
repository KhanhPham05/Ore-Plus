package com.khanhpham.events;

import com.khanhpham.RawOres;
import com.khanhpham.registries.machine.oreenricher.EnricherScreen;
import com.khanhpham.registries.ContainerRegistries;
import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = RawOres.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    private ClientEvents(){}

    @SubscribeEvent
    public static void clientSetup(final FMLClientSetupEvent event) {
        ScreenManager.register(ContainerRegistries.ENRICHER.get(), EnricherScreen::new);
    }
}
