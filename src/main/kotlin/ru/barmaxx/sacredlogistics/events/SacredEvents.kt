package ru.barmaxx.sacredlogistics.events

import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

object SacredEvents {
    @SubscribeEvent
    fun onEntityKilled(event: LivingDeathEvent) {
        event.source.entity?.let { source ->
            if (source is Player) {
                source.sendSystemMessage(Component.literal("You killed ${event.entity.name.string}"))
            }
        }
    }
}