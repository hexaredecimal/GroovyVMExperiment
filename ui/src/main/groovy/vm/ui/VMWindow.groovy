
package vm.ui;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

import vm.ui.VirtualMachine;
import java.util.Random;
import vm.ui.VMDesktop;

public class VMWindow extends JInternalFrame {
    private List<JMenu> menus; 

    public VMWindow(String title) {
        super(title, true, true, true, true);
        setSize(100, 100);
        menus = new ArrayList<JMenu>();
        VirtualMachine vm = VirtualMachine.getInstance();

        Random rand = new Random();
        Dimension dSize = vm.getSize();
        int x = rand.nextInt(Math.max(1, dSize.width) as Integer);
        int y = rand.nextInt(Math.max(1, dSize.height / 2 + dSize.height / 4) as Integer);
        setLocation(x, y);
    }

    public List<JMenu> getMenus() {
        return menus;
    }

    @Override
    public Component add(Component other) {
        if (other instanceof JMenu) {
            JMenu menu = other as JMenu;
            menus.add(menu);
            return other;
        }

        super.add(other);
    }
}

