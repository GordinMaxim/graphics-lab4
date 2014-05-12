package ru.nsu.gordin.controller.actions;

import ru.nsu.gordin.view.DrawPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class OrderedDitheringAction extends AbstractAction {
    private DrawPanel panel;

    public OrderedDitheringAction(String text, ImageIcon icon,
                                  String desc, Integer mnemonic, DrawPanel panel) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!panel.isImageLoaded())
            return;
        panel.orderedDithering(16, 16, 16);
        final JDialog dialog = new JDialog();
        JPanel settingPanel = new JPanel(new GridLayout(4, 1));
        settingPanel.setBorder(BorderFactory.createTitledBorder("color channel gradation"));

        final JSlider rSlider = new JSlider(JSlider.HORIZONTAL, 1, 8, 4);
        rSlider.setMajorTickSpacing(1);
        rSlider.setPaintTicks(true);
        rSlider.setPaintLabels(true);
        JPanel redPanel = new JPanel();
        redPanel.setBorder(BorderFactory.createTitledBorder("red"));
        redPanel.add(rSlider);
        settingPanel.add(redPanel);

        final JSlider gSlider = new JSlider(JSlider.HORIZONTAL, 1, 8, 4);
        gSlider.setMajorTickSpacing(1);
        gSlider.setPaintTicks(true);
        gSlider.setPaintLabels(true);
        JPanel greenPanel = new JPanel();
        greenPanel.setBorder(BorderFactory.createTitledBorder("green"));
        greenPanel.add(gSlider);
        settingPanel.add(greenPanel);

        final JSlider bSlider = new JSlider(JSlider.HORIZONTAL, 1, 8, 4);
        bSlider.setMajorTickSpacing(1);
        bSlider.setPaintTicks(true);
        bSlider.setPaintLabels(true);
        JPanel bluePanel = new JPanel();
        bluePanel.setBorder(BorderFactory.createTitledBorder("blue"));
        bluePanel.add(bSlider);
        settingPanel.add(bluePanel);

        final JButton saveButton = new JButton("Save");
        final JButton cancelButton = new JButton("Cancel");

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton source = (JButton)e.getSource();
                if(cancelButton == source) {
                    panel.cancel();
                }
                dialog.dispose();
            }
        };
        saveButton.addActionListener(actionListener);
        cancelButton.addActionListener(actionListener);

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int r = rSlider.getValue();
                r = 1 << r;
                int g = gSlider.getValue();
                g = 1 << g;
                int b = bSlider.getValue();
                b = 1 << b;
                panel.orderedDithering(r, g, b);
            }
        };

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                panel.cancel();
            }
        });
        rSlider.addChangeListener(changeListener);
        gSlider.addChangeListener(changeListener);
        bSlider.addChangeListener(changeListener);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        settingPanel.add(buttonPanel);
        dialog.add(settingPanel);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.pack();
        dialog.setLocationRelativeTo(panel);
        dialog.setVisible(true);
    }
}
