package ru.nsu.gordin;

import ru.nsu.gordin.controller.*;
import ru.nsu.gordin.controller.actions.*;
import ru.nsu.gordin.model.BMPImage;
import ru.nsu.gordin.view.DrawPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;

public class MainPanel extends JPanel{
    protected Action openAction, saveAction, copyAction, aquarelleAction, monochromeAction, doubleSizeAction,
            floydSteinbergAction, gaussBlurAction, greyShadeAction, negativeAction, orderedDitheringAction,
            robertsOperatorAction, sobelOperatorAction, stampAction, aboutAction, exitAction;

    public MainPanel() {
        super(new BorderLayout());
    }

    public void initActions(DrawPanel panel) {
        openAction = new OpenAction("Open",
                createToolIcon(""),
                "Open image",
                new Integer(KeyEvent.VK_O), panel);
        saveAction = new SaveAction("Save",
                createToolIcon(""),
                "Save image",
                new Integer(KeyEvent.VK_S), panel);
        copyAction = new CopyAction("Copy",
                createToolIcon(""),
                "copy 3rd image to 2nd",
                new Integer(KeyEvent.VK_C), panel);
        aquarelleAction = new AquarelleAction("AQ",
                createToolIcon(""),
                "aquarelle filter",
                new Integer(KeyEvent.VK_A), panel);
        monochromeAction = new MonochromeAction("MO",
                createToolIcon(""),
                "monochrome filter",
                new Integer(KeyEvent.VK_M), panel);
        doubleSizeAction = new DoubleSizeAction("DS",
                createToolIcon(""),
                "double size image",
                new Integer(KeyEvent.VK_D), panel);
        floydSteinbergAction = new FloydSteinbergAction("FS",
                createToolIcon(""),
                "Floyd Steinberg filter",
                new Integer(KeyEvent.VK_F), panel);
        gaussBlurAction = new GaussBlurAction("GB",
                createToolIcon(""),
                "Gauss blur filter",
                new Integer(KeyEvent.VK_B), panel);
        greyShadeAction = new GreyShadeAction("GS",
                createToolIcon(""),
                "grey shades",
                new Integer(KeyEvent.VK_G), panel);
        negativeAction = new NegativeAction("NG",
                createToolIcon(""),
                "negative filter",
                new Integer(KeyEvent.VK_N), panel);
        orderedDitheringAction = new OrderedDitheringAction("OD",
                createToolIcon(""),
                "ordered dithering filter",
                new Integer(KeyEvent.VK_O), panel);
        robertsOperatorAction = new RobertsOperatorAction("RO",
                createToolIcon(""),
                "Roberts operator",
                new Integer(KeyEvent.VK_R), panel);
        sobelOperatorAction = new SobelOperatorAction("SO",
                createToolIcon(""),
                "Sobel operator",
                new Integer(KeyEvent.VK_L), panel);
        stampAction = new StampAction("S",
                createToolIcon(""),
                "stamp filter",
                new Integer(KeyEvent.VK_P), panel);
        aboutAction = new AboutAction("About",
                createToolIcon("info_icon&16"),
                "About lab1",
                new Integer(KeyEvent.VK_F1), panel);
        exitAction = new ExitAction("Exit",
                createToolIcon("on-off_icon&16"),
                "Quit the application",
                new Integer(KeyEvent.VK_E));
    }

    protected static ImageIcon createToolIcon(String imageName) {
        String imgLocation = "/res/icon/"
                + imageName
                + ".png";
        java.net.URL imageURL = MainPanel.class.getResource(imgLocation);
        System.out.println();
        if (imageURL == null) {
//            System.err.println("Resource not found: "
//                    + imgLocation);
            return null;
        } else {
            return new ImageIcon(imageURL);
        }
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createImageMenu());
        menuBar.add(createHelpMenu());
        return menuBar;
    }

    public JMenu createFileMenu() {
        JMenuItem menuItem = null;
        JMenu fileMenu = new JMenu("File");

        Action[] actions = {openAction, saveAction, copyAction, exitAction};
        for (int i = 0; i < actions.length; i++) {
            menuItem = new JMenuItem(actions[i]);
            menuItem.setIcon(null); //arbitrarily chose not to use icon
            fileMenu.add(menuItem);
        }

        return fileMenu;
    }

    public JMenu createImageMenu() {
        JMenuItem menuItem = null;
        JMenu fileMenu = new JMenu("Image");

        Action[] actions = {aquarelleAction, monochromeAction, doubleSizeAction,
                floydSteinbergAction, gaussBlurAction, greyShadeAction, negativeAction, orderedDitheringAction,
                robertsOperatorAction, sobelOperatorAction, stampAction};
        for (int i = 0; i < actions.length; i++) {
            menuItem = new JMenuItem(actions[i]);
            menuItem.setIcon(null); //arbitrarily chose not to use icon
            fileMenu.add(menuItem);
        }

        return fileMenu;
    }

    public JMenu createHelpMenu() {
        JMenuItem menuItem = null;
        JMenu helpMenu = new JMenu("Help");

        Action[] actions = {aboutAction};
        for (int i = 0; i < actions.length; i++) {
            menuItem = new JMenuItem(actions[i]);
            menuItem.setIcon(null);
            helpMenu.add(menuItem);
        }

        return helpMenu;
    }

    public void createToolBar() {
        JButton button = null;

        ToolBar toolBar = new ToolBar();
        add(toolBar, BorderLayout.PAGE_START);

        Action[] enabledActions = {openAction, saveAction, copyAction, aquarelleAction, monochromeAction, doubleSizeAction,
                floydSteinbergAction, gaussBlurAction, greyShadeAction, negativeAction, orderedDitheringAction,
                robertsOperatorAction, sobelOperatorAction, stampAction, aboutAction, exitAction};

        for(int i = 0; i < enabledActions.length; i++){
            button = new JButton(enabledActions[i]);
            if (button.getIcon() != null) {
                button.setText("");
            }
            toolBar.add(button);
        }
    }

    public void enableImageButtons() {

    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("lab #1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainPanel demo = new MainPanel();
        BMPImage bmpImage = null;
        try {
            FileInputStream fis = new FileInputStream("src/res/lenna.bmp");
            bmpImage = new BMPImage(fis);
        }
        catch (Exception e) {
            System.out.println(e);
        }
        DrawPanel drawPanel = new DrawPanel();
        demo.initActions(drawPanel);
        frame.setJMenuBar(demo.createMenuBar());
        demo.createToolBar();
        demo.add(drawPanel);
        demo.setOpaque(true);
        frame.setContentPane(demo);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}