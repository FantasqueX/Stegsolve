/*
 * FrameBrowser.java
 */

package stegsolve;

// todo: jphide checker/ invisible secrets

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Frame Browser
 *
 * @author Caesum
 */
public class FrameBrowser extends JFrame {
    /**
     * Label with the text showing the frame number
     */
    private JLabel nowShowing;
    /**
     * Panel with image on it
     */
    private DPanel dp;
    /**
     * Scroll pane for the image
     */
    private JScrollPane scrollPane;

    /**
     * The image being viewed
     */
    private BufferedImage bi = null;
    /**
     * The individual frames of the image
     */
    private java.util.List<BufferedImage> frames = null;
    /**
     * Number of the current frame
     */
    private int fnum = 0;
    /**
     * Number of frames
     */
    private int frameNumber = 0;

    /**
     * Creates a new frame browser
     *
     * @param b    The image the view
     * @param file The file of the image
     */
    public FrameBrowser(BufferedImage b, File file) {
        bi = b;
        initComponents();
        fnum = 0;
        frameNumber = 0;
        frames = new ArrayList<>();
        ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
        try (ImageInputStream iis = ImageIO.createImageInputStream(new FileInputStream(file))) {
            reader.setInput(iis);
            frameNumber = reader.getNumImages(true);
            for (int i = 0; i < frameNumber; i++) {
                frames.add(reader.read(i));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load file: " + e.toString());
        }
        newImage();
    }

    private void initComponents() {

        nowShowing = new JLabel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());

        this.add(nowShowing, BorderLayout.NORTH);

        // Panel for the buttons
        JPanel buttonPanel = new JPanel();
        //Backward button
        JButton backwardButton = new JButton("<");
        backwardButton.addActionListener(this::backwardButtonActionPerformed);

        //Forward button
        JButton forwardButton = new JButton(">");
        forwardButton.addActionListener(this::forwardButtonActionPerformed);
        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(this::saveButtonActionPerformed);
        buttonPanel.add(backwardButton);
        buttonPanel.add(forwardButton);
        buttonPanel.add(saveButton);

        add(buttonPanel, BorderLayout.SOUTH);

        backwardButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "back");
        backwardButton.getActionMap().put("back", backButtonPress);
        forwardButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "forward");
        forwardButton.getActionMap().put("forward", forwardButtonPress);

        dp = new DPanel();
        scrollPane = new JScrollPane(dp);
        add(scrollPane, BorderLayout.CENTER);

        pack();
    }
    /**
     * This is used to map the left arrow key to the back button
     */
    private final Action backButtonPress = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            backwardButtonActionPerformed(e);
        }
    };

    /**
     * Move back by one frame
     *
     * @param evt Event
     */
    private void backwardButtonActionPerformed(ActionEvent evt) {
        fnum--;
        if (fnum < 0) fnum = frameNumber - 1;
        updateImage();
    }

    /**
     * This is used to map the right arrow key to the forward button
     */
    private final Action forwardButtonPress = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            forwardButtonActionPerformed(e);
        }
    };

    /**
     * Move forward by one frame
     *
     * @param evt Event
     */
    private void forwardButtonActionPerformed(ActionEvent evt) {
        fnum++;
        if (fnum >= frameNumber) fnum = 0;
        updateImage();
    }

    /**
     * Save the current frame
     *
     * @param event Event
     */
    private void saveButtonActionPerformed(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Images", "jpg", "gif", "png", "bmp");
        fileChooser.setFileFilter(filter);
        fileChooser.setSelectedFile(new File("frame" + (fnum + 1) + ".png"));
        int rVal = fileChooser.showSaveDialog(this);
        System.setProperty("user.dir", fileChooser.getCurrentDirectory().getAbsolutePath());
        if (rVal == JFileChooser.APPROVE_OPTION) {
            File sfile = fileChooser.getSelectedFile();
            BufferedImage bbx = frames.get(fnum);
            int rns = sfile.getName().lastIndexOf(".") + 1;
            try {
                if (rns == 0)
                    ImageIO.write(bbx, "png", sfile);
                else
                    ImageIO.write(bbx, sfile.getName().substring(rns), sfile);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to write file: " + e.toString());
            }
        }
    }

    /**
     * Update the text description and repaint the image
     */
    private void updateImage() {
        nowShowing.setText("Frame : " + (fnum + 1) + " of " + frameNumber);
        if (frameNumber == 0) return;
        dp.setImage(frames.get(fnum));
        repaint();
    }

    /**
     * Show the image and make sure the frame browser looks right
     */
    private void newImage() {
        nowShowing.setText("Frame : " + (fnum + 1) + " of " + frameNumber);
        if (frameNumber == 0) return;
        dp.setImage(frames.get(fnum));
        dp.setSize(bi.getWidth(), bi.getHeight());
        dp.setPreferredSize(new Dimension(bi.getWidth(), bi.getHeight()));
        this.setMaximumSize(getToolkit().getScreenSize());
        pack();
        scrollPane.revalidate();
        repaint();
    }

}
