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

public class MonochromeAction extends AbstractAction {
    private DrawPanel panel;

    public MonochromeAction(String text, ImageIcon icon,
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
        panel.monochrome(100);
        final JDialog dialog = new JDialog();
        JPanel settingPanel = new JPanel(new GridLayout(2, 1));
        JPanel buttonPanel = new JPanel();

        JPanel blackPanel = new JPanel();
        blackPanel.setBorder(BorderFactory.createTitledBorder("Blackness"));
        final JSlider hSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 100);
        hSlider.setMajorTickSpacing(100);
        hSlider.setMinorTickSpacing(20);
        hSlider.setPaintTicks(true);
        hSlider.setPaintLabels(true);
        blackPanel.add(hSlider);

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
                int blackness = hSlider.getValue();
                panel.monochrome(blackness);
            }
        };

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                panel.cancel();
            }
        });
        hSlider.addChangeListener(changeListener);
        settingPanel.add(blackPanel);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        settingPanel.add(buttonPanel);
        dialog.add(settingPanel);
        dialog.pack();
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(panel);
        dialog.setVisible(true);
    }
}
