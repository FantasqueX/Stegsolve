/*
 * StegSolve.java
 *
 * Created on 18-Apr-2011, 08:48:02
 */

package stegsolve;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

// todo - sort out dimensions in linux
// todo - width/height explorer

/**
 * StegSolve
 *
 * @author Caesum
 */
public class StegSolve extends JFrame {
    /**
     * label that shows the number of the frame currently being shown
     */
    private JLabel nowShowing;
    /**
     * Panel with image on it
     */
    private DPanel dp;
    /**
     * Scroll bars for panel with image
     */
    private JScrollPane scrollPane;

    /**
     * The image file
     */
    private File sfile = null;
    /**
     * The image
     */
    private BufferedImage bi = null;
    /**
     * The transformation being viewed
     */
    private Transform transform = null;

    /**
     * Creates new form stegsolve
     */
    public StegSolve() {
        initComponents();
    }

    private void initComponents() {


        // Menu bar
        JMenuBar menuBar = new JMenuBar();
        // Sub menu - file
        JMenu menuFile = new JMenu();
        // Menu option - file save
        JMenuItem fileOpen = new JMenuItem();
        // Menu option - file save
        JMenuItem fileSave = new JMenuItem();
        // Menu option - file exit
        JMenuItem fileExit = new JMenuItem();
        // Sub menu - analyse
        JMenu menuAnalyse = new JMenu();
        // Menu option - analyse format
        JMenuItem analyseFormat = new JMenuItem();
        // Menu option - extract data
        JMenuItem analyseExtract = new JMenuItem();
        // Menu option - solve stereogram
        JMenuItem stereoSolve = new JMenuItem();
        // Menu option - frame browser
        JMenuItem frameBrowse = new JMenuItem();
        // Menu option - frame browser
        JMenuItem imageCombine = new JMenuItem();
        // Sub menu - help
        JMenu menuHelp = new JMenu();
        // Menu option - about
        JMenuItem about = new JMenuItem();
        nowShowing = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        menuFile.setText("File");

        fileOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, 0));
        fileOpen.setText("Open");
        fileOpen.addActionListener(this::fileOpenActionPerformed);
        menuFile.add(fileOpen);

        fileSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0));
        fileSave.setText("Save As");
        fileSave.addActionListener(this::fileSaveActionPerformed);
        menuFile.add(fileSave);

        fileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0));
        fileExit.setText("Exit");
        fileExit.addActionListener(this::fileExitActionPerformed);
        menuFile.add(fileExit);

        menuBar.add(menuFile);

        menuAnalyse.setText("Analyse");

        analyseFormat.setText("File Format");
        analyseFormat.addActionListener(this::analyseFormatActionPerformed);
        menuAnalyse.add(analyseFormat);

        analyseExtract.setText("Data Extract");
        analyseExtract.addActionListener(this::analyseExtractActionPerformed);
        menuAnalyse.add(analyseExtract);

        stereoSolve.setText("Stereogram Solver");
        stereoSolve.addActionListener(this::stereoSolveActionPerformed);
        menuAnalyse.add(stereoSolve);

        frameBrowse.setText("Frame Browser");
        frameBrowse.addActionListener(this::frameBrowseActionPerformed);
        menuAnalyse.add(frameBrowse);

        imageCombine.setText("Image Combiner");
        imageCombine.addActionListener(this::imageCombineActionPerformed);
        menuAnalyse.add(imageCombine);

        menuBar.add(menuAnalyse);

        menuHelp.setText("Help");

        about.setText("About");
        about.addActionListener(this::aboutActionPerformed);
        menuHelp.add(about);

        menuBar.add(menuHelp);

        setJMenuBar(menuBar);

        setLayout(new BorderLayout());

        this.add(nowShowing, BorderLayout.NORTH);

        // panel for buttons
        JPanel buttonPanel = new JPanel();
        // Previous frame button
        JButton backwardButton = new JButton("<");
        backwardButton.addActionListener(this::backwardButtonActionPerformed);
        // Next frame button
        JButton forwardButton = new JButton(">");
        forwardButton.addActionListener(this::forwardButtonActionPerformed);
        buttonPanel.add(backwardButton);
        buttonPanel.add(forwardButton);

        add(buttonPanel, BorderLayout.SOUTH);

        dp = new DPanel();
        scrollPane = new JScrollPane(dp);
        add(scrollPane, BorderLayout.CENTER);

        backwardButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "back");
        backwardButton.getActionMap().put("back", backButtonPress);
        forwardButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "forward");
        forwardButton.getActionMap().put("forward", forwardButtonPress);

        this.setTitle("StegSolve 1.3 by Caesum");
        this.setMaximumSize(getToolkit().getScreenSize());
        pack();
    }

    /**
     * Close the form on file exit
     *
     * @param evt Event
     */
    private void fileExitActionPerformed(ActionEvent evt) {
        dispose();
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
     * Move back by one image
     *
     * @param evt Event
     */
    private void backwardButtonActionPerformed(ActionEvent evt) {
        if (transform == null) return;
        transform.back();
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
     * Move forward by one image
     *
     * @param evt Event
     */
    private void forwardButtonActionPerformed(ActionEvent evt) {
        if (bi == null) return;
        transform.forward();
        updateImage();
    }

    /**
     * Show the help/about frame
     *
     * @param evt Event
     */
    private void aboutActionPerformed(ActionEvent evt) {
        new AboutFrame().setVisible(true);
    }

    /**
     * Open the file format analyser
     *
     * @param evt Event
     */
    private void analyseFormatActionPerformed(ActionEvent evt) {
        new FileAnalysis(sfile).setVisible(true);
    }

    /**
     * Open the stereogram solver
     *
     * @param evt Event
     */
    private void stereoSolveActionPerformed(ActionEvent evt) {
        new Stereo(bi).setVisible(true);
    }

    /**
     * Open the frame browser
     *
     * @param evt Event
     */
    private void frameBrowseActionPerformed(ActionEvent evt) {
        new FrameBrowser(bi, sfile).setVisible(true);
    }

    /**
     * Open the image combiner
     *
     * @param evt Event
     */
    private void imageCombineActionPerformed(ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "gif", "bmp", "png");
        fileChooser.setFileFilter(filter);
        fileChooser.setDialogTitle("Select image to combine with");
        int rVal = fileChooser.showOpenDialog(this);
        System.setProperty("user.dir", fileChooser.getCurrentDirectory().getAbsolutePath());
        if (rVal == JFileChooser.APPROVE_OPTION) {
            sfile = fileChooser.getSelectedFile();
            try {
                BufferedImage bi2 = null;
                bi2 = ImageIO.read(sfile);
                new Combiner(bi, bi2).setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to load file: " + e.toString());
            }
        }
    }

    /**
     * Open the data extractor
     *
     * @param evt Event
     */
    private void analyseExtractActionPerformed(ActionEvent evt) {
        new Extract(bi).setVisible(true);
    }

    /**
     * Save the current transformed image
     *
     * @param evt Event
     */
    private void fileSaveActionPerformed(ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        fileChooser.setSelectedFile(new File("solved.bmp"));
        int rVal = fileChooser.showSaveDialog(this);
        System.setProperty("user.dir", fileChooser.getCurrentDirectory().getAbsolutePath());
        if (rVal == JFileChooser.APPROVE_OPTION) {
            sfile = fileChooser.getSelectedFile();
            try {
                bi = transform.getImage();
                int rns = sfile.getName().lastIndexOf(".") + 1;
                if (rns == 0)
                    ImageIO.write(bi, "bmp", sfile);
                else
                    ImageIO.write(bi, sfile.getName().substring(rns), sfile);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to write file: " + e.toString());
            }
        }
    }

    /**
     * Open a file
     *
     * @param evt Event
     */
    private void fileOpenActionPerformed(ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg", "gif", "bmp", "png");
        fileChooser.setFileFilter(filter);
        int rVal = fileChooser.showOpenDialog(this);
        System.setProperty("user.dir", fileChooser.getCurrentDirectory().getAbsolutePath());
        if (rVal == JFileChooser.APPROVE_OPTION) {
            sfile = fileChooser.getSelectedFile();
            try {
                bi = ImageIO.read(sfile);
                transform = new Transform(bi);
                newImage();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to load file: " + e.toString());
            }
        }
    }

    /**
     * Reset settings for a new image
     */
    private void newImage() {
        nowShowing.setText(transform.getText());
        dp.setImage(transform.getImage());
        dp.setSize(transform.getImage().getWidth(), transform.getImage().getHeight());
        dp.setPreferredSize(new Dimension(transform.getImage().getWidth(), transform.getImage().getHeight()));
        this.setMaximumSize(getToolkit().getScreenSize());
        pack();
        scrollPane.revalidate();
        repaint();
    }

    /**
     * Update the image being shown for new transform
     */
    private void updateImage() {
        nowShowing.setText(transform.getText());
        dp.setImage(transform.getImage());
        repaint();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new StegSolve().setVisible(true));
    }

}
