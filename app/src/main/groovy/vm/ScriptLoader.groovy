package vm;

import org.codehaus.groovy.control.CompilerConfiguration;
import groovy.lang.GroovyClassLoader
import groovy.lang.GroovyShell;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.SwingUtilities;
import java.util.Arrays;
import java.io.File;


public class ScriptLoader {
    private static final CompilerConfiguration config = new CompilerConfiguration();
    private static final GroovyClassLoader classLoader = new GroovyClassLoader(ScriptLoader.class.getClassLoader(), config);
    private static final GroovyShell shell = new GroovyShell(classLoader, config);

    static {
        String classpath = System.getProperty("java.class.path");
        config.setClasspath(classpath);
    }

    public static void execute(String code) {

        synchronized (shell) {
            shell.evaluate(code);
        }

    }

    public static void executeScript(String path) {
    	classLoader.parseClass(new File(path));
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
            System.out.println("[Info]: Loading " + file.getParentFile().getName() + "/" + file.getName());
            executeScript(file.getAbsolutePath());
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
