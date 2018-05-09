package ocvday01;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.
                    BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import util.Mat2BufImg;

public class WebcamQRDemo extends JFrame implements Runnable, ThreadFactory,WebcamImageTransformer {

	
	private static final long serialVersionUID =6441489157408381878L;
	private Executor executor =Executors.newSingleThreadExecutor(this);

    private Webcam webcam = null;
    private WebcamPanel wpanel = null;
    private JTextArea textarea = null;
    private JLabel iconLabel=null;
    
    static{  
        // 载入opencv的库  
        String opencvpath = System.getProperty("user.dir") + "\\opencv\\";  
        String opencvDllName = opencvpath + Core.NATIVE_LIBRARY_NAME + ".dll";  
        System.load(opencvDllName);  
    }  
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new WebcamQRDemo();
    }
    public WebcamQRDemo() {
        super();

        setLayout(new FlowLayout());
        setTitle("Read QR / Bar Code With Webcam");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Dimension size = WebcamResolution.QVGA.getSize();

        webcam = Webcam.getWebcams().get(0);
        webcam.setImageTransformer(this);
        webcam.setViewSize(size);

        wpanel = new WebcamPanel(webcam);
        wpanel.setPreferredSize(size);

        textarea = new JTextArea();
        textarea.setEditable(false);
        textarea.setPreferredSize(size);
        
        iconLabel = new JLabel();
        iconLabel.setPreferredSize(size);
        
        
        add(wpanel);
        add(textarea);
        add(iconLabel);
        pack();

        setVisible(true);
        executor.execute(this);
    }
    @Override
    public Thread newThread(Runnable r) {
        // TODO Auto-generated method stub
        Thread t = new Thread(r, "example-runner");
        t.setDaemon(true);
        return t;
    }
    @Override
    public void run() {
        // TODO Auto-generated method stub
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Result result = null;
            BufferedImage image = null;
            if (webcam.isOpen()) {
              if ((image = webcam.getImage()) == null) {
                    continue;
                }
              	//iconlabel
	            ImageIcon icon=new ImageIcon(image);
	            icon.setImage(icon.getImage().getScaledInstance(300,300,Image.SCALE_DEFAULT));
	            iconLabel.setIcon(icon); 
	            
              	//qrcode
                LuminanceSource source =new BufferedImageLuminanceSource( image) ;
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    result = new MultiFormatReader().decode(bitmap);
                } catch (NotFoundException e) {
                // fall thru, it means there is no QR code in image
                }
            }
            if (result != null) {
                textarea.setText(result.getText());
                System.out.println(result.getText());
            }

        } while (true);
    }
	@Override
	public BufferedImage transform(BufferedImage image){
		// TODO Auto-generated method stub
		//bufferimage --> mat
		Mat mat = Mat2BufImg.BufImg2Mat(image, BufferedImage.TYPE_3BYTE_BGR,  CvType.CV_8UC3);
		//人脸识别
		try {
			//mat --> bufferimage
			return Mat2BufImg.Mat2BufImg(detectFace(mat),".png");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return image;
	}
	
	public static Mat detectFace(Mat img) throws Exception
    {

        System.out.println("Running DetectFace ... ");
        // 从配置文件lbpcascade_frontalface.xml中创建一个人脸识别器，该文件位于opencv安装目录中
        //CascadeClassifier faceDetector = new CascadeClassifier("D:\\TDDOWNLOAD\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml");
        CascadeClassifier faceDetector = new CascadeClassifier(  
                "D:\\opencv\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml"); 

        // 在图片中检测人脸
        MatOfRect faceDetections = new MatOfRect();

        faceDetector.detectMultiScale(img, faceDetections);

        //System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

        Rect[] rects = faceDetections.toArray();
        if(rects != null && rects.length >= 1){          
            for (Rect rect : rects) {  
                Imgproc.rectangle(img, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),  
                        new Scalar(0, 0, 255), 2);  
            } 
        }
        return img;
    }
	
	/*private BufferedImage mat2BI(Mat mat){  
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
    }  */
	
}