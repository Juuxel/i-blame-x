package juuxel.iblamex.mixin;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.crash.CrashReportSection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Collectors;

@Mixin(CrashReportSection.class)
public class CrashReportSectionMixin {
    @Shadow @Final private String title;
    private static final String I_BLAME_X$TARGET_TITLE = "System Details";

    @Inject(method = "addStackTrace", at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;", ordinal = 0))
    private void onAddStackTraceHead(StringBuilder builder, CallbackInfo info) {
        if (I_BLAME_X$TARGET_TITLE.equals(title)) {
            builder.append("\n\tMods: ").append(FabricLoader.getInstance()
                    .getAllMods()
                    .stream()
                    .map(mod -> String.format("%s@%s", mod.getMetadata().getId(), mod.getMetadata().getVersion()))
                    .collect(Collectors.joining(", ")));
        }
    }

    @Inject(method = "addStackTrace", at = @At(value = "RETURN"))
    private void onAddStackTraceReturn(StringBuilder builder, CallbackInfo info) {
        if (I_BLAME_X$TARGET_TITLE.equals(title)) {
            builder.append("\n\tBlaming and listing mods brought to you by I Blame X.");
        }
    }
}
