package gunsmith.registries.parts;

import gunsmith.items.parts.barrels.shotgun.*;
import gunsmith.items.parts.bodies.shotgun.*;
import gunsmith.items.parts.stocks.shotgun.*;
import gunsmith.items.tools.ModularShotgun;
import necesse.engine.registries.ItemRegistry;

public class ShotgunPartsRegistry {
    public static void register() {
        ItemRegistry.registerItem("ModularShotgun", new ModularShotgun(), 30f, true);
        registerBarrel();
        registerBody();
        registerStock();
    }

    private static void registerBarrel(){
        ItemRegistry.registerItem("BarrelShotgunIron", new BarrelShotgunIron(), 10f, true, false);
        ItemRegistry.registerItem("BarrelShotgunCopper", new BarrelShotgunCopper(), 10f, true, false);
        ItemRegistry.registerItem("BarrelShotgunGold", new BarrelShotgunGold(), 10f, true, false);
        ItemRegistry.registerItem("BarrelShotgunShadow", new BarrelShotgunShadow(), 10f, true, false);
        ItemRegistry.registerItem("BarrelShotgunVoid", new BarrelShotgunVoid(), 10f, true, false);
        ItemRegistry.registerItem("BarrelShotgunNatural", new BarrelShotgunNatural(), 10f, true, false);
        ItemRegistry.registerItem("BarrelShotgunPowered", new BarrelShotgunPowered(), 10f, true, false);
        ItemRegistry.registerItem("BarrelShotgunCrystalized", new BarrelShotgunCrystalized(), 10f, true, false);
    }
    private static void registerBody(){
        ItemRegistry.registerItem("BodyShotgunBasic", new BodyShotgunBasic(), 10f, true,false);
        ItemRegistry.registerItem("BodyShotgunLiving", new BodyShotgunLiving(), 10f, true,false);
        ItemRegistry.registerItem("BodyShotgunCharged", new BodyShotgunCharged(), 10f, true,false);
        ItemRegistry.registerItem("BodyShotgunStuffed", new BodyShotgunStuffed(), 10f, true,false);
        ItemRegistry.registerItem("BodyShotgunDoubleBarrel", new BodyShotgunDoubleBarrel(), 10f, true,false);
        ItemRegistry.registerItem("BodyShotgunNaturesWrath", new BodyShotgunNaturesWrath(), 10f, true,false);
    }
    private static void registerStock(){
        ItemRegistry.registerItem("StockShotgunIron", new StockShotgunIron(), 10f, true,false);
        ItemRegistry.registerItem("StockShotgunChilled", new StockShotgunChilled(), 10f, true,false);
        ItemRegistry.registerItem("StockShotgunIvy", new StockShotgunIvy(), 10f, true,false);
        ItemRegistry.registerItem("StockShotgunEnchanted", new StockShotgunEnchanted(), 10f, true,false);
        ItemRegistry.registerItem("StockShotgunDark", new StockShotgunDark(), 10f, true,false);
        ItemRegistry.registerItem("StockShotgunGlacial", new StockShotgunGlacial(), 10f, true,false);
        ItemRegistry.registerItem("StockShotgunAmber", new StockShotgunAmber(), 10f, true,false);
        ItemRegistry.registerItem("StockShotgunAncient", new StockShotgunAncient(), 10f, true,false);
        ItemRegistry.registerItem("StockShotgunPrimal", new StockShotgunPrimal(), 10f, true,false);
    }
}

