package ru.nsu.gordin.controller.actions;

import ru.nsu.gordin.view.DrawPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AllocatingAction extends AbstractAction {
    private DrawPanel panel;

    public AllocatingAction(String text, ImageIcon icon,
                      String desc, Integer mnemonic, DrawPanel panel) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.panel = panel;
    }

    public void actionPerformed(ActionEvent e) {
        panel.setAllocating(true);
    }
}