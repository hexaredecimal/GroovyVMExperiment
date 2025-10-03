package vm.ui;

import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.border.LineBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.ImageIcon;

import java.util.concurrent.ConcurrentLinkedQueue;

import vm.theme.FlatMetalTheme;
import javax.swing.UIManager;
import javax.swing.JFrame;
import javax.swing.Box;
import vm.ui.Utils;

public class VirtualMachine extends JFrame {
    private static VirtualMachine vmInstance = null;
    private JPanel menuWrapper;
	private static JMenuBar menuBar =  new JMenuBar();
	private double menuBarPaddingPercent = 0.20;

	private ConcurrentLinkedQueue<JMenu> leftSideMenus = new ConcurrentLinkedQueue<>();
	private ConcurrentLinkedQueue<JMenu> rightSideMenus = new ConcurrentLinkedQueue<>();
    private VMDesktop desktop;

    private VirtualMachine() {
        super("Groovy VM");
        setupUI();
    }

    public static VirtualMachine getInstance() {
        if (vmInstance == null) {
            vmInstance = new VirtualMachine();
        }

        return vmInstance;
    }

    private void setupUI() {
		setLookAndFeel();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);

		ImageIcon frameIcon = new ImageIcon("../vm/share/assets/logo.png");
		setIconImage(frameIcon.getImage());


		desktop = new VMDesktop(this);
        desktop.setLeftMouseClickCallBack { event -> 
			this.useSystemMenus();
        }

		setContentPane(desktop);
		desktop.setBackground(Color.WHITE);

		menuBar.setBorder(new LineBorder(Color.DARK_GRAY, 1));

		menuWrapper = new JPanel(null) {
			@Override
			public Dimension getPreferredSize() {
				int menuHeight = menuBar.getPreferredSize().height;
				return new Dimension(super.getWidth(), menuHeight);
			}
		};
		menuWrapper.setOpaque(false);

		menuWrapper.setLayout(null);
		menuWrapper.add(menuBar);

		int frameWidth = getWidth();
		int menuHeight = menuBar.getPreferredSize().height;

		int startX = (int) (frameWidth * menuBarPaddingPercent);
		int endX = (int) (frameWidth * (1 - menuBarPaddingPercent));
		int menuWidth = endX - startX;

		menuWrapper.setBounds(0, 0, frameWidth, menuHeight);
		menuBar.setBounds(startX, 0, menuWidth, menuHeight);

		var frame = this;

		addComponentListener(new java.awt.event.ComponentAdapter() {
			@Override
			public void componentResized(java.awt.event.ComponentEvent e) {
				int _frameWidth = frame.getWidth();
				int _menuHeight = menuBar.getPreferredSize().height;

				int _startX = (int) (_frameWidth * menuBarPaddingPercent);
				int _endX = (int) (_frameWidth * (1 - menuBarPaddingPercent));
				int _menuWidth = _endX - _startX;

				menuWrapper.setBounds(0, 0, _frameWidth, _menuHeight);
				menuBar.setBounds(_startX, 0, _menuWidth, _menuHeight);
			}
		});

		add(menuWrapper, BorderLayout.NORTH);
    }



    public JMenu findMenuByText(String text) {
		for (JMenu menu: leftSideMenus) {
			if (text.equals(menu.getText())) {
				return menu;
			}
		}

		for (JMenu menu: rightSideMenus) {
			if (text.equals(menu.getText())) {
				return menu;
			}
		}
		
        return null;
    }

	public JMenuBar getAppMenuBar() {
		return menuBar;
	}

    public VMDesktop getDesktop() {
        return desktop;
    }

	public synchronized void addSysLeftMenu(JMenu menu) {
		addMenu(menu, leftSideMenus);
	}

	public synchronized void addSysRightMenu(JMenu menu) {
		addMenu(menu, rightSideMenus);
	}

    void refreshMenubar() {
		menuBar.removeAll();
		leftSideMenus.forEach { menu -> menuBar.add(menu) };
		menuBar.add(Box.createHorizontalGlue());
		rightSideMenus.forEach { menu -> menuBar.add(menu) };
		menuBar.revalidate();
		menuBar.repaint();


		dispatchEvent(new java.awt.event.ComponentEvent(
						this, java.awt.event.ComponentEvent.COMPONENT_RESIZED));
	}

    private void addMenu(JMenu menu, ConcurrentLinkedQueue<JMenu> sink) {
		sink.add(menu);
		menuBar.add(menu);
	}

    public void useMenus(List<JMenu> menus) {
		menuBar.removeAll();
		JMenu systemMenu = Utils.getCategoryMenu("VMSystemMenu");
		menuBar.add(systemMenu);
        menus.forEach { menu -> menuBar.add(menu); }
		menuBar.add(Box.createHorizontalGlue());
		rightSideMenus.forEach { menu -> menuBar.add(menu) };
		menuBar.revalidate();
		menuBar.repaint();
    }


    public void useSystemMenus() {
        refreshMenubar();
    }


    public void run() {
		refreshMenubar();
		setVisible(true);

		dispatchEvent(new java.awt.event.ComponentEvent(
						this, java.awt.event.ComponentEvent.COMPONENT_RESIZED));
	}

    private void setLookAndFeel() {
		try {
			MetalLookAndFeel.setCurrentTheme(new FlatMetalTheme());
			UIManager.setLookAndFeel(new MetalLookAndFeel());

			FontUIResource font = new FontUIResource("Dialog", Font.PLAIN, 12);
			for (Object key : UIManager.getDefaults().keySet()) {
				if (UIManager.get(key) instanceof FontUIResource) {
					UIManager.put(key, font);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
