package ru.barmaxx.sacredlogistics.mixins;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.barmaxx.sacredlogistics.events.SacredEventsKt;

@Mixin(Player.class)
public class PlayerMixin {
    @Inject(method = "travel", at = @At(value = "HEAD"), cancellable = true)
    private void onTravel(Vec3 vec3, CallbackInfo ci) {
        if (SacredEventsKt.blockSwimming((Player) (Object) this)) ci.cancel();
    }
}
