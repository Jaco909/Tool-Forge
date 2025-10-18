package gunsmith.registries;


import gunsmith.buffs.*;
import necesse.engine.registries.BuffRegistry;


public class BuffsRegistry {
    public static void register() {
        registerBuffs();
    }

    private static void registerBuffs(){
        BuffRegistry.registerBuff("DoubleBarrelCooldownDebuff", new DoubleBarrelCooldownDebuff());
        BuffRegistry.registerBuff("JenPushedDebuff", new JenPushedDebuff());
        BuffRegistry.registerBuff("DowsingPickaxeCooldown", new DowsingPickaxeCooldown());
        BuffRegistry.registerBuff("AccelerationPickaxeBuff", new AccelerationPickaxeBuff());
        BuffRegistry.registerBuff("AccelerationPickaxeStacks", new AccelerationPickaxeStacks());
        BuffRegistry.registerBuff("MagmaPickDebuff", new MagmaPickDebuff());
        BuffRegistry.registerBuff("GlaiveBlockingStacks", new GlaiveBlockingStacks());
        BuffRegistry.registerBuff("GlaiveDryadStacks", new GlaiveDryadStacks());
    }
}
