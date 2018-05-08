package webcam_example;

import javax.swing.JFrame;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

/**
 * 图像充满panel
 * @author Shensg
 * 2018年5月8日
 */
public class WebcamPanelFillAreaExample {

	public static void main(String[] args) throws InterruptedException {

		Webcam webcam = Webcam.getDefault();

		WebcamPanel panel = new WebcamPanel(webcam);
		panel.setFPSDisplayed(true);
		panel.setFillArea(true);

		JFrame window = new JFrame("Test webcam panel");
		window.add(panel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}
}