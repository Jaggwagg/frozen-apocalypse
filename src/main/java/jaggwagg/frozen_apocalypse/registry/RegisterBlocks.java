package jaggwagg.frozen_apocalypse.registry;

import jaggwagg.frozen_apocalypse.FrozenApocalypse;
import jaggwagg.frozen_apocalypse.block.ModBlocks;
import jaggwagg.frozen_apocalypse.item.ModItemGroups;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Arrays;

public class RegisterBlocks {
    public static void init() {
        Arrays.stream(ModBlocks.values()).forEach(value -> registerBlockWithItem(value.getId(), value.getBlock()));
    }

    public static void registerBlockWithItem(String id, Block block) {
        Registry.register(Registries.BLOCK, new Identifier(FrozenApocalypse.MOD_ID, id), block);
        BlockItem item = Registry.register(Registries.ITEM, new Identifier(FrozenApocalypse.MOD_ID, id), new BlockItem(block, new FabricItemSettings()));
        ItemGroupEvents.modifyEntriesEvent(ModItemGroups.FROZEN_APOCALYPSE.getItemGroup()).register(content -> content.add(item));
    }
}
