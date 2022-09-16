package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Converter implements TextGraphicsConverter {
    protected TextColorSchema schema = new Schema();
    private double maxRatio;
    private int maxWidth;
    private int maxHeight;


    @Override
    public String convert(String url) throws IOException, BadImageSizeException {

        BufferedImage img = ImageIO.read(new URL(url));
        int newWidth = img.getWidth();
        int newHeight = img.getHeight();
        ratioCheck(maxRatio, newWidth, newHeight);


        int[] formatSize = imageSizeRatio(newWidth, newHeight);
        newWidth = formatSize[0];
        newHeight = formatSize[1];
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);


        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);

        ImageIO.write(bwImg, "png", new File("out.png"));

        WritableRaster bwRaster = bwImg.getRaster();

        StringBuilder imgToTxt = new StringBuilder();


        for (int h = 0; h < formatSize[1]; h++) {
            for (int w = 0; w < formatSize[0]; w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];

                char c = schema.convert(color);
                imgToTxt.append(c);
                imgToTxt.append(c);

            }

            imgToTxt.append("\n");
        }
        return imgToTxt.toString();
    }


    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;

    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }

    public void ratioCheck(double maxRatio, int width, int height) throws BadImageSizeException {
        int ratio1 = width / height;
        int ratio2 = height / width;
        double ratio = Math.max((double) ratio1, ratio2);
        if (ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }
    }

    public int[] imageSizeRatio(int width, int height) {
        if (width < maxWidth && height < maxHeight) {
            return new int[]{width, height};
        } else {
            int maxSize = Math.max(height, width);
            double coefficient = (double) maxSize / maxHeight;
            double w = (double) width / coefficient;
            double h = (double) height / coefficient;
            width = (int) w;
            height = (int) h;
        }
        return new int[]{width, height};

    }

}
