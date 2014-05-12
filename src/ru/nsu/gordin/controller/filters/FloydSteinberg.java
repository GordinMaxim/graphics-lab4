package ru.nsu.gordin.controller.filters;

import ru.nsu.gordin.model.BMPImage;

public class FloydSteinberg implements Filter{
    private int redBits;
    private int greenBits;
    private int blueBits;

    public FloydSteinberg(int redBits, int greenBits, int blueBits) {
        this.redBits = redBits;
        this.greenBits = greenBits;
        this.blueBits = blueBits;
    }

    @Override
    public BMPImage apply(BMPImage image) {
        BMPImage filteredImage = new BMPImage(image);
        int redGap = 256/redBits;
        int greenGap = 256/greenBits;
        int blueGap = 256/blueBits;
        BMPImage.BMPColor[][] bitmap = image.getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                int er = filteredBitmap[i][j].red % redGap;
                int eg = filteredBitmap[i][j].green % greenGap;
                int eb = filteredBitmap[i][j].blue % blueGap;
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
        return filteredImage;
    }
}
