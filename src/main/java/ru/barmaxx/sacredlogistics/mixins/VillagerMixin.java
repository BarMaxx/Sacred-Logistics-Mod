package ru.barmaxx.sacredlogistics.mixins;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.barmaxx.sacredlogistics.events.VillagerTradeEvent;

@Mixin(Villager.class)
public class VillagerMixin {
    @Inject(method = "startTrading", at = @At("HEAD"), cancellable = true)
    private void onTrade(Player p_35537_, CallbackInfo ci) {
        var event = new VillagerTradeEvent(p_35537_, (Villager) (Object) this);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCanceled()) ci.cancel();
    }
}
