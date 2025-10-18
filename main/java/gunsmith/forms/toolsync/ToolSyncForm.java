package gunsmith.forms.toolsync;

import necesse.engine.input.InputEvent;
import necesse.engine.network.client.Client;
import necesse.engine.network.client.ClientClient;
import necesse.engine.window.GameWindow;
import necesse.engine.window.WindowManager;
import necesse.gfx.forms.Form;
import necesse.gfx.forms.components.lists.FormScoreboardList;

public class ToolSyncForm extends Form {
    private FormScoreboardList list;

    public ToolSyncForm(String name, Client client) {
        super(name, 10, 10);
        this.addComponent(this.list = new FormScoreboardList(0, 0, this.getWidth(), this.getHeight(), client));
        this.drawBase = false;
        this.onWindowResized(WindowManager.getWindow());
    }

    public void fixSize() {
        //GameWindow window = WindowManager.getWindow();
        int width = 64;
        int height = 64;
        this.setWidth(width);
        this.setHeight(height);
        this.list.setWidth(width);
        this.list.setHeight(height);
    }

    public void onWindowResized(GameWindow window) {
        super.onWindowResized(window);
        this.fixSize();
        this.setPosition(window.getHudWidth(), 0);
    }
}