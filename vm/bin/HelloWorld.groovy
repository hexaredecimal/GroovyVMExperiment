
import vm.ui.VirtualMachine;
import vm.ui.VMWindow;
import vm.ui.VMDesktop
import vm.ui.Utils; 

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JLabel;
import javax.swing.JPanel;


class HelloWorldApp {
    public static VMWindow run() {
        VMWindow window = new VMWindow("Hello World");
        window.setSize(130, 60);

        JPanel mainPanel = new JPanel();
        window.add(mainPanel);
        
        JLabel helloLabel = new JLabel("Hello, World");
        mainPanel.add(helloLabel);
        
        window.setResizable(false);
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
