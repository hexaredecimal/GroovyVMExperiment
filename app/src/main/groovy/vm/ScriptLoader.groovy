import groovy.lang.GroovyShell;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScriptLoader {
    public static void execute(String code) {
        GroovyShell shell = new GroovyShell();
        shell.evaluate(code);
    }

    public static void executeScript(String path) {
        String code = readFile(path);
        if (code == null) {
            return;
        }



        execute(code);
    }

	public static String readFile(String path) {
		try {
			return Files.readString(Path.of(path));
		} catch (IOException ex) {
		}
		return null;
	}
}