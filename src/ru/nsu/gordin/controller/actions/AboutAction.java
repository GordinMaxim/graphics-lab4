package ru.nsu.gordin.controller.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AboutAction extends AbstractAction {
    private JPanel panel;

    public AboutAction(String text, ImageIcon icon,
                       String desc, Integer mnemonic, JPanel panel) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.panel = panel;
    }

    public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(panel,"Gordin Maxim, gormakc.ru@gmail.com");
    }
}