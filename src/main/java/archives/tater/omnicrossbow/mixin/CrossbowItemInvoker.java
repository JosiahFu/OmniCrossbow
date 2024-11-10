package archives.tater.omnicrossbow.mixin;

import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CrossbowItem.class)
public interface CrossbowItemInvoker {
    @Invoker
    static void invokeClearProjectiles(ItemStack crossbow) {
        throw new AssertionError();
    }
}
