package mod.azure.azurelib.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

import mod.azure.azurelib.common.internal.common.AzureLib;
import mod.azure.azurelib.common.internal.common.AzureLibMod;
import mod.azure.azurelib.common.internal.common.config.io.ConfigIO;
import mod.azure.azurelib.common.internal.common.network.packet.*;
import mod.azure.azurelib.fabric.platform.FabricAzureLibNetwork;

public final class FabricAzureLibMod implements ModInitializer {

    @Override
    public void onInitialize() {
        ConfigIO.FILE_WATCH_MANAGER.startService();
        AzureLib.initialize();
        AzureLibMod.initRegistry();
        new FabricAzureLibNetwork();
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> ConfigIO.FILE_WATCH_MANAGER.stopService());
        PayloadTypeRegistry.playS2C()
            .register(AzBlockEntityDispatchCommandPacket.TYPE, AzBlockEntityDispatchCommandPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(AzEntityDispatchCommandPacket.TYPE, AzEntityDispatchCommandPacket.CODEC);
        PayloadTypeRegistry.playS2C()
            .register(AzItemStackDispatchCommandPacket.TYPE, AzItemStackDispatchCommandPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(SendConfigDataPacket.TYPE, SendConfigDataPacket.CODEC);
    }
}
