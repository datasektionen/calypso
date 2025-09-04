package se.datasektionen.calypso.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
    public static BufferedImage convertFileToImage(MultipartFile file) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());

        if (image == null) {
            throw new IOException("File is not a valid image");
        }

        return image;
    }

    //TODO: tika only checks the header for information, might be ok?
    public static String getImageExtension(MultipartFile image) throws IOException{
        Tika tika = new Tika();
        
        return tika.detect(image.getBytes());
    }

    public static String getNameExtension(MultipartFile file) {
        var index = file.getOriginalFilename().lastIndexOf(".");

        var extension = index == -1 ? null : file.getOriginalFilename().substring(index+1);

        if (extension.equals("jpg")){
            extension = "jpeg";
        }
        
        return extension;
    }

    public static int[] getImageDimensions(BufferedImage image) {
        return new int[] { image.getWidth(), image.getHeight() };
    }
}
