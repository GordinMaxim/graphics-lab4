package ru.nsu.gordin.controller.filters;

import ru.nsu.gordin.model.BMPImage;

public class SobelOperator implements Filter {
    private double k;

    public SobelOperator(double k) {
        this.k = k;
    }

    @Override
    public BMPImage apply(BMPImage image) {
        Filter grey = new Grey();
        image = grey.apply(image);
        BMPImage filteredImage = new BMPImage(image);
        BMPImage.BMPColor[][] bitmap = image.getBitMap();
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

        return filteredImage;
    }
}
