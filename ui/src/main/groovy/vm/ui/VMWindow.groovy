
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
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import vm.ui.Utils;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.JComponent

public class VMWindow extends JInternalFrame {
    private List<JMenu> menus; 

    public VMWindow(String title) {
        super(title, true, true, true, true);
        setSize(100, 100);
        menus = new ArrayList<JMenu>();
        VirtualMachine vm = VirtualMachine.getInstance();

        Random rand = new Random();
        Dimension dSize = vm.getSize();
        int x = rand.nextInt(Math.max(1, dSize.width / 2 - dSize.width / 4) as Integer);
        int y = rand.nextInt(Math.max(1, dSize.height / 2 - dSize.height / 4) as Integer);
        setLocation(x, y);

        toFront();

        JMenu appWindowCloseMenu = new JMenu(title);
        JMenuItem minimize = new JMenuItem("Minimize Window");
        JMenuItem maximize = new JMenuItem("Maximize Window");
        JMenuItem close = new JMenuItem("Close Window");

        appWindowCloseMenu.add(minimize);
        appWindowCloseMenu.add(maximize);
        appWindowCloseMenu.add(new JSeparator());
        appWindowCloseMenu.add(close);

        menus.add(appWindowCloseMenu);

        minimize.addActionListener { event -> 
            this.setIcon(true);
        }

        maximize.addActionListener { event -> 
            this.setMaximum(true);
        }

        close.addActionListener { event -> 
            this.setClosed(true);
        }

        VMDesktop desktop = vm.getDesktop();

        addInternalFrameListener(new InternalFrameListener() {
            @Override
            public void internalFrameActivated(InternalFrameEvent ev) {
                if (!menus.isEmpty())
                    vm.useMenus(menus);
            }

            @Override
            public void internalFrameDeactivated(InternalFrameEvent ev) {
                if (desktop.getSelectedFrame() == null) {
                    vm.useSystemMenus();
                }
            }

            @Override
            public void internalFrameOpened(InternalFrameEvent ev) {
                if (!menus.isEmpty())
                    vm.useMenus(menus);
            }

            public void internalFrameClosing(InternalFrameEvent ev) {}
            
            @Override
            public void internalFrameClosed(InternalFrameEvent ev) {
                if (desktop.getSelectedFrame() == null) {
                    vm.useSystemMenus();
                }
            }
            
            @Override
            public void internalFrameIconified(InternalFrameEvent ev) {
                vm.useSystemMenus();
            }

            @Override
            public void internalFrameDeiconified(InternalFrameEvent ev) {
                if (!menus.isEmpty())
                    vm.useMenus(menus);
            }
        });

        Utils.addClickEvent(this, {item -> 
            if (!menus.isEmpty())
               vm.useMenus(menus);
        })

        BasicInternalFrameUI ui = (BasicInternalFrameUI) this.getUI();
        JComponent titleBar = ui.getNorthPane();
        
        Utils.addClickEvent(titleBar, {item -> 
            if (!menus.isEmpty())
               vm.useMenus(menus);
        })

        try {
            setSelected(true);
        }   catch (Exception e) {
            e.printStackTrace();
        }
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

