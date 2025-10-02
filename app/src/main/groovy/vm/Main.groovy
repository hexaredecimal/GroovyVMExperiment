
package vm;

import java.lang.System;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.Timer;

import vm.ui.VirtualMachine;
import vm.ui.VMWindow;
import vm.ui.VMDesktop

import java.io.File;


public class Main {
    public static void main(args) {
        VirtualMachine vm = VirtualMachine.getInstance();
        setUpMenuBar(vm);
        loadScripts("../vm/startup");


        VMDesktop desktop = vm.getDesktop();
        desktop.setWallpaper("/home/hexaredecimal/Wallpapers/wp2853583.jpg")

        vm.run();
    }

    private static void loadScripts(String path) {
        File dir = new File(path);

        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] groovyFiles = dir.listFiles((d, name) -> name.endsWith(".groovy"));

        if (groovyFiles == null || groovyFiles.length == 0) {
            return;
        }

        for (File file : groovyFiles) {
            String scriptPath = file.getAbsolutePath();
            ScriptLoader.executeScript(scriptPath);
        }
    }

    private static void setUpMenuBar(VirtualMachine vm) {
        JMenu appsMenu = new JMenu("Applications");
        vm.addSysLeftMenu(appsMenu);

        JMenu workSpacesMenu = new JMenu("WorkSpaces");
        vm.addSysLeftMenu(workSpacesMenu);

        JMenu addOnsMenu = new JMenu("AddOns");
        vm.addSysLeftMenu(addOnsMenu);


        setUpDefaultMenus(vm);
    }


    private static void setUpDefaultMenus(VirtualMachine vm) {
        JMenu saveVMMenu = new JMenu("Save VM");
        vm.addSysRightMenu(saveVMMenu);
        
        JMenu reloadVMMenu = new JMenu("Reload");
        vm.addSysRightMenu(reloadVMMenu);


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

