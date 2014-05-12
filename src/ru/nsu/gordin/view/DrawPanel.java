package ru.nsu.gordin.view;

import ru.nsu.gordin.controller.filters.*;
import ru.nsu.gordin.model.BMPImage;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class DrawPanel extends JPanel implements MouseMotionListener {
    protected boolean imageLoaded;
    protected BMPImage image;
    protected BMPPanel originImagePanel;
    protected BMPPanel imageAreaPanel;
    protected BMPPanel filterPanel;
    static protected int subPanelWidth = 256;
    static protected int subPanelHeight = 256;
    protected int x = 0;
    protected int y = 0;
    private boolean allocating;

    public DrawPanel() {
        super(new GridLayout(1, 3));
        imageLoaded = false;
        allocating = false;
        originImagePanel = new BMPPanel();
        originImagePanel.addMouseMotionListener(this);
        originImagePanel.setSize(subPanelWidth, subPanelHeight);
        JPanel originBorderPanel = new JPanel(new GridLayout(1, 1));
        originBorderPanel.setBorder(BorderFactory.createTitledBorder("origin image"));
        originBorderPanel.add(originImagePanel);
        add(originBorderPanel);
        imageAreaPanel = new BMPPanel();
        imageAreaPanel.setSize(subPanelWidth, subPanelHeight);
        JPanel imageBorderPanel = new JPanel(new GridLayout(1, 1));
        imageBorderPanel.setBorder(BorderFactory.createTitledBorder("image area"));
        imageBorderPanel.add(imageAreaPanel);
        add(imageBorderPanel);
        filterPanel = new BMPPanel();
        filterPanel.setSize(subPanelWidth, subPanelHeight);
        JPanel filterBorderPanel = new JPanel(new GridLayout(1, 1));
        filterBorderPanel.setBorder(BorderFactory.createTitledBorder("filtered image"));
        filterBorderPanel.add(filterPanel);
        add(filterBorderPanel);
    }

    public boolean isImageLoaded() {
        return imageLoaded;
    }

    public void setImage(BMPImage image) {
        this.image = image;
//        removeAll();
        originImagePanel.setImage(image);
//        imageAreaPanel.setImage(image.copyPart(x, y, subPanelWidth, subPanelHeight));
//        filterPanel.setImage(image.copyPart(x, y, subPanelWidth, subPanelHeight));
        revalidate();
        repaint();
    }

    public void copy() {
        if(null == filterPanel.getImage())
            return;
        BMPImage copyImage = new BMPImage(filterPanel.getImage());
        imageAreaPanel.setImage(copyImage);
        imageAreaPanel.repaint();
    }

    public void saveImage() {
        final JFileChooser fc = new JFileChooser();
        fc.setApproveButtonText("Save");
        String fileName = null;
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "BMP Images", "bmp");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            fileName = fc.getCurrentDirectory() + "/" + fc.getSelectedFile().getName();
            try {
                imageAreaPanel.getImage().write(new FileOutputStream(fileName));
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(this,"Bad file");
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
//        if(!allocating)
//            return;
        imageLoaded = true;

        float k = (float)(image.getWidth())/subPanelWidth;
        int x = (int) (e.getX() * k) - subPanelWidth/2;
        int y = (int) (e.getY() * k) - subPanelHeight/2;
        if(x < 0) {
            x = 0;
        }
        else if(x + subPanelWidth > image.getWidth()) {
            x = image.getWidth() - subPanelWidth;
        }

        if(y < 0) {
            y = 0;
        }
        else if(y + subPanelHeight > image.getHeight()) {
            y = image.getHeight() - subPanelHeight;
        }
        imageAreaPanel.setImage(image.copyPart(x, y, subPanelWidth, subPanelHeight));
        imageAreaPanel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    public void gauss() {
        Filter gauss = new Gauss();
        BMPImage filteredImage = gauss.apply(imageAreaPanel.getImage());
        filterPanel.setImage(filteredImage);
        filterPanel.repaint();
    }

    public void aquarelle() {
        Filter aqua = new Aquarelle();
        BMPImage filteredImage = aqua.apply(imageAreaPanel.getImage());
        filterPanel.setImage(filteredImage);
        filterPanel.repaint();
    }

    public void doubleSize() {
        Filter ds = new DoubleSize();
        BMPImage filteredImage = ds.apply(imageAreaPanel.getImage());
        filterPanel.setImage(filteredImage);
        filterPanel.repaint();
    }

    public void floydSteinberg(int r, int g, int b) {
        Filter floyd = new FloydSteinberg(r, g, b);
        BMPImage filteredImage = floyd.apply(imageAreaPanel.getImage());
        filterPanel.setImage(filteredImage);
        filterPanel.repaint();
    }

    public void negative() {
        Filter negative = new Negative();
        BMPImage filteredImage = negative.apply(imageAreaPanel.getImage());
        filterPanel.setImage(filteredImage);
        filterPanel.repaint();
    }

    public void grey() {
        Filter grey = new Grey();
        BMPImage filteredImage = grey.apply(imageAreaPanel.getImage());
        filterPanel.setImage(filteredImage);
        filterPanel.repaint();
    }

    public void orderedDithering(int r, int g, int b) {
        Filter orderedDithering = new OrderedDithering(r, g, b);
        BMPImage filteredImage = orderedDithering.apply(imageAreaPanel.getImage());
        filterPanel.setImage(filteredImage);
        filterPanel.repaint();
    }

    public void robertsOperator(int blackness) {
        Filter roberts = new RobertsCross();
        Filter mono = new Monochrome(blackness);
        BMPImage filteredImage = roberts.apply(imageAreaPanel.getImage());
        filteredImage = mono.apply(filteredImage);
        filterPanel.setImage(filteredImage);
        filterPanel.repaint();
    }

    public void sobelOperator(int blackness) {
        Filter sobel = new SobelOperator();
        Filter mono = new Monochrome(blackness);
        BMPImage filteredImage = sobel.apply(imageAreaPanel.getImage());
        filteredImage = mono.apply(filteredImage);
        filterPanel.setImage(filteredImage);
        filterPanel.repaint();
    }

    public void stamp() {
        Filter stamp = new Stamp();
        BMPImage filteredImage = stamp.apply(imageAreaPanel.getImage());
        filterPanel.setImage(filteredImage);
        filterPanel.repaint();
    }

    public void monochrome(int blackness) {
        Filter mono = new Monochrome(blackness);
        BMPImage filteredImage = mono.apply(imageAreaPanel.getImage());
        filterPanel.setImage(filteredImage);
        filterPanel.repaint();
    }

    public void cancel() {
        if(!imageLoaded)
            return;
        filterPanel.setImage(new BMPImage(imageAreaPanel.getImage()));
        filterPanel.repaint();
    }

    public void setAllocating(boolean allocating) {
        this.allocating = allocating;
    }
}