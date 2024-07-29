package ru.barmaxx.sacredlogistics

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.apache.logging.log4j.LogManager
import ru.barmaxx.sacredlogistics.events.SacredEvents
import ru.barmaxx.sacredlogistics.registry.SacredBlocks
import ru.barmaxx.sacredlogistics.registry.SacredItems

@Mod(SacredLogistics.MODID)
class SacredLogistics {
    init {
        MinecraftForge.EVENT_BUS.register(SacredEvents)
        SacredBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().modEventBus)
        SacredItems.ITEMS.register(FMLJavaModLoadingContext.get().modEventBus)

        LOGGER.info("Initialized Sacred Logistics Mod!")
    }

    companion object {
        const val MODID = "sacred_logistics"
        val LOGGER = LogManager.getLogger()
    }
}