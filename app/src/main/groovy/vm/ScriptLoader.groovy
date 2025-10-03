package vm;

import groovy.lang.GroovyShell;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.SwingUtilities;
import java.util.Arrays;

public class ScriptLoader {
    private static GroovyShell shell = new GroovyShell();

    public static void execute(String code) {

        synchronized (shell) {
            shell.evaluate(code);
        }

    }

    public static void executeScript(String path) {
        String code = readFile(path);
        if (code == null) {
            return;
        }
        execute(code);
    }

    public static void loadScripts(String path) {
        File dir = new File(path);

        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] groovyFiles = dir.listFiles((d, name) -> name.endsWith(".groovy"));

        if (groovyFiles == null || groovyFiles.length == 0) {
            return;
        }

        Arrays.sort(groovyFiles, (f1, f2) -> f1.getName().compareToIgnoreCase(f2.getName()));
        for (File file : groovyFiles) {
            String scriptPath = file.getAbsolutePath();
            System.out.println("RUNNING: " + scriptPath);
            executeScript(scriptPath);
        }
    }


	public static String readFile(String path) {
		try {
			return Files.readString(Path.of(path));
		} catch (IOException ex) {
		}
		return null;
	}
}