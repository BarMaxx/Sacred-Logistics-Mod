package ru.barmaxx.sacredlogistics.events

import artifacts.common.init.ModItems
import net.darkhax.gamestages.GameStageHelper
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraftforge.event.TickEvent.PlayerTickEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.registries.ForgeRegistries
import ru.barmaxx.sacredlogistics.SacredLogistics
import ru.barmaxx.sacredlogistics.entities.MeteoriteEntity
import ru.barmaxx.sacredlogistics.registry.SacredEntities
import ru.hollowhorizon.hc.client.utils.mcTranslate
import top.theillusivec4.curios.api.CuriosApi

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

        val player = event.player
        val curios = CuriosApi.getCuriosHelper().getEquippedCurios(player)
        val meteorCount = player.persistentData.getInt("meteorite_count")

        val interval = when {
            meteorCount < 10 -> 7 * 24000
            meteorCount < 20 -> 10 * 24000
            meteorCount < 30 -> 15 * 24000
            else -> (15 + ((meteorCount / 10) - 2) * 5) * 24000
        }

        if (player.tickCount % interval == 0 && player.level.dimension().location()
                .toString() == "minecraft:overworld"
        ) {
            val pos = player.blockPosition().mutable().setY(player.level.maxBuildHeight)
            player.sendSystemMessage("sacred_logistics.messages.meteorite".mcTranslate)

            val meteorite = MeteoriteEntity(SacredEntities.METEORITE.get(), player.level)
            meteorite.setPos(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())
            player.level.addFreshEntity(meteorite)
            meteorite.setDeltaMovement(0.1, -0.2, 0.1)
            meteorite.xPower = 0.01
            meteorite.yPower = -0.2
            meteorite.zPower = 0.01

            player.persistentData.putInt("meteorite_count", meteorCount + 1)
        }

        if (player.level.dimension() == Level.NETHER) curios.ifPresent {
            if (player.tickCount % 20 * 15 == 0 && !it.getStackInSlot(4).`is`(ModItems.FIRE_GAUNTLET.get())) {
                player.setSecondsOnFire(5)
                player.sendSystemMessage("sacred_logistics.messages.fire".mcTranslate)
            }
        }
    }
}

var showMessage = false
fun blockSwimming(player: Player): Boolean {
    val curios = CuriosApi.getCuriosHelper().getEquippedCurios(player)
    val swimTime = player.persistentData.getInt("swim_time")
    if (player.isInWater) {
        if (curios.isPresent && swimTime >= 200) {
            val curio = curios.orElseThrow { NullPointerException("Curios API not initialized!") }
            if (!curio.getStackInSlot(9).`is`(ModItems.FLIPPERS.get())) {
                player.setDeltaMovement(player.deltaMovement.x, -0.1, player.deltaMovement.z)
            }
        }
        if (showMessage && swimTime > 20) {
            player.sendSystemMessage("sacred_logistics.messages.swim".mcTranslate)
            showMessage = false
        }
        player.persistentData.putInt("swim_time", (swimTime + 1).coerceAtMost(200))
    } else if (swimTime > 0) {
        player.persistentData.putInt("swim_time", (swimTime - 1).coerceAtLeast(0))
    } else {
        showMessage = true
    }
    return false
}