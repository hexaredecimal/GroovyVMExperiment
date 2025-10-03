
import vm.ui.VirtualMachine;
import vm.ui.VMWindow;
import vm.ui.VMDesktop
import vm.ui.Utils; 

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar


public class VMAppCategories {
    public static void init() {
        VirtualMachine vm = VirtualMachine.getInstance();
        VMDesktop desktop = vm.getDesktop();

        JMenu item = vm.findMenuByText("Applications");
        JMenu examples = Utils.getCategoryMenu("Examples");
        item.add(examples);
    }
}

VMAppCategories.init();

