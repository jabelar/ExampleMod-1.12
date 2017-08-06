package com.blogspot.jabelarminecraft.blocksmith.registries;

import java.util.HashSet;
import java.util.Set;

import com.blogspot.jabelarminecraft.blocksmith.BlockSmith;
import com.blogspot.jabelarminecraft.blocksmith.blocks.BlockCompactor;
import com.blogspot.jabelarminecraft.blocksmith.blocks.BlockDeconstructor;
import com.blogspot.jabelarminecraft.blocksmith.blocks.BlockForge;
import com.blogspot.jabelarminecraft.blocksmith.blocks.BlockGrinder;
import com.blogspot.jabelarminecraft.blocksmith.blocks.BlockMovingLightSource;
import com.blogspot.jabelarminecraft.blocksmith.blocks.BlockTanningRack;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(BlockSmith.MODID)
public class BlockRegistry {
//	public static class ArmorMaterials {
//		public static final BlockArmor.ArmorMaterial ARMOUR_MATERIAL_REPLACEMENT = EnumHelper.addArmorMaterial(Constants.RESOURCE_PREFIX + "replacement", Constants.RESOURCE_PREFIX + "replacement", 15, new int[]{1, 4, 5, 2}, 12, SoundEvents.Block_ARMOR_EQUIP_CHAIN, (float) 0);
//	}
//
//	public static class ToolMaterials {
//		public static final Block.ToolMaterial TOOL_MATERIAL_GLOWSTONE = EnumHelper.addToolMaterial("glowstone", 1, 5, 0.5f, 1.0f, 10);
//	}

    // instantiate blocks
	public final static BlockTanningRack TANNING_RACK = new BlockTanningRack();
	public final static BlockGrinder GRINDER = new BlockGrinder();
	public final static BlockCompactor COMPACTOR = new BlockCompactor();
    public final static BlockDeconstructor DECONSTRUCTOR = new BlockDeconstructor();
    public final static BlockForge FORGE = new BlockForge(false);
//    public final static BlockForge blockForgeLit = new BlockForge(true);
    public final static BlockMovingLightSource MOVING_LIGHT_SOURCE = new BlockMovingLightSource();

	/**
	 * Initialize this mod's {@link Block}s with any post-registration data.
	 */
	private static void initialize() 
	{
	}

	@Mod.EventBusSubscriber(modid = BlockSmith.MODID)
	public static class RegistrationHandler 
	{
		public static final Set<Block> SET_BLOCKS = new HashSet<>();

		/**
		 * Register this mod's {@link Block}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onEvent(final RegistryEvent.Register<Block> event) 
		{
			final Block[] arrayBlocks = {
					TANNING_RACK,
					GRINDER,
					COMPACTOR,
					DECONSTRUCTOR,
					FORGE
			};

			final IForgeRegistry<Block> registry = event.getRegistry();

			for (final Block Block : arrayBlocks) {
				registry.register(Block);
				SET_BLOCKS.add(Block);
			}

			initialize();
		}
	}
}
