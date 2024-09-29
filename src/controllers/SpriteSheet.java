package controllers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;

public class SpriteSheet {
    private BufferedImage sheet;
    private final int spriteWidth;
    private final int spriteHeight;

    public SpriteSheet(String path, int spriteWidth, int spriteHeight) {
        try {
            sheet = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
    }

    public BufferedImage getSprite(int col, int row) {
        // Ensure the requested sprite is within the bounds of the sprite sheet
        if (col < 0 || row < 0 || col * spriteWidth >= sheet.getWidth() || row * spriteHeight >= sheet.getHeight()) {
            throw new RasterFormatException("(x + width) or (y + height) is outside of Raster");
        }
        return sheet.getSubimage(col * spriteWidth, row * spriteHeight, spriteWidth, spriteHeight);
    }
}
