[![](https://jitpack.io/v/paulevsGitch/Creative-b.1.7.3-.svg)](https://jitpack.io/#paulevsGitch/Creative-b.1.7.3-)
# Creative

This mod adds a creative mode from modern MC versions to beta 1.7.3.

Requires **[Fabric Legacy](https://github.com/calmilamsy/Cursed-Fabric-MultiMC)** and **[Station API](https://github.com/ModificationStation/StationAPI)** (2.0-PRE1 or newer)

## Features:
- Separate creative and survival worlds;
- Modern-like creative GUI (based on beta 1.8 and current creative GUI);
- Creative invulnerability;
- Creative flight (double tap jump key);
- Creative block breaking;
- Middle click to get block you're looking on when in creative;

## How to install (MultiMC):
- Download [MultiMC instance](https://github.com/calmilamsy/Cursed-Fabric-MultiMC) and import it as zip
- Download [Station API](https://github.com/ModificationStation/StationAPI/releases) and import it as a mod in MultiMC
- Import this mod

## Mod Api
Creative has a possibility for mods to add their own tabs, the simple tab can be done like this:
```java
SimpleTab tab = CreativeTabs.register(new SimpleTab("test", "coolmod", YOUR_ITEM_ICON));
tab.addItem(new ItemInstance(YOUR_ITEM_OR_BLOCK, 1, YOUR_META));
CreativeTabs.register(tab);
```
**Where:**
- YOUR_ITEM_ICON is an ItemInstance for display as a tab icon;
- YOUR_ITEM_OR_BLOCK is an ItemInstance for your item;
- YOUR_META is meta for your item, if your item don't have meta you can use:

```java
new ItemInstance(YOUR_ITEM_OR_BLOCK)
```
