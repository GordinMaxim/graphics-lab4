package ru.nsu.gordin.controller.filters;

import ru.nsu.gordin.model.BMPImage;

public class SobelOperator implements Filter {

    @Override
    public BMPImage apply(BMPImage image) {
        Filter grey = new Grey();
        image = grey.apply(image);
        BMPImage filteredImage = new BMPImage(image);
        BMPImage.BMPColor[][] bitmap = image.getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {

                int y = (int) (-bitmap[i - 1][j - 1].red - 2*bitmap[i - 1][j].red - bitmap[i - 1][j + 1].red +
                        bitmap[i + 1][j - 1].red + 2*bitmap[i + 1][j].red + bitmap[i + 1][j + 1].red);
                int x = (int) (-bitmap[i - 1][j - 1].red - 2*bitmap[i][j - 1].red - bitmap[i + 1][j - 1].red +
                        bitmap[i - 1][j + 1].red + 2*bitmap[i][j + 1].red + bitmap[i + 1][j + 1].red);

                int r = (int)Math.sqrt(x*x + y*y);

                if(r < 0) r = 0;
                if(r > 255) r = 255;

                filteredBitmap[i][j].red = r;
                filteredBitmap[i][j].green = r;
                filteredBitmap[i][j].blue = r;
            }
        }

        return filteredImage;
    }
}
