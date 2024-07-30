package ru.barmaxx.sacredlogistics.mixins;

import earth.terrarium.ad_astra.common.networking.packet.server.StartRocketPacket;
import it.hurts.sskirillss.relics.init.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "earth/terrarium/ad_astra/common/networking/packet/server/StartRocketPacket$Handler")
public class RocketMixin {
    @Inject(method = "lambda$handle$0", at = @At("HEAD"), cancellable = true)
    private static void onLaunch(StartRocketPacket packet, Player player, Level level, CallbackInfo ci) {
        var hasCoin = player.getInventory().contains(new ItemStack(ItemRegistry.HORSE_FLUTE.get()));

        if (!hasCoin) {
            player.sendSystemMessage(Component.translatable("sacred_logistics.messages.coin"));
            ci.cancel();
        }
    }
}