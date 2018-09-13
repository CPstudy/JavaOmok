import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicPanelUI;

import java.awt.*;

public class PlayerPanel extends JPanel {

	String name = "";

	JButton btnStart;
	JLabel lblName;
	JLabel lblScore;
	JImageView imgStone;

	PlayerPanel() {
		setLayout(null);
		setBackground(StaticColor.BACKGROUND);
		setUI(new StylePanelUI());

		imgStone = new JImageView("img/stone_none.png");
		imgStone.setBounds(10, 23, 25, 25);
		add(imgStone);

		lblName = new JLabel("", SwingConstants.CENTER);
		lblName.setBounds(imgStone.getX() + imgStone.getWidth() + 5, 10, 110, 50);
		lblName.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 20));
		lblName.setForeground(StaticColor.TEXT);
		add(lblName);

		lblScore = new JLabel("", SwingConstants.CENTER);
		lblScore.setBounds(0, 50, 150, 30);
		lblScore.setFont(new Font("¸¼Àº °íµñ", Font.BOLD, 12));
		lblScore.setForeground(StaticColor.TEXT);
		add(lblScore);

		btnStart = new JButton("Âü¿©ÇÏ±â");
		btnStart.setBounds(25, 90, 100, 40);
		btnStart.setFont(new Font("µ¸¿òÃ¼", Font.BOLD, 12));
		btnStart.setBackground(new Color(255, 187, 0));
		btnStart.setUI(new StyleButtonUI());
		add(btnStart);
	}

	public void setName(String name) {
		this.name = name;
		lblName.setText(name);
	}

	public void setScore(String score) {
		lblScore.setText(score);
	}

	public void setStone(int color) {
		if (color == 1) {
			imgStone.setImage("img/stone_black.png");
		} else if (color == 2) {
			imgStone.setImage("img/stone_white.png");
		}
	}
}
