

import vm.ui.VirtualMachine;
import vm.ui.VMWindow;
import vm.ui.VMDesktop
import vm.ui.Utils; 


import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar



class HelloWorldApp {
    public static VMWindow run() {
        VMWindow window = new VMWindow("Hello");
        window.setSize(300, 200);
        window.setVisible(true);
        return window;
    }
}




VirtualMachine vm = VirtualMachine.getInstance();
VMDesktop desktop = vm.getDesktop();

JMenuBar menuBar = vm.geVMMenuBar();
JMenu item = Utils.findMenuItemByText(menuBar, "Applications");

// Add myself to the app menu
JMenuItem app = new JMenuItem("HelloWorld");
app.addActionListener {event -> 
    desktop.add(HelloWorldApp.run());
}

item.add(app)

System.out.println(item);
