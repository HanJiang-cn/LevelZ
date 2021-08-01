package net.levelz.mixin.item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.levelz.access.PlayerStatsManagerAccess;
import net.levelz.init.ConfigInit;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MiningToolItem;
import net.minecraft.util.ActionResult;

@Mixin(HoeItem.class)
public class HoeItemMixin {

    @Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Ljava/util/function/Predicate;test(Ljava/lang/Object;)Z"), cancellable = true)
    private void useOnBlockMixin(ItemUsageContext context, CallbackInfoReturnable<ActionResult> info) {
        if (!context.getPlayer().isCreative()) {
            int playerFarmingLevel = ((PlayerStatsManagerAccess) context.getPlayer()).getPlayerStatsManager(context.getPlayer()).getLevel("farming");
            if (playerFarmingLevel < ConfigInit.CONFIG.maxLevel) {
                ItemStack itemStack = context.getStack();
                if (playerFarmingLevel < 1 || ((MiningToolItem) itemStack.getItem()).getMaterial().getMiningLevel() * 4 > playerFarmingLevel) {
                    info.setReturnValue(ActionResult.FAIL);
                }
            }
        }
    }

}
