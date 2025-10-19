package gunsmith.registries;


import necesse.inventory.item.ItemCategory;

import static necesse.inventory.item.ItemCategory.craftingManager;

public class CategoriesRegistry {
    public CategoriesRegistry() {
    }

    public static void register() {
        registerItemCategory();
        registerToolsCategory();
        registerMeleeCategory();
        registerRangedGunCategory();
        registerRangedBowCategory();
    }

    private static void registerItemCategory() {
        ItemCategory.masterManager.createCategory("Z-A-A", new String[]{"parts"});
        ItemCategory.masterManager.createCategory("Z-B-A", new String[]{"TF"});
        ItemCategory.masterManager.createCategory("A-A-A", new String[]{"TF", "pickaxe"});
        ItemCategory.masterManager.createCategory("A-A-B", new String[]{"TF", "shovel"});
        ItemCategory.masterManager.createCategory("A-A-C", new String[]{"TF", "axe"});
        ItemCategory.masterManager.createCategory("A-B-A", new String[]{"TF", "glaive"});
        ItemCategory.masterManager.createCategory("A-C-A", new String[]{"TF", "shotgun"});
        ItemCategory.masterManager.createCategory("A-C-B", new String[]{"TF", "machinegun"});
    }

    private static void registerToolsCategory() {
        ItemCategory.masterManager.createCategory("A-A-A", new String[]{"parts", "pickaxe"});
        ItemCategory.masterManager.createCategory("A-A-A", new String[]{"parts", "pickaxe", "pickaxeHead"});
        ItemCategory.masterManager.createCategory("A-A-B", new String[]{"parts", "pickaxe", "pickaxeBody"});
        ItemCategory.masterManager.createCategory("A-B-A", new String[]{"parts", "shovel"});
        ItemCategory.masterManager.createCategory("A-B-A", new String[]{"parts", "shovel", "shovelHead"});
        ItemCategory.masterManager.createCategory("A-B-B", new String[]{"parts", "shovel", "shovelBody"});
        ItemCategory.masterManager.createCategory("A-C-A", new String[]{"parts", "axe"});
        ItemCategory.masterManager.createCategory("A-C-A", new String[]{"parts", "axe", "axeHead"});
        ItemCategory.masterManager.createCategory("A-C-A", new String[]{"parts", "axe", "axeBody"});
        ItemCategory.masterManager.createCategory("A-D-A", new String[]{"parts", "pickaxeShaft"});
        ItemCategory.craftingManager.createCategory("A-A-A", new String[]{"pickaxeHead"});
        ItemCategory.craftingManager.createCategory("A-A-B", new String[]{"pickaxeBody"});
        ItemCategory.craftingManager.createCategory("A-B-A", new String[]{"shovelHead"});
        ItemCategory.craftingManager.createCategory("A-B-B", new String[]{"shovelBody"});
        ItemCategory.craftingManager.createCategory("A-C-A", new String[]{"axeHead"});
        ItemCategory.craftingManager.createCategory("A-C-B", new String[]{"axeBody"});
        ItemCategory.craftingManager.createCategory("A-D-A", new String[]{"pickaxeShaft"});
    }

    private static void registerMeleeCategory() {
        ItemCategory.masterManager.createCategory("B-A-A", new String[]{"parts", "glaive"});
        ItemCategory.masterManager.createCategory("B-A-A", new String[]{"parts", "glaive", "glaiveBlade"});
        ItemCategory.masterManager.createCategory("B-A-B", new String[]{"parts", "glaive", "glaiveGrip"});
        ItemCategory.masterManager.createCategory("B-A-C", new String[]{"parts", "glaive", "glaiveShaft"});
        ItemCategory.craftingManager.createCategory("B-A-A", new String[]{"glaiveBlade"});
        ItemCategory.craftingManager.createCategory("B-A-B", new String[]{"glaiveGrip"});
        ItemCategory.craftingManager.createCategory("B-A-C", new String[]{"glaiveShaft"});
    }

    private static void registerRangedGunCategory() {
        ItemCategory.masterManager.createCategory("C-A-A", new String[]{"parts", "pistol"});
        ItemCategory.masterManager.createCategory("C-A-A", new String[]{"parts", "pistol", "pistolBarrel"});
        ItemCategory.masterManager.createCategory("C-A-B", new String[]{"parts", "pistol", "pistolBody"});
        ItemCategory.masterManager.createCategory("C-A-C", new String[]{"parts", "pistol", "pistolStock"});
        ItemCategory.masterManager.createCategory("C-B-A", new String[]{"parts", "machinegun"});
        ItemCategory.masterManager.createCategory("C-B-A", new String[]{"parts", "machinegun", "machinegunBarrel"});
        ItemCategory.masterManager.createCategory("C-B-B", new String[]{"parts", "machinegun", "machinegunBody"});
        ItemCategory.masterManager.createCategory("C-B-C", new String[]{"parts", "machinegun", "machinegunStock"});
        ItemCategory.masterManager.createCategory("C-C-A", new String[]{"parts", "shotgun"});
        ItemCategory.masterManager.createCategory("C-C-A", new String[]{"parts", "shotgun", "shotgunBarrel"});
        ItemCategory.masterManager.createCategory("C-C-B", new String[]{"parts", "shotgun", "shotgunBody"});
        ItemCategory.masterManager.createCategory("C-C-C", new String[]{"parts", "shotgun", "shotgunStock"});
        ItemCategory.masterManager.createCategory("C-D-A", new String[]{"parts", "rifle"});
        ItemCategory.masterManager.createCategory("C-D-A", new String[]{"parts", "rifle", "rifleBarrel"});
        ItemCategory.masterManager.createCategory("C-D-B", new String[]{"parts", "rifle", "rifleBody"});
        ItemCategory.masterManager.createCategory("C-D-C", new String[]{"parts", "rifle", "rifleStock"});
        ItemCategory.craftingManager.createCategory("C-A-A", new String[]{"pistolBarrel"});
        ItemCategory.craftingManager.createCategory("C-A-B", new String[]{"pistolBody"});
        ItemCategory.craftingManager.createCategory("C-A-C", new String[]{"pistolStock"});
        ItemCategory.craftingManager.createCategory("C-B-A", new String[]{"machinegunBarrel"});
        ItemCategory.craftingManager.createCategory("C-B-B", new String[]{"machinegunBody"});
        ItemCategory.craftingManager.createCategory("C-B-C", new String[]{"machinegunStock"});
        ItemCategory.craftingManager.createCategory("C-C-A", new String[]{"shotgunBarrel"});
        ItemCategory.craftingManager.createCategory("C-C-B", new String[]{"shotgunBody"});
        ItemCategory.craftingManager.createCategory("C-C-C", new String[]{"shotgunStock"});
        ItemCategory.craftingManager.createCategory("C-D-A", new String[]{"rifleBarrel"});
        ItemCategory.craftingManager.createCategory("C-D-B", new String[]{"rifleBody"});
        ItemCategory.craftingManager.createCategory("C-D-C", new String[]{"rifleStock"});
    }

    private static void registerRangedBowCategory() {
    }
}
