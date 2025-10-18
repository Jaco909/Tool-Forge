package gunsmith.scripts;

import gunsmith.items.tools.ModularGun;
import necesse.engine.network.Packet;
import necesse.engine.network.gameNetworkData.GNDItemMap;
import necesse.engine.network.packet.PacketPlayerStopAttack;
import necesse.engine.network.server.ServerClient;
import necesse.engine.sound.SoundEffect;
import necesse.engine.sound.SoundManager;
import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.attackHandler.MouseAngleAttackHandler;
import necesse.entity.mobs.itemAttacker.ItemAttackSlot;
import necesse.entity.mobs.itemAttacker.ItemAttackerMob;
import necesse.gfx.GameResources;
import necesse.inventory.InventoryItem;
import necesse.inventory.item.Item;

import java.awt.geom.Point2D;

public class BurstFireAttackHandler extends MouseAngleAttackHandler {
    private final InventoryItem item;
    private final ModularGun toolItem;
    private final int attackSeed;
    private int shotsRemaining;
    private int shotsInitial;
    private int shots;
    private int bullets;
    private long timeBuffer;
    private final GameRandom random = new GameRandom();
    private int timeBetweenReloads;
    private int timeBetweenBurstShots;
    private int seed;

    public BurstFireAttackHandler(ItemAttackerMob attackerMob, ItemAttackSlot slot, InventoryItem item, ModularGun toolItem, int seed, int startTargetX, int startTargetY, int reloadTime, int shotTime, int shotsInitial) {
        super(attackerMob, slot, 20, 1000.0F, startTargetX, startTargetY);
        this.attackSeed = seed;
        this.timeBuffer = (long)this.timeBetweenReloads;
        this.item = item;
        this.seed = seed;
        this.toolItem = toolItem;
        this.shotsInitial = shotsInitial;
        this.shotsRemaining = shotsInitial;
        this.timeBetweenReloads = reloadTime - Math.round(toolItem.getUpgradeTier(item)*100);
        this.timeBetweenBurstShots = shotTime - Math.round(toolItem.getUpgradeTier(item)*8);
    }

    public void onUpdate() {
        super.onUpdate();
        Point2D.Float dir = GameMath.getAngleDir(this.currentAngle);
        int attackX = this.attackerMob.getX() + (int)(dir.x * 100.0F);
        int attackY = this.attackerMob.getY() + (int)(dir.y * 100.0F);
        bullets = this.toolItem.getAvailableAmmoBurst(this.attackerMob, bullets);

        if (this.toolItem.canAttack(this.attackerMob.getLevel(), attackX, attackY, this.attackerMob, this.item) == null) {
            int seed = Item.getRandomAttackSeed(this.random.seeded((long)GameRandom.prime(this.attackSeed * this.shots)));
            Packet attackContent = new Packet();
            //this.attackerMob.showAttack(this.item, attackX, attackY, seed, attackContent);
            this.attackerMob.showAttackAndSendAttacker(this.item, attackX, attackY, 0, seed);

            this.timeBuffer += (long)this.updateInterval;

            while(true) {
                float speedModifier = this.getSpeedModifier();
                if ((float)this.timeBuffer < (float)this.timeBetweenReloads * speedModifier) {
                    break;
                }

                seed = Item.getRandomAttackSeed(this.random.nextSeeded(GameRandom.prime(this.attackSeed * this.shots)));
                ++this.shots;
                --this.shotsRemaining;
                this.bullets--;
                this.item.getGndData().setInt("shotsRemaining",this.shotsRemaining);
                this.toolItem.getAvailableAmmoBurst(this.attackerMob,bullets);
                InventoryItem attackItem = this.item.copy();
                GNDItemMap attackMap = this.attackerMob.showAttackAndSendAttacker(attackItem, attackX, attackY, 0, this.seed);
                this.toolItem.superOnAttack(this.attackerMob.getLevel(), attackX, attackY, this.attackerMob, this.attackerMob.getCurrentAttackHeight(), this.item, this.slot, 0, seed, attackMap);
                if (this.attackerMob.isClient() && this.item.getGndData().getString("BodyItem","BodyMachinegunBurst").equalsIgnoreCase("BodyMachinegunBurst")) {
                    SoundManager.playSound(GameResources.handgun, SoundEffect.effect(this.attackerMob)
                            .volume(0.65f)
                            .pitch(GameRandom.globalRandom.getFloatBetween(1.0f, 1.1f)));
                } else if (this.attackerMob.isClient() && this.item.getGndData().getString("BodyItem","BodyMachinegunBurst").equalsIgnoreCase("BodyShotgunDoubleBarrel")) {
                    SoundManager.playSound(GameResources.shotgun, SoundEffect.effect(this.attackerMob)
                            .volume(0.7f)
                            .pitch(GameRandom.globalRandom.getFloatBetween(0.86f, 0.95f)));
                }

                if (this.shotsRemaining <= 0) {
                    this.shotsRemaining = this.shotsInitial;
                    this.timeBuffer = 0L;
                    break;
                }
                int get = 1;

                this.timeBuffer = (long)((int)((float)(this.timeBetweenReloads - this.timeBetweenBurstShots) * speedModifier));
            }
        }
    }

    private float getSpeedModifier() {
        return 1.0F / this.toolItem.getAttackSpeedModifier(this.item, this.attackerMob);
    }

    public void onEndAttack(boolean bySelf) {
        //reload quicker on partial bursts
        bullets = this.toolItem.getAvailableAmmoBurst(this.attackerMob, bullets);
        this.item.getGndData().clearItem("shotsRemaining");
        if (bullets*-1 > 3) {
            while (bullets*-1 > 3) {
                bullets = bullets + 3;
            }
        }
        if (bullets*-1 != 3) {
            this.attackerMob.startItemCooldown(this.toolItem, (int) ((float) 400 * (bullets * -1) * this.getSpeedModifier()));
            this.attackerMob.stopAttack(false);
            if (this.attackerMob.isServer() && this.attackerMob.isPlayer) {
                ServerClient client = this.attackerMob.getFirstPlayerOwner().getServerClient();
                this.attackerMob.getLevel().getServer().network.sendToAllClientsExcept(new PacketPlayerStopAttack(client.slot), client);
            }
        } else {
            //System.out.println(this.timeBuffer);
            this.attackerMob.startItemCooldown(this.toolItem, (int) ((float) (400-timeBuffer) * this.getSpeedModifier()));
            this.attackerMob.stopAttack(false);
            if (this.attackerMob.isServer()  && this.attackerMob.isPlayer) {
                ServerClient client = this.attackerMob.getFirstPlayerOwner().getServerClient();
                this.attackerMob.getLevel().getServer().network.sendToAllClientsExcept(new PacketPlayerStopAttack(client.slot), client);
            }
        }
    }
}