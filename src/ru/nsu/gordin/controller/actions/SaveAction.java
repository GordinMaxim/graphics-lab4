package ru.nsu.gordin.controller.actions;

import ru.nsu.gordin.view.DrawPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class SaveAction extends AbstractAction {
    private DrawPanel panel;

    public SaveAction(String text, ImageIcon icon,
                       String desc, Integer mnemonic, DrawPanel panel) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.panel = panel;
    }

    public void actionPerformed(ActionEvent e) {
        panel.save();
    }
}