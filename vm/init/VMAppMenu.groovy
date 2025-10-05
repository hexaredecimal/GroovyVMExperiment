
import vm.Main;
import vm.ui.VirtualMachine;
import vm.ui.VMWindow;
import vm.ui.VMDesktop
import vm.ui.Utils; 
import vm.ScriptLoader;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.ImageIcon;

import java.lang.management.ManagementFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class VMAppMenus {
    public static void init() {
        VirtualMachine vm = VirtualMachine.getInstance();

        JMenuBar menuBar = vm.getAppMenuBar();

        menuBar.removeAll();

        JMenu sysMenu = new JMenu();
        ImageIcon sysIcon = new ImageIcon("../vm/share/assets/icons/test-tube-16.png");
        sysMenu.setIcon(sysIcon);
        Utils.putCategoryMenu("VMSystemMenu", sysMenu);
        vm.addSysLeftMenu(sysMenu);

        JMenu projectsMenu = new JMenu("Projects");
        vm.addSysLeftMenu(projectsMenu);
        
        JMenu toolsMenu = new JMenu("Tools");
        vm.addSysLeftMenu(toolsMenu);

        JMenu appsMenu = new JMenu("Apps");
        vm.addSysLeftMenu(appsMenu);

        JMenu doMenu = new JMenu("Do");
        vm.addSysLeftMenu(doMenu);

        JMenu windowMenu = new JMenu("Window");
        vm.addSysLeftMenu(windowMenu);


        JMenu saveVMMenu = new JMenu("Save VM");
        vm.addSysRightMenu(saveVMMenu);

        JMenu reloadVMMenu = new JMenu("Reload");
        vm.addSysRightMenu(reloadVMMenu);

	Utils.addClickEvent(reloadVMMenu, {e -> 
	   vm.dispose();
	   try {
		   String javaHome = System.getProperty("java.home");
		    String javaBin = javaHome + "/bin/java";
		    String classpath = System.getProperty("java.class.path");
		    String className = Main.class.getName();

		    List<String> command = new ArrayList<>();
		    command.add(javaBin);
		    command.add("-cp");
		    command.add(classpath);
		    command.add(className);

		    ProcessBuilder builder = new ProcessBuilder(command);
		    builder.inheritIO(); 
		    Process process = builder.start();
		    System.exit(0);
	    } catch (Exception ex) {

	    }

	   // ScriptLoader.loadScripts("../vm/init");
	});


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


        vm.refreshMenubar();
    }
}

VMAppMenus.init();

