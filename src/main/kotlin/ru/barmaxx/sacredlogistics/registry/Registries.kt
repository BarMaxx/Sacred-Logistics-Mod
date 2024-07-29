package ru.barmaxx.sacredlogistics.registry

import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Material
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import ru.barmaxx.sacredlogistics.SacredLogistics


object SacredItems {
    val ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SacredLogistics.MODID)

    val CHLOROPHYTE_INGOT = ITEMS.register("chlorophyte_ingot") { Item(Item.Properties().tab(SACRED_TAB)) }
    val COBALT_INGOT = ITEMS.register("cobalt_ingot") { Item(Item.Properties().tab(SACRED_TAB)) }
    val CRIMSON_INGOT = ITEMS.register("crimson_ingot") { Item(Item.Properties().tab(SACRED_TAB)) }
    val DEMONITE_INGOT = ITEMS.register("demonite_ingot") { Item(Item.Properties().tab(SACRED_TAB)) }
    val LUMINITE_INGOT = ITEMS.register("luminite_ingot") { Item(Item.Properties().tab(SACRED_TAB)) }
    val METEORITE_INGOT = ITEMS.register("meteorite_ingot") { Item(Item.Properties().tab(SACRED_TAB)) }
    val RAW_COBALT = ITEMS.register("raw_cobalt") { Item(Item.Properties().tab(SACRED_TAB)) }
    val RAW_CHLOROPHYTE: RegistryObject<Item> =
        ITEMS.register("raw_chlorophyte") { Item(Item.Properties().tab(SACRED_TAB)) }
    val RAW_CRIMSON = ITEMS.register("raw_crimson") { Item(Item.Properties().tab(SACRED_TAB)) }
    val RAW_DEMONITE = ITEMS.register("raw_demonite") { Item(Item.Properties().tab(SACRED_TAB)) }
    val RAW_LUMINITE = ITEMS.register("raw_luminite") { Item(Item.Properties().tab(SACRED_TAB)) }
    val RAW_METEORITE = ITEMS.register("raw_meteorite") { Item(Item.Properties().tab(SACRED_TAB)) }
}

object SacredBlocks {
    val BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SacredLogistics.MODID)

    val CHLOROPHYTE_ORE =
        BLOCKS.registerOre("chlorophyte_ore") {
            Block(
                BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops()
                    .strength(5f, 8f)
            )
        }
    val COBALT_ORE = BLOCKS.registerOre("cobalt_ore") {
        Block(
            BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops()
                .strength(5f, 8f)
        )
    }
    val CRIMSON_ORE = BLOCKS.registerOre("crimson_ore") {
        Block(
            BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops()
                .strength(5f, 8f)
        )
    }
    val DEEPSLATE_CHLOROPHYTE_ORE =
        BLOCKS.registerOre("deepslate_chlorophyte_ore") {
            Block(
                BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops()
                    .strength(5f, 8f)
            )
        }
    val DEMONITE_ORE = BLOCKS.registerOre("demonite_ore") {
        Block(
            BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops()
                .strength(5f, 8f)
        )
    }
    val LUMINITE_ORE = BLOCKS.registerOre("luminite_ore") {
        Block(
            BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops()
                .strength(5f, 8f)
        )
    }
    val METEORITE_ORE = BLOCKS.registerOre("meteorite_ore") {
        Block(
            BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops()
                .strength(5f, 8f)
        )
    }
}

fun <T : Block> DeferredRegister<T>.registerOre(
    name: String,
    block: () -> T,
): RegistryObject<T> {
    val result = register(name, block)
    SacredItems.ITEMS.register(name) { BlockItem(result.get(), Item.Properties().tab(SACRED_TAB)) }
    return result
}

private const val WOOD_GOLD = 0
private const val STONE = 1
private const val IRON = 2
private const val DIAMOND = 3
private const val NETHERITE = 4

val SACRED_TAB = object : CreativeModeTab(SacredLogistics.MODID) {
    override fun makeIcon(): ItemStack {
        return ItemStack(SacredItems.RAW_CHLOROPHYTE.get())
    }
}
