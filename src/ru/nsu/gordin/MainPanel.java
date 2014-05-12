package ru.nsu.gordin;

import ru.nsu.gordin.controller.*;
import ru.nsu.gordin.controller.actions.*;
import ru.nsu.gordin.view.DrawPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MainPanel extends JPanel{
    protected Action openAction, saveAction, copyAction, aquarelleAction, monochromeAction, doubleSizeAction,
            floydSteinbergAction, gaussBlurAction, greyShadeAction, negativeAction, orderedDitheringAction,
            robertsOperatorAction, sobelOperatorAction, stampAction, aboutAction, exitAction;

    public MainPanel() {
        super(new BorderLayout());
    }

    public void initActions(DrawPanel panel) {
        openAction = new OpenAction("Open",
                createToolIcon("folder_open_icon&16"),
                "Open image",
                new Integer(KeyEvent.VK_O), panel);
        saveAction = new SaveAction("Save",
                createToolIcon("save_icon&16"),
                "Save image",
                new Integer(KeyEvent.VK_S), panel);
//        allocAction = new SaveAction("allocate image",
//                createToolIcon("cursor_arrow_icon&16"),
//                "allocate image",
//                new Integer(KeyEvent.VK_Q), panel);
        copyAction = new CopyAction("Copy",
                createToolIcon("clipboard_copy_icon&16"),
                "copy 3rd image to 2nd",
                new Integer(KeyEvent.VK_C), panel);
        aquarelleAction = new AquarelleAction("aquarelle",
                createToolIcon("arrow_bottom_icon&16"),
                "aquarelle filter",
                new Integer(KeyEvent.VK_A), panel);
        monochromeAction = new MonochromeAction("monochrome",
                createToolIcon("arrow_left_icon&16"),
                "monochrome filter",
                new Integer(KeyEvent.VK_M), panel);
        doubleSizeAction = new DoubleSizeAction("double size",
                createToolIcon("arrow_right_icon&16"),
                "double size image",
                new Integer(KeyEvent.VK_D), panel);
        floydSteinbergAction = new FloydSteinbergAction("Floyd Steinberg",
                createToolIcon("arrow_top_icon&16"),
                "Floyd Steinberg filter",
                new Integer(KeyEvent.VK_F), panel);
        gaussBlurAction = new GaussBlurAction("Gauss blur",
                createToolIcon("round_plus_icon&16"),
                "Gauss blur filter",
                new Integer(KeyEvent.VK_B), panel);
        greyShadeAction = new GreyShadeAction("grey",
                createToolIcon("brush_icon&16"),
                "grey shades",
                new Integer(KeyEvent.VK_G), panel);
        negativeAction = new NegativeAction("negative",
                createToolIcon("bug_icon&16"),
                "negative filter",
                new Integer(KeyEvent.VK_N), panel);
        orderedDitheringAction = new OrderedDitheringAction("ordered dithering",
                createToolIcon("burst_icon&16"),
                "ordered dithering filter",
                new Integer(KeyEvent.VK_O), panel);
        robertsOperatorAction = new RobertsOperatorAction("Roberts cross",
                createToolIcon("cog_icon&16"),
                "Roberts cross",
                new Integer(KeyEvent.VK_R), panel);
        sobelOperatorAction = new SobelOperatorAction("Sobel operator",
                createToolIcon("refresh_icon&16"),
                "Sobel operator",
                new Integer(KeyEvent.VK_L), panel);
        stampAction = new StampAction("stamp filter",
                createToolIcon("round_minus_icon&16"),
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

        Action[] enabledActions = {openAction, saveAction, copyAction, aquarelleAction, monochromeAction,
                doubleSizeAction,floydSteinbergAction, gaussBlurAction, greyShadeAction, negativeAction,
                orderedDitheringAction, robertsOperatorAction, sobelOperatorAction, stampAction, aboutAction,
                exitAction};

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
        JFrame frame = new JFrame("lab #4");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainPanel demo = new MainPanel();
        DrawPanel drawPanel = new DrawPanel();
        demo.initActions(drawPanel);
        frame.setJMenuBar(demo.createMenuBar());
        demo.createToolBar();
        demo.add(drawPanel);
        demo.setOpaque(true);
        frame.setContentPane(demo);
        frame.setMinimumSize(new Dimension(800, 360));
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