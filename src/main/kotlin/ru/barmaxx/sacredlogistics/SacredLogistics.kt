package ru.barmaxx.sacredlogistics

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import ru.barmaxx.sacredlogistics.events.SacredEvents

@Mod(SacredLogistics.MODID)
class SacredLogistics {
    init {
        MinecraftForge.EVENT_BUS.register(SacredEvents)

        LOGGER.info("Initialized Sacred Logistics Mod!")
    }

    companion object {
        const val MODID = "sacred_logistics"
        val LOGGER = LogManager.getLogger()
    }
}