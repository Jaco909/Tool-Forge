package gunsmith.registries;


import necesse.inventory.item.ItemCategory;

import static necesse.inventory.item.ItemCategory.craftingManager;

public class CategoriesRegistry {
    public static void register() {
        registerItemCategory();
        registerToolsCategory();
        registerMeleeCategory();
        registerRangedGunCategory();
        registerRangedBowCategory();
    }
    private static void registerItemCategory(){
        ItemCategory.masterManager.createCategory("Z-A-A", "parts");
        ItemCategory.masterManager.createCategory("Z-A-B", "barrel");
        ItemCategory.masterManager.createCategory("Z-A-C", "body");
        ItemCategory.masterManager.createCategory("Z-A-D", "stock");
    }
    private static void registerToolsCategory(){
        ItemCategory.masterManager.createCategory("A-A-A", "pickaxeHead");
        craftingManager.createCategory("A-A-A", new String[]{"pickaxeHead"});
        ItemCategory.masterManager.createCategory("A-A-B", "pickaxeBody");
        craftingManager.createCategory("A-A-B", new String[]{"pickaxeBody"});
        ItemCategory.masterManager.createCategory("A-B-A", "shovelHead");
        craftingManager.createCategory("A-B-A", new String[]{"shovelHead"});
        ItemCategory.masterManager.createCategory("A-B-B", "shovelBody");
        craftingManager.createCategory("A-B-B", new String[]{"shovelBody"});
        ItemCategory.masterManager.createCategory("A-B-C", "shovelShaft");
        craftingManager.createCategory("A-B-C", new String[]{"shovelShaft"});
        ItemCategory.masterManager.createCategory("A-C-A", "axeHead");
        craftingManager.createCategory("A-C-A", new String[]{"axeHead"});
        ItemCategory.masterManager.createCategory("A-C-B", "axeBody");
        craftingManager.createCategory("A-C-B", new String[]{"axeBody"});
        ItemCategory.masterManager.createCategory("A-C-C", "axeShaft");
        craftingManager.createCategory("A-C-C", new String[]{"axeShaft"});
        ItemCategory.masterManager.createCategory("A-D-A", "pickaxeShaft");
        craftingManager.createCategory("A-D-A", new String[]{"pickaxeShaft"});
    }
    private static void registerMeleeCategory(){
        ItemCategory.masterManager.createCategory("B-C-A", "glaiveBlade");
        craftingManager.createCategory("B-C-A", new String[]{"glaiveBlade"});
        ItemCategory.masterManager.createCategory("B-C-B", "glaiveGrip");
        craftingManager.createCategory("B-A-B", new String[]{"glaiveGrip"});
        ItemCategory.masterManager.createCategory("B-C-C", "glaiveShaft");
        craftingManager.createCategory("B-C-C", new String[]{"glaiveShaft"});
    }
    private static void registerRangedGunCategory(){
        ItemCategory.masterManager.createCategory("C-A-A", "pistolBarrel");
        craftingManager.createCategory("C-A-A", new String[]{"pistolBarrel"});
        ItemCategory.masterManager.createCategory("C-A-B", "pistolBody");
        craftingManager.createCategory("C-A-B", new String[]{"pistolBody"});
        ItemCategory.masterManager.createCategory("C-A-C", "pistolStock");
        craftingManager.createCategory("C-A-C", new String[]{"pistolStock"});
        ItemCategory.masterManager.createCategory("C-B-A", "machinegunBarrel");
        craftingManager.createCategory("C-B-A", new String[]{"machinegunBarrel"});
        ItemCategory.masterManager.createCategory("C-B-B", "machinegunBody");
        craftingManager.createCategory("C-B-B", new String[]{"machinegunBody"});
        ItemCategory.masterManager.createCategory("C-B-C", "machinegunStock");
        craftingManager.createCategory("C-B-C", new String[]{"machinegunStock"});
        ItemCategory.masterManager.createCategory("C-C-A", "shotgunBarrel");
        craftingManager.createCategory("C-C-A", new String[]{"shotgunBarrel"});
        ItemCategory.masterManager.createCategory("C-C-B", "shotgunBody");
        craftingManager.createCategory("C-C-B", new String[]{"shotgunBody"});
        ItemCategory.masterManager.createCategory("C-C-C", "shotgunStock");
        craftingManager.createCategory("C-C-C", new String[]{"shotgunStock"});
        ItemCategory.masterManager.createCategory("C-D-A", "rifleBarrel");
        craftingManager.createCategory("C-D-A", new String[]{"rifleBarrel"});
        ItemCategory.masterManager.createCategory("C-D-B", "rifleBody");
        craftingManager.createCategory("C-D-B", new String[]{"rifleBody"});
        ItemCategory.masterManager.createCategory("C-D-C", "rifleStock");
        craftingManager.createCategory("C-D-C", new String[]{"rifleStock"});
    }
    private static void registerRangedBowCategory(){}
}
