
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

JMenu appsMenu = vm.findMenuByText("Apps");

JPopupMenu contextMenu = new JPopupMenu();
Utils.putPopUpMenu("DesktopPopUpMenu", contextMenu);

desktop.setRightMouseClickCallBack { event -> 
	if (event.isPopupTrigger()) {
		contextMenu.show(desktop, event.getX(), event.getY());
	}
}



