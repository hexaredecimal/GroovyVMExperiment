package vm.ui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.JMenu;


import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;

import vm.ui.VMWindow;
import vm.ui.VMDesktop


public class VMDesktop extends JDesktopPane {
	private BufferedImage image;
	private JPopupMenu popupMenu;
	private VirtualMachine vm; 
	private Closure leftClick = null;
	private Closure rightClick = null;

	public VMDesktop(VirtualMachine vm) {
		this.image = null;
		this.vm = vm;

		VMDesktop desktop = this;

		addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e) && desktop.leftClick != null) {
					desktop.leftClick(e);
					return;
                }

				if (SwingUtilities.isRightMouseButton(e) && desktop.rightClick != null) {
					desktop.rightClick(e);
					return;
                }
            }

			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e) && desktop.leftClick != null) {
					desktop.leftClick(e);
					return;
                }

				if (SwingUtilities.isRightMouseButton(e) && desktop.rightClick != null) {
					desktop.rightClick(e);
					return;
                }
			}

        });


		addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                if (e.getChild() instanceof VMWindow) {
                    VMWindow f = e.getChild() as VMWindow;
		    f.toFront();
		    f.setSelected(true);
                }
	     }
	  });

	}

	public void setLeftMouseClickCallBack(Closure closure) {
		leftClick = closure;
	}

	public void setRightMouseClickCallBack(Closure closure) {
		rightClick = closure;
	}


	public void setWallpaper(String path) {
		setWallpaper(path, 0.01f);
	}

	public void setWallpaper(String path, float dithering) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(path));
			this.image = makeRetroCopy(img, dithering);
			repaint();
		} catch (IOException _) {
			System.out.println("Image not found");
		}
	}


	private static BufferedImage makeRetroCopy(BufferedImage src, double blackChance) {
		BufferedImage copy = new BufferedImage(
						src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
		Random rand = new Random();

		for (int y = 0; y < src.getHeight(); y++) {
			for (int x = 0; x < src.getWidth(); x++) {
				int rgb = src.getRGB(x, y);
				if (rand.nextDouble() < blackChance) {
					rgb = Color.BLACK.getRGB();
				}
				copy.setRGB(x, y, rgb);
			}
		}
		return copy;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image == null) {
			return;
		}

		Graphics2D g2 = (Graphics2D) g.create();
		try {
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
							RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

			int paneW = getWidth();
			int paneH = getHeight();

			int drawW = image.getWidth() / 2;
			int drawH = image.getHeight() / 2;

			int x = (paneW - drawW) / 2;
			int y = (paneH - drawH) / 2;

			g2.drawImage(image, x, y, drawW, drawH, null);
		} finally {
			g2.dispose();
		}
	}
}

