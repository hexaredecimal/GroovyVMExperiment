
package vm;

import java.lang.System;

import vm.ui.VirtualMachine;
import vm.ui.VMWindow;
import vm.ui.VMDesktop
import vm.ScriptLoader

import java.io.File;


public class Main {
    public static void main(args) {
        VirtualMachine vm = VirtualMachine.getInstance();
        vm.run();
        
        ScriptLoader.loadScripts("../vm/init");
    }
}

