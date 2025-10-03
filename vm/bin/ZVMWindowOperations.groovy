
import vm.ui.VirtualMachine;
import vm.ui.VMWindow;
import vm.ui.VMDesktop
import vm.ui.Utils; 

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JMenuBar;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JInternalFrame


VirtualMachine vm = VirtualMachine.getInstance();
VMDesktop desktop = vm.getDesktop();

JMenu windowMenu = vm.findMenuByText("Window");

JMenuItem closeSelected = new JMenuItem("Close Selected Window");
closeSelected.addActionListener {event -> 
    if (desktop.getSelectedFrame() != null) {
        desktop.getSelectedFrame().setClosed(true);
    }
}

JMenuItem closeAll = new JMenuItem("Close All Windows");
closeAll.addActionListener {event -> 
    for (JInternalFrame frame : desktop.getAllFrames()) {
        frame.dispose();
    }
}

JMenuItem minimizeAll = new JMenuItem("Minimize All Windows");
minimizeAll.addActionListener {event -> 
    for (JInternalFrame frame : desktop.getAllFrames()) {
        try {
            frame.setIcon(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


windowMenu.add(closeSelected);
windowMenu.add(closeAll);
windowMenu.add(new JSeparator());
windowMenu.add(minimizeAll);
