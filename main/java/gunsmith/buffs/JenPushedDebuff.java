package gunsmith.buffs;

import necesse.engine.util.GameRandom;
import necesse.entity.mobs.Attacker;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.Buff;
import necesse.entity.particle.Particle;

import java.awt.*;

public class JenPushedDebuff extends Buff {
    public JenPushedDebuff() {
        this.canCancel = false;
        this.isImportant = true;
        this.isVisible = false;
        //this.preSpeed = 0;
    }

    public void init(ActiveBuff buff, BuffEventSubscriber eventSubscriber) {
        buff.setModifier(BuffModifiers.ACCELERATION, 3.0F);
        buff.setMaxModifier(BuffModifiers.FRICTION, 0.01F, Integer.MAX_VALUE);
        //this.preSpeed = buff.owner.getCurrentSpeed();
    }
    public void onRemoved(ActiveBuff buff) {
        if (buff.owner != null) {
            buff.owner.setFriction(buff.getGndData().getFloat("friction", 1));
        }
    }
    public void clientTick(ActiveBuff buff) {
        super.clientTick(buff);
        /*System.out.println(buff.owner.getLevel().objectLayer.getObject(upper.x,upper.y).isSolid);
        System.out.println(buff.owner.getLevel().objectLayer.getObject(lower.x,lower.y).isSolid);
        System.out.println(buff.owner.getLevel().objectLayer.getObject(left.x,left.y).isSolid);
        System.out.println(buff.owner.getLevel().objectLayer.getObject(right.x,right.y).isSolid);*/
        //buff.getGndData().setInt("attacker",buff.getAttacker().getAttackerUniqueID());
        if (buff.owner.isVisible()) {
            Mob owner = buff.owner;
            owner.getLevel().entityManager.addParticle(owner.x + (float)(GameRandom.globalRandom.nextGaussian() * 6.0), owner.y + (float)(GameRandom.globalRandom.nextGaussian() * 8.0), Particle.GType.IMPORTANT_COSMETIC).movesConstant(owner.dx / 10.0F, owner.dy / 10.0F).color(new Color(23, 114, 193)).givesLight(200.0F, 0.5F).height(16.0F);
        }
        /*Point upper = buff.owner.getTilePoint();
        upper.move(buff.owner.getTilePoint().x,buff.owner.getTilePoint().y+1);
        Point lower = buff.owner.getTilePoint();
        lower.move(buff.owner.getTilePoint().x,buff.owner.getTilePoint().y-1);
        Point left = buff.owner.getTilePoint();
        left.move(buff.owner.getTilePoint().x-1,buff.owner.getTilePoint().y);
        Point right = buff.owner.getTilePoint();
        right.move(buff.owner.getTilePoint().x+1,buff.owner.getTilePoint().y);
        this.curSpeed = buff.owner.getCurrentSpeed();
        System.out.println(this.curSpeed);
        if (buff.owner.getLevel().objectLayer.getObject(upper.x,upper.y).isSolid || buff.owner.getLevel().objectLayer.getObject(lower.x,lower.y).isSolid || buff.owner.getLevel().objectLayer.getObject(left.x,left.y).isSolid || buff.owner.getLevel().objectLayer.getObject(right.x,right.y).isSolid) {
            hitWall(buff);
        } else if (!buff.owner.hasCurrentMovement() || this.curSpeed <= buff.owner.getSpeed()) {
            buff.remove();
        }*/
    }
    /*public void hitWall(ActiveBuff buff){
        System.out.println("wack");
        if (this.curSpeed > buff.owner.getSpeed()) {
            buff.owner.setHealth(buff.owner.getHealth()-Math.round(this.preSpeed),buff.getAttacker());
        }
        buff.remove();
    }*/

    /*public void serverTick(ActiveBuff buff) {
        super.serverTick(buff);
        Point upper = buff.owner.getTilePoint();
        upper.move(buff.owner.getTilePoint().x,buff.owner.getTilePoint().y+1);
        Point lower = buff.owner.getTilePoint();
        lower.move(buff.owner.getTilePoint().x,buff.owner.getTilePoint().y-1);
        Point left = buff.owner.getTilePoint();
        left.move(buff.owner.getTilePoint().x-1,buff.owner.getTilePoint().y);
        Point right = buff.owner.getTilePoint();
        right.move(buff.owner.getTilePoint().x+1,buff.owner.getTilePoint().y);
        this.curSpeed = buff.owner.getCurrentSpeed();
        System.out.println(this.curSpeed);
        if (buff.owner.getLevel().objectLayer.getObject(upper.x,upper.y).isSolid || buff.owner.getLevel().objectLayer.getObject(lower.x,lower.y).isSolid || buff.owner.getLevel().objectLayer.getObject(left.x,left.y).isSolid || buff.owner.getLevel().objectLayer.getObject(right.x,right.y).isSolid) {
            hitWall(buff);
        } else if (!buff.owner.hasCurrentMovement() || this.curSpeed <= buff.owner.getSpeed()) {
            buff.remove();
        }
    }*/
}
