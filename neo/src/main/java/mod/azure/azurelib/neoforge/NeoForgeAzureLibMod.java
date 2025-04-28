package mod.azure.azurelib.neoforge;

import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;

import mod.azure.azurelib.common.internal.common.AzureLib;
import mod.azure.azurelib.common.internal.common.AzureLibMod;
import mod.azure.azurelib.common.internal.common.config.io.ConfigIO;
import mod.azure.azurelib.common.internal.common.network.packet.*;
import mod.azure.azurelib.neoforge.platform.NeoForgeCommonRegistry;

@Mod(AzureLib.MOD_ID)
public final class NeoForgeAzureLibMod {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS_REGISTER = DeferredRegister
        .createDataComponents(
            Registries.DATA_COMPONENT_TYPE,
            AzureLib.MOD_ID
        );

    public NeoForgeAzureLibMod(IEventBus modEventBus) {
        AzureLib.initialize();
        AzureLibMod.initRegistry();
        DATA_COMPONENTS_REGISTER.register(modEventBus);
        if (NeoForgeCommonRegistry.blockEntityTypeDeferredRegister != null)
            NeoForgeCommonRegistry.blockEntityTypeDeferredRegister.register(modEventBus);
        if (NeoForgeCommonRegistry.blockDeferredRegister != null)
            NeoForgeCommonRegistry.blockDeferredRegister.register(modEventBus);
        modEventBus.addListener(this::init);
        modEventBus.addListener(this::registerMessages);
    }

    private void init(FMLCommonSetupEvent event) {
        ConfigIO.FILE_WATCH_MANAGER.startService();
    }

    public void registerMessages(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(AzureLib.MOD_ID);

        registrar.playBidirectional(
            AzEntityDispatchCommandPacket.TYPE,
            AzEntityDispatchCommandPacket.CODEC,
            (msg, ctx) -> msg.handle()
        );
        registrar.playBidirectional(
            AzItemStackDispatchCommandPacket.TYPE,
            AzItemStackDispatchCommandPacket.CODEC,
            (msg, ctx) -> msg.handle()
        );
        registrar.playBidirectional(
            AzBlockEntityDispatchCommandPacket.TYPE,
            AzBlockEntityDispatchCommandPacket.CODEC,
            (msg, ctx) -> msg.handle()
        );
        registrar.playBidirectional(SendConfigDataPacket.TYPE, SendConfigDataPacket.CODEC, (msg, ctx) -> msg.handle());
    }
}
