
import vm.ui.VirtualMachine;
import vm.ui.VMDesktop;


public class DesktopWallPaper {
    public static void init() {
        VirtualMachine vm = VirtualMachine.getInstance();

        VMDesktop desktop = vm.getDesktop();
        desktop.setWallpaper("/home/hexaredecimal/Wallpapers/wp2853583.jpg");
    }
}

DesktopWallPaper.init()

