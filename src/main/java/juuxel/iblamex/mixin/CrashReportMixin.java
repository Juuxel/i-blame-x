package juuxel.iblamex.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Lazy;
import net.minecraft.util.crash.CrashReport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CrashReport.class)
public class CrashReportMixin {
    private static final Lazy<String[]> I_BLAME_X$WITTY_COMMENTS = new Lazy<>(
            () -> FabricLoader.getInstance().getAllMods()
                    .stream()
                    .map(mod -> String.format("I blame %s.", mod.getMetadata().getName()))
                    .toArray(String[]::new)
    );

    @ModifyVariable(method = "generateWittyComment", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/SystemUtil;getMeasuringTimeNano()J", shift = At.Shift.BEFORE), ordinal = 0)
    private static String[] getWittyComments(String[] original) {
        return I_BLAME_X$WITTY_COMMENTS.get();
    }
}
