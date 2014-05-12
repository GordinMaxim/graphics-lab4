package ru.nsu.gordin.controller.filters;

import ru.nsu.gordin.model.BMPImage;

public class RobertsCross implements Filter {

    @Override
    public BMPImage apply(BMPImage image) {
        Filter grey = new Grey();
        image = grey.apply(image);
        BMPImage filteredImage = new BMPImage(image);
        BMPImage.BMPColor[][] bitmap = image.getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                int r1 = (int) (bitmap[i][j].red - bitmap[i + 1][j + 1].red);
                int r2 = (int) (bitmap[i][j + 1].red - bitmap[i - 1][j].red);

                int r = (int)Math.sqrt(r2*r2 + r1*r1);

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
