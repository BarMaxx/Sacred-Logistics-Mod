package ru.barmaxx.sacredlogistics

import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager

@Mod(SacredLogistics.MODID)
class SacredLogistics {
    init {
        LOGGER.info("Initialized Sacred Logistics Mod!")
    }

    companion object {
        const val MODID = "sacred_logistics"
        val LOGGER = LogManager.getLogger()
    }
}