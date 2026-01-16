package com.firstproject.telfat_w_lqina.util;

import javafx.scene.image.Image;

import java.io.*;

public class ImageConverterUtil {
    // Convert image to byte pour stocker dans la base de donnee car on ne peut pas stocker une image directement
    public static  byte[] convertImageToByte(File file) throws IOException {
        FileInputStream openFile = null;
        try {
            openFile = new FileInputStream(file);
            byte[]data = new byte[(int) file.length()];
            openFile.read(data);
            return data;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (openFile != null) {
                openFile.close();
            }
        }
    }

    // Convert byte to image pour afficher l'image a partir de la base de donnee
    public static Image convertByteToImage(byte[] imageData){

        if (imageData.length==0){
            return null;
        }
        ByteArrayInputStream openByte = new ByteArrayInputStream(imageData);
        return new Image(openByte);
    }
}
