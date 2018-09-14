import java.awt.*;
import javax.swing.*;

public class PopupRule extends JPanel {
	
	JLabel2D lblMessage;
	
	PopupRule() {
		setSize(300, 100);
		setBackground(new Color(0, 0, 0, 0));
		setOpaque(true);
		lblMessage = new JLabel2D("이곳에는 놓을 수 없습니다.");
		lblMessage.setFont(new Font("궁서체", Font.BOLD, 12));
		lblMessage.setForeground(Color.white);
		lblMessage.setOutlineColor(Color.black);
		lblMessage.setStroke(new BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
		add("Center", lblMessage);
		
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(3000);
					setVisible(false);
				} catch(Exception e) {
					
				}
			}
		});
		thread.start();
		setVisible(true);
	}
}
