package ru.nsu.gordin.controller.filters;

import ru.nsu.gordin.model.BMPImage;

public class OrderedDithering implements Filter {
    private int n;

    public OrderedDithering(int n) {
        this.n = n;
    }

    @Override
    public BMPImage apply(BMPImage image) {
        BMPImage filteredImage = new BMPImage(image);
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

        return filteredImage;
    }
}
