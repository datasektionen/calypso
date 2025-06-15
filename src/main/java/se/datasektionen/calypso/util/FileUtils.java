package se.datasektionen.calypso.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
     public static String getFileExtension(MultipartFile file) {
        //TODO: should grab file extension from file headers are set the same as extension to avoid easy bypass?
        //don't know what to do with trojans
        var index = file.getOriginalFilename().lastIndexOf(".");

        var extension = index == -1 ? null : file.getOriginalFilename().substring(index);

        return extension;
    }

    public static int[] getImageDimensions(MultipartFile file) throws IOException{
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) {
            throw new IOException("File is not a valid image");
        }
        System.out.println(image.getWidth());
        System.out.println(image.getHeight());
        return new int[] { image.getWidth(), image.getHeight() };
    }
}
