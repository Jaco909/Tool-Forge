package gunsmith.registries;

import gunsmith.items.ammo.bullets.ModularBullet;
import gunsmith.items.ammo.bullets.body.debugBody;
import gunsmith.items.ammo.bullets.end.debugEnd;
import gunsmith.items.ammo.bullets.head.debugHead;
import gunsmith.items.parts.barrels.pistol.BarrelPistolGilded;
import gunsmith.items.parts.barrels.pistol.BarrelPistolIron;
import gunsmith.items.parts.bodies.pistol.BodyPistolBasic;
import gunsmith.items.parts.bodies.pistol.BodyPistolMagic;
import gunsmith.items.parts.stocks.pistol.StockPistolIron;
import gunsmith.items.tools.ModularPistol;
import gunsmith.items.tools.ModularShovel;
import gunsmith.items.tools.special.jenWand.JenWand;
import necesse.engine.registries.ItemRegistry;
import necesse.inventory.item.ItemCategory;

public class ItemsRegistry {
    public static void register() {
        //registerItemUnused();
        registerParts();
    }

    private static void registerParts(){
        gunsmith.registries.parts.PickaxePartsRegistry.register();
        gunsmith.registries.parts.ShovelPartsRegistry.register();
        gunsmith.registries.parts.GlaivePartsRegistry.register();
        gunsmith.registries.parts.ShotgunPartsRegistry.register();
        gunsmith.registries.parts.MachinegunPartsRegistry.register();
    }

    private static void registerItemUnused(){
        //Unused
        ItemRegistry.registerItem("ModularPistol", new ModularPistol(), 50f, false);
        ItemRegistry.registerItem("ModularBullet", new ModularBullet(), 1f, true);

        //BULLETS
        ItemRegistry.registerItem("DebugBody", new debugBody(), 1f, true,false);
        ItemRegistry.registerItem("DebugEnd", new debugEnd(), 1f, true,false);
        ItemRegistry.registerItem("DebugHead", new debugHead(), 1f, true,false);

        //PISTOL
        ItemRegistry.registerItem("BarrelPistolIron", new BarrelPistolIron(), 10f, true,false);
        ItemRegistry.registerItem("BarrelPistolGilded", new BarrelPistolGilded(), 10f, true,false);

        //PISTOL
        ItemRegistry.registerItem("BodyPistolBasic", new BodyPistolBasic(), 10f, true,false);
        ItemRegistry.registerItem("BodyPistolMagic", new BodyPistolMagic(), 10f, true,false);

        //PISTOL
        ItemRegistry.registerItem("StockPistolIron", new StockPistolIron(), 10f, true,false);

        //SPECIAL
        ItemRegistry.registerItem("JenWand", new JenWand(),100f,true);
    }
}
