
import vm.ui.VirtualMachine;
import vm.ui.VMWindow;
import vm.ui.VMDesktop
import vm.ui.Utils; 

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Image;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Component;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.awt.BorderLayout;


public class AboutApp {
    public static VMWindow run() {
        VMWindow window = new VMWindow("About");
        window.setSize(450, 400);
        window.setVisible(true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        int gridY = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("LiveGroovyExperiment");
        Font currentFont = titleLabel.getFont();
        titleLabel.setFont(new Font(currentFont.getName(), currentFont.getStyle(), 25));
        gbc.gridy = gridY++;
        panel.add(titleLabel, gbc);
        
        JLabel versionLabel = new JLabel("    v0.1");
        currentFont = versionLabel.getFont();
        versionLabel.setFont(new Font(currentFont.getName(), currentFont.getStyle(), 20));
        gbc.gridy = gridY++;
        panel.add(versionLabel, gbc);

        ImageIcon originalIcon = new ImageIcon("../vm/share/assets/science_bgwhite.png");
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(128, 128, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);


        double oldGbcWeightX = gbc.weightx;
        JLabel imageLabel = new JLabel(scaledIcon);
        currentFont = imageLabel.getFont();
        imageLabel.setFont(new Font(currentFont.getName(), currentFont.getStyle(), 16));
        gbc.gridy = gridY++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(imageLabel, gbc);

        JLabel bottomLabel = new JLabel("""<html>
<p>
  What if We used the JVM as a <b>*LIVE Environment*</b> for <b>Quick Experiments</b> and </b>Recreational Programming</b>?
  Its possible since the JVM is a dynamic stack machine, with live resource loading. 
</p>
        </html>""");
        gbc.gridy = gridY++;
        gbc.weightx = oldGbcWeightX;
        panel.add(bottomLabel, gbc);


        JLabel aboutLabel = new JLabel("""<html>
<p>
    (C) 2025 - <a href="https://github/hexaredecimal"><u>Gama Sibusiso</u></a>
</p>
        </html>""");

        JPanel bottomRow = new JPanel(new BorderLayout());

        JButton closeButton = new JButton("Explore!");
        closeButton.addActionListener { event -> 
            window.setClosed(true);
        }

        bottomRow.add(aboutLabel, BorderLayout.WEST);
        bottomRow.add(closeButton, BorderLayout.EAST);
        
        gbc.gridy = gridY++;
        gbc.weightx = oldGbcWeightX;
        panel.add(bottomRow, gbc);

        window.add(panel);

        window.setMaximizable(false);
        window.setResizable(false);
        return window;
    }
}


VirtualMachine vm = VirtualMachine.getInstance();
VMDesktop desktop = vm.getDesktop();

JMenu systemMenu = Utils.getCategoryMenu("VMSystemMenu");

JMenuItem app = new JMenuItem("About");
app.addActionListener {event -> 
    desktop.add(AboutApp.run());
}

systemMenu.add(app);
