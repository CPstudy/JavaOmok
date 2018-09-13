import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Sammok extends JFrame implements ActionListener {
	
	public static final int WIDTH = 400;
	public static final int HEIGHT = 400;
	
	JButton[] btn = new JButton[9];
	
	boolean boolPlayer = false;
	
	Sammok() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		int axisX = (int) (width / 2) - (WIDTH / 2);
		int axisY = (int) (height / 2) - (HEIGHT / 2);
		
		setSize(WIDTH, HEIGHT);
		setLayout(new GridLayout(3, 3, 10, 10));
		setLocation(axisX, axisY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		for(int i = 0; i < 9; i++) {
			btn[i] = new JButton();
			btn[i].addActionListener(this);
			btn[i].setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 30));
			btn[i].setBackground(new Color(67, 116, 217));
			btn[i].setUI(new StyleButtonUI());
			add(btn[i]);
		}
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(int i = 0; i < 9; i++) {
			if(btn[i] == e.getSource()) {
				boolPlayer = !boolPlayer;
				if(boolPlayer) {
					btn[i].setText("O");
					btn[i].setEnabled(false);
				} else {
					btn[i].setText("X");
					btn[i].setEnabled(false);
				}
			}
		}
	}
}
