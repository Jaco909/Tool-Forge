//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gunsmith.scripts;

import java.awt.geom.Point2D;

import gunsmith.items.tools.ModularGun;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.network.packet.PacketFireDeathRipper;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.ai.behaviourTree.leaves.ChaserAINode;
import necesse.entity.mobs.attackHandler.MouseAngleAttackHandler;
import necesse.entity.mobs.itemAttacker.ItemAttackSlot;
import necesse.entity.mobs.itemAttacker.ItemAttackerMob;
import necesse.gfx.GameResources;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.Item;

public class RipperAttackHandler extends MouseAngleAttackHandler {
    private final InventoryItem item;
    private final ModularGun toolItem;
    private long lastTime;
    private long timeBuffer;
    private final int attackSeed;
    private int shots;
    private final GameRandom random = new GameRandom();

    public RipperAttackHandler(ItemAttackerMob attackerMob, ItemAttackSlot slot, InventoryItem item, ModularGun toolItem, int seed, int startTargetX, int startTargetY) {
        super(attackerMob, slot, 50, 1000.0F, startTargetX, startTargetY);
        this.item = item;
        this.toolItem = toolItem;
        this.attackSeed = seed;
        this.lastTime = attackerMob.getLocalTime();
    }

    public void onUpdate() {
        super.onUpdate();
        if (!this.attackerMob.isPlayer && this.lastItemAttackerTarget != null && !ChaserAINode.hasLineOfSightToTarget(this.attackerMob, this.lastItemAttackerTarget, 5.0F)) {
            this.attackerMob.endAttackHandler(true);
        } else {
            Point2D.Float dir = GameMath.getAngleDir(this.currentAngle);
            int attackX = this.attackerMob.getX() + (int)(dir.x * 100.0F);
            int attackY = this.attackerMob.getY() + (int)(dir.y * 100.0F);
            long currentTime = this.attackerMob.getLevel().getLocalTime();
            if (this.toolItem.canAttack(this.attackerMob.getLevel(), attackX, attackY, this.attackerMob, this.item) == null) {
                this.timeBuffer += currentTime - this.lastTime;
                int seed = Item.getRandomAttackSeed(this.random.seeded((long)GameRandom.prime(this.attackSeed * this.shots)));
                GNDItemMap attackMap = this.attackerMob.showAttackAndSendAttacker(this.item, attackX, attackY, 0, seed);

                while(true) {
                    int cooldown = this.getShootCooldown();
                    if (this.timeBuffer < (long)cooldown) {
                        break;
                    }

                    this.timeBuffer -= (long)cooldown;
                    seed = Item.getRandomAttackSeed(this.random.nextSeeded(GameRandom.prime(this.attackSeed * this.shots)));
                    ++this.shots;
                    this.toolItem.superOnAttack(this.attackerMob.getLevel(), attackX, attackY, this.attackerMob, this.attackerMob.getCurrentAttackHeight(), this.item, this.slot, 0, seed, attackMap);
                    if (this.attackerMob.isClient()) {
                        playFireSound(this.attackerMob);
                    } else if (this.attackerMob.isServer()) {
                        this.attackerMob.sendAttackerPacket(this.attackerMob, new PacketFireDeathRipper(this.attackerMob));
                    }
                }
            } else {
                this.onEndAttack(true);
            }

            this.lastTime = currentTime;
        }
    }

    public static void playFireSound(Mob target) {
        SoundManager.playSound(GameResources.handgun, SoundEffect.effect(target));
    }

    private int getShootCooldown() {
        float multiplier = 1.0F / this.toolItem.getAttackSpeedModifier(this.item, this.attackerMob);
        if (this.shots > 9) {
            return (int)(multiplier * 150.0F);
        } else if (this.shots > 6) {
            return (int)(multiplier * 200.0F);
        } else {
            return this.shots > 3 ? (int)(multiplier * 275.0F) : (int)(multiplier * 350.0F);
        }
    }

    public void onEndAttack(boolean bySelf) {
        this.attackerMob.doAndSendStopAttackAttacker(false);
        this.shots = 0;
    }
}
