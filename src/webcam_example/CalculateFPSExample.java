package webcam_example;

import com.github.sarxos.webcam.Webcam;

/**
 * 计算FPS
 * @author Shensg
 * 2018年5月8日
 */
public class CalculateFPSExample {

	public static void main(String[] args) {

		long t1 = 0;
		long t2 = 0;

		int p = 10;
		int r = 5;

		Webcam webcam = Webcam.getDefault();

		for (int k = 0; k < p; k++) {

			webcam.open();
			webcam.getImage();

			t1 = System.currentTimeMillis();
			for (int i = 0; ++i <= r; webcam.getImage()) {
			}
			t2 = System.currentTimeMillis();

			System.out.println("FPS " + k + ": " + (1000 * r / (t2 - t1 + 1)));

			webcam.close();
		}

	}
}