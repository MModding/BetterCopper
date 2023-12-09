package com.mmodding.better_copper.mixin;

import com.mmodding.better_copper.copper_capabilities.CopperCapabilitiesTracker;
import com.mmodding.better_copper.ducks.ServerPlayerEntityDuckInterface;
import com.mmodding.mmodding_lib.library.utils.Self;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements ServerPlayerEntityDuckInterface, Self<ServerPlayerEntity> {

	@Unique
	private CopperCapabilitiesTracker copperCapabilitiesTracker;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(MinecraftServer server, ServerWorld world, GameProfile gameProfile, PlayerPublicKey publicKey, CallbackInfo ci) {
		this.copperCapabilitiesTracker = new CopperCapabilitiesTracker(this.getObject());
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void tick(CallbackInfo ci) {
		this.copperCapabilitiesTracker.sendUpdate(this.getObject());
	}

	@Override
	public CopperCapabilitiesTracker better_copper$getCopperCapabilitiesTracker() {
		return this.copperCapabilitiesTracker;
	}
}
