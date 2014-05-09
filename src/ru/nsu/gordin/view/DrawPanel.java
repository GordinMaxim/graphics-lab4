package ru.nsu.gordin.view;

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

    public DrawPanel() {
        super(new GridLayout(1, 3));
        imageLoaded = false;
        originImagePanel = new BMPPanel();
        originImagePanel.addMouseMotionListener(this);
        originImagePanel.setSize(subPanelWidth, subPanelHeight);
        add(originImagePanel);
        imageAreaPanel = new BMPPanel();
        imageAreaPanel.setSize(subPanelWidth, subPanelHeight);
        add(imageAreaPanel);
        filterPanel = new BMPPanel();
        filterPanel.setSize(subPanelWidth, subPanelHeight);
        add(filterPanel);
    }

    public void setImage(BMPImage image) {
        imageLoaded = true;
        this.image = image;
//        removeAll();
        originImagePanel.setImage(image);
        imageAreaPanel.setImage(image.copyPart(x, y, subPanelWidth, subPanelHeight));
        filterPanel.setImage(image.copyPart(x, y, subPanelWidth, subPanelHeight));
        revalidate();
        repaint();
    }

    public void copy() {
        if(!imageLoaded)
            return;
        BMPImage copyImage = new BMPImage(filterPanel.getImage());
        imageAreaPanel.setImage(copyImage);
        imageAreaPanel.repaint();
    }

    public void save() {
        if(!imageLoaded)
            return;
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
        if(!imageLoaded)
            return;

        BMPImage filteredImage = new BMPImage(imageAreaPanel.getImage());
        double[][] core = {{0.5/6, 0.75/6, 0.5/6},
                             {0.75/6, 1.0/6, 0.75/6},
                             {0.5/6, 0.75/6, 0.5/6}};

        BMPImage.BMPColor[][] bitmap = imageAreaPanel.getImage().getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                filteredBitmap[i][j].red = (int) (core[0][0] * bitmap[i - 1][j - 1].red +
                        core[0][1] * bitmap[i - 1][j].red +
                        core[0][2] * bitmap[i - 1][j + 1].red + core[1][0] * bitmap[i][j + 1].red +
                        core[1][1] * bitmap[i][j].red + core[1][2] * bitmap[i][j + 1].red +
                        core[2][0] * bitmap[i + 1][j - 1].red + core[2][1] * bitmap[i + 1][j].red +
                        core[2][2] * bitmap[i + 1][j + 1].red);

                filteredBitmap[i][j].green = (int) (core[0][0] * bitmap[i - 1][j - 1].green +
                        core[0][1] * bitmap[i - 1][j].green +
                        core[0][2] * bitmap[i - 1][j + 1].green + core[1][0] * bitmap[i][j + 1].green +
                        core[1][1] * bitmap[i][j].green + core[1][2] * bitmap[i][j + 1].green +
                        core[2][0] * bitmap[i + 1][j - 1].green + core[2][1] * bitmap[i + 1][j].green +
                        core[2][2] * bitmap[i + 1][j + 1].green);

                filteredBitmap[i][j].blue = (int) (core[0][0] * bitmap[i - 1][j - 1].blue +
                        core[0][1] * bitmap[i - 1][j].blue +
                        core[0][2] * bitmap[i - 1][j + 1].blue + core[1][0] * bitmap[i][j + 1].blue +
                        core[1][1] * bitmap[i][j].blue + core[1][2] * bitmap[i][j + 1].blue +
                        core[2][0] * bitmap[i + 1][j - 1].blue + core[2][1] * bitmap[i + 1][j].blue +
                        core[2][2] * bitmap[i + 1][j + 1].blue);
            }
        }

        filterPanel.setImage(filteredImage);
        filterPanel.repaint();

        int n = JOptionPane.showOptionDialog(this,
                "Save changes?", "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);
        if(n != JOptionPane.YES_OPTION) {
            cancel();
        }
    }

    public void aquarelle() {
        if(!imageLoaded)
            return;

        BMPImage medianImage = new BMPImage(imageAreaPanel.getImage());
        BMPImage filteredImage = new BMPImage(imageAreaPanel.getImage());
        double ratio = 0.75;
        double[][] core = {{-ratio, -ratio, -ratio},
                           {-ratio, 1.0 + 8*ratio, -ratio},
                           {-ratio, -ratio, -ratio}};

        BMPImage.BMPColor[][] bitmap = imageAreaPanel.getImage().getBitMap();
        BMPImage.BMPColor[][] medianBitmap = medianImage.getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                int[] r = new int[9];
                int[] g = new int[9];
                int[] b = new int[9];
                for(int k = 0; k < 3; k++) {
                    for(int l = 0; l < 3; l++) {
                        r[k*3 + l] = bitmap[i + (k-1)][j + (l-1)].red;
                    }
                }

                for(int k = 0; k < 3; k++) {
                    for(int l = 0; l < 3; l++) {
                        g[k*3 + l] = bitmap[i + (k-1)][j + (l-1)].green;
                    }
                }

                for(int k = 0; k < 3; k++) {
                    for(int l = 0; l < 3; l++) {
                        b[k*3 + l] = bitmap[i + (k-1)][j + (l-1)].blue;
                    }
                }
                Arrays.sort(r);
                Arrays.sort(g);
                Arrays.sort(b);
                medianBitmap[i][j].red = r[5];
                medianBitmap[i][j].green = g[5];
                medianBitmap[i][j].blue = b[5];
            }
        }

        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                filteredBitmap[i][j].red = (int) (core[0][0] * medianBitmap[i - 1][j - 1].red +
                        core[0][1] * medianBitmap[i - 1][j].red +
                        core[0][2] * medianBitmap[i - 1][j + 1].red + core[1][0] * medianBitmap[i][j + 1].red +
                        core[1][1] * medianBitmap[i][j].red + core[1][2] * medianBitmap[i][j + 1].red +
                        core[2][0] * medianBitmap[i + 1][j - 1].red + core[2][1] * medianBitmap[i + 1][j].red +
                        core[2][2] * medianBitmap[i + 1][j + 1].red);
                if(filteredBitmap[i][j].red < 0) filteredBitmap[i][j].red = 0;
                if(filteredBitmap[i][j].red > 255) filteredBitmap[i][j].red = 255;

                filteredBitmap[i][j].green = (int) (core[0][0] * medianBitmap[i - 1][j - 1].green +
                        core[0][1] * medianBitmap[i - 1][j].green +
                        core[0][2] * medianBitmap[i - 1][j + 1].green + core[1][0] * medianBitmap[i][j + 1].green +
                        core[1][1] * medianBitmap[i][j].green + core[1][2] * medianBitmap[i][j + 1].green +
                        core[2][0] * medianBitmap[i + 1][j - 1].green + core[2][1] * medianBitmap[i + 1][j].green +
                        core[2][2] * medianBitmap[i + 1][j + 1].green);

                if(filteredBitmap[i][j].green < 0) filteredBitmap[i][j].green = 0;
                if(filteredBitmap[i][j].green > 255) filteredBitmap[i][j].green = 255;

                filteredBitmap[i][j].blue = (int) (core[0][0] * medianBitmap[i - 1][j - 1].blue +
                        core[0][1] * medianBitmap[i - 1][j].blue +
                        core[0][2] * medianBitmap[i - 1][j + 1].blue + core[1][0] * medianBitmap[i][j + 1].blue +
                        core[1][1] * medianBitmap[i][j].blue + core[1][2] * medianBitmap[i][j + 1].blue +
                        core[2][0] * medianBitmap[i + 1][j - 1].blue + core[2][1] * medianBitmap[i + 1][j].blue +
                        core[2][2] * medianBitmap[i + 1][j + 1].blue);

                if(filteredBitmap[i][j].blue < 0) filteredBitmap[i][j].blue = 0;
                if(filteredBitmap[i][j].blue > 255) filteredBitmap[i][j].blue = 255;
            }
        }

        filterPanel.setImage(filteredImage);
        filterPanel.repaint();

        int n = JOptionPane.showOptionDialog(this,
                "Save changes?", "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);
        if(n != JOptionPane.YES_OPTION) {
            cancel();
        }
    }

    public void doubleSize() {
        if(!imageLoaded)
            return;

        BMPImage filteredImage = new BMPImage(imageAreaPanel.getImage());
        BMPImage.BMPColor[][] bitmap = imageAreaPanel.getImage().getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        int height = filteredImage.getHeight();
        int width = filteredImage.getWidth();
        for(int i = 1; i <= height; i++) {
            for(int j = 1; j <= width; j++) {
                int r;
                int g;
                int b;

                if((i - 1) % 2 == 0 && (j - 1) % 2 == 0) {
                    int i1 = width / 4 + (i - 1) / 2;
                    int j1 = height / 4 + (j - 1) / 2;
                    r = bitmap[i1][j1].red;
                    g = bitmap[i1][j1].green;
                    b = bitmap[i1][j1].blue;
                } else
                if((i - 1) % 2 == 0) {
                    int i1 = width / 4 + (i - 1) / 2;
                    int j1 = height / 4 + (j - 2) / 2;
                    r = (bitmap[i1][j1].red + bitmap[i1][j1+1].red) / 2;
                    g = (bitmap[i1][j1].green + bitmap[i1][j1+1].green) / 2;
                    b = (bitmap[i1][j1].blue + bitmap[i1][j1+1].blue) / 2;
                } else
                if((j - 1) % 2 == 0) {
                    int i1 = width / 4 + (i - 2) / 2;
                    int j1 = height / 4 + (j - 1) / 2;
                    r = (bitmap[i1][j1].red + bitmap[i1+1][j1].red) / 2;
                    g = (bitmap[i1][j1].green + bitmap[i1+1][j1].green) / 2;
                    b = (bitmap[i1][j1].blue + bitmap[i1+1][j1].blue) / 2;
                } else {
                    int i1 = width / 4 + (i - 2) / 2;
                    int j1 = height / 4 + (j - 2) / 2;
                    r = (bitmap[i1][j1].red + bitmap[i1][j1+1].red +
                            bitmap[i1+1][j1].red + bitmap[i1+1][j1+1].red) / 4;
                    g = (bitmap[i1][j1].green + bitmap[i1][j1+1].green +
                            bitmap[i1][j1+1].green + bitmap[i1+1][j1+1].green) / 4;
                    b = (bitmap[i1][j1].blue + bitmap[i1][j1+1].blue +
                            bitmap[i1][j1+1].blue + bitmap[i1+1][j1+1].blue) / 4;
                }
                filteredBitmap[i][j].red = r;
                filteredBitmap[i][j].green = g;
                filteredBitmap[i][j].blue = b;
            }
        }

        filterPanel.setImage(filteredImage);
        filterPanel.repaint();

        int n = JOptionPane.showOptionDialog(this,
                "Save changes?", "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);
        if(n != JOptionPane.YES_OPTION) {
            cancel();
        }
    }

    public void floydSteinberg(int n) {
        if(!imageLoaded)
            return;

        BMPImage filteredImage = new BMPImage(imageAreaPanel.getImage());
        int gap = 256/n;
        BMPImage.BMPColor[][] bitmap = imageAreaPanel.getImage().getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                int er = filteredBitmap[i][j].red % gap;
                int eg = filteredBitmap[i][j].green % gap;
                int eb = filteredBitmap[i][j].blue % gap;
                filteredBitmap[i][j].red -= er;
                filteredBitmap[i][j].green -= eg;
                filteredBitmap[i][j].blue -= eb;

                filteredBitmap[i][j+1].red += (int)(er * 7.0/16);
                filteredBitmap[i][j+1].green += (int)(eg * 7.0/16);
                filteredBitmap[i][j+1].blue += (int)(eb * 7.0/16);

                filteredBitmap[i+1][j-1].red += (int)(er * 3.0/16);
                filteredBitmap[i+1][j-1].green += (int)(eg * 3.0/16);
                filteredBitmap[i+1][j-1].blue += (int)(eb * 3.0/16);

                filteredBitmap[i+1][j].red += (int)(er * 5.0/16);
                filteredBitmap[i+1][j].green += (int)(eg * 5.0/16);
                filteredBitmap[i+1][j].blue += (int)(eb * 5.0/16);

                filteredBitmap[i+1][j+1].red += (int)(er * 1.0/16);
                filteredBitmap[i+1][j+1].green += (int)(eg * 1.0/16);
                filteredBitmap[i+1][j+1].blue += (int)(eb * 1.0/16);

                if(filteredBitmap[i][j].red > 255) filteredBitmap[i][j].red = 255;
                if(filteredBitmap[i][j].green > 255) filteredBitmap[i][j].green = 255;
                if(filteredBitmap[i][j].blue > 255) filteredBitmap[i][j].blue = 255;
            }
        }

        filterPanel.setImage(filteredImage);
        filterPanel.repaint();
    }

    public void negative() {
        if(!imageLoaded)
            return;

        BMPImage filteredImage = new BMPImage(imageAreaPanel.getImage());
        double[][] core = {{0.5/6, 0.75/6, 0.5/6},
                {0.75/6, 1.0/6, 0.75/6},
                {0.5/6, 0.75/6, 0.5/6}};

        BMPImage.BMPColor[][] bitmap = imageAreaPanel.getImage().getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                filteredBitmap[i][j].red = 255 - filteredBitmap[i][j].red;
                filteredBitmap[i][j].green = 255 - filteredBitmap[i][j].green;
                filteredBitmap[i][j].blue = 255 - filteredBitmap[i][j].blue;
            }
        }

        filterPanel.setImage(filteredImage);
        filterPanel.repaint();

        int n = JOptionPane.showOptionDialog(this,
                "Save changes?", "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);
        if(n != JOptionPane.YES_OPTION) {
            cancel();
        }
    }

    public void grayShade(int red, int green, int blue) {
        if(!imageLoaded)
            return;

        BMPImage filteredImage = new BMPImage(imageAreaPanel.getImage());
        double[][] core = {{0, 0, 0},
                {0, 0, 7},
                {5, 3, 1}};

        BMPImage.BMPColor[][] bitmap = imageAreaPanel.getImage().getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                int r = filteredBitmap[i][j].red;
                int g = filteredBitmap[i][j].green;
                int b = filteredBitmap[i][j].blue;
                int y = (int)(((red + 0.333) * r + (green + 0.333) * g + (blue + 0.333) * b)/
                        (red + green + blue + 0.999));
                if(y < 0) {
                    y = 0;
                }
                if(y > 255) y = 255;
                filteredBitmap[i][j].red = y;
                filteredBitmap[i][j].green = y;
                filteredBitmap[i][j].blue = y;
            }
        }

        filterPanel.setImage(filteredImage);
        filterPanel.repaint();
    }

    public void orderedDithering(int n) {
        if(!imageLoaded)
            return;

        BMPImage filteredImage = new BMPImage(imageAreaPanel.getImage());
//        double[][] core = {{1, 9, 3, 11},
//                           {13, 5, 15, 7},
//                           {4, 12, 2, 10},
//                           {16, 8, 14, 6}};

        double[][] core = {
                {1, 49, 13, 61, 4, 52, 16, 64},
                {33, 17, 45, 29, 36, 20, 48, 32},
                {9, 57, 5, 53, 12, 60, 8, 56},
                {41, 25, 37, 21, 44, 28, 40, 24},
                {3, 51, 15, 63, 2, 50, 14, 62},
                {35, 19, 47, 31, 34, 18, 46, 30},
                {11, 59, 7, 55, 10, 58, 6, 54},
                {43, 47, 39, 23, 42, 26, 38, 22},
        };
        int mod = core.length;
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        int gap = 256/n;
        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                int er = filteredBitmap[i][j].red % gap;
                int eg = filteredBitmap[i][j].green % gap;
                int eb = filteredBitmap[i][j].blue % gap;
                filteredBitmap[i][j].red -= er;
                filteredBitmap[i][j].green -= eg;
                filteredBitmap[i][j].blue -= eb;

                if(er > core[(i-1)%mod][(j-1)%mod])
                    filteredBitmap[i][j].red += gap;
                if(eg > core[(i-1)%mod][(j-1)%mod])
                    filteredBitmap[i][j].green += gap;
                if(eb > core[(i-1)%mod][(j-1)%mod])
                    filteredBitmap[i][j].blue += gap;

                if(filteredBitmap[i][j].red > 255) filteredBitmap[i][j].red = 255;
                if(filteredBitmap[i][j].green > 255) filteredBitmap[i][j].green = 255;
                if(filteredBitmap[i][j].blue > 255) filteredBitmap[i][j].blue = 255;
            }
        }

        filterPanel.setImage(filteredImage);
        filterPanel.repaint();
    }

    public void robertsOperator(double k) {
        if(!imageLoaded)
            return;

        BMPImage filteredImage = new BMPImage(imageAreaPanel.getImage());


        BMPImage.BMPColor[][] bitmap = imageAreaPanel.getImage().getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                int r1 = (int) (k*bitmap[i][j].red - k*bitmap[i + 1][j + 1].red);
                int r2 = (int) (k*bitmap[i][j + 1].red - k*bitmap[i - 1][j].red);
                int g1 = (int) (k*bitmap[i][j].green - k*bitmap[i + 1][j + 1].green);
                int g2 = (int) (k*bitmap[i][j + 1].green - k*bitmap[i - 1][j].green);
                int b1 = (int) (k*bitmap[i][j].blue - k*bitmap[i + 1][j + 1].blue);
                int b2 = (int) (k*bitmap[i][j + 1].blue - k*bitmap[i - 1][j].blue);

                int r = (int)Math.sqrt(r2*r2 + r1*r1);
                int g = (int)Math.sqrt(g2*g2 + g1*g1);
                int b = (int)Math.sqrt(b2*b2 + b1*b1);

                if(r < 0) r = 0;
                if(g < 0) g = 0;
                if(b < 0) b = 0;
                if(r > 255) r = 255;
                if(g > 255) g = 255;
                if(b > 255) b = 255;

                filteredBitmap[i][j].red = r;
                filteredBitmap[i][j].green = g;
                filteredBitmap[i][j].blue = b;
            }
        }

        filterPanel.setImage(filteredImage);
        filterPanel.repaint();
    }

    public void sobelOperator(double k) {
        if(!imageLoaded)
            return;

        BMPImage filteredImage = new BMPImage(imageAreaPanel.getImage());


        BMPImage.BMPColor[][] bitmap = imageAreaPanel.getImage().getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                int ry = (int) (-k*bitmap[i - 1][j - 1].red - 2*k*bitmap[i - 1][j].red - k*bitmap[i - 1][j + 1].red +
                                         k*bitmap[i + 1][j - 1].red + 2*k*bitmap[i + 1][j].red + k*bitmap[i + 1][j + 1].red);

                int gy = (int) (-k*bitmap[i - 1][j - 1].green - 2*k*bitmap[i - 1][j].green - k*bitmap[i - 1][j + 1].green +
                                         k*bitmap[i + 1][j - 1].green + 2*k*bitmap[i + 1][j].green + k*bitmap[i + 1][j + 1].green);

                int by = (int) (-k*bitmap[i - 1][j - 1].blue - 2*k*bitmap[i - 1][j].blue - k*bitmap[i - 1][j + 1].blue +
                                         k*bitmap[i + 1][j + 1].blue + 2*k*bitmap[i + 1][j].blue + k*bitmap[i + 1][j + 1].blue);

                int rx = (int) (-k*bitmap[i - 1][j - 1].red - 2*k*bitmap[i][j - 1].red - k*bitmap[i + 1][j - 1].red +
                                         k*bitmap[i - 1][j + 1].red + 2*k*bitmap[i][j + 1].red + k*bitmap[i + 1][j + 1].red);

                int gx = (int) (-k*bitmap[i - 1][j - 1].green - 2*k*bitmap[i][j - 1].green - k*bitmap[i + 1][j - 1].green +
                                         k*bitmap[i - 1][j + 1].green + 2*k*bitmap[i][j + 1].green + k*bitmap[i + 1][j + 1].green);

                int bx = (int) (-k*bitmap[i - 1][j - 1].blue - 2*k*bitmap[i][j - 1].blue - k*bitmap[i + 1][j - 1].blue +
                                         k*bitmap[i - 1][j + 1].blue + 2*k*bitmap[i][j + 1].blue + k*bitmap[i + 1][j + 1].blue);

                int r = (int)Math.sqrt(rx*rx + ry*ry);
                int g = (int)Math.sqrt(rx*rx + ry*ry);
                int b = (int)Math.sqrt(rx*rx + ry*ry);

                if(r < 0) r = 0;
                if(g < 0) g = 0;
                if(b < 0) b = 0;
                if(r > 255) r = 255;
                if(g > 255) g = 255;
                if(b > 255) b = 255;

                filteredBitmap[i][j].red = r;
                filteredBitmap[i][j].green = g;
                filteredBitmap[i][j].blue = b;
            }
        }

        filterPanel.setImage(filteredImage);
        filterPanel.repaint();
    }

    public void stamp() {
        if(!imageLoaded)
            return;

        BMPImage filteredImage = new BMPImage(imageAreaPanel.getImage());

        BMPImage.BMPColor[][] bitmap = imageAreaPanel.getImage().getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                int r =
                        1 * bitmap[i - 1][j].red - 1 * bitmap[i][j + 1].red +
                        1 * bitmap[i][j + 1].red - 1 * bitmap[i + 1][j].red;

                int g =
                        1 * bitmap[i - 1][j].green - 1 * bitmap[i][j + 1].green +
                        1 * bitmap[i][j + 1].green - 1 * bitmap[i + 1][j].green;

                int b =
                        1 * bitmap[i - 1][j].blue - 1 * bitmap[i][j + 1].blue +
                        1 * bitmap[i][j + 1].blue - 1 * bitmap[i + 1][j].blue;

                if(r < 0) r = 0;
                if(g < 0) g = 0;
                if(b < 0) b = 0;
                if(r > 255) r = 255;
                if(g > 255) g = 255;
                if(b > 255) b = 255;

                filteredBitmap[i][j].red = r;
                filteredBitmap[i][j].green = g;
                filteredBitmap[i][j].blue = b;
            }
        }

        filterPanel.setImage(filteredImage);
        filterPanel.repaint();

        int n = JOptionPane.showOptionDialog(this,
                "Save changes?", "",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null);
        if(n != JOptionPane.YES_OPTION) {
            cancel();
        }
    }

    public void monochrome(int blackness) {
        if(!imageLoaded)
            return;

        BMPImage filteredImage = new BMPImage(imageAreaPanel.getImage());

        BMPImage.BMPColor[][] bitmap = imageAreaPanel.getImage().getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                int r = filteredBitmap[i][j].red;
                int g = filteredBitmap[i][j].green;
                int b = filteredBitmap[i][j].blue;

                int y = (int)(0.299 * r + 0.587 * g + 0.114 * b);
                if(y < blackness) {
                    r = 0;
                    g = 0;
                    b = 0;
                } else {
                    r = 255;
                    g = 255;
                    b = 255;
                }

                filteredBitmap[i][j].red = r;
                filteredBitmap[i][j].green = g;
                filteredBitmap[i][j].blue = b;
            }
        }

        filterPanel.setImage(filteredImage);
        filterPanel.repaint();
    }

    public void cancel() {
        if(!imageLoaded)
            return;
        filterPanel.setImage(new BMPImage(imageAreaPanel.getImage()));
        filterPanel.repaint();
    }
}