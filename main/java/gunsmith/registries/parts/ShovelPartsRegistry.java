package gunsmith.registries.parts;

import gunsmith.items.parts.barrels.shovel.*;
import gunsmith.items.parts.bodies.shovel.*;
import gunsmith.items.tools.ModularShovel;
import necesse.engine.registries.ItemRegistry;

public class ShovelPartsRegistry {
    public static void register() {
        ItemRegistry.registerItem("ModularShovel", new ModularShovel(), 50f, true);
        registerHead();
        registerBrace();
    }

    private static void registerHead(){
        ItemRegistry.registerItem("HeadShovelWood", new HeadShovelWood(), 10f, true,false);
        ItemRegistry.registerItem("HeadShovelCopper", new HeadShovelCopper(), 10f, true,false);
        ItemRegistry.registerItem("HeadShovelIron", new HeadShovelIron(), 10f, true,false);
        ItemRegistry.registerItem("HeadShovelGold", new HeadShovelGold(), 10f, true,false);
        ItemRegistry.registerItem("HeadShovelFrost", new HeadShovelFrost(), 10f, true,false);
        ItemRegistry.registerItem("HeadShovelDemonic", new HeadShovelDemonic(), 10f, true,false);
        ItemRegistry.registerItem("HeadShovelIvy", new HeadShovelIvy(), 10f, true,false);
        ItemRegistry.registerItem("HeadShovelRunic", new HeadShovelRunic(), 10f, true,false);
        ItemRegistry.registerItem("HeadShovelQuartz", new HeadShovelQuartz(), 10f, true,false);
        ItemRegistry.registerItem("HeadShovelTungsten", new HeadShovelTungsten(), 10f, true,false);
        ItemRegistry.registerItem("HeadShovelGlacial", new HeadShovelGlacial(), 10f, true,false);
        ItemRegistry.registerItem("HeadShovelDryad", new HeadShovelDryad(), 10f, true,false);
        ItemRegistry.registerItem("HeadShovelMycelium", new HeadShovelMycelium(), 10f, true,false);
        ItemRegistry.registerItem("HeadShovelAncient", new HeadShovelAncient(), 10f, true,false);
        ItemRegistry.registerItem("HeadShovelIce", new HeadShovelIce(), 10f, true,false);
        ItemRegistry.registerItem("HeadShovelMagma", new HeadShovelMagma(), 10f, true,false);
        ItemRegistry.registerItem("HeadShovelCrystal", new HeadShovelCrystal(), 10f, true,false);
        //ItemRegistry.registerItem("HeadShovelPlanetary", new HeadShovelPlanetary(), 500f, true,true);
    }
    private static void registerBrace(){
        ItemRegistry.registerItem("BodyShovelBasic", new BodyShovelBasic(), 10f, true,false);
        //ItemRegistry.registerItem("BodyShovelBucket", new BodyShovelBucket(), 10f, true,false);
        ItemRegistry.registerItem("BodyShovelBlock", new BodyShovelBlock(), 10f, true,false);
        ItemRegistry.registerItem("BodyShovelGrass", new BodyShovelGrass(), 10f, true,false);
    }
}

