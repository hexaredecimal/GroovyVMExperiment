
import vm.ui.VirtualMachine;
import vm.ui.VMWindow;
import vm.ui.VMDesktop
import vm.ui.Utils; 

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JSeparator;
import javax.swing.JPopupMenu;


VirtualMachine vm = VirtualMachine.getInstance();
VMDesktop desktop = vm.getDesktop();

JMenu item = vm.findMenuByText("Applications");
JMenu examples = Utils.getCategoryMenu("Examples");
item.add(examples);

JPopupMenu contextMenu = new JPopupMenu();


JMenuItem refresh = new JMenuItem("Refresh");
contextMenu.add(refresh);


desktop.setRightMouseClickCallBack { event -> 
	if (event.isPopupTrigger()) {
		contextMenu.show(desktop, event.getX(), event.getY());
	}
}


