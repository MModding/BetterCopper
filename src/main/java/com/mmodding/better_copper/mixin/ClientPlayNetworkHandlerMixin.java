package com.mmodding.better_copper.mixin;

import com.mmodding.better_copper.copper_capabilities.client.ClientCopperCapabilitiesManager;
import com.mmodding.better_copper.ducks.ClientPlayNetworkHandlerDuckInterface;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.telemetry.TelemetryManager;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin implements ClientPlayNetworkHandlerDuckInterface {

	@Unique
	private ClientCopperCapabilitiesManager copperCapabilitiesManager;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void init(MinecraftClient client, Screen screen, ClientConnection connection, GameProfile profile, TelemetryManager telemetryManager, CallbackInfo ci) {
		this.copperCapabilitiesManager = new ClientCopperCapabilitiesManager();
	}

	@Override
	public ClientCopperCapabilitiesManager better_copper$getCopperCapabilitiesManager() {
		return this.copperCapabilitiesManager;
	}
}
