package gunsmith.registries;

import gunsmith.objects.bonuspartsbench.BonusPartStation;
import gunsmith.objects.embroider.EtchingStation;
import gunsmith.objects.gunsmithStation.GunsmithStation;
import gunsmith.objects.partsBench.*;

import necesse.engine.registries.ObjectRegistry;
import necesse.engine.registries.RecipeTechRegistry;

public class BenchesRegistry {
    public static void register() {
        registerTech();
        registerObjects();
    }

    private static void registerTech(){
        RecipeTechRegistry.registerTech("PARTSBENCH", "PartsBench");
        RecipeTechRegistry.registerTech("PARTSBENCH2", "PartsBenchTier2");
        RecipeTechRegistry.registerTech("PARTSBENCH3", "PartsBenchTier3");
        RecipeTechRegistry.registerTech("PARTSBENCH4", "PartsBenchTier4");
        RecipeTechRegistry.registerTech("GUNSMITHSTATION", "GunsmithStation");
        RecipeTechRegistry.registerTech("BONUSPARTSTATION", "BonusPartStation");
    }
    private static void registerObjects(){
        ObjectRegistry.registerObject("PartsBench", new PartsBench(), 10f, true);
        ObjectRegistry.registerObject("PartsBenchTier2", new PartsBenchTier2(), 10f, true);
        ObjectRegistry.registerObject("PartsBenchTier3", new PartsBenchTier3(), 10f, true);
        ObjectRegistry.registerObject("PartsBenchTier4", new PartsBenchTier4(), 10f, true);
        ObjectRegistry.registerObject("GunsmithStation", new GunsmithStation(), 10f, true);
        ObjectRegistry.registerObject("BonusPartStation", new BonusPartStation(), 10f, true);
        ObjectRegistry.registerObject("EtchingStation", new EtchingStation(), 10f, false);

    }
}
