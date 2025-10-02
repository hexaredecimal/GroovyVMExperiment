
package vm;

import java.lang.System;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JMenu;
import javax.swing.Timer;

import vm.ui.VirtualMachine;
import vm.ui.VMWindow;
import vm.ui.VMDesktop


public class Main {
    public static void main(args) {
        VirtualMachine vm = VirtualMachine.getInstance();
        setUpMenuBar(vm);

        VMDesktop desktop = vm.getDesktop();
        desktop.setWallpaper("/home/hexaredecimal/Wallpapers/wp2853583.jpg")

        for (int i =0; i < 3; i++) {
            VMWindow window = new VMWindow("Hello: " + i);
            desktop.add(window);

            JMenu fileMenu = new JMenu("File: " + i);
            window.add(fileMenu);


            window.setSize(100, 100);
            window.setVisible(true);
        }

        vm.run();
    }

    private static void setUpMenuBar(VirtualMachine vm) {
        JMenu appsMenu = new JMenu("Applications");
        vm.addSysLeftMenu(appsMenu);
        
        setUpDefaultMenus(vm);
    }


    private static void setUpDefaultMenus(VirtualMachine vm) {
        JMenu clockMenu = new JMenu("Clock");
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        clockMenu.setText(time);
        vm.addSysRightMenu(clockMenu);

        Timer timer = new Timer(1000, e -> {
            String _time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            clockMenu.setText(_time);
            clockMenu.removeAll();
        });
        timer.start();

        JMenu dateMenu = new JMenu("Date");
        String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        dateMenu.setText(date);
        vm.addSysRightMenu(dateMenu);

        // Update the text of the date menu periodically
        Timer timer2 = new Timer(1000, e -> {
            String _date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            dateMenu.setText(_date);
            dateMenu.removeAll();
        });
        timer2.start();
    }
}

