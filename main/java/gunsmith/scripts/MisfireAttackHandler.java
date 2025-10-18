package gunsmith.scripts;

import gunsmith.items.tools.ModularGun;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.registries.ItemRegistry;
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

import java.awt.geom.Point2D;

public class MisfireAttackHandler extends MouseAngleAttackHandler {
    private final InventoryItem item;
    private final ModularGun toolItem;
    private long lastTime;
    private long timeBuffer;
    private final int attackSeed;
    private int shots;
    private final GameRandom random = new GameRandom();

    public MisfireAttackHandler(ItemAttackerMob attackerMob, ItemAttackSlot slot, InventoryItem item, ModularGun toolItem, int seed, int startTargetX, int startTargetY, float chance) {
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
                    if (GameRandom.globalRandom.getIntBetween(1,10) > 1) {
                        InventoryItem attackItem = this.item.copy();
                        this.toolItem.superOnAttack(this.attackerMob.getLevel(), attackX, attackY, this.attackerMob, this.attackerMob.getCurrentAttackHeight(), attackItem, this.slot, 0, seed, attackMap);
                        if (this.attackerMob.isClient()) {
                            playFireSound(this.attackerMob);
                        }
                    } else {
                        //((AmmoUserMob)this.attackerMob).removeAmmo(this.toolItem.getBulletItem(this.attackerMob.getLevel(),this.attackerMob,this.attackSeed,this.item),1,"bullet");
                        if (this.attackerMob.isClient()) {
                            SoundManager.playSound(GameResources.tick, SoundEffect.effect(this.attackerMob)
                                    .volume(1.62f)
                                    .pitch(1.8F));
                        }
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
        InventoryItem barrelItem = ItemRegistry.getItem(item.getGndData().getString("BarrelItem","BarrelMachinegunIron")).getDefaultItem(null,1);
        InventoryItem bodyItem = ItemRegistry.getItem(item.getGndData().getString("BodyItem","BodyMachinegunBasic")).getDefaultItem(null,1);
        InventoryItem stockItem = ItemRegistry.getItem(item.getGndData().getString("StockItem","StockMachinegunIron")).getDefaultItem(null,1);
        float multiplier = 1/(stockItem.getGndData().getFloat("AttackSpeed",1F)*barrelItem.getGndData().getFloat("AttackSpeed",1F)*bodyItem.getGndData().getFloat("AttackSpeed",1F));
        return Math.round(200*multiplier);
    }

    public void onEndAttack(boolean bySelf) {
        this.attackerMob.doAndSendStopAttackAttacker(false);
        this.shots = 0;
    }
}
