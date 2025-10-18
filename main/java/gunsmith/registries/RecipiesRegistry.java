package gunsmith.registries;

import necesse.engine.registries.RecipeTechRegistry;
import necesse.inventory.recipe.Ingredient;
import necesse.inventory.recipe.Recipe;
import necesse.inventory.recipe.Recipes;

public class RecipiesRegistry {
    public static void register() {
        registerObjects();
        registerPickaxeHeads();
        registerPickaxeBrace();
        registerShovelHeads();
        registerShovelBrace();
        registerPickaxeShafts();
        registerShotgunBarrel();
        registerShotgunBody();
        registerShotgunStock();
    }

    private static void registerObjects(){
        Recipes.registerModRecipe(new Recipe(
                "GunsmithStation",
                1,
                RecipeTechRegistry.WORKSTATION,
                new Ingredient[]{
                        new Ingredient("ironbar", 5),
                        new Ingredient("anylog", 10)
                }
        ).showAfter("forge"));
        Recipes.registerModRecipe(new Recipe(
                "BonusPartStation",
                1,
                RecipeTechRegistry.TUNGSTEN_WORKSTATION,
                new Ingredient[]{
                        new Ingredient("ironbar", 5),
                        new Ingredient("anylog", 10)
                }
        ).showAfter("landscapingstation"));
        Recipes.registerModRecipe(new Recipe(
                "PartsBench",
                1,
                RecipeTechRegistry.WORKSTATION,
                new Ingredient[]{
                        new Ingredient("forge", 1),
                        new Ingredient("ironanvil", 1)
                }
        ).showAfter("forge"));
        Recipes.registerModRecipe(new Recipe(
                "PartsBenchTier2",
                1,
                RecipeTechRegistry.DEMONIC_WORKSTATION,
                new Ingredient[]{
                        new Ingredient("demonicbar", 10),
                        new Ingredient("demonicanvil", 1)
                }
        ).showBefore("forge"));
        Recipes.registerModRecipe(new Recipe(
                "PartsBenchTier3",
                1,
                RecipeTechRegistry.TUNGSTEN_WORKSTATION,
                new Ingredient[]{
                        new Ingredient("tungstenbar", 8),
                        new Ingredient("obsidian", 5),
                        new Ingredient("tungstenanvil", 1)
                }
        ).showBefore("landscapingstation"));
        Recipes.registerModRecipe(new Recipe(
                "PartsBenchTier4",
                1,
                RecipeTechRegistry.FALLEN_WORKSTATION,
                new Ingredient[]{
                        new Ingredient("upgradeshard", 10),
                        new Ingredient("ruby", 4),
                        new Ingredient("sapphire", 4),
                        new Ingredient("emerald", 4),
                        new Ingredient("fallenanvil", 1)
                }
        ).showBefore("cookingstation"));
    }

    private static void registerPickaxeHeads(){
        Recipes.registerModRecipe(new Recipe(
                "HeadPickaxeFrost",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH"),
                new Ingredient[]{
                        new Ingredient("frostshard", 8)
                }
        ));
        Recipes.registerModRecipe(new Recipe(
                "HeadPickaxeGold",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH"),
                new Ingredient[]{
                        new Ingredient("goldbar", 8)
                }
        ));
        Recipes.registerModRecipe(new Recipe(
                "HeadPickaxeIron",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH"),
                new Ingredient[]{
                        new Ingredient("ironbar", 8)
                }
        ));
        Recipes.registerModRecipe(new Recipe(
                "HeadPickaxeCopper",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 8)
                }
        ));
        Recipes.registerModRecipe(new Recipe(
                "HeadPickaxeWood",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH"),
                new Ingredient[]{
                        new Ingredient("anylog", 8)
                }
        ));
        Recipes.registerModRecipe(new Recipe(
                "HeadPickaxeQuartz",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("quartz", 10)
                }
        ).showBefore("HeadPickaxeFrost"));
        Recipes.registerModRecipe(new Recipe(
                "HeadPickaxeIvy",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("ivybar", 10)
                }
        ).showBefore("HeadPickaxeFrost"));
        Recipes.registerModRecipe(new Recipe(
                "HeadPickaxeRunic",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("runestone", 10)
                }
        ).showBefore("HeadPickaxeFrost"));
        Recipes.registerModRecipe(new Recipe(
                "HeadPickaxeDemonic",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("demonicbar", 10)
                }
        ).showBefore("HeadPickaxeFrost"));
        Recipes.registerModRecipe(new Recipe(
                "HeadPickaxeAncient",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("ancientfossilbar", 16)
                }
        ).showBefore("HeadPickaxeQuartz"));
        Recipes.registerModRecipe(new Recipe(
                "HeadPickaxeMycelium",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("myceliumbar", 16)
                }
        ).showBefore("HeadPickaxeQuartz"));
        Recipes.registerModRecipe(new Recipe(
                "HeadPickaxeDryad",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("amber", 16)
                }
        ).showBefore("HeadPickaxeQuartz"));
        Recipes.registerModRecipe(new Recipe(
                "HeadPickaxeGlacial",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("glacialbar", 16)
                }
        ).showBefore("HeadPickaxeQuartz"));
        Recipes.registerModRecipe(new Recipe(
                "HeadPickaxeTungsten",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("tungstenbar", 16)
                }
        ).showBefore("HeadPickaxeQuartz"));
        Recipes.registerModRecipe(new Recipe(
                "HeadPickaxeCrystal",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH4"),
                new Ingredient[]{
                        new Ingredient("pearlescentdiamond", 20)
                }
        ).showBefore("HeadPickaxeAncient"));
        Recipes.registerModRecipe(new Recipe(
                "HeadPickaxeMagma",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH4"),
                new Ingredient[]{
                        new Ingredient("primordialessence", 20)
                }
        ).showBefore("HeadPickaxeAncient"));
        Recipes.registerModRecipe(new Recipe(
                "HeadPickaxeIce",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH4"),
                new Ingredient[]{
                        new Ingredient("cryoessence", 20)
                }
        ).showBefore("HeadPickaxeAncient"));
    }

    private static void registerPickaxeBrace(){
        Recipes.registerModRecipe(new Recipe(
                "BodyPickaxeBasic",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH"),
                new Ingredient[]{
                        new Ingredient("ironbar", 4),
                        new Ingredient("copperbar", 4)
                }
        ));
        Recipes.registerModRecipe(new Recipe(
                "BodyPickaxeMini",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("voidshard", 5),
                        new Ingredient("goldbar", 5),
                        new Ingredient("BodyPickaxeBasic", 1)
                }
        ).showBefore("BodyPickaxeBasic"));
        Recipes.registerModRecipe(new Recipe(
                "BodyPickaxeBattle",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("battlepotion", 5),
                        new Ingredient("demonicbar", 8),
                        new Ingredient("BodyPickaxeBasic", 1)
                }
        ).showBefore("BodyPickaxeBasic"));
        Recipes.registerModRecipe(new Recipe(
                "BodyPickaxeAccelerating",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("miningpotion", 5),
                        new Ingredient("rapidpotion", 5),
                        new Ingredient("demonicbar", 10),
                        new Ingredient("BodyPickaxeBasic", 1)
                }
        ).showBefore("BodyPickaxeMini"));
        Recipes.registerModRecipe(new Recipe(
                "BodyPickaxeDowsing",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("spelunkerpotion", 6),
                        new Ingredient("ectoplasm", 10),
                        new Ingredient("BodyPickaxeMini", 1)
                }
        ).showBefore("BodyPickaxeAccelerating"));
        Recipes.registerModRecipe(new Recipe(
                "BodyPickaxeVein",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("dynamitestick", 5),
                        new Ingredient("ruby", 8),
                        new Ingredient("goldbar", 10),
                        new Ingredient("BodyPickaxeDowsing", 1)
                }
        ).showBefore("BodyPickaxeDowsing"));
    }

    private static void registerShovelHeads(){
        Recipes.registerModRecipe(new Recipe(
                "HeadShovelFrost",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH"),
                new Ingredient[]{
                        new Ingredient("frostshard", 8)
                }
        ));
        Recipes.registerModRecipe(new Recipe(
                "HeadShovelGold",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH"),
                new Ingredient[]{
                        new Ingredient("goldbar", 8)
                }
        ));
        Recipes.registerModRecipe(new Recipe(
                "HeadShovelIron",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH"),
                new Ingredient[]{
                        new Ingredient("ironbar", 8)
                }
        ));
        Recipes.registerModRecipe(new Recipe(
                "HeadShovelCopper",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 8)
                }
        ));
        Recipes.registerModRecipe(new Recipe(
                "HeadShovelWood",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH"),
                new Ingredient[]{
                        new Ingredient("anylog", 8)
                }
        ));
        Recipes.registerModRecipe(new Recipe(
                "HeadShovelQuartz",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("quartz", 10)
                }
        ).showBefore("HeadShovelFrost"));
        Recipes.registerModRecipe(new Recipe(
                "HeadShovelIvy",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("ivybar", 10)
                }
        ).showBefore("HeadShovelFrost"));
        Recipes.registerModRecipe(new Recipe(
                "HeadShovelRunic",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("runestone", 10)
                }
        ).showBefore("HeadShovelFrost"));
        Recipes.registerModRecipe(new Recipe(
                "HeadShovelDemonic",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("demonicbar", 10)
                }
        ).showBefore("HeadShovelFrost"));
        Recipes.registerModRecipe(new Recipe(
                "HeadShovelAncient",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("ancientfossilbar", 16)
                }
        ).showBefore("HeadShovelQuartz"));
        Recipes.registerModRecipe(new Recipe(
                "HeadShovelMycelium",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("myceliumbar", 16)
                }
        ).showBefore("HeadShovelQuartz"));
        Recipes.registerModRecipe(new Recipe(
                "HeadShovelDryad",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("amber", 16)
                }
        ).showBefore("HeadShovelQuartz"));
        Recipes.registerModRecipe(new Recipe(
                "HeadShovelGlacial",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("glacialbar", 16)
                }
        ).showBefore("HeadShovelQuartz"));
        Recipes.registerModRecipe(new Recipe(
                "HeadShovelTungsten",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("tungstenbar", 16)
                }
        ).showBefore("HeadShovelQuartz"));
        Recipes.registerModRecipe(new Recipe(
                "HeadShovelCrystal",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH4"),
                new Ingredient[]{
                        new Ingredient("pearlescentdiamond", 20)
                }
        ).showBefore("HeadShovelAncient"));
        Recipes.registerModRecipe(new Recipe(
                "HeadShovelMagma",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH4"),
                new Ingredient[]{
                        new Ingredient("primordialessence", 20)
                }
        ).showBefore("HeadShovelAncient"));
        Recipes.registerModRecipe(new Recipe(
                "HeadShovelIce",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH4"),
                new Ingredient[]{
                        new Ingredient("cryoessence", 20)
                }
        ).showBefore("HeadShovelAncient"));
    }

    private static void registerShovelBrace(){
        Recipes.registerModRecipe(new Recipe(
                "BodyShovelBasic",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH"),
                new Ingredient[]{
                        new Ingredient("ironbar", 4),
                        new Ingredient("copperbar", 4)
                }
        ));
        /*Recipes.registerModRecipe(new Recipe(
                "BodyShovelBucket",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("bucket", 5),
                        new Ingredient("ironbar", 10),
                        new Ingredient("BodyShovelBasic", 1)
                }
        ).showBefore("BodyShovelBasic"));*/
        Recipes.registerModRecipe(new Recipe(
                "BodyShovelGrass",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("grassseed", 10),
                        new Ingredient("swampgrassseed", 10),
                        new Ingredient("plainsgrassseed", 10),
                        new Ingredient("BodyShovelBasic", 1)
                }
        ).showBefore("BodyShovelBasic"));
        Recipes.registerModRecipe(new Recipe(
                "BodyShovelBlock",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("resistancepotion", 5),
                        new Ingredient("tungstenbar", 16),
                        new Ingredient("BodyShovelBasic", 1)
                }
        ).showBefore("BodyShovelGrass"));
    }

    private static void registerPickaxeShafts(){
        Recipes.registerModRecipe(new Recipe(
                "ShaftPickaxeWood",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH"),
                new Ingredient[]{
                        new Ingredient("anylog", 10)
                }
        ));
        Recipes.registerModRecipe(new Recipe(
                "ShaftPickaxeRunestone",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("runestone", 10),
                        new Ingredient("ShaftPickaxeWood", 1)
                }
        ).showBefore("ShaftPickaxeWood"));
        Recipes.registerModRecipe(new Recipe(
                "ShaftPickaxeQuartz",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("quartz", 10),
                        new Ingredient("ShaftPickaxeWood", 1)
                }
        ).showBefore("ShaftPickaxeWood"));
        Recipes.registerModRecipe(new Recipe(
                "ShaftPickaxeIron",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("ironbar", 10)
                }
        ).showBefore("ShaftPickaxeWood"));
        Recipes.registerModRecipe(new Recipe(
                "ShaftPickaxeRefined",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("amber", 10),
                        new Ingredient("ShaftPickaxeRunestone", 1)
                }
        ).showBefore("ShaftPickaxeRunestone"));
        Recipes.registerModRecipe(new Recipe(
                "ShaftPickaxeWeighted",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("tungstenbar", 10),
                        new Ingredient("ShaftPickaxeIron", 1)
                }
        ).showBefore("ShaftPickaxeRunestone"));
        Recipes.registerModRecipe(new Recipe(
                "ShaftPickaxeStrengthened",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("obsidian", 10),
                        new Ingredient("ShaftPickaxeIron", 1)
                }
        ).showBefore("ShaftPickaxeRunestone"));
        Recipes.registerModRecipe(new Recipe(
                "ShaftPickaxeCelestial",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH4"),
                new Ingredient[]{
                        new Ingredient("shadowessence", 5),
                        new Ingredient("cryoessence", 5),
                        new Ingredient("bioessence", 5),
                        new Ingredient("primordialessence", 5),
                        new Ingredient("upgradeshard", 10),
                        new Ingredient("ShaftPickaxeRefined", 1)
                }
        ).showBefore("ShaftPickaxeRefined"));
        Recipes.registerModRecipe(new Recipe(
                "ShaftPickaxeCrystal",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH4"),
                new Ingredient[]{
                        new Ingredient("omnicrystal", 8),
                        new Ingredient("ShaftPickaxeWeighted", 1)
                }
        ).showBefore("ShaftPickaxeRefined"));
        Recipes.registerModRecipe(new Recipe(
                "ShaftPickaxeSlime",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH4"),
                new Ingredient[]{
                        new Ingredient("slimeum", 10),
                        new Ingredient("ShaftPickaxeStrengthened", 1)
                }
        ).showBefore("ShaftPickaxeRefined"));
    }

    private static void registerShotgunBarrel(){
        Recipes.registerModRecipe(new Recipe(
                "BarrelShotgunIron",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("ironbar", 4)
                }
        ));
        Recipes.registerModRecipe(new Recipe(
                "BarrelShotgunVoid",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("voidshard", 5),
                        new Ingredient("BarrelShotgunIron", 1)
                }
        ).showBefore("BarrelShotgunIron"));
        Recipes.registerModRecipe(new Recipe(
                "BarrelShotgunGold",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("goldbar", 6),
                        new Ingredient("BarrelShotgunIron", 1)
                }
        ).showBefore("BarrelShotgunIron"));
        Recipes.registerModRecipe(new Recipe(
                "BarrelShotgunCopper",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("copperbar", 6),
                        new Ingredient("BarrelShotgunIron", 1)
                }
        ).showBefore("BarrelShotgunIron"));
        Recipes.registerModRecipe(new Recipe(
                "BarrelShotgunCrystalized",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("quartz", 10),
                        new Ingredient("emerald", 15),
                        new Ingredient("BarrelShotgunGold", 1)
                }
        ).showBefore("BarrelShotgunVoid"));
        Recipes.registerModRecipe(new Recipe(
                "BarrelShotgunPowered",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("tungstenbar", 5),
                        new Ingredient("wire", 20),
                        new Ingredient("obsidian", 6),
                        new Ingredient("BarrelShotgunCopper", 1)
                }
        ).showBefore("BarrelShotgunVoid"));
        Recipes.registerModRecipe(new Recipe(
                "BarrelShotgunNatural",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("ivybar", 4),
                        new Ingredient("swampsludge", 6),
                        new Ingredient("BarrelShotgunGold", 1)
                }
        ).showBefore("BarrelShotgunVoid"));
        Recipes.registerModRecipe(new Recipe(
                "BarrelShotgunShadow",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH4"),
                new Ingredient[]{
                        new Ingredient("shadowessence", 8),
                        new Ingredient("BarrelShotgunPowered", 1)
                }
        ).showBefore("BarrelShotgunCrystalized"));
    }

    private static void registerShotgunBody(){
        Recipes.registerModRecipe(new Recipe(
                "BodyShotgunBasic",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("ironbar", 6)
                }
        ));
        Recipes.registerModRecipe(new Recipe(
                "BodyShotgunCharged",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("wire", 30),
                        new Ingredient("runestone", 11),
                        new Ingredient("BodyShotgunBasic", 1)
                }
        ).showBefore("BodyShotgunBasic"));
        Recipes.registerModRecipe(new Recipe(
                "BodyShotgunStuffed",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("demonicbar", 10),
                        new Ingredient("BodyShotgunBasic", 3)
                }
        ).showBefore("BodyShotgunBasic"));
        Recipes.registerModRecipe(new Recipe(
                "BodyShotgunDoubleBarrel",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("amber", 8),
                        new Ingredient("dryadlog", 12),
                        new Ingredient("BodyShotgunStuffed", 1)
                }
        ).showBefore("BodyShotgunCharged"));
        Recipes.registerModRecipe(new Recipe(
                "BodyShotgunLiving",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("swampsludge", 15),
                        new Ingredient("myceliumbar", 9),
                        new Ingredient("BodyShotgunBasic", 1)
                }
        ).showBefore("BodyShotgunCharged"));
        Recipes.registerModRecipe(new Recipe(
                "BodyShotgunNaturesWrath",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH4"),
                new Ingredient[]{
                        new Ingredient("cryoessence", 5),
                        new Ingredient("bioessence", 5),
                        new Ingredient("primordialessence", 5),
                        new Ingredient("BodyShotgunBasic", 1)
                }
        ).showBefore("BodyShotgunDoubleBarrel"));
    }

    private static void registerShotgunStock(){
        Recipes.registerModRecipe(new Recipe(
                "StockShotgunIron",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("ironbar", 8),
                        new Ingredient("anylog", 10)
                }
        ));
        Recipes.registerModRecipe(new Recipe(
                "StockShotgunIvy",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("ivybar", 10),
                        new Ingredient("willowlog", 20),
                        new Ingredient("StockShotgunIron", 1)
                }
        ).showBefore("StockShotgunIron"));
        Recipes.registerModRecipe(new Recipe(
                "StockShotgunEnchanted",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("runestone", 10),
                        new Ingredient("voidshard", 20),
                        new Ingredient("StockShotgunIron", 1)
                }
        ).showBefore("StockShotgunIron"));
        Recipes.registerModRecipe(new Recipe(
                "StockShotgunChilled",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH2"),
                new Ingredient[]{
                        new Ingredient("frostshard", 10),
                        new Ingredient("demonicbar", 10),
                        new Ingredient("StockShotgunIron", 1)
                }
        ).showBefore("StockShotgunIron"));
        Recipes.registerModRecipe(new Recipe(
                "StockShotgunAncient",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("ancientfossilbar", 16),
                        new Ingredient("wormcarapace", 10),
                        new Ingredient("StockShotgunDark", 1)
                }
        ).showBefore("StockShotgunIvy"));
        Recipes.registerModRecipe(new Recipe(
                "StockShotgunAmber",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("amber", 16),
                        new Ingredient("dryadlog", 20),
                        new Ingredient("StockShotgunIvy", 1)
                }
        ).showBefore("StockShotgunIvy"));
        Recipes.registerModRecipe(new Recipe(
                "StockShotgunGlacial",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("glacialbar", 16),
                        new Ingredient("sapphire", 6),
                        new Ingredient("StockShotgunChilled", 1)
                }
        ).showBefore("StockShotgunIvy"));
        Recipes.registerModRecipe(new Recipe(
                "StockShotgunDark",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH3"),
                new Ingredient[]{
                        new Ingredient("tungstenbar", 16),
                        new Ingredient("obsidian", 12),
                        new Ingredient("StockShotgunIron", 1)
                }
        ).showBefore("StockShotgunIvy"));
        Recipes.registerModRecipe(new Recipe(
                "StockShotgunPrimal",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH4"),
                new Ingredient[]{
                        new Ingredient("primordialessence", 20),
                        new Ingredient("obsidian", 10),
                        new Ingredient("StockShotgunAncient", 1)
                }
        ).showBefore("StockShotgunAncient"));
    }

    private static void registerDebug(){
        //Pistol Barrel
        /*Recipes.registerModRecipe(new Recipe(
                "BarrelPistolIron",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH"),
                new Ingredient[]{
                        new Ingredient("ironbar", 3)
                }
        ));
        //Pistol Body
        Recipes.registerModRecipe(new Recipe(
                "BodyPistolBasic",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH"),
                new Ingredient[]{
                        new Ingredient("ironbar", 8),
                        new Ingredient("copperbar", 4)
                }
        ));
        //Pistol Stock
        Recipes.registerModRecipe(new Recipe(
                "StockPistolIron",
                1,
                RecipeTechRegistry.getTech("PARTSBENCH"),
                new Ingredient[]{
                        new Ingredient("ironbar", 4),
                        new Ingredient("copperbar", 5)
                }
        ));*/
    }
}
