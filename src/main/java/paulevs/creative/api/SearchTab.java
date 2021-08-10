package paulevs.creative.api;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockBase;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;

import java.util.List;
import java.util.Locale;

public class SearchTab extends SimpleTab {
    private List<ItemInstance> filteredItems = Lists.newArrayList();
    public SearchTab(String name, String modID, ItemInstance icon) {
        super(name, modID, icon);
        setFilter("");
    }

    public SearchTab(String name, String modID, int id, int meta) {
        this(name, modID, new ItemInstance(id, 1, meta));
    }

    public SearchTab(String name, String modID, BlockBase block) {
        this(name, modID, new ItemInstance(block));
    }

    public SearchTab(String name, String modID, ItemBase item) {
        this(name, modID, new ItemInstance(item));
    }

    public void setFilter(String filter) {
        if(filter == null || filter.isEmpty()){
            filteredItems = getItems();
            return;
        }
        filteredItems = Lists.newArrayList();
        for (ItemInstance item : getItems()) {
            if((TranslationStorage.getInstance().method_995(item.getTranslationKey())).trim().toLowerCase(Locale.ROOT).contains(filter)){
                filteredItems.add(item);
            }
        }
    }

    public List<ItemInstance> getFilteredItems(){
        return filteredItems;
    }
}
