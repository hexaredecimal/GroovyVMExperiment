
import vm.ui.VirtualMachine;
import vm.ui.VMWindow;
import vm.ui.VMDesktop
import vm.ui.Utils; 

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.JMenuBar;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JInternalFrame;


VirtualMachine vm = VirtualMachine.getInstance();
VMDesktop desktop = vm.getDesktop();

JMenu projectsMenu = vm.findMenuByText("Projects");

JMenu newProjectMenu = new JMenu("New Project");
projectsMenu.add(newProjectMenu);

JMenuItem webProject = new JMenuItem("Web Project");
newProjectMenu.add(webProject);

JMenuItem groovyProject = new JMenuItem("Groovy Project");
newProjectMenu.add(groovyProject);

JMenuItem mdProject = new JMenuItem("MarkDown Project");
newProjectMenu.add(mdProject);


JMenuItem openProject = new JMenuItem("Open Project");
projectsMenu.add(openProject);

projectsMenu.add(new JSeparator());

JMenuItem browseOpenProjects = new JMenuItem("Browse Open Project");
projectsMenu.add(browseOpenProjects);


JMenuItem cloneGitRepo = new JMenuItem("Clone Git Repo");
projectsMenu.add(cloneGitRepo);

projectsMenu.add(new JSeparator());

JMenuItem saveProject = new JMenuItem("Save Project");
projectsMenu.add(saveProject);

JMenuItem saveAllProjects = new JMenuItem("Save All Projects");
projectsMenu.add(saveAllProjects);
projectsMenu.add(new JSeparator());


JMenuItem closeProject = new JMenuItem("Close Project");
projectsMenu.add(closeProject);

JMenuItem closeAllProjects = new JMenuItem("Close All Projects");
projectsMenu.add(closeAllProjects);

