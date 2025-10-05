import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import java.awt.Stroke;
import java.awt.GradientPaint;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import javax.swing.ScrollPaneConstants;

import vm.ui.VirtualMachine;


public class SplashScreen extends JFrame {
    private JTextArea logArea;
    private AnimationPanel animationPanel;
    private PrintStream out = System.out;

    private SplashScreen() {
        setLayout(new BorderLayout());
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        // Log area
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(Color.WHITE);
        logArea.setForeground(Color.BLACK);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Redirect System.out
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                appendText(String.valueOf((char) b));
            }
        }, true));

        // Animation panel
        animationPanel = new AnimationPanel();

        // Layout
        JPanel content = new JPanel(new GridLayout(1, 2));
        content.add(scrollPane);
        content.add(animationPanel);

        add(content, BorderLayout.CENTER);

        setSize(700, 300);
        setLocationRelativeTo(null); 
    }


    @Override
    public void dispose() {
	if (animationPanel != null && animationPanel.timer != null) {
            animationPanel.timer.stop();
            animationPanel.timer = null;
        }

        System.setOut(out);
        super.dispose(); 
    }

    private void appendText(String text) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(text);
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    private class AnimationPanel extends JPanel implements ActionListener {
        Timer timer;
        private double angle1 = 0;
        private double angle2 = 120;
        private double angle3 = 240;


        private BufferedImage gridImage; 

        public AnimationPanel() {
            setBackground(Color.WHITE);
            timer = new Timer(40, this); 
            timer.start();
        }

	@Override
	public void removeNotify() {
	    super.removeNotify();
	    if (timer != null) {
		timer.stop();
	    }
	}

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();

            int w = getWidth();
            int h = getHeight();

            if (gridImage == null || gridImage.getWidth() != w || gridImage.getHeight() != h) {
                gridImage = buildGridImage(w, h);
            }

            g2.drawImage(gridImage, 0, 0, null);

            int radius = Math.min(w, h) / 4; 
            int centerX = w / 2;
            int centerY = h / 2 - 30;

            Stroke oldStroke = g2.getStroke();
            g2.setStroke(new BasicStroke(2.5f)); 
            g2.setColor(Color.DARK_GRAY);

            drawOrbit(g2, centerX, centerY, radius, angle1, Color.RED);
            drawOrbit(g2, centerX, centerY, radius, angle2, Color.GREEN);
            drawOrbit(g2, centerX, centerY, radius, angle3, Color.BLUE);

            g2.setStroke(oldStroke);

            drawTitle(g2, centerX, centerY + radius + 50);

            g2.dispose();
        }

        private BufferedImage buildGridImage(int w, int h) {
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int gridSize = 30;
            GradientPaint fade = new GradientPaint(0, 0, new Color(200, 200, 200, 150),
                                                   w, 0, new Color(200, 200, 200, 0));
            g2.setPaint(fade);

            for (int x = 0; x < w; x += gridSize) {
                g2.drawLine(x, 0, x, h);
            }
            for (int y = 0; y < h; y += gridSize) {
                g2.drawLine(0, y, w, y);
            }

            g2.dispose();
            return img;
        }

        private void drawOrbit(Graphics2D g2, int cx, int cy, int radius, double angle, Color dotColor) {
            // Rotate for ellipse

	    int x = cx - radius;
	    int y = cy - radius/2;
	    int w = radius*2;
	    int h = radius;

            g2.rotate(Math.toRadians(angle), cx, cy);
            g2.drawOval(x, y, w, h);

            // Dot on rightmost point of ellipse
            int dotX = cx + radius;
            int dotY = cy;
            int dotSize = 10;

	    x = dotX - dotSize/2;
	    y = dotY - dotSize/2;
	    w = dotSize;
	    h = dotSize; 

            g2.setColor(dotColor);
            g2.fillOval(x, y, w, h);

            // Reset rotation + restore ring color
            g2.rotate(Math.toRadians(-angle), cx, cy);
            g2.setColor(Color.DARK_GRAY);
        }

        private void drawTitle(Graphics2D g2, int centerX, int baseY) {
            Font titleFont = new Font("SansSerif", Font.BOLD, 20);
            Font versionFont = new Font("SansSerif", Font.PLAIN, 16);

            String title = "LiveGroovyExperiment";
            String version = "v0.1";

            FontMetrics fmTitle = g2.getFontMetrics(titleFont);
            FontMetrics fmVersion = g2.getFontMetrics(versionFont);

            int titleX = centerX - fmTitle.stringWidth(title) / 2;
            int versionX = centerX - fmVersion.stringWidth(version) / 2;

            g2.setColor(Color.BLACK);
            g2.setFont(titleFont);
            g2.drawString(title, titleX, baseY);

            g2.setFont(versionFont);
            g2.drawString(version, versionX, baseY + 20);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            angle1 += 2;
            angle2 += 2.5;
            angle3 += 3;
            repaint();
        }
    }

}

VirtualMachine vm = VirtualMachine.getInstance();
SplashScreen splash = new SplashScreen();
SwingUtilities.invokeLater(() -> splash.setVisible(true));

vm.putFrame("Splash", splash);



