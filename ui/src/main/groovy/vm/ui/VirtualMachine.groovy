package vm.ui;

import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;

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


import vm.theme.FlatMetalTheme;
import javax.swing.UIManager;
import javax.swing.JFrame;
import javax.swing.Box;

public class VirtualMachine extends JFrame {
    private static VirtualMachine vmInstance = null;
    private JPanel menuWrapper;
	private static JMenuBar menuBar =  new JMenuBar();
	private double menuBarPaddingPercent = 0.30;

	private List<JMenu> leftSideMenus;
	private List<JMenu> rightSideMenus;

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

		leftSideMenus = new ArrayList<>();
		rightSideMenus = new ArrayList<>();

		// Custom desktop pane with retro background
		desktop = new VMDesktop(this);

		setContentPane(desktop);
		desktop.setBackground(Color.WHITE); // or whatever you like

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

		// add wrapper at top
		add(menuWrapper, BorderLayout.NORTH);
    }

    public VMDesktop getDesktop() {
        return desktop;
    }

	public void addSysLeftMenu(JMenu menu) {
		addMenu(menu, leftSideMenus);
	}

	public void addSysRightMenu(JMenu menu) {
		addMenu(menu, rightSideMenus);
	}

    private void refreshMenubar() {
        
		menuBar.removeAll();
		leftSideMenus.forEach { menu -> menuBar.add(menu) };
		menuBar.add(Box.createHorizontalGlue());
		rightSideMenus.forEach { menu -> menuBar.add(menu) };
		menuBar.revalidate();
		menuBar.repaint();
	}

    private void addMenu(JMenu menu, List<JMenu> sink) {
		sink.add(menu);
		menuBar.add(menu);
	}

    public void useMenus(List<JMenu> menus) {
		menuBar.removeAll();
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

			// Force global font size 12
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
