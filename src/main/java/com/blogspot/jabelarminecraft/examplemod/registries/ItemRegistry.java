package com.blogspot.jabelarminecraft.examplemod.registries;

import java.util.HashSet;
import java.util.Set;

import com.blogspot.jabelarminecraft.examplemod.MainMod;
import com.blogspot.jabelarminecraft.examplemod.items.ItemCowHide;
import com.blogspot.jabelarminecraft.examplemod.items.ItemHorseHide;
import com.blogspot.jabelarminecraft.examplemod.items.ItemPigSkin;
import com.blogspot.jabelarminecraft.examplemod.items.ItemSheepSkin;
import com.blogspot.jabelarminecraft.examplemod.items.ItemSwordExtended;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

@ObjectHolder(MainMod.MODID)
public class ItemRegistry {
//	public static class ArmorMaterials {
//		public static final ItemArmor.ArmorMaterial ARMOUR_MATERIAL_REPLACEMENT = EnumHelper.addArmorMaterial(Constants.RESOURCE_PREFIX + "replacement", Constants.RESOURCE_PREFIX + "replacement", 15, new int[]{1, 4, 5, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, (float) 0);
//	}
//
//	public static class ToolMaterials {
//		public static final Item.ToolMaterial TOOL_MATERIAL_GLOWSTONE = EnumHelper.addToolMaterial("glowstone", 1, 5, 0.5f, 1.0f, 10);
//	}

    // instantiate items
	public final static ItemCowHide COW_HIDE = new ItemCowHide();
	public final static ItemSheepSkin SHEEP_SKIN = new ItemSheepSkin();
	public final static ItemPigSkin PIG_SKIN = new ItemPigSkin();
	public final static ItemHorseHide HORSE_HIDE = new ItemHorseHide();
	public final static ItemSwordExtended SWORD_EXTENDED = new ItemSwordExtended(ToolMaterial.IRON);

	/**
	 * Initialize this mod's {@link Item}s with any post-registration data.
	 */
	private static void initialize() 
	{
		// You can put furnace smelting recipes here
	}

	@Mod.EventBusSubscriber(modid = MainMod.MODID)
	public static class RegistrationHandler 
	{
		public static final Set<Item> SET_ITEMS = new HashSet<>();

		/**
		 * Register this mod's {@link Item}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onEvent(final RegistryEvent.Register<Item> event) 
		{
			final Item[] arrayItems = {
					COW_HIDE,
					SHEEP_SKIN,
					PIG_SKIN,
					HORSE_HIDE,
					SWORD_EXTENDED
			};

			final IForgeRegistry<Item> registry = event.getRegistry();

	        System.out.println("Registering items");

			for (final Item item : arrayItems) {
				registry.register(item);
				SET_ITEMS.add(item);
				// DEBUG
				System.out.println("Registering item: "+item.getRegistryName());
			}

			registerItemModels();
			
			initialize();
		}
	}
	
    public static void registerItemModels()
    {
        // DEBUG
        System.out.println("Registering item renderers");
        
        registerItemModel(COW_HIDE);
        registerItemModel(SHEEP_SKIN);
        registerItemModel(PIG_SKIN);
        registerItemModel(HORSE_HIDE);
        registerItemModel(SWORD_EXTENDED);
    }
    
    public static void registerItemModel(Item parItem)
    {
    	registerItemModel(parItem, 0);
    }
    
    public static void registerItemModel(Item parItem, int parMetaData)
    {
        ModelLoader.setCustomModelResourceLocation(parItem, parMetaData, new ModelResourceLocation(MainMod.MODID + ":" + parItem.getUnlocalizedName().substring(5), "inventory"));
    }

	//	public static class ArmorMaterials {
	//		public static final ItemArmor.ArmorMaterial ARMOUR_MATERIAL_REPLACEMENT = EnumHelper.addArmorMaterial(Constants.RESOURCE_PREFIX + "replacement", Constants.RESOURCE_PREFIX + "replacement", 15, new int[]{1, 4, 5, 2}, 12, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, (float) 0);
	//	}
	//
	//	public static class ToolMaterials {
	//		public static final Item.ToolMaterial TOOL_MATERIAL_GLOWSTONE = EnumHelper.addToolMaterial("glowstone", 1, 5, 0.5f, 1.0f, 10);
	//	}
}

