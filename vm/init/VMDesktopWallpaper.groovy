
import vm.ui.VirtualMachine;
import vm.ui.VMDesktop;


public class VMDesktopWallPaper {
    public static void init() {
        VirtualMachine vm = VirtualMachine.getInstance();

        VMDesktop desktop = vm.getDesktop();
        desktop.setWallpaper("../vm/share/assets/logo_bgwhite.png", 0);
    }
}

VMDesktopWallPaper.init()

