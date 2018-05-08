package util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class Mat2BufImg {

        /**
         * Mat转换成BufferedImage
         * 
         * @param matrix
         *            要转换的Mat
         * @param fileExtension
         *            格式为 ".jpg", ".png", etc
         * @return
         */
        public static BufferedImage Mat2BufImg (Mat matrix, String fileExtension) {
            // convert the matrix into a matrix of bytes appropriate for
            // this file extension
            MatOfByte mob = new MatOfByte();
            Imgcodecs.imencode(fileExtension, matrix, mob);
            // convert the "matrix of bytes" into a byte array
            byte[] byteArray = mob.toArray();
            BufferedImage bufImage = null;
            try {
                InputStream in = new ByteArrayInputStream(byteArray);
                bufImage = ImageIO.read(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bufImage;
        }

        /**
         * BufferedImage转换成Mat
         * 
         * @param original
         *            要转换的BufferedImage
         * @param imgType
         *            bufferedImage的类型 如 BufferedImage.TYPE_3BYTE_BGR
         * @param matType
         *            转换成mat的type 如 CvType.CV_8UC3
         */
        public static Mat BufImg2Mat (BufferedImage original, int imgType, int matType) {
            if (original == null) {
                throw new IllegalArgumentException("original == null");
            }

            // Don't convert if it already has correct type
            if (original.getType() != imgType) {

                // Create a buffered image
                BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), imgType);

                // Draw the image onto the new buffer
                Graphics2D g = image.createGraphics();
                try {
                    g.setComposite(AlphaComposite.Src);
                    g.drawImage(original, 0, 0, null);
                } finally {
                    g.dispose();
                }
            }

            byte[] pixels = ((DataBufferByte) original.getRaster().getDataBuffer()).getData();
            Mat mat = Mat.eye(original.getHeight(), original.getWidth(), matType);
            mat.put(0, 0, pixels);
            return mat;
        }
        public static BufferedImage mat2BI(Mat mat){  
            int dataSize =mat.cols()*mat.rows()*(int)mat.elemSize();  
            byte[] data=new byte[dataSize];  
            mat.get(0, 0,data);  
            int type=mat.channels()==1?  
                    BufferedImage.TYPE_BYTE_GRAY:BufferedImage.TYPE_3BYTE_BGR;  
              
            if(type==BufferedImage.TYPE_3BYTE_BGR){  
                for(int i=0;i<dataSize;i+=3){  
                    byte blue=data[i+0];  
                    data[i+0]=data[i+2];  
                    data[i+2]=blue;  
                }  
            }  
            BufferedImage image=new BufferedImage(mat.cols(),mat.rows(),type);  
            image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);  
              
            return image;  
        }  
        
    }