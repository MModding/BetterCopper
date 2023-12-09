package com.mmodding.better_copper.mixin;

import com.mmodding.better_copper.copper_capabilities.CopperCapabilitiesTracker;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

	@Shadow
	public abstract List<ServerPlayerEntity> getPlayerList();

	@Inject(method = "onDataPacksReloaded", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/PlayerAdvancementTracker;reload(Lnet/minecraft/server/ServerAdvancementLoader;)V"))
	private void onDataPacksReloaded(CallbackInfo ci) {
		this.getPlayerList().forEach(serverPlayerEntity -> CopperCapabilitiesTracker.getInstance(serverPlayerEntity).reload());
	}
}
