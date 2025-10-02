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

public class Utils {
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


    public static Component findMenuItemByText(Component root, String text) {
        if (root instanceof JMenu) {
            JMenu menu = (JMenu) root;
            if (text.equals(menu.getText())) {
                return menu;
            }
            for (int i = 0; i < menu.getMenuComponentCount(); i++) {
                Component found = findMenuItemByText(menu.getMenuComponent(i), text);
                if (found != null) {
                    return found;
                }
            }
        } else if (root instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) root;
            if (text.equals(menuItem.getText())) {
                return menuItem;
            }
        } else if (root instanceof JComponent) {
            for (Component child : ((JComponent) root).getComponents()) {
                Component found = findMenuItemByText(child, text);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}