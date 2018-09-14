import java.awt.*;
import javax.swing.*;

public class RuleFrame extends JFrame {
	
	public static final int WIDTH = 600;
	public static final int HEIGHT = 530;
	
	RuleFrame() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		int axisX = (int) (width / 2) - (WIDTH / 2);
		int axisY = (int) (height / 2) - (HEIGHT / 2);
		
		setTitle("게임 규칙");
		setSize(WIDTH, HEIGHT);
		setLayout(null);
		setLocation(axisX, axisY);
		setResizable(false);
		
		JImageView imgBG = new JImageView("img/bg_rule.png");
		imgBG.setBounds(0, 0, WIDTH, HEIGHT);
		imgBG.setAutoScale(true);
		add(imgBG);
		
		setVisible(true);
	}
}
