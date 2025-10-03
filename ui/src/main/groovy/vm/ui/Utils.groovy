package vm.ui;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JComponent;
import java.awt.Component;
import javax.swing.SwingUtilities;

import java.util.HashMap;

public class Utils {
    private static HashMap<String, JMenu> categories = new HashMap<>();
    private static HashMap<String, JPopupMenu> popUps = new HashMap<>();

    public static JMenu getCategoryMenu(String category) {
        if (categories.containsKey(category)) {
            return categories.get(category);
        }

        JMenu menu = new JMenu(category);
        categories.put(category, menu);
        return menu;
    }

    public static void putCategoryMenu(String category, JMenu menu) {
        categories.put(category, menu);
    }

    public static void putPopUpMenu(String category, JPopupMenu menu) {
        categories.put(category, menu);
    }

    public static JPopupMenu getPopUpMenu(String category) {
        return categories.get(category);
    }


    public static void addRightClickMenu(JComponent parent, JPopupMenu menu) {
		parent.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				maybeShowPopup(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}

			private void maybeShowPopup(MouseEvent e) {
				if (e.isPopupTrigger()) {
					menu.show(parent, e.getX(), e.getY());
				}
			}
		});
	}

    public static void addClickEvent(JComponent parent, Closure closure) {
		parent.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e)) {
					return;
                }

                closure(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e)) {
					return;
                }
                closure(e);
			}
		});
	}


}