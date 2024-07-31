package ru.barmaxx.sacredlogistics.entities

import net.minecraft.core.BlockPos
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile
import net.minecraft.world.level.Explosion.BlockInteraction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.Vec3
import net.minecraftforge.registries.ForgeRegistries
import ru.barmaxx.sacredlogistics.registry.SacredBlocks
import ru.hollowhorizon.hc.client.utils.rl
import java.util.stream.Collectors

class MeteoriteEntity(type: EntityType<MeteoriteEntity>, level: Level) : AbstractHurtingProjectile(type, level) {
    override fun shouldBurn() = false

    override fun tick() {
        super.tick()

        if (!level.getBlockState(blockPosition()).isAir) discard()
    }

    override fun remove(reason: RemovalReason) {
        level.explode(this, this.x, this.getY(0.0625), this.z, 20.0f, BlockInteraction.BREAK)

        (blockPosition().x - 3..blockPosition().x + 3).forEach { x ->
            (blockPosition().y - 3..blockPosition().y + 3).forEach { y ->
                (blockPosition().z - 3..blockPosition().z + 3).forEach { z ->
                    val len = Vec3(
                        blockPosition().x.toDouble() + 0.5,
                        blockPosition().y.toDouble() + 0.5,
                        blockPosition().z.toDouble() + 0.5
                    ).subtract(Vec3(x.toDouble(), y.toDouble(), z.toDouble())).length()

                    var state = ForgeRegistries.BLOCKS.tags()
                        ?.getTag(TagKey.create(ForgeRegistries.BLOCKS.registryKey, "forge:ores_in_ground/stone".rl))
                        ?.stream()?.collect(Collectors.toList())?.randomOrNull()?.defaultBlockState()
                        ?: Blocks.IRON_ORE.defaultBlockState()

                    if (random.nextBoolean()) {
                        state = if (random.nextBoolean()) SacredBlocks.METEORITE_ORE.get().defaultBlockState()
                        else ForgeRegistries.BLOCKS.getValue("assemblylinemachines:black_granite".rl)!!
                            .defaultBlockState()
                    }

                    if (len < 3) level.setBlockAndUpdate(BlockPos(x, y, z), state)
                }
            }
        }

        super.remove(reason)
    }
}