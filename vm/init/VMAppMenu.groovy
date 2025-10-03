
import vm.ui.VirtualMachine;
import vm.ui.VMWindow;
import vm.ui.VMDesktop
import vm.ui.Utils; 

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar


import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Timer;

public class VMAppMenu {
    public static void init() {
        VirtualMachine vm = VirtualMachine.getInstance();

        JMenuBar menuBar = vm.getAppMenuBar();

        menuBar.removeAll();

        JMenu appsMenu = new JMenu("Applications");
        vm.addSysLeftMenu(appsMenu);

        JMenu workSpacesMenu = new JMenu("WorkSpaces");
        vm.addSysLeftMenu(workSpacesMenu);

        JMenu addOnsMenu = new JMenu("AddOns");
        vm.addSysLeftMenu(addOnsMenu);



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


        vm.refreshMenubar();
    }
}

VMAppMenu.init();

