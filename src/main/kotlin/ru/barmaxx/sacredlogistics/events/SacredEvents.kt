package ru.barmaxx.sacredlogistics.events

import net.darkhax.gamestages.GameStageHelper
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.TickEvent.PlayerTickEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.registries.ForgeRegistries
import ru.barmaxx.sacredlogistics.SacredLogistics
import ru.barmaxx.sacredlogistics.entities.MeteoriteEntity
import ru.barmaxx.sacredlogistics.registry.SacredEntities
import ru.hollowhorizon.hc.client.utils.mcTranslate

object SacredEvents {
    @SubscribeEvent
    fun onEntityKilled(event: LivingDeathEvent) {
        event.source.entity?.let { source ->
            if (source is Player) {
                val entity = event.entity
                val type = ForgeRegistries.ENTITY_TYPES.getKey(entity.type)?.toString() ?: return@let
                source.sendSystemMessage(Component.literal("You killed ${event.entity.name.string}"))

                fun checkAndAdd(entity: String, stage: String) {
                    if (type == entity && !GameStageHelper.hasStage(source, stage)) {
                        SacredLogistics.LOGGER.info("Player ${source.name.string} unlocked $stage!")
                        GameStageHelper.addStage(source as? ServerPlayer ?: return, stage)
                    }
                }

                checkAndAdd("minecraft:wither", "${SacredLogistics.MODID}:demonite_and_crimson")
                checkAndAdd("minecraft:ender_dragon", "${SacredLogistics.MODID}:cobalt_ore")
                listOf(
                    "iceandfire:fire_dragon", "iceandfire:ice_dragon", "iceandfire:lightning_dragon",
                ).forEach { checkAndAdd(it, "${SacredLogistics.MODID}:chlorophyte_ore") }
            }
        }
    }

    @SubscribeEvent
    fun playerTick(event: PlayerTickEvent) {
        if (event.player.level.isClientSide) return

        val meteorCount = event.player.persistentData.getInt("meteorite_count")

        val interval = when {
            meteorCount < 10 -> 7 * 24000
            meteorCount < 20 -> 10 * 24000
            meteorCount < 30 -> 15 * 24000
            else -> (15 + ((meteorCount / 10) - 2) * 5) * 24000
        }

        if (event.player.tickCount % interval == 0 && event.player.level.dimension().location()
                .toString() == "minecraft:overworld"
        ) {
            val pos = event.player.blockPosition().mutable().setY(event.player.level.maxBuildHeight)
            event.player.sendSystemMessage("sacred_logistics.messages.meteorite".mcTranslate)

            val meteorite = MeteoriteEntity(SacredEntities.METEORITE.get(), event.player.level)
            meteorite.setPos(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
            event.player.level.addFreshEntity(meteorite)
            meteorite.setDeltaMovement(0.1, -0.2, 0.1)
            meteorite.xPower = 0.01
            meteorite.yPower = -0.2
            meteorite.zPower = 0.01
        }
    }
}