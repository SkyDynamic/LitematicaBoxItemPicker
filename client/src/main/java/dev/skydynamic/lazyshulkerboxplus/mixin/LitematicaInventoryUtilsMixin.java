package dev.skydynamic.lazyshulkerboxplus.mixin;

import dev.skydynamic.lazyshulkerboxplus.config.Configs;
import fi.dy.masa.litematica.util.InventoryUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.skydynamic.lazyshulkerboxplus.utils.PlayerSlotUtils.getPlayerSlotHaveEmpty;
import static dev.skydynamic.lazyshulkerboxplus.utils.PlayerSlotUtils.moveBoxItem;
import static fi.dy.masa.litematica.util.InventoryUtils.findSlotWithBoxWithItem;
import static fi.dy.masa.litematica.util.InventoryUtils.setPickedItemToHand;

@Environment(EnvType.CLIENT)
@Mixin(InventoryUtils.class)
public abstract class LitematicaInventoryUtilsMixin {

    @Inject(
        method = "schematicWorldPickBlock",
        at = @At(
            value = "INVOKE",
            target = "Lfi/dy/masa/litematica/util/InventoryUtils;findSlotWithBoxWithItem(Lnet/minecraft/screen/ScreenHandler;Lnet/minecraft/item/ItemStack;Z)I"
        ),
        cancellable = true
    )
    private static void getStack(ItemStack stack, BlockPos pos, World schematicWorld, MinecraftClient mc, CallbackInfo ci) {
        if (Configs.Generic.ENABLE_LSBP.getBooleanValue()) {
            ClientPlayerEntity player = mc.player;
            int slot = findSlotWithBoxWithItem(player.currentScreenHandler, stack, false);
            int maxMoveCount = Configs.Generic.LSBP_COUNT.getIntegerValue();
            if (slot != -1 && getPlayerSlotHaveEmpty(player)) {
                ItemStack boxStack = player.playerScreenHandler.slots.get(slot).getStack();
                if (mc.getCurrentServerEntry() == null) {
                    ServerPlayerEntity serverPlayer = mc.getServer().getPlayerManager().getPlayer(player.getUuid());
                    moveBoxItem(serverPlayer, stack, boxStack, maxMoveCount, slot);
                    setPickedItemToHand(stack, mc);
                    ci.cancel();
                }
                Identifier id = new Identifier("lsbp", "move_item_count");
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeInt(maxMoveCount);
                buf.writeInt(slot);
                buf.writeItemStack(stack);
                buf.writeItemStack(boxStack);
                ClientPlayNetworking.send(id, buf);
            }
        }
    }
}

