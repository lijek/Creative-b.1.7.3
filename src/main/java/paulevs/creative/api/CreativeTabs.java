package paulevs.creative.api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.item.Block;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.armour.Armour;
import net.minecraft.item.food.FoodBase;
import net.minecraft.item.tool.Hoe;
import net.minecraft.item.tool.Sword;
import net.minecraft.item.tool.ToolBase;
import paulevs.creative.Creative;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CreativeTabs {
	private static final Map<Integer, List<CreativeTab>> TABS_RENDER = Maps.newHashMap();
	private static final Map<String, CreativeTab> TABS = Maps.newHashMap();
	private static final List<CreativeTab> TAB_ORDERED = Lists.newArrayList();
	private static final List<ItemInstance> EMPTY_ITEMS = Collections.emptyList();
	private static final List<CreativeTab> EMPTY_TABS = Collections.emptyList();
	
	public static List<ItemInstance> getItemList(String id) {
		CreativeTab tab = TABS.get(id);
		if (tab != null) {
			return tab.getItems();
		}
		return EMPTY_ITEMS;
	}
	
	public static <T extends CreativeTab> T register(T tab) {
		TABS.put(tab.getName(), tab);
		TAB_ORDERED.add(tab);
		return tab;
	}
	
	public static void initVanilla() {
		SearchTab tabAll = register(new SearchTab("minecraft_all", Creative.MODID.toString(), ItemBase.compass));
		SimpleTab tabFullBlocks = register(new SimpleTab("minecraft_full_blocks", Creative.MODID.toString(), BlockBase.STONE));
		SimpleTab tabOtherBlocks = register(new SimpleTab("minecraft_other_blocks", Creative.MODID.toString(), BlockBase.LADDER));
		SimpleTab tabTools = register(new SimpleTab("minecraft_tools", Creative.MODID.toString(), ItemBase.ironPickaxe));
		SimpleTab tabWeaponry = register(new SimpleTab("minecraft_weapons", Creative.MODID.toString(), ItemBase.ironSword));
		SimpleTab tabResources = register(new SimpleTab("minecraft_resources", Creative.MODID.toString(), ItemBase.ironIngot));
		SimpleTab tabFood = register(new SimpleTab("minecraft_food", Creative.MODID.toString(), ItemBase.apple));
		SimpleTab tabItems = register(new SimpleTab("minecraft_other_items", Creative.MODID.toString(), ItemBase.slimeball));
		
		Set<Integer> resources = Sets.newHashSet();
		resources.add(ItemBase.glowstoneDust.id);
		resources.add(ItemBase.redstoneDust.id);
		resources.add(ItemBase.goldIngot.id);
		resources.add(ItemBase.gunpowder.id);
		resources.add(ItemBase.ironIngot.id);
		resources.add(ItemBase.slimeball.id);
		resources.add(ItemBase.diamond.id);
		resources.add(ItemBase.feather.id);
		resources.add(ItemBase.string.id);
		resources.add(ItemBase.brick.id);
		resources.add(ItemBase.flint.id);
		resources.add(ItemBase.paper.id);
		resources.add(ItemBase.stick.id);
		resources.add(ItemBase.bone.id);
		resources.add(ItemBase.clay.id);
		resources.add(ItemBase.coal.id);
		
		for (int i = 0; i < 32000; i++) {
			if (ItemBase.byId[i] != null) {
				ItemBase item = ItemBase.byId[i];
				tabAll.addItemWithVariants(item);
				if (i < BlockBase.BY_ID.length && BlockBase.BY_ID[i] != null) {
					BlockBase block = BlockBase.BY_ID[i];

					if (block.isFullCube()) {
						tabFullBlocks.addItemWithVariants(item);
					}
					else {
						tabOtherBlocks.addItemWithVariants(item);
					}
				}
				else if (item instanceof Block) {
					if (item == ItemBase.cake) {
						tabFood.addItemWithVariants(item);
					}
					else if (item == ItemBase.sugarCanes) {
						tabFood.addItemWithVariants(item);
					}
					else {
						tabOtherBlocks.addItemWithVariants(item);
					}
				}
				else if (isTool(item)) {
					tabTools.addItemWithVariants(item);
				}
				else if (isWeaponry(item)) {
					tabWeaponry.addItemWithVariants(item);
				}
				else if (isFood(item)) {
					tabFood.addItemWithVariants(item);
				}
				else if (resources.contains(i)) {
					tabResources.addItemWithVariants(item);
				}
				else {
					tabItems.addItemWithVariants(item);
				}
			}
		}
	}

	private static boolean isTool(ItemBase item){
		return item instanceof ToolBase ||
				item instanceof Hoe ||
				item == ItemBase.flintAndSteel ||
				item == ItemBase.fishingRod ||
				item == ItemBase.shears ||
				item == ItemBase.clock;
	}

	private static boolean isWeaponry(ItemBase item) {
		return item instanceof Armour || item instanceof Sword || item == ItemBase.bow || item == ItemBase.arrow;
	}

	private static boolean isFood(ItemBase item) {
		return item instanceof FoodBase ||
				item == ItemBase.seeds ||
				item == ItemBase.sugar ||
				item == ItemBase.wheat ||
				item == ItemBase.bowl ||
				item == ItemBase.egg;
	}

	public static void initTabs() {
		for (int i = 0; i < TAB_ORDERED.size(); i++) {
			int index = i / 7;
			List<CreativeTab> list = TABS_RENDER.computeIfAbsent(index, k -> Lists.newArrayList());
			list.add(TAB_ORDERED.get(i));
		}
	}
	
	public static int getPagesCount() {
		return TABS_RENDER.size();
	}
	
	public static CreativeTab getTab(int page, int index) {
		List<CreativeTab> list = TABS_RENDER.get(page);
		if (list == null || index < 0 || index >= list.size()) {
			return null;
		}
		return list.get(index);
	}
	
	public static int getTabCount(int page) {
		return TABS_RENDER.getOrDefault(page, EMPTY_TABS).size();
	}
}
