package ru.barmaxx.sacredlogistics

import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityAttributeCreationEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.LogManager
import ru.barmaxx.sacredlogistics.entities.MeteoriteEntity
import ru.barmaxx.sacredlogistics.entities.MeteoriteRenderer
import ru.barmaxx.sacredlogistics.events.SacredEvents
import ru.barmaxx.sacredlogistics.events.StructureEvents
import ru.barmaxx.sacredlogistics.registry.SacredBlocks
import ru.barmaxx.sacredlogistics.registry.SacredEntities
import ru.barmaxx.sacredlogistics.registry.SacredItems

@Mod(SacredLogistics.MODID)
class SacredLogistics {
    init {
        MinecraftForge.EVENT_BUS.register(SacredEvents)
        MinecraftForge.EVENT_BUS.register(StructureEvents)
        SacredBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().modEventBus)
        SacredItems.ITEMS.register(FMLJavaModLoadingContext.get().modEventBus)
        SacredEntities.ENTITIES.register(FMLJavaModLoadingContext.get().modEventBus)

        FMLJavaModLoadingContext.get().modEventBus.addListener(::entityRenderers)

        LOGGER.info("Initialized Sacred Logistics Mod!")
    }

    fun entityRenderers(event: EntityRenderersEvent.RegisterRenderers) {
        event.registerEntityRenderer(SacredEntities.METEORITE.get(), ::MeteoriteRenderer)
    }

    companion object {
        const val MODID = "sacred_logistics"
        val LOGGER = LogManager.getLogger()
    }
}