package ru.nsu.gordin.controller.filters;

import ru.nsu.gordin.model.BMPImage;

public class RobertsCross implements Filter {
    private double k;

    public RobertsCross(double k) {
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

        return filteredImage;
    }
}
