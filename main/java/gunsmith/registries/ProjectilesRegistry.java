package gunsmith.registries;


import gunsmith.buffs.DoubleBarrelCooldownDebuff;
import gunsmith.projectiles.ReflectedProjectile;
import gunsmith.projectiles.special.JenWandPushProjectile;
import gunsmith.projectiles.special.JenWandPushProjectilePre;
import gunsmith.projectiles.special.LivingShottyLeafBuffed;
import gunsmith.projectiles.modifiers.CrystalizeModifier;
import gunsmith.projectiles.modifiers.TriDotModifier;
import necesse.engine.modLoader.LoadedMod;
import necesse.engine.modLoader.ModLoader;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.registries.ProjectileModifierRegistry;
import necesse.engine.registries.ProjectileRegistry;

import java.util.Iterator;

public class ProjectilesRegistry {
    public static void register() {
        registerProjectiles();
        registerModifiers();
    }

    private static void registerProjectiles(){
        ProjectileRegistry.registerProjectile("LivingShottyLeafBuffed", LivingShottyLeafBuffed.class,"livingshotty","livingshotty_shadow");
        ProjectileRegistry.registerProjectile("JenWandPushProjectile", JenWandPushProjectile.class,"","");
        ProjectileRegistry.registerProjectile("JenWandPushProjectilePre", JenWandPushProjectilePre.class,"","");
        ProjectileRegistry.registerProjectile("ReflectedProjectile", ReflectedProjectile.class,"quartzbolt","bouncingslimeball_shadow");
    }
    private static void registerModifiers(){
        boolean noRA = false;
        Iterator mods = ModLoader.getEnabledMods().iterator();
        while (mods.hasNext()) {
            LoadedMod mod = (LoadedMod) mods.next();
            if (mod.id.equalsIgnoreCase("jakapoa.rangedarsenal2")){
                noRA = true;
            }
        }
        if (!noRA) {
            ProjectileModifierRegistry.registerModifier("CrystalizeModifier", CrystalizeModifier.class);
        }

        ProjectileModifierRegistry.registerModifier("TriDotModifier", TriDotModifier.class);
    }
}
