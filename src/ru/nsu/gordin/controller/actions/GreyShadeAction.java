package ru.nsu.gordin.controller.actions;

import ru.nsu.gordin.view.DrawPanel;

import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GreyShadeAction extends AbstractAction {
    private DrawPanel panel;

    public GreyShadeAction(String text, ImageIcon icon,
                           String desc, Integer mnemonic, DrawPanel panel) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        panel.grayShade(10, 10, 10);
        final JDialog dialog = new JDialog();
        dialog.setLocationRelativeTo(panel);
        JPanel settingPanel = new JPanel(new GridLayout(4, 1));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        JPanel hPanel = new JPanel();
        hPanel.setBorder(BorderFactory.createTitledBorder("Red"));
        final JSlider hSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);
        hSlider.setMajorTickSpacing(100);
        hSlider.setMinorTickSpacing(20);
        hSlider.setPaintTicks(true);
        hSlider.setPaintLabels(true);
        hPanel.add(hSlider);

        JPanel sPanel = new JPanel();
        sPanel.setBorder(BorderFactory.createTitledBorder("Green"));
        final JSlider sSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);
        sSlider.setMajorTickSpacing(100);
        sSlider.setMinorTickSpacing(20);
        sSlider.setPaintTicks(true);
        sSlider.setPaintLabels(true);
        sPanel.add(sSlider);

        JPanel vPanel = new JPanel();
        vPanel.setBorder(BorderFactory.createTitledBorder("Blue"));
        final JSlider vSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);
        vSlider.setMajorTickSpacing(100);
        vSlider.setMinorTickSpacing(20);
        vSlider.setPaintTicks(true);
        vSlider.setPaintLabels(true);
        vPanel.add(vSlider);

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
                int red = hSlider.getValue();
                int green = sSlider.getValue();
                int blue = vSlider.getValue();
                panel.grayShade(red, green, blue);
            }
        };

        hSlider.addChangeListener(changeListener);
        sSlider.addChangeListener(changeListener);
        vSlider.addChangeListener(changeListener);
        settingPanel.add(hPanel);
        settingPanel.add(sPanel);
        settingPanel.add(vPanel);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        settingPanel.add(buttonPanel);
        dialog.add(settingPanel);
        dialog.pack();
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
}

