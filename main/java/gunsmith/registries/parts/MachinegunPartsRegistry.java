package gunsmith.registries.parts;

import gunsmith.items.parts.barrels.smg.*;
import gunsmith.items.parts.bodies.smg.*;
import gunsmith.items.parts.stocks.smg.*;
import gunsmith.items.tools.ModularSMG;

import necesse.engine.registries.ItemRegistry;

public class MachinegunPartsRegistry {
    public static void register() {
        ItemRegistry.registerItem("ModularSMG", new ModularSMG(), 50f, true);
        registerBarrel();
        registerBody();
        registerStock();
    }

    private static void registerBarrel(){
        ItemRegistry.registerItem("BarrelMachinegunIron", new BarrelMachinegunIron(), 10f, true,false);
        ItemRegistry.registerItem("BarrelMachinegunCopper", new BarrelMachinegunCopper(), 10f, true,false);
        ItemRegistry.registerItem("BarrelMachinegunGold", new BarrelMachinegunGold(), 10f, true,false);
        ItemRegistry.registerItem("BarrelMachinegunIvy", new BarrelMachinegunIvy(), 10f, true,false);
        ItemRegistry.registerItem("BarrelMachinegunLight", new BarrelMachinegunLight(), 10f, true,false);
        ItemRegistry.registerItem("BarrelMachinegunHeavy", new BarrelMachinegunHeavy(), 10f, true,false);
        ItemRegistry.registerItem("BarrelMachinegunLoose", new BarrelMachinegunLoose(), 10f, true,false);
    }
    private static void registerBody(){
        ItemRegistry.registerItem("BodyMachinegunBasic", new BodyMachinegunBasic(), 10f, true,false);
        ItemRegistry.registerItem("BodyMachinegunRunner", new BodyMachinegunRunner(), 10f, true,false);
        ItemRegistry.registerItem("BodyMachinegunBurst", new BodyMachinegunBurst(), 10f, true,false);
        ItemRegistry.registerItem("BodyMachinegunRipper", new BodyMachinegunRipper(), 10f, true,false);
        ItemRegistry.registerItem("BodyMachinegunEconomic", new BodyMachinegunEconomic(), 10f, true,false);
        ItemRegistry.registerItem("BodyMachinegunCrystal", new BodyMachinegunCrystal(), 10f, true,false);
        ItemRegistry.registerItem("BodyMachinegunChaingun", new BodyMachinegunChaingun(), 10f, true,false);
    }
    private static void registerStock(){
        ItemRegistry.registerItem("StockMachinegunIron", new StockMachinegunIron(), 10f, true,false);
        ItemRegistry.registerItem("StockMachinegunVoid", new StockMachinegunVoid(), 10f, true,false);
        ItemRegistry.registerItem("StockMachinegunHardened", new StockMachinegunHardened(), 10f, true,false);
        ItemRegistry.registerItem("StockMachinegunTempered", new StockMachinegunTempered(), 10f, true,false);
        ItemRegistry.registerItem("StockMachinegunPristine", new StockMachinegunPristine(), 10f, true,false);
    }
}

