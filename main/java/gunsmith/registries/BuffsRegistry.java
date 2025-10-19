package gunsmith.registries;


import gunsmith.buffs.*;
import necesse.engine.modLoader.LoadedMod;
import necesse.engine.modLoader.ModLoader;
import necesse.engine.registries.BuffRegistry;

import java.util.Iterator;


public class BuffsRegistry {
    public static void register() {
        registerBuffs();
    }

    private static void registerBuffs(){
        boolean noRA = false;
        Iterator mods = ModLoader.getEnabledMods().iterator();
        while (mods.hasNext()) {
            LoadedMod mod = (LoadedMod) mods.next();
            if (mod.id.equalsIgnoreCase("jakapoa.rangedarsenal2")){
                noRA = true;
            }
        }
        if (!noRA) {
            BuffRegistry.registerBuff("DoubleBarrelCooldownDebuff", new DoubleBarrelCooldownDebuff());
        }

        BuffRegistry.registerBuff("JenPushedDebuff", new JenPushedDebuff());
        BuffRegistry.registerBuff("DowsingPickaxeCooldown", new DowsingPickaxeCooldown());
        BuffRegistry.registerBuff("AccelerationPickaxeBuff", new AccelerationPickaxeBuff());
        BuffRegistry.registerBuff("AccelerationPickaxeStacks", new AccelerationPickaxeStacks());
        BuffRegistry.registerBuff("MagmaPickDebuff", new MagmaPickDebuff());
        BuffRegistry.registerBuff("GlaiveBlockingStacks", new GlaiveBlockingStacks());
        BuffRegistry.registerBuff("GlaiveDryadStacks", new GlaiveDryadStacks());
    }
}
