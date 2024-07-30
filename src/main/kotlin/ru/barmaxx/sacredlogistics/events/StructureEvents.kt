package ru.barmaxx.sacredlogistics.events

import artifacts.common.init.ModItems
import com.github.L_Ender.cataclysm.structures.*
import com.github.alexthe666.iceandfire.world.structure.GorgonTempleStructure
import it.hurts.sskirillss.relics.init.ItemRegistry
import net.minecraft.data.worldgen.Structures
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure
import net.minecraft.world.level.levelgen.structure.structures.OceanMonumentStructure
import net.minecraftforge.event.TickEvent.PlayerTickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.registries.ForgeRegistries
import ru.hollowhorizon.hc.client.utils.mcTranslate
import ru.hollowhorizon.hc.client.utils.rl

object StructureEvents {
    @SubscribeEvent
    fun playerTick(event: PlayerTickEvent) {
        if (event.player is ServerPlayer) {
            val player = event.player as ServerPlayer
            val structures = player.getLevel().structureManager().getAllStructuresAt(player.blockPosition())
            structures.keys.forEach {
                when {
                    it is OceanMonumentStructure -> {
                        val hasCharm = player.hasCurio(ModItems.CHARM_OF_SINKING.get())
                        if (!hasCharm) {
                            player.addEffect(MobEffectInstance(MobEffects.BLINDNESS, 100, 3))
                            player.sendSystemMessage(
                                "sacred_logistics.messages.monument".mcTranslate(ModItems.CHARM_OF_SINKING.get().description),
                                true
                            )
                            if (player.tickCount % 40 == 0) player.hurt(DamageSource.DROWN, 3f)
                        }
                    }

                    it is GorgonTempleStructure -> {
                        val hasCharm = player.hasCurio(ModItems.ANTIDOTE_VESSEL.get()) && (
                                ForgeRegistries.ITEMS.getKey(player.getItemBySlot(EquipmentSlot.HEAD).item) == "mekanism:hazmat_mask".rl &&
                                        ForgeRegistries.ITEMS.getKey(player.getItemBySlot(EquipmentSlot.CHEST).item) == "mekanism:hazmat_gown".rl &&
                                        ForgeRegistries.ITEMS.getKey(player.getItemBySlot(EquipmentSlot.LEGS).item) == "mekanism:hazmat_pants".rl &&
                                        ForgeRegistries.ITEMS.getKey(player.getItemBySlot(EquipmentSlot.FEET).item) == "mekanism:hazmat_boots".rl
                                )
                        if (!hasCharm && player.tickCount % 40 == 0) {
                            player.addEffect(MobEffectInstance(MobEffects.POISON, 100, 3))
                            player.sendSystemMessage("sacred_logistics.messages.gorgon".mcTranslate(ModItems.ANTIDOTE_VESSEL.get().description))
                            player.hurt(DamageSource.IN_WALL, 3f)
                        }
                    }

                    it is Burning_Arena_Structure || it is Cursed_Pyramid_Structure ||
                            it is RuinedCitadelStructure || it is SoulBlackSmithStructure ||
                            it is Sunken_City_Structure -> {
                        val hasCharm = player.hasCurio(ModItems.OBSIDIAN_SKULL.get())
                        if (!hasCharm && player.tickCount % 40 == 0) {
                            player.addEffect(MobEffectInstance(MobEffects.BLINDNESS, 100, 3))
                            player.sendSystemMessage(
                                "sacred_logistics.messages.cataclysm".mcTranslate(ModItems.OBSIDIAN_SKULL.get().description),
                                true
                            )
                            player.hurt(DamageSource.DROWN, 6f)
                        }
                    }

                    it is JigsawStructure -> {
                        when {
                            it == Structures.BASTION_REMNANT.get() || it == Structures.FORTRESS.get() -> {
                                val hasCharm = player.hasCurio(ModItems.FLAME_PENDANT.get())
                                if (!hasCharm && player.tickCount % 100 == 0) {
                                    player.addEffect(MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1))
                                    player.sendSystemMessage(
                                        "sacred_logistics.messages.nether".mcTranslate(ModItems.FLAME_PENDANT.get().description),
                                        true
                                    )
                                    player.setSecondsOnFire(10)
                                }
                            }

                            it == Structures.END_CITY.get() -> {
                                val hasCharm = player.inventory.contains(ItemStack(ItemRegistry.SPACE_DISSECTOR.get()))
                                if (!hasCharm && player.tickCount % 100 == 0) {
                                    player.addEffect(MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1))
                                    player.sendSystemMessage(
                                        "sacred_logistics.messages.end_structure".mcTranslate(ItemRegistry.SPACE_DISSECTOR.get().description),
                                        true
                                    )
                                    player.setSecondsOnFire(10)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}