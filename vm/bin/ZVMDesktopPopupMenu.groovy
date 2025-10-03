
import vm.ui.VirtualMachine;
import vm.ui.VMDesktop;
import vm.ui.Utils; 

import javax.swing.JSeparator;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;


VirtualMachine vm = VirtualMachine.getInstance();
VMDesktop desktop = vm.getDesktop();

JPopupMenu contextMenu = Utils.getPopUpMenu("DesktopPopUpMenu");

JMenuItem repaint = new JMenuItem("Repaint");
contextMenu.add(repaint);

repaint.addActionListener { event -> 
  desktop.repaint();
}


JMenuItem sysSubMenu = Utils.getCategoryMenu("System");
contextMenu.add(new JSeparator());
contextMenu.add(sysSubMenu);

JMenuItem gc = new JMenuItem("Garbage Collect");
sysSubMenu.add(gc);

gc.addActionListener { event -> 
    System.gc();
}

