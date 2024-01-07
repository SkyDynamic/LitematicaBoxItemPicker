package dev.skydynamic.litematicaboxitempicker.utils;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerSlotUtils {

    public static boolean isPlayerHaveEmptySlot(ServerPlayerEntity player) {
        return player.getInventory().getEmptySlot() != -1;
    }

    public static int getPlayerEmptySlot(ServerPlayerEntity player) {
        Inventory inventory = player.getInventory();
        for (int i = 0; i < 36; i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    // 给予玩家物品
    public static void givePlayerItems(ItemStack stack, ServerPlayerEntity player) {
        PlayerInventory inventory = player.getInventory();
        int emptySlot = getPlayerEmptySlot(player);
        if (emptySlot != -1 && inventory.insertStack(emptySlot, stack)) {
            ItemEntity itemEntity = player.dropItem(stack, false);
            if (itemEntity != null) {
                itemEntity.setDespawnImmediately();
            }
            player.currentScreenHandler.sendContentUpdates();
        }
    }

    // 将盒子内的物品转移到手上
    public static void moveBoxItem(ServerPlayerEntity player, ItemStack stack, ItemStack boxStack, int maxMoveCount, int boxSlot) {
        NbtCompound boxItemsNbtCompound = boxStack.getOrCreateNbt();
        NbtList itemList = boxItemsNbtCompound.getCompound("BlockEntityTag").getList("Items", 10);
        String stackItemId = Registries.ITEM.getId(stack.getItem()).toString();
        for (int i = 0; i < itemList.size(); ++i) {
            NbtCompound boxItemNbtCompound = itemList.getCompound(i);
            String boxItemId = boxItemNbtCompound.getString("id");
            if (boxItemId.equals(stackItemId)) {
                int itemCount = boxItemNbtCompound.getInt("Count");
                if (itemCount <= maxMoveCount) {
                    ItemStack itemToGive = ItemStack.fromNbt(boxItemNbtCompound).copy();
                    givePlayerItems(itemToGive, player);
                    itemList.remove(i);
                    player.playerScreenHandler.slots.get(boxSlot).getStack().setNbt(boxItemsNbtCompound);
                } else {
                    boxItemNbtCompound.putInt("Count", itemCount - maxMoveCount);
                    ItemStack itemToGive = ItemStack.fromNbt(boxItemNbtCompound).copy();
                    itemToGive.setCount(maxMoveCount);
                    givePlayerItems(itemToGive, player);
                    player.playerScreenHandler.slots.get(boxSlot).getStack().setNbt(boxItemsNbtCompound);
                }
                return;
            }
        }
    }

}
