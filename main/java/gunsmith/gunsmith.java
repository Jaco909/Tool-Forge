package gunsmith;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;


import gunsmith.events.DeflectingToolItemMobAbilityEvent;
import necesse.engine.GameLog;
import necesse.engine.GlobalData;
import necesse.engine.modLoader.ModSettings;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.network.PacketReader;
import necesse.engine.registries.*;
import necesse.engine.sound.gameSound.GameSound;
import necesse.gfx.gameTexture.GameTexture;
import necesse.inventory.item.Item;
import necesse.inventory.item.toolItem.projectileToolItem.gunProjectileToolItem.GunProjectileToolItem;
import gunsmith.forms.bonusparts.*;
import gunsmith.forms.gunsmith.*;
import gunsmith.objects.gunsmithStation.*;
import gunsmith.objects.bonuspartsbench.*;
import gunsmith.registries.*;
import gunsmith.events.JenWandBlastEvent;

import customsettingslib.components.settings.*;
import customsettingslib.settings.*;

import static necesse.engine.registries.ContainerRegistry.registerOEContainer;

@ModEntry
public class gunsmith {
    public static File modDirectory = new File(GlobalData.appDataPath() + "cfg/mods/ToolForge/");
    public static int GUNSMITH_CONTAINER;
    public static int BONUS_CONTAINER;
    public static GameTexture icon_barrel;
    public static GameTexture icon_body;
    public static GameTexture icon_stock;
    public static GameTexture icon_shaft;
    public static GameTexture icon_brace;
    public static GameTexture icon_pickaxe;
    public static GameTexture icon_shovel;
    public static GameSound JenWandBlast;
    public static GameSound PickaxeDowsing;
    public static GameSound PickaxeAccelerate;
    public static CustomModSettingsGetter settingsGetter;

    public gunsmith(){
    }
    public void preInit() {
        File attackTextures = new File(GlobalData.appDataPath() + "cfg/mods/ToolForge/attackTextures");
        File itemTextures = new File(GlobalData.appDataPath() + "cfg/mods/ToolForge/itemTextures");

        if (!modDirectory.exists()) {
            //generate directory if missing
            modDirectory.mkdir();
        }
        if (!attackTextures.exists()) {
            //generate directory if missing
            attackTextures.mkdir();
        }
        if (!itemTextures.exists()) {
            //generate directory if missing
            itemTextures.mkdir();
        }

        GunProjectileToolItem.NORMAL_AMMO_TYPES.add("ModularBullet");

        CategoriesRegistry.register();
    }

    public void init() {
        GUNSMITH_CONTAINER = registerOEContainer((client, uniqueSeed, oe, content) -> new GunsmithContainerForm<>(client, new GunsmithContainer(client.getClient(), uniqueSeed, (GunsmithStationObjectEntity)oe, new PacketReader(content))), (client, uniqueSeed, oe, content, serverObject) -> new GunsmithContainer(client, uniqueSeed, (GunsmithStationObjectEntity)oe, new PacketReader(content)));
        BONUS_CONTAINER = registerOEContainer((client, uniqueSeed, oe, content) -> new BonusPartContainerForm<>(client, new BonusPartContainer(client.getClient(), uniqueSeed, (BonusPartStationObjectEntity)oe, new PacketReader(content))), (client, uniqueSeed, oe, content, serverObject) -> new BonusPartContainer(client, uniqueSeed, (BonusPartStationObjectEntity)oe, new PacketReader(content)));
        BenchesRegistry.register();
        BuffsRegistry.register();
        ProjectilesRegistry.register();
        PacketsRegistry.register();

        ItemsRegistry.register();

        LevelEventRegistry.registerEvent("JenWandEvent", JenWandBlastEvent.class);
        LevelEventRegistry.registerEvent("DeflectingToolItemMobAbilityEvent", DeflectingToolItemMobAbilityEvent.class);

        LootRegestries.register();
    }
    public void initResources() {
        try {
            icon_barrel = new GameTexture(GameTexture.fromFileRaw("ui/inventoryslot_icon_barrel"));
            icon_body = new GameTexture(GameTexture.fromFileRaw("ui/inventoryslot_icon_body"));
            icon_stock = new GameTexture(GameTexture.fromFileRaw("ui/inventoryslot_icon_stock"));
            icon_brace = new GameTexture(GameTexture.fromFileRaw("ui/inventoryslot_icon_brace"));
            icon_pickaxe = new GameTexture(GameTexture.fromFileRaw("ui/inventoryslot_icon_pickaxehead"));
            icon_shovel = new GameTexture(GameTexture.fromFileRaw("ui/inventoryslot_icon_shovelhead"));
            icon_shaft = new GameTexture(GameTexture.fromFileRaw("ui/inventoryslot_icon_shaft"));
            JenWandBlast = GameSound.fromFile("JenWandBlast");
            PickaxeDowsing = GameSound.fromFile("DowsingSFX");
            PickaxeAccelerate = GameSound.fromFile("AccelerateSFX");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void postInit() {
        int count = 0;
        for (Item item : ItemRegistry.getItems()) {
            if (ItemRegistry.getItemMod(item.getID()) != null) {
                if (ItemRegistry.getItemMod(item.getID()).id.equalsIgnoreCase("jakapoa.toolforge")) {
                    count++;
                }
            }
        }
        GameLog.debug.println("Toolsmith items: " + count);

        RecipiesRegistry.register();
    }
    public ModSettings initSettings() {
        CustomModSettings customModSettings = new CustomModSettings()
                .addTextSeparator("craftingSection")
                .addBooleanSetting("allowBonusInCrafting", false)
                .addParagraph("allowBonusInCraftingtext")
                .addTextSeparator("controlSection")
                .addParagraph("controlSectiontext")
                .addParagraph("viewStatstext")
                .addSelectionSetting("viewStats", 0,
                        new SelectionSetting.Option("invquickmove"),
                        new SelectionSetting.Option("invtrash"),
                        new SelectionSetting.Option("invdrop"),
                        new SelectionSetting.Option("invlock")
                )
                .addParagraph("viewPartstext")
                .addSelectionSetting("viewParts", 1,
                        new SelectionSetting.Option("invquickmove"),
                        new SelectionSetting.Option("invtrash"),
                        new SelectionSetting.Option("invdrop"),
                        new SelectionSetting.Option("invlock")
                )
                .addTextSeparator("colorSection")
                .addColorSetting("bonusColor", new Color(7, 191, 232))
                .addColorSetting("bonusMaxColor", new Color(230, 85, 230));
        settingsGetter = customModSettings.getGetter();
        return customModSettings;
    }
}
