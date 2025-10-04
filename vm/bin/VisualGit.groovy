
import vm.ui.VirtualMachine;
import vm.ui.VMWindow;
import vm.ui.VMDesktop
import vm.ui.Utils; 

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Dimension;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class VisualGit {

	private final VMWindow frame = new VMWindow("Visual Git");
	private final JTextField repoUrlField = new JTextField(40);
	private final JTextField repoDirField = new JTextField(30);
	private final JTextField commitMsgField = new JTextField(40);
	private final JTextArea logArea = new JTextArea();

	private Git git;

	private final DefaultListModel<String> stagedModel = new DefaultListModel<>();
	private final DefaultListModel<String> unstagedModel = new DefaultListModel<>();
	private final DefaultListModel<String> untrackedModel = new DefaultListModel<>();

	private final JList<String> stagedList = new JList<>(stagedModel);
	private final JList<String> unstagedList = new JList<>(unstagedModel);
	private final JList<String> untrackedList = new JList<>(untrackedModel);

	public static VMWindow init() {
		return new VisualGit().createAndShowGui();
	}

	private VMWindow createAndShowGui() {
		frame.setSize(750, 400);

		buildMenuBar();
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel urlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton cloneButton = new JButton("Clone");
		urlPanel.add(new JLabel("Repo URL:"));
		urlPanel.add(repoUrlField);
		urlPanel.add(cloneButton);

		JPanel dirPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton chooseDirButton = new JButton("Choose...");
		JButton openButton = new JButton("Open Repo");
		JButton initButton = new JButton("Init Repo");
		dirPanel.add(new JLabel("Repo Dir:"));
		dirPanel.add(repoDirField);
		dirPanel.add(chooseDirButton);
		dirPanel.add(openButton);
		dirPanel.add(initButton);

		topPanel.add(urlPanel, BorderLayout.NORTH);
		topPanel.add(dirPanel, BorderLayout.SOUTH);

		stagedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		unstagedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		untrackedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		JScrollPane stagedScroll = new JScrollPane(stagedList);
		stagedScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Staged (index)", TitledBorder.LEFT, TitledBorder.TOP));
		JScrollPane unstagedScroll = new JScrollPane(unstagedList);
		unstagedScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Modified / Missing (unstaged)", TitledBorder.LEFT, TitledBorder.TOP));
		JScrollPane untrackedScroll = new JScrollPane(untrackedList);
		untrackedScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Untracked", TitledBorder.LEFT, TitledBorder.TOP));

		JSplitPane rightSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, unstagedScroll, untrackedScroll);
		rightSplit.setResizeWeight(0.5);
		JSplitPane centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, stagedScroll, rightSplit);
		centerSplit.setResizeWeight(0.33);

		JPanel stagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton stageButton = new JButton("Stage Selected");
		JButton unstageButton = new JButton("Unstage Selected");
		JButton stageAllButton = new JButton("Stage All");
		JButton unstageAllButton = new JButton("Unstage All");
		JButton refreshButton = new JButton("Refresh");
		JButton restoreButton = new JButton("Restore Selected"); 
		stagePanel.add(stageButton);
		stagePanel.add(unstageButton);
		stagePanel.add(stageAllButton);
		stagePanel.add(unstageAllButton);
		stagePanel.add(refreshButton);
		stagePanel.add(restoreButton);

		JButton commitButton = new JButton("Commit");
		JButton pushButton = new JButton("Push");

		JPanel commitPanel = new JPanel(new BorderLayout(5, 5));
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		buttonsPanel.add(commitButton);
		buttonsPanel.add(pushButton);

		commitPanel.add(new JLabel("Message:"), BorderLayout.WEST);
		commitPanel.add(commitMsgField, BorderLayout.CENTER);
		commitPanel.add(buttonsPanel, BorderLayout.EAST);

		JScrollPane logScroll = new JScrollPane(logArea);
		logScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Log"));
		logScroll.setPreferredSize(new Dimension(10, 100));
		logArea.setEditable(false);

		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(centerSplit, BorderLayout.CENTER);
		centerPanel.add(stagePanel, BorderLayout.NORTH);
		centerPanel.add(commitPanel, BorderLayout.SOUTH);

		frame.add(topPanel, BorderLayout.NORTH);

		JSplitPane windowSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, centerPanel, logScroll);
		frame.add(windowSplit, BorderLayout.CENTER);

		/*
		frame.add(centerPanel, BorderLayout.CENTER);
		frame.add(logScroll, BorderLayout.SOUTH);
		*/


		chooseDirButton.addActionListener(e -> chooseDirectory());
		cloneButton.addActionListener(e -> cloneRepo());
		openButton.addActionListener(e -> openRepo());
		initButton.addActionListener(e -> initRepo());
		refreshButton.addActionListener(e -> refreshStatus());
		stageButton.addActionListener(e -> stageSelected());
		stageAllButton.addActionListener(e -> stageAll());
		unstageButton.addActionListener(e -> unstageSelected());
		unstageAllButton.addActionListener(e -> unstageAll());
		restoreButton.addActionListener(e -> restoreSelected()); // Restore feature
		commitButton.addActionListener(e -> commit());
		pushButton.addActionListener(e -> push());

		frame.setVisible(true);
		return frame;
	}

	private buildMenuBar() {
		JMenu repo = new JMenu("Repository");
		JMenuItem mClone = new JMenuItem("Clone Repository");
		JMenuItem mOpen = new JMenuItem("Open Directory");
		JMenuItem mInit = new JMenuItem("Init Repository");
		
		mClone.addActionListener(e -> cloneRepo());
		mOpen.addActionListener(e -> chooseDirectory());
		mInit.addActionListener(e -> initRepo());
		
		repo.add(mInit);
		repo.addSeparator();
		repo.add(mClone);
		repo.add(mOpen);

		JMenu actions = new JMenu("Actions");
		JMenuItem mRefresh = new JMenuItem("Refresh");
		JMenuItem mCommit = new JMenuItem("Commit");
		JMenuItem mPush = new JMenuItem("Push");
		JMenuItem mPull = new JMenuItem("Pull");
		JMenuItem mLog = new JMenuItem("Log");

		mRefresh.addActionListener(e -> refreshStatus());
		mCommit.addActionListener(e -> commit());
		mPush.addActionListener(e -> push());
		mPull.addActionListener(e -> pull());
		mLog.addActionListener(e -> showLog());

		actions.add(mRefresh);
		actions.addSeparator();
		actions.add(mCommit);
		actions.add(mPush);
		actions.add(mPull);
		actions.addSeparator();
		actions.add(mLog);

		JMenu branchMenu = new JMenu("Branches");
		JMenuItem listBranches = new JMenuItem("List Branches");
		JMenuItem switchBranch = new JMenuItem("Switch Branch");
		JMenuItem createBranch = new JMenuItem("Create Branch");
		JMenuItem deleteBranch = new JMenuItem("Delete Branch");

		listBranches.addActionListener(e -> listBranches());
		switchBranch.addActionListener(e -> switchBranch());
		createBranch.addActionListener(e -> createBranch());
		deleteBranch.addActionListener(e -> deleteBranch());

		branchMenu.add(listBranches);
		branchMenu.addSeparator();
		branchMenu.add(switchBranch);
		branchMenu.add(createBranch);
		branchMenu.add(deleteBranch);

		JMenu help = new JMenu("Help");
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(e -> JOptionPane.showInternalMessageDialog(frame,
						"Visual Git — A minimal JGit client.",
						"About", JOptionPane.INFORMATION_MESSAGE));
		help.add(about);

		frame.add(repo);
		frame.add(actions);
		frame.add(branchMenu); 
		frame.add(help);
	}

	private void listBranches() {
		if (git == null) {
			log("No repo open");
			return;
		}
		new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() {
				try {
					List<Ref> refs = git.branchList().call();
					publish("Branches:");
					for (Ref ref : refs) {
						publish(" - " + ref.getName());
					}
				} catch (GitAPIException ex) {
					publish("List branches failed: " + ex.getMessage());
				}
				return null;
			}

			@Override
			protected void process(List<String> chunks) {
				chunks.forEach(this::publish);
			}

			private void publish(String s) {
				log(s);
			}
		}.execute();
	}

	private void showLog() {
	    if (git == null) { log("No repo open"); return; }
	    new SwingWorker<Void,String>() {
		@Override
		protected Void doInBackground() {
		    publish("Fetching log...");
		    try {
			git.log().call().forEach(commit -> 
			    publish(commit.getId().getName().substring(0, 6) + ": " + commit.getShortMessage())
			);
		    } catch (Exception ex) {
			publish("Log failed: " + ex.getMessage());
		    }
		    return null;
		}
		@Override
		protected void process(List<String> chunks) {
		    for (String s : chunks) log(s);
		}
	    }.execute();
	}


	private void switchBranch() {
		if (git == null) {
			log("No repo open");
			return;
		}
		String branch = JOptionPane.showInputDialog(frame, "Enter branch name to switch to:");
		if (branch == null || branch.isEmpty()) {
			return;
		}
		new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() {
				try {
					git.checkout().setName(branch).call();
					publish("Switched to branch " + branch);
					refreshStatus();
				} catch (GitAPIException ex) {
					publish("Switch branch failed: " + ex.getMessage());
				}
				return null;
			}

			@Override
			protected void process(List<String> chunks) {
				chunks.forEach(this::publish);
			}

			private void publish(String s) {
				log(s);
			}
		}.execute();
	}

	private void createBranch() {
		if (git == null) {
			log("No repo open");
			return;
		}
		String branch = JOptionPane.showInputDialog(frame, "Enter new branch name:");
		if (branch == null || branch.isEmpty()) {
			return;
		}
		new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() {
				try {
					git.branchCreate().setName(branch).call();
					publish("Created branch " + branch);
				} catch (GitAPIException ex) {
					publish("Create branch failed: " + ex.getMessage());
				}
				return null;
			}

			@Override
			protected void process(List<String> chunks) {
				chunks.forEach(this::publish);
			}

			private void publish(String s) {
				log(s);
			}
		}.execute();
	}

	private void deleteBranch() {
		if (git == null) {
			log("No repo open");
			return;
		}
		String branch = JOptionPane.showInputDialog(frame, "Enter branch name to delete:");
		if (branch == null || branch.isEmpty()) {
			return;
		}
		new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() {
				try {
					git.branchDelete().setBranchNames(branch).setForce(true).call();
					publish("Deleted branch " + branch);
				} catch (GitAPIException ex) {
					publish("Delete branch failed: " + ex.getMessage());
				}
				return null;
			}

			@Override
			protected void process(List<String> chunks) {
				chunks.forEach(this::publish);
			}

			private void publish(String s) {
				log(s);
			}
		}.execute();
	}

	private void restoreSelected() {
		if (git == null) {
			log("No repo open");
			return;
		}
		List<String> files = unstagedList.getSelectedValuesList();
		if (files.isEmpty()) {
			log("No files selected to restore");
			return;
		}
		new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() {
				publish("Restoring selected files...");
				try {
					for (String f : files) {
						git.checkout().addPath(f).call();
					}
					publish("Restored selected files.");
					refreshStatus();
				} catch (GitAPIException ex) {
					publish("Restore failed: " + ex.getMessage());
				}
				return null;
			}

			@Override
			protected void process(List<String> chunks) {
				chunks.forEach(this::publish);
			}

			private void publish(String s) {
				log(s);
			}
		}.execute();
	}

	private void chooseDirectory() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
			repoDirField.setText(chooser.getSelectedFile().getAbsolutePath());
		}
		openRepo();
	}

	private void cloneRepo() {
		final String url = repoUrlField.getText().trim();
		if (url.isEmpty()) {
			log("Repo URL empty");
			return;
		}
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		final File parent = chooser.getSelectedFile();
		new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() {
				publish("Cloning " + url + " ...");
				try {
					String name = inferNameFromUrl(url);
					File dest = new File(parent, name);
					if (dest.exists()) {
						publish("Destination exists: " + dest);
						return null;
					}
					Git g = Git.cloneRepository().setURI(url).setDirectory(dest).call();
					git = g;
					SwingUtilities.invokeLater(() -> repoDirField.setText(dest.getAbsolutePath()));
					publish("Clone finished: " + dest.getAbsolutePath());
					openRepo();
				} catch (GitAPIException ex) {
					publish("Clone failed: " + ex.getMessage());
				}
				return null;
			}

			@Override
			protected void process(List<String> chunks) {
				for (String s : chunks) {
					log(s);
				}
			}
		}.execute();
	}

	private void openRepo() {
		final String path = repoDirField.getText().trim();
		if (path.isEmpty()) {
			log("Repo directory empty");
			return;
		}
		new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() {
				publish("Opening " + path);
				try {
					Git g = Git.open(new File(path));
					git = g;
					publish("Opened repository: " + path);
					refreshStatus();
				} catch (IOException ioe) {
					publish("Open failed: " + ioe.getMessage());
				}
				return null;
			}

			@Override
			protected void process(List<String> chunks) {
				for (String s : chunks) {
					log(s);
				}
			}
		}.execute();
	}

	private void initRepo() {
		final String path = repoDirField.getText().trim();
		if (path.isEmpty()) {
			log("Repo directory empty");
			return;
		}
		new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() {
				publish("Initializing " + path);
				try {
					File f = new File(path);
					if (!f.exists()) {
						f.mkdirs();
					}
					Git g = Git.init().setDirectory(f).call();
					git = g;
					publish("Initialized: " + path);
					refreshStatus();
				} catch (GitAPIException ex) {
					publish("Init failed: " + ex.getMessage());
				}
				return null;
			}

			@Override
			protected void process(List<String> chunks) {
				for (String s : chunks) {
					log(s);
				}
			}
		}.execute();
	}

	private void refreshStatus() {
		if (git == null) {
			log("No repo open");
			return;
		}
		new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() {
				publish("Refreshing status...");
				try {
					Status st = git.status().call();
					SwingUtilities.invokeLater(() -> {
						stagedModel.clear();
						unstagedModel.clear();
						untrackedModel.clear();
						st.getAdded().forEach(stagedModel::addElement);
						st.getChanged().forEach(stagedModel::addElement);
						st.getRemoved().forEach(stagedModel::addElement);
						st.getModified().forEach(unstagedModel::addElement);
						st.getMissing().forEach(unstagedModel::addElement);
						st.getUntracked().forEach(untrackedModel::addElement);
					});
					publish("Status updated.");
				} catch (GitAPIException | NoWorkTreeException ex) {
					publish("Status failed: " + ex.getMessage());
				}
				return null;
			}

			@Override
			protected void process(List<String> chunks) {
				for (String s : chunks) {
					log(s);
				}
			}
		}.execute();
	}

	private void stageSelected() {
		if (git == null) {
			log("No repo open");
			return;
		}
		List<String> files = new ArrayList<>();
		files.addAll(unstagedList.getSelectedValuesList());
		files.addAll(untrackedList.getSelectedValuesList());
		if (files.isEmpty()) {
			log("No files selected to stage");
			return;
		}

		new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() {
				publish("Staging " + files.size() + " files...");
				try {
					AddCommand add = git.add();
					for (String f : files) {
						add.addFilepattern(f);
					}
					add.call();
					publish("Staged selected files.");
					refreshStatus();
				} catch (GitAPIException ex) {
					publish("Stage failed: " + ex.getMessage());
				}
				return null;
			}

			@Override
			protected void process(List<String> chunks) {
				for (String s : chunks) {
					log(s);
				}
			}
		}.execute();
	}

	private void stageAll() {
		if (git == null) {
			log("No repo open");
			return;
		}
		new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() {
				publish("Staging all (add .) ...");
				try {
					git.add().addFilepattern(".").call();
					publish("All staged.");
					refreshStatus();
				} catch (GitAPIException ex) {
					publish("Stage all failed: " + ex.getMessage());
				}
				return null;
			}

			@Override
			protected void process(List<String> chunks) {
				for (String s : chunks) {
					log(s);
				}
			}
		}.execute();
	}

	private void unstageSelected() {
		if (git == null) {
			log("No repo open");
			return;
		}
		List<String> files = stagedList.getSelectedValuesList();
		if (files.isEmpty()) {
			log("No staged files selected to unstage");
			return;
		}

		new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() {
				publish("Unstaging " + files.size() + " files...");
				try {
					ResetCommand rc = git.reset(); 
					for (String f : files) {
						rc.addPath(f);
					}
					rc.call(); 
					publish("Unstaged selected files.");
					refreshStatus();
				} catch (GitAPIException ex) {
					publish("Unstage failed: " + ex.getMessage());
				}
				return null;
			}

			@Override
			protected void process(List<String> chunks) {
				for (String s : chunks) {
					log(s);
				}
			}
		}.execute();
	}

	private void unstageAll() {
		if (git == null) {
			log("No repo open");
			return;
		}
		new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() {
				publish("Unstaging all (reset --mixed) ...");
				try {
					git.reset().setMode(ResetType.MIXED).call();
					publish("All unstaged.");
					refreshStatus();
				} catch (GitAPIException ex) {
					publish("Unstage all failed: " + ex.getMessage());
				}
				return null;
			}

			@Override
			protected void process(List<String> chunks) {
				for (String s : chunks) {
					log(s);
				}
			}
		}.execute();
	}

	private void commit() {
		if (git == null) {
			log("No repo open");
			return;
		}
		final String msg = commitMsgField.getText().trim();
		new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() {
				publish("Committing...");
				try {
					git.commit().setMessage(msg.isEmpty() ? "commit" : msg).setAll(true).call();
					publish("Commit done.");
					SwingUtilities.invokeLater(() -> commitMsgField.setText(""));
					refreshStatus();
				} catch (GitAPIException ex) {
					publish("Commit failed: " + ex.getMessage());
				}
				return null;
			}

			@Override
			protected void process(List<String> chunks) {
				for (String s : chunks) {
					log(s);
				}
			}
		}.execute();
	}

	private void pull() {
		if (git == null) {
			log("No repo open");
			return;
		}
		new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() {
				publish("Pulling...");
				try {
					git.pull().call();
					publish("Pull completed.");
					refreshStatus();
				} catch (GitAPIException ex) {
					publish("Pull failed: " + ex.getMessage());
				}
				return null;
			}

			@Override
			protected void process(List<String> chunks) {
				for (String s : chunks) {
					log(s);
				}
			}
		}.execute();
	}

	private void push() {
		if (git == null) {
			log("No repo open");
			return;
		}
		final javax.swing.JPanel panel = new javax.swing.JPanel(new GridLayout(2, 2));
		final javax.swing.JTextField user = new javax.swing.JTextField();
		final javax.swing.JPasswordField pass = new javax.swing.JPasswordField();
		panel.add(new javax.swing.JLabel("Username:"));
		panel.add(user);
		panel.add(new javax.swing.JLabel("Password/Token:"));
		panel.add(pass);
		if (JOptionPane.showConfirmDialog(frame, panel, "Push - credentials", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
			return;
		}

		new SwingWorker<Void, String>() {
			@Override
			protected Void doInBackground() {
				publish("Pushing...");
				try {
					UsernamePasswordCredentialsProvider cp = new UsernamePasswordCredentialsProvider(user.getText(), new String(pass.getPassword()));
					git.push().setCredentialsProvider(cp).call();
					publish("Push completed.");
				} catch (GitAPIException ex) {
					publish("Push failed: " + ex.getMessage());
				}
				return null;
			}

			@Override
			protected void process(List<String> chunks) {
				for (String s : chunks) {
					log(s);
				}
			}
		}.execute();
	}

	private void log(String s) {
		SwingUtilities.invokeLater(() -> {
			logArea.append(s + "\n");
			logArea.setCaretPosition(logArea.getDocument().getLength());
		});
	}

	private void publish(String msg) { log(msg); }

	private String inferNameFromUrl(String url) {
		String trimmed = url.endsWith(".git") ? url.substring(0, url.length() - 4) : url;
		int idx = Math.max(trimmed.lastIndexOf('/'), trimmed.lastIndexOf(':'));
		if (idx >= 0 && idx + 1 < trimmed.length()) {
			return trimmed.substring(idx + 1);
		}
		return "repo";
	}
}




VirtualMachine vm = VirtualMachine.getInstance();
VMDesktop desktop = vm.getDesktop();

JMenu apps = vm.findMenuByText("Apps");

JMenuItem app = new JMenuItem("Visual Git");
app.addActionListener {event -> 
    desktop.add(VisualGit.init());
}
apps.add(app);

JPopupMenu contextMenu = Utils.getPopUpMenu("DesktopPopUpMenu");

JMenuItem vgitContextMenuItem = new JMenuItem("Visual Git");
vgitContextMenuItem.addActionListener {event -> 
    desktop.add(VisualGit.init());
}

contextMenu.add(vgitContextMenuItem);

