package ru.barmaxx.sacredlogistics.mixins;

import it.hurts.sskirillss.relics.init.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WitherSkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import reliquary.init.ModItems;

@Mixin(WitherSkullBlock.class)
public class WitherSkullBlockMixin {
    @Inject(method = "setPlacedBy", at = @At("HEAD"), cancellable = true)
    private void onCheckSpawn(Level level, BlockPos pos, BlockState state, LivingEntity entity, ItemStack item, CallbackInfo ci) {
        if (entity instanceof ServerPlayer player) {
            var hasWater = player.getInventory().contains(new ItemStack(ModItems.GLOWING_WATER.get()));

            if (!hasWater && level.getBlockState(pos.below()).is(Blocks.SOUL_SAND)) {
                player.sendSystemMessage(Component.translatable("sacred_logistics.messages.wither"));
                ci.cancel();
            }
        }
    }
}
