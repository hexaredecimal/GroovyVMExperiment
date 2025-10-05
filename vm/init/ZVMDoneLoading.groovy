
import vm.ui.VirtualMachine;

import java.awt.event.WindowEvent;


import java.lang.Thread;
import javax.swing.JFrame;

VirtualMachine vm = VirtualMachine.getInstance();
JFrame splash = vm.getFrame("Splash")
System.out.println("[INFO]: Done loading.");
try {
    System.out.println("""
[INFO]:           A                           
[INFO]:        BK JB                          
[INFO]:       C TJ  RB                        
[INFO]:        BV     F                       
[INFO]:          B NN  WB                     
[INFO]:            IY    WLCBBB               
[INFO]:             BS   P  STRHVUQIEB        
[INFO]:              BZ    VYOKVVVVVVVD       
[INFO]:              BN  WEMWWURVVVTTTB       
[INFO]:               FHSOVVVVVVVVVVSC        
[INFO]:               BKUVVUVVVUTVVEA         
[INFO]:                HVVVQUVVVVJB           
[INFO]:                BVWVVVVULA             
[INFO]:                 CRVVTDB   
[INFO]: Starting...
    """);

    Thread.sleep(4000); 
    // splash.dispose();
    splash.dispatchEvent(new WindowEvent(splash, WindowEvent.WINDOW_CLOSING));
    vm.run();
} catch (InterruptedException e) {
    e.printStackTrace();
}

