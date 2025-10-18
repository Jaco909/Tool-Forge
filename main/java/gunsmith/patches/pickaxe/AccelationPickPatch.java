package gunsmith.patches.pickaxe;

import necesse.engine.GameLog;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.registries.ItemRegistry;
import necesse.entity.mobs.Attacker;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.level.gameObject.GameObject;
import necesse.level.gameObject.RockOreObject;
import necesse.level.maps.Level;
import necesse.level.maps.LevelObject;
import net.bytebuddy.asm.Advice;

import java.util.ArrayList;
import java.util.Objects;

@ModMethodPatch(target = GameObject.class, name = "onDestroyed", arguments = {Level.class,int.class,int.class,int.class,Attacker.class,ServerClient.class,ArrayList.class})
public class AccelationPickPatch {
    //This gives the player a stack of the acceleration pickaxe buff on breaking an object, granted they have the required item
    @Advice.OnMethodExit()
    static void onExit(@Advice.This GameObject gameObject, @Advice.Argument(0) Level level, @Advice.Argument(1) int layerID, @Advice.Argument(2) int x, @Advice.Argument(3) int y, @Advice.Argument(4) Attacker attacker, @Advice.Argument(5) ServerClient client) {
        if (level.isServer()) {
            if (attacker != null) {
                if (attacker.getAttackOwner() instanceof PlayerMob) {
                    if (((PlayerMob) attacker.getAttackOwner()).getSelectedItem().getGndData().getString("BodyItem", "BodyPickaxeBasic").equalsIgnoreCase("BodyPickaxeAccelerating")) {
                        if (!attacker.getAttackOwner().buffManager.hasBuff(BuffRegistry.getBuff("AccelerationPickaxeBuff"))) {
                            ActiveBuff ab = new ActiveBuff("AccelerationPickaxeStacks", attacker.getAttackOwner(), 15F, attacker.getAttackOwner());
                            if (attacker.getAttackOwner().buffManager.hasBuff(BuffRegistry.getBuff("AccelerationPickaxeStacks"))) {
                                attacker.getAttackOwner().buffManager.getBuff(BuffRegistry.getBuff("AccelerationPickaxeStacks")).getGndData().setLong("lastTime", level.lastWorldTime);
                            } else {
                                ab.getGndData().setLong("lastTime", level.lastWorldTime);
                            }
                            attacker.getAttackOwner().buffManager.addBuff(ab, true);
                        }
                    }
                }
            }
        }
    }
}
