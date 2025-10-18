package gunsmith.registries.parts;

import gunsmith.items.parts.barrels.pickaxe.*;
import gunsmith.items.parts.barrels.shovel.HeadShovelQuartz;
import gunsmith.items.parts.barrels.shovel.HeadShovelRunic;
import gunsmith.items.parts.bodies.pickaxe.*;
import gunsmith.items.parts.stocks.pickaxe.*;
import gunsmith.items.tools.ModularPickaxe;
import necesse.engine.registries.ItemRegistry;

public class PickaxePartsRegistry {
    public static void register() {
        ItemRegistry.registerItem("ModularPickaxe", new ModularPickaxe(), 50f, true);
        registerHead();
        registerBrace();
        registerShaft();
    }

    private static void registerHead(){
        ItemRegistry.registerItem("HeadPickaxeWood", new HeadPickaxeWood(), 10f, true,false);
        ItemRegistry.registerItem("HeadPickaxeCopper", new HeadPickaxeCopper(), 10f, true,false);
        ItemRegistry.registerItem("HeadPickaxeIron", new HeadPickaxeIron(), 10f, true,false);
        ItemRegistry.registerItem("HeadPickaxeGold", new HeadPickaxeGold(), 10f, true,false);
        ItemRegistry.registerItem("HeadPickaxeFrost", new HeadPickaxeFrost(), 10f, true,false);
        ItemRegistry.registerItem("HeadPickaxeDemonic", new HeadPickaxeDemonic(), 10f, true,false);
        ItemRegistry.registerItem("HeadPickaxeIvy", new HeadPickaxeIvy(), 10f, true,false);
        ItemRegistry.registerItem("HeadPickaxeTungsten", new HeadPickaxeTungsten(), 10f, true,false);
        ItemRegistry.registerItem("HeadPickaxeGlacial", new HeadPickaxeGlacial(), 10f, true,false);
        ItemRegistry.registerItem("HeadPickaxeDryad", new HeadPickaxeDryad(), 10f, true,false);
        ItemRegistry.registerItem("HeadPickaxeMycelium", new HeadPickaxeMycelium(), 10f, true,false);
        ItemRegistry.registerItem("HeadPickaxeAncient", new HeadPickaxeAncient(), 10f, true,false);
        ItemRegistry.registerItem("HeadPickaxeIce", new HeadPickaxeIce(), 10f, true,false);
        ItemRegistry.registerItem("HeadPickaxeMagma", new HeadPickaxeMagma(), 10f, true,false);
        ItemRegistry.registerItem("HeadPickaxeCrystal", new HeadPickaxeCrystal(), 10f, true,false);
        ItemRegistry.registerItem("HeadPickaxePlanetary", new HeadPickaxePlanetary(), 500f, true,true);
        ItemRegistry.registerItem("HeadPickaxeRunic", new HeadPickaxeRunic(), 10f, true,false);
        ItemRegistry.registerItem("HeadPickaxeQuartz", new HeadPickaxeQuartz(), 10f, true,false);
    }
    private static void registerBrace(){
        ItemRegistry.registerItem("BodyPickaxeBasic", new BodyPickaxeBasic(), 10f, true,false);
        ItemRegistry.registerItem("BodyPickaxeBattle", new BodyPickaxeBattle(), 10f, true,false);
        ItemRegistry.registerItem("BodyPickaxeVein", new BodyPickaxeVein(), 10f, true,false);
        ItemRegistry.registerItem("BodyPickaxeDowsing", new BodyPickaxeDowsing(), 10f, true,false);
        ItemRegistry.registerItem("BodyPickaxeAccelerating", new BodyPickaxeAccelerating(), 10f, true,false);
        ItemRegistry.registerItem("BodyPickaxeMini", new BodyPickaxeMini(), 10f, true,false);

    }
    private static void registerShaft(){
        ItemRegistry.registerItem("ShaftPickaxeWood", new ShaftPickaxeWood(), 10f, true,false);
        ItemRegistry.registerItem("ShaftPickaxeIron", new ShaftPickaxeIron(), 10f, true,false);
        ItemRegistry.registerItem("ShaftPickaxeQuartz", new ShaftPickaxeQuartz(), 10f, true,false);
        ItemRegistry.registerItem("ShaftPickaxeRunestone", new ShaftPickaxeRunestone(), 10f, true,false);
        ItemRegistry.registerItem("ShaftPickaxeRefined", new ShaftPickaxeRefined(), 10f, true,false);
        ItemRegistry.registerItem("ShaftPickaxeWeighted", new ShaftPickaxeWeighted(), 10f, true,false);
        ItemRegistry.registerItem("ShaftPickaxeCrystal", new ShaftPickaxeCrystal(), 10f, true,false);
        ItemRegistry.registerItem("ShaftPickaxeCelestial", new ShaftPickaxeCelestial(), 10f, true,false);
        ItemRegistry.registerItem("ShaftPickaxeSlime", new ShaftPickaxeSlime(), 10f, true,false);
        ItemRegistry.registerItem("ShaftPickaxeStrengthened", new ShaftPickaxeStrengthened(), 10f, true,false);
    }
}

