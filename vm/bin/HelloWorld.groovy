
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

JMenu item = vm.findMenuByText("Applications");
JMenu examples = Utils.getCategoryMenu("Examples");

JMenuItem app = new JMenuItem("HelloWorld");
app.addActionListener {event -> 
    desktop.add(HelloWorldApp.run());
}

examples.add(app);
