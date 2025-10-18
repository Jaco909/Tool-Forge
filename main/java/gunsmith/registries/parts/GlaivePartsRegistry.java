package gunsmith.registries.parts;

import com.sun.tools.classfile.ClassFile;
import com.sun.tools.javac.code.Attribute;
import gunsmith.items.parts.barrels.glaive.*;
import gunsmith.items.parts.bodies.glaive.*;
import gunsmith.items.parts.stocks.glaive.*;
import gunsmith.items.tools.ModularGlaive;
import necesse.engine.registries.ItemRegistry;

public class GlaivePartsRegistry {
    public static void register() {
        ItemRegistry.registerItem("ModularGlaive", new ModularGlaive(), 50f, true);
        registerHead();
        registerBrace();
        registerShaft();
    }

    private static void registerHead(){
        ItemRegistry.registerItem("HeadGlaiveWood", new HeadGlaiveWood(), 10f, true,false);
        ItemRegistry.registerItem("HeadGlaiveGold", new HeadGlaiveGold(), 10f, true,false);
        ItemRegistry.registerItem("HeadGlaiveFrost", new HeadGlaiveFrost(), 10f, true,false);
        ItemRegistry.registerItem("HeadGlaiveRunestone", new HeadGlaiveRunestone(), 10f, true,false);
        ItemRegistry.registerItem("HeadGlaiveIvy", new HeadGlaiveIvy(), 10f, true,false);

    }
    private static void registerBrace(){
        ItemRegistry.registerItem("BodyGlaiveBasic", new BodyGlaiveBasic(), 10f, true,false);
        ItemRegistry.registerItem("BodyGlaiveFire", new BodyGlaiveFire(), 10f, true,false);
        ItemRegistry.registerItem("BodyGlaiveBlock", new BodyGlaiveBlock(), 10f, true,false);
        ItemRegistry.registerItem("BodyGlaiveDryad", new BodyGlaiveDryad(), 10f, true,false);
    }
    private static void registerShaft(){
        ItemRegistry.registerItem("ShaftGlaiveWood", new ShaftGlaiveWood(), 10f, true,false);
        ItemRegistry.registerItem("ShaftGlaiveIvy", new ShaftGlaiveIvy(), 10f, true,false);
        ItemRegistry.registerItem("ShaftGlaiveRunestone", new ShaftGlaiveRunestone(), 10f, true,false);
        ItemRegistry.registerItem("ShaftGlaiveTungsten", new ShaftGlaiveTungsten(), 10f, true,false);
        ItemRegistry.registerItem("ShaftGlaiveShadow", new ShaftGlaiveShadow(), 10f, true,false);
    }
}
