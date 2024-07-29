package ru.barmaxx.sacredlogistics.events

import com.sun.source.util.SourcePositions
import net.darkhax.gamestages.GameStageHelper
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.registries.ForgeRegistries

object SacredEvents {
    @SubscribeEvent
    fun onEntityKilled(event: LivingDeathEvent) {
        event.source.entity?.let { source ->
            if (source is Player) {
                val entity = event.entity
                val type = ForgeRegistries.ENTITY_TYPES.getKey(entity.type)?.toString() ?: return@let
                source.sendSystemMessage(Component.literal("You killed ${event.entity.name.string}"))

                if(type.equals("minecraft:zombie")) {
                    GameStageHelper.addStage(source as? ServerPlayer ?: return, "demonite_and_crimson")
                }
            }
        }
    }
}

val ORE_STAGES = mapOf(
    "minecraft:wither" to listOf("demonite_ore", "bagryan_ore"),
    "ice_and_fire:dragon" to listOf("chlorofil_ore"),
    "minecraft:ender_dragon" to listOf("cobalt_ore")
)