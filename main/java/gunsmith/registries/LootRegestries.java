package gunsmith.registries;

import necesse.engine.GameLog;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.registries.ItemRegistry;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.hostile.bosses.*;
import necesse.inventory.lootTable.LootItemInterface;
import necesse.inventory.lootTable.LootTablePresets;
import necesse.inventory.lootTable.lootItem.ChanceLootItem;
import necesse.inventory.lootTable.lootItem.LootItem;
import necesse.inventory.lootTable.presets.DesertBiomeOldVinylsLootTable;
import necesse.inventory.lootTable.presets.FarmerChestLootTable;
import necesse.inventory.lootTable.presets.RareIncursionWeaponsLootTable;

import java.util.ArrayList;

public class LootRegestries {
    public static void register() {
        registerIncursions();
        registerChests();
        registerMobs();
    }
    private static void registerIncursions(){
        RareIncursionWeaponsLootTable.rareIncursionWeapons.add(new LootItem("HeadPickaxePlanetary",1));
        //LootTablePresets.rareIncursionRewards.items.add(new LootItem("HeadPickaxePlanetary",1,new GNDItemMap().setBoolean("BonusActivate",true)));
    }
    private static void registerChests(){
        //tool shaft
        LootTablePresets.basicCaveRuinsChest.items.add(new ChanceLootItem(0.1F, "ShaftPickaxeWood",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.snowCaveRuinsChest.items.add(new ChanceLootItem(0.1F, "ShaftPickaxeIron",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.desertCaveRuinsChest.items.add(new ChanceLootItem(0.1F, "ShaftPickaxeQuartz",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.plainsCaveRuinsChest.items.add(new ChanceLootItem(0.1F, "ShaftPickaxeRunestone",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.deepPlainsCaveChest.items.add(new ChanceLootItem(0.1F, "ShaftPickaxeRefined",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.basicDeepCaveRuinsChest.items.add(new ChanceLootItem(0.1F, "ShaftPickaxeStrengthened",new GNDItemMap().setBoolean("BonusActivate",true)));

        //shotgun
        LootTablePresets.basicCaveChest.items.add(new ChanceLootItem(0.1F, "BarrelShotgunIron",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.snowCaveChest.items.add(new ChanceLootItem(0.1F, "BarrelShotgunCopper",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.swampCaveChest.items.add(new ChanceLootItem(0.1F, "BarrelShotgunGold",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.dungeonChest.items.add(new ChanceLootItem(0.1F, "BarrelShotgunVoid",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.deepSwampCaveChest.items.add(new ChanceLootItem(0.1F, "BarrelShotgunNatural",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.deepSnowCaveChest.items.add(new ChanceLootItem(0.1F, "BarrelShotgunPowered",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.deepDesertCaveChest.items.add(new ChanceLootItem(0.1F, "BarrelShotgunCrystalized",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.plainsCaveRuinsChest.items.add(new ChanceLootItem(0.1F, "BodyShotgunCharged",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.desertCaveRuinsChest.items.add(new ChanceLootItem(0.1F, "BodyShotgunStuffed",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.plainsDeepCaveRuinsChest.items.add(new ChanceLootItem(0.1F, "BodyShotgunDoubleBarrel",new GNDItemMap().setBoolean("BonusActivate",true)));

        //pickaxe
        LootTablePresets.basicCaveChest.items.add(new ChanceLootItem(0.1F, "BodyPickaxeBasic",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.dungeonChest.items.add(new ChanceLootItem(0.1F, "BodyPickaxeBattle",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.swampCaveChest.items.add(new ChanceLootItem(0.1F, "BodyPickaxeAccelerating",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.deepCaveChest.items.add(new ChanceLootItem(0.1F, "BodyPickaxeDowsing",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.deepDesertCaveChest.items.add(new ChanceLootItem(0.1F, "BodyPickaxeVein",new GNDItemMap().setBoolean("BonusActivate",true)));

        //shovel
        LootTablePresets.basicCaveChest.items.add(new ChanceLootItem(0.1F, "BodyShovelBasic",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.swampCaveChest.items.add(new ChanceLootItem(0.1F, "BodyShovelGrass",new GNDItemMap().setBoolean("BonusActivate",true)));
        LootTablePresets.deepCaveChest.items.add(new ChanceLootItem(0.1F, "BodyShovelBlock",new GNDItemMap().setBoolean("BonusActivate",true)));
    }
    private static void registerMobs(){
        //pickaxe
        QueenSpiderMob.lootTable.items.add(new ChanceLootItem(0.15F, "BodyPickaxeBattle",new GNDItemMap().setBoolean("BonusActivate",true)));
        VoidWizard.lootTable.items.add(new ChanceLootItem(0.15F, "BodyPickaxeAccelerating",new GNDItemMap().setBoolean("BonusActivate",true)));
        ReaperMob.lootTable.items.add(new ChanceLootItem(0.20F, "BodyPickaxeDowsing",new GNDItemMap().setBoolean("BonusActivate",true)));
        MotherSlimeMob.lootTable.items.add(new ChanceLootItem(0.25F, "ShaftPickaxeSlime",new GNDItemMap().setBoolean("BonusActivate",true)));
        CrystalDragonHead.lootTable.items.add(new ChanceLootItem(0.25F, "ShaftPickaxeCrystal",new GNDItemMap().setBoolean("BonusActivate",true)));

        //shovel
        SwampGuardianHead.lootTable.items.add(new ChanceLootItem(0.15F, "BodyShovelGrass",new GNDItemMap().setBoolean("BonusActivate",true)));
        CryoQueenMob.lootTable.items.add(new ChanceLootItem(0.20F, "BodyShovelBlock",new GNDItemMap().setBoolean("BonusActivate",true)));

        //shotgun
        SpiderEmpressMob.lootTable.items.add(new ChanceLootItem(0.1F, "BarrelShotgunShadow",new GNDItemMap().setBoolean("BonusActivate",true)));
        EvilsProtectorMob.lootTable.items.add(new ChanceLootItem(0.15F, "BodyShotgunBasic",new GNDItemMap().setBoolean("BonusActivate",true)));
        PestWardenHead.lootTable.items.add(new ChanceLootItem(0.15F, "BodyShotgunLiving",new GNDItemMap().setBoolean("BonusActivate",true)));
        CrystalDragonHead.lootTable.items.add(new ChanceLootItem(0.25F, "BodyShotgunNaturesWrath",new GNDItemMap().setBoolean("BonusActivate",true)));
    }
}
