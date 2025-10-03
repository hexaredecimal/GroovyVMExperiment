
import vm.ui.VirtualMachine;
import vm.ui.VMDesktop
import vm.ui.Utils; 

import javax.swing.JSeparator;
import javax.swing.JMenu
import javax.swing.JMenuItem


VirtualMachine vm = VirtualMachine.getInstance();

JMenu systemMenu = Utils.getCategoryMenu("VMSystemMenu");

JMenuItem shutdown = new JMenuItem("Shutdown VM");
shutdown.addActionListener { event -> 
    System.exit(0);
}

systemMenu.add(new JSeparator());
systemMenu.add(shutdown);

