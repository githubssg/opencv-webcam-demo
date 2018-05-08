
/**
 * 获取默认摄像头名称
 * @author Shensg
 * 2018年5月8日
 */
package webcam_example;

import com.github.sarxos.webcam.Webcam;

public class DetectWebcamExample {

	public static void main(String[] args) {
		Webcam webcam = Webcam.getDefault();
		if (webcam != null) {
			System.out.println("Webcam: " + webcam.getName());
		} else {
			System.out.println("No webcam detected");
		}
	}
}