package ru.barmaxx.sacredlogistics.events

import net.minecraft.world.entity.npc.Villager
import net.minecraft.world.entity.player.Player
import net.minecraftforge.eventbus.api.Cancelable
import net.minecraftforge.eventbus.api.Event

@Cancelable
class VillagerTradeEvent(val player: Player, val villager: Villager): Event()