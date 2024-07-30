package ru.barmaxx.sacredlogistics.mixins;

import artifacts.common.init.ModItems;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.barmaxx.sacredlogistics.events.SacredEventsKt;

@Mixin(IronGolem.class)
public class IronGolemMixin {
    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void isAngryAt(CallbackInfo ci) {
        var mob = (IronGolem) (Object) this;
        mob.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(mob, Player.class, 10, true, false, entity -> {
            if (entity instanceof Player player) {
                var hasHat = player.getInventory().contains(new ItemStack(ModItems.VILLAGER_HAT.get())) || SacredEventsKt.hasCurio(player, 0, ModItems.VILLAGER_HAT.get());

                return !hasHat;
            }
            return false;
        }));
    }
}
