package gunsmith.patches.pickaxe;

import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.server.ServerClient;
import necesse.engine.registries.ItemRegistry;
import necesse.entity.mobs.Attacker;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.PlayerMob;
import necesse.level.gameObject.GameObject;
import necesse.level.gameObject.RockOreObject;
import necesse.level.gameObject.SingleOreRockSmall;
import necesse.level.maps.Level;
import necesse.level.maps.LevelObject;
import net.bytebuddy.asm.Advice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

import static gunsmith.gunsmith.modDirectory;

@ModMethodPatch(target = GameObject.class, name = "onDestroyed", arguments = {Level.class,int.class,int.class,int.class,Attacker.class,ServerClient.class,ArrayList.class})
public class VeinminerPatch {
    @Advice.OnMethodExit()
    static void onExit(@Advice.This GameObject gameObject, @Advice.Argument(0) Level level, @Advice.Argument(1) int layerID, @Advice.Argument(2) int x, @Advice.Argument(3) int y, @Advice.Argument(4) Attacker attacker, @Advice.Argument(5) ServerClient client) {
        if (level.isServer()) {
            if (attacker != null && client != null) {
                if (attacker.getAttackOwner() instanceof PlayerMob) {
                    if (ItemRegistry.getItem(client.playerMob.getSelectedItem().getGndData().getString("BodyItem", "BodyPickaxeBasic")).getDefaultItem(client.playerMob, 1).getGndData().getString("BodyName", "Basic").equalsIgnoreCase("VeinMine")) {
                        if (gameObject instanceof RockOreObject) {
                            RockOreObject thisOre = (RockOreObject) gameObject;
                            LevelObject upper = level.getLevelObject(layerID, x, y + 1);
                            LevelObject lower = level.getLevelObject(layerID, x, y - 1);
                            LevelObject left = level.getLevelObject(layerID, x - 1, y);
                            LevelObject right = level.getLevelObject(layerID, x + 1, y);
                            if (upper.object instanceof RockOreObject) {
                                RockOreObject oreRockSmall = (RockOreObject) upper.object;
                                if (Objects.equals(oreRockSmall.droppedOre, thisOre.droppedOre)) {
                                    level.entityManager.doObjectDamage(layerID, upper.tileX, upper.tileY, 999, 99F, attacker, client);
                                }
                            }
                            if (lower.object instanceof RockOreObject) {
                                RockOreObject oreRockSmall = (RockOreObject) lower.object;
                                if (Objects.equals(oreRockSmall.droppedOre, thisOre.droppedOre)) {
                                    level.entityManager.doObjectDamage(layerID, lower.tileX, lower.tileY, 999, 99F, attacker, client);
                                }
                            }
                            if (left.object instanceof RockOreObject) {
                                RockOreObject oreRockSmall = (RockOreObject) left.object;
                                if (Objects.equals(oreRockSmall.droppedOre, thisOre.droppedOre)) {
                                    level.entityManager.doObjectDamage(layerID, left.tileX, left.tileY, 999, 99F, attacker, client);
                                }
                            }
                            if (right.object instanceof RockOreObject) {
                                RockOreObject oreRockSmall = (RockOreObject) right.object;
                                if (Objects.equals(oreRockSmall.droppedOre, thisOre.droppedOre)) {
                                    level.entityManager.doObjectDamage(layerID, right.tileX, right.tileY, 999, 99F, attacker, client);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
