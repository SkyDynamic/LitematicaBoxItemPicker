package dev.skydynamic.litematicaboxitempicker;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static dev.skydynamic.litematicaboxitempicker.utils.PlayerSlotUtils.*;

public class LitematicaShulkerBoxPickerServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        ServerPlayNetworking.registerGlobalReceiver(
            new Identifier("lsbp", "move_item_count"), (server, player, handler, buf, responseSender) -> {
                int maxCount = buf.readInt();
                int boxSlot = buf.readInt();
                ItemStack stack = buf.readItemStack();
                ItemStack boxStack = buf.readItemStack();
                ServerPlayerEntity serverPlayer = server.getPlayerManager().getPlayer(player.getUuid());
                if (!isPlayerHaveEmptySlot(serverPlayer)) {
                    return;
                }
                moveBoxItem(serverPlayer, stack, boxStack, maxCount, boxSlot);
                System.out.println("执行完成");
                Identifier id = new Identifier("lsbp", "set_picked_item");
                PacketByteBuf sendBuf = PacketByteBufs.create();
                sendBuf.writeItemStack(stack.copy());
                ServerPlayNetworking.send(serverPlayer, id, sendBuf);
        });
    }

}
