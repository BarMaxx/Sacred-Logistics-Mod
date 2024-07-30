package ru.barmaxx.sacredlogistics.events

import artifacts.common.init.ModItems
import it.hurts.sskirillss.relics.init.ItemRegistry
import net.darkhax.gamestages.GameStageHelper
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.TickEvent.PlayerTickEvent
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.ModList
import net.minecraftforge.registries.ForgeRegistries
import reliquary.init.ModBlocks
import ru.barmaxx.sacredlogistics.SacredLogistics
import ru.barmaxx.sacredlogistics.entities.MeteoriteEntity
import ru.barmaxx.sacredlogistics.registry.SacredEntities
import ru.hollowhorizon.hc.client.utils.mcTranslate
import ru.hollowhorizon.hc.client.utils.rl
import top.theillusivec4.curios.api.CuriosApi
import java.util.concurrent.CompletableFuture
import reliquary.init.ModItems as ReliquaryItems

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
                        source.sendSystemMessage("sacred_logistics.messages.boss_kill".mcTranslate)
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
        if (event.phase == TickEvent.Phase.START) return

        val player = event.player
        val curios = CuriosApi.getCuriosHelper().getEquippedCurios(player)
        val meteorCount = player.persistentData.getInt("meteorite_count")

        val interval = when {
            meteorCount < 10 -> 7 * 24000
            meteorCount < 20 -> 10 * 24000
            meteorCount < 30 -> 15 * 24000
            else -> (15 + ((meteorCount / 10) - 2) * 5) * 24000
        }

        if (player.level.gameTime % interval == 0L && player.level.dimension().location()
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
            if (player.tickCount % (20 * 15) == 0 && !it.getStackInSlot(4).`is`(ModItems.FIRE_GAUNTLET.get())) {
                player.setSecondsOnFire(5)
                player.sendSystemMessage("sacred_logistics.messages.fire".mcTranslate)
            }
        }
    }

    @SubscribeEvent
    fun onTrade(event: VillagerTradeEvent) {
        val profession = event.villager.villagerData.profession
        when {
            profession == VillagerProfession.CLERIC && !event.player.hasCurio(1, ModItems.CROSS_NECKLACE.get()) -> {
                event.player.sendSystemMessage("sacred_logistics.messages.cleric".mcTranslate(ModItems.CROSS_NECKLACE.get().description))
                event.isCanceled = true
            }

            profession == VillagerProfession.FARMER && event.player.inventory.contains(ItemStack(ModBlocks.FERTILE_LILY_PAD_ITEM.get())) -> {
                event.player.sendSystemMessage("sacred_logistics.messages.farmer".mcTranslate(ModBlocks.FERTILE_LILY_PAD_ITEM.get().description))
                event.isCanceled = true
            }

            (profession == VillagerProfession.FLETCHER || profession == VillagerProfession.ARMORER || profession == VillagerProfession.WEAPONSMITH) &&
                    event.player.inventory.contains(ItemStack(ReliquaryItems.MIDAS_TOUCHSTONE.get())) -> {
                event.player.sendSystemMessage("sacred_logistics.messages.farmer".mcTranslate(ReliquaryItems.MIDAS_TOUCHSTONE.get().description))
                event.isCanceled = true
            }
        }
    }

    @SubscribeEvent
    fun onChangeDimension(event: EntityTravelToDimensionEvent) {
        if (event.entity is ServerPlayer) {
            val player = event.entity as ServerPlayer
            val hasSpatialSign = player.inventory.contains(ItemStack(ItemRegistry.SPATIAL_SIGN.get()))

            event.isCanceled = !hasSpatialSign
            if (!hasSpatialSign) {
                player.sendSystemMessage("sacred_logistics.messages.dimension".mcTranslate(ItemRegistry.SPATIAL_SIGN.get().description), true)
                return
            }

            val hasReliquary = ModList.get().isLoaded("reliquary")
            val hasEndItems =
                (!hasReliquary || player.inventory.contains(ItemStack(ReliquaryItems.HERO_MEDALLION.get())))
                        && (!hasReliquary || player.inventory.contains(ItemStack(ReliquaryItems.ANGELIC_FEATHER.get())))
                        && player.inventory.contains(ItemStack(ItemRegistry.ENDER_HAND.get()))

            if (!hasEndItems && event.dimension == Level.END) {
                if (hasReliquary) {
                    player.sendSystemMessage(
                        "sacred_logistics.messages.end".mcTranslate(
                            ReliquaryItems.HERO_MEDALLION.get().description,
                            ReliquaryItems.ANGELIC_FEATHER.get().description,
                            ItemRegistry.ENDER_HAND.get().description
                        ), true
                    )
                } else {
                    player.sendSystemMessage(
                        "sacred_logistics.messages.end".mcTranslate(
                            ItemRegistry.ENDER_HAND.get().description,
                            ItemRegistry.ENDER_HAND.get().description,
                            ItemRegistry.ENDER_HAND.get().description
                        ), true
                    )
                }
                event.isCanceled = true
                return
            }

            if(event.dimension.location() == "allthemodium:the_other".rl || event.dimension.location() == "allthemodium:mining".rl) {
                val hasOtherItems = player.inventory.contains(ItemStack(ModItems.SUPERSTITIOUS_HAT.get()))

                if(hasOtherItems) {
                    player.sendSystemMessage("sacred_logistics.messages.dimension".mcTranslate(ModItems.SUPERSTITIOUS_HAT.get().description), true)
                }
            }
        }
    }
}

fun Player.hasCurio(index: Int, item: Item): Boolean {
    val helper = CuriosApi.getCuriosHelper().getEquippedCurios(this)
    if (!helper.isPresent) return false
    return helper.orElseThrow { IllegalStateException("Curio not found!") }.getStackInSlot(index).`is`(item)
}


var showMessage = false
fun blockSwimming(player: Player): Boolean {
    val curios = CuriosApi.getCuriosHelper().getEquippedCurios(player)
    val swimTime = player.persistentData.getInt("swim_time")
    val hasFlippes =
        curios.isPresent && curios.orElseThrow { NullPointerException("Curios API not initialized!") }
            .getStackInSlot(9)
            .`is`(ModItems.FLIPPERS.get())

    if (player.isInWater && !hasFlippes) {
        if (curios.isPresent && swimTime >= 200) {
            player.setDeltaMovement(player.deltaMovement.x, -0.1, player.deltaMovement.z)
        }
        if (showMessage && swimTime > 50) {
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