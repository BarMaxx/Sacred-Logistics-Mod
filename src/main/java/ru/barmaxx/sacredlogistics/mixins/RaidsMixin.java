package ru.barmaxx.sacredlogistics.mixins;

import it.hurts.sskirillss.relics.init.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raids;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import reliquary.init.ModItems;

@Mixin(Raids.class)
public class RaidsMixin {
    @Inject(method = "createOrExtendRaid", at = @At("HEAD"), cancellable = true)
    private void onStartRaid(ServerPlayer player, CallbackInfoReturnable<Raid> cir) {
        var hasCoin = player.getInventory().contains(new ItemStack(ModItems.TWILIGHT_CLOAK.get()));

        if (!hasCoin) {
            player.sendSystemMessage(Component.translatable("sacred_logistics.messages.raid"), true);
            cir.setReturnValue(null);
        }
    }
}
