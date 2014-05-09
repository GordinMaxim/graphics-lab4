package ru.nsu.gordin.model;

public class Palette {
    private int bits;

    public Palette(int bits) {
        this.bits = bits;
    }

    public BMPImage.BMPColor getClosestColor(int red, int green, int blue, int grad) {
        int colors = (int) Math.pow(2, bits);
        if(grad < colors || grad % colors != 0) {
            return null;
        }
        int gap = grad / colors;
        int r = red - red % gap;
        int g = green - green % gap;
        int b = blue - blue % gap;

        return new BMPImage.BMPColor(r, g, b);
    }
}
