
package vm.ui;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;

import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import java.util.List;
import java.util.ArrayList;

import vm.ui.VirtualMachine;

public class VMWindow extends JInternalFrame {
    private List<JMenu> menus; 

    public VMWindow(String title) {
        super(title, true, true, true, true);
        menus = new ArrayList<JMenu>();
        VirtualMachine vm = VirtualMachine.getInstance();

        addFocusListener(new java.awt.event.FocusListener() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                System.out.println("Focus gained!");
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                System.out.println("Focus lost!");
            }
        });        


    /*
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!menus.isEmpty())
                    vm.useMenus(menus);
            }

            public void mouseExited(MouseEvent e) {
                vm.useSystemMenus();
            }
		}); */
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

