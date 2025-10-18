package gunsmith.registries;


import gunsmith.projectiles.ReflectedProjectile;
import gunsmith.projectiles.special.JenWandPushProjectile;
import gunsmith.projectiles.special.JenWandPushProjectilePre;
import gunsmith.projectiles.special.LivingShottyLeafBuffed;
import gunsmith.projectiles.modifiers.CrystalizeModifier;
import gunsmith.projectiles.modifiers.TriDotModifier;
import necesse.engine.registries.ProjectileModifierRegistry;
import necesse.engine.registries.ProjectileRegistry;

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
        ProjectileModifierRegistry.registerModifier("TriDotModifier", TriDotModifier.class);
        ProjectileModifierRegistry.registerModifier("CrystalizeModifier", CrystalizeModifier.class);
    }
}
