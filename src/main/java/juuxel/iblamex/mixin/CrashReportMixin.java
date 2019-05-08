package juuxel.iblamex.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(CrashReport.class)
public class CrashReportMixin {
    @Shadow
    @Final
    private CrashReportSection systemDetailsSection;

    @Unique
    private static Stream<ModContainer> i_blame_x$getMods() {
        return FabricLoader.getInstance().getAllMods().stream();
    }

    @ModifyVariable(method = "generateWittyComment", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/SystemUtil;getMeasuringTimeNano()J", shift = At.Shift.BEFORE), ordinal = 0)
    private static String[] getWittyComments(String[] original) {
        return i_blame_x$getMods()
                .map(mod -> String.format("I blame %s.", mod.getMetadata().getName()))
                .toArray(String[]::new);
    }

    @Inject(method = "fillSystemDetails", at = @At(value = "INVOKE", ordinal = 1, target = "Lnet/minecraft/util/crash/CrashReportSection;add(Ljava/lang/String;Lnet/minecraft/util/crash/ICrashCallable;)V", shift = At.Shift.BEFORE))
    private void onFillSystemDetailsAfterGameVersion(CallbackInfo info) {
        systemDetailsSection.add(
                "Mods",
                i_blame_x$getMods().map(ModContainer::getMetadata)
                        .map(meta -> String.format("%s@%s", meta.getId(), meta.getVersion()))
                        .collect(Collectors.joining(", "))
        );
    }

    @Inject(method = "asString", at = @At("RETURN"), cancellable = true)
    private void onAsString(CallbackInfoReturnable<String> info) {
        info.setReturnValue(info.getReturnValue() + "\n\tBlaming and listing mods brought to you by I Blame X.");
    }
}
