package ru.barmaxx.sacredlogistics.mixins;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Blocks;
import net.sixik.orestages.StageContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.barmaxx.sacredlogistics.SacredLogistics;
import ru.barmaxx.sacredlogistics.registry.SacredBlocks;

@Mixin(value = StageContainer.class)
public class StageContainerMixin {
    @Inject(at = @At("TAIL"), method = "apply(Ljava/lang/Void;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", remap = false)
    private void onApply(Void p_10793_, ResourceManager p_10794_, ProfilerFiller p_10795_, CallbackInfo ci) {
        StageContainer.getOrCreateBlock(SacredLogistics.MODID + ":demonite_and_crimson", SacredBlocks.INSTANCE.getDEMONITE_ORE().get().defaultBlockState(), null, Blocks.NETHERRACK.defaultBlockState(), false);
        StageContainer.getOrCreateBlock(SacredLogistics.MODID + ":demonite_and_crimson", SacredBlocks.INSTANCE.getCRIMSON_ORE().get().defaultBlockState(), null, Blocks.NETHERRACK.defaultBlockState(), false);
        StageContainer.getOrCreateBlock(SacredLogistics.MODID + ":cobalt_ore", SacredBlocks.INSTANCE.getCOBALT_ORE().get().defaultBlockState(), null, Blocks.END_STONE.defaultBlockState(), false);
        StageContainer.getOrCreateBlock(SacredLogistics.MODID + ":chlorophyte_ore", SacredBlocks.INSTANCE.getCHLOROPHYTE_ORE().get().defaultBlockState(), null, Blocks.STONE.defaultBlockState(), false);
    }
}
