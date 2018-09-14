import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.plaf.metal.MetalTextFieldUI;

public class Login extends JPanel implements ActionListener {

	public static final int WIDTH = 900;
	public static final int HEIGHT = 600;
	public static final int FRAME_MARGIN = 6;
	
	int posX = 0, posY = 0;
	
	MemberDAO memDAO;
	MemberDTO memDTO;

	JImageView imgBG;
	JLabel lblID, lblPW;
	JLabel lblTitle;
	JLabel lblMessage;
	JTextField txtID;
	JPasswordField txtPW;
	JButton btnLogin;
	JButton btnJoin;
	JButton btnRule;
	JPanel panelJoin;
	JPanel panelLogin;
	JImageView imgTitle;
	
	JTextField txtIP;
	
	public Login() {

		setBounds(0, 0, WIDTH, HEIGHT);
		setLayout(null);

		panelLogin = new JPanel();
		panelLogin.setLayout(null);
		panelLogin.setBackground(StaticColor.BACKGROUND);
		panelLogin.setBounds(580, 450, 310, 110);
		add(panelLogin);
		
		panelJoin = new InsertMemberFrame();
		panelJoin.setLocation(580, 50);
		panelJoin.setVisible(false);
		add(panelJoin);

		imgTitle = new JImageView("img/swing_title.png");
		imgTitle.setBounds(530, 50, 400, 400);
		add(imgTitle);

		lblID = new JLabel("ID", JLabel.RIGHT);
		lblID.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 14));
		lblID.setBounds(0, 5, 30, 20);
		lblID.setForeground(StaticColor.TEXT);
		panelLogin.add(lblID);

		lblPW = new JLabel("PW", JLabel.RIGHT);
		lblPW.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 14));
		lblPW.setBounds(0, 40, 30, 20);
		lblPW.setForeground(StaticColor.TEXT);
		panelLogin.add(lblPW);

		txtID = new JTextField(10);
		txtID.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 14));
		txtID.setBounds(lblID.getX() + 40, 0, 150, 30);
		txtID.setUI(new StyleTextFieldUI());
		panelLogin.add(txtID);

		txtPW = new JPasswordField();
		txtPW.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 14));
		txtPW.setBounds(lblPW.getX() + 40, 35, 150, 30);
		txtPW.setUI(new StylePasswordFieldUI());
		panelLogin.add(txtPW);

		btnLogin = new JButton("¿‘¿Â");
		btnLogin.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 14));
		btnLogin.setBounds(txtID.getX() + txtID.getWidth() + 10, txtID.getY(), 100, 65);
		btnLogin.addActionListener(this);
		btnLogin.setBackground(new Color(65, 175, 57));
		btnLogin.setUI(new StyleButtonUI());
		panelLogin.add(btnLogin);

		btnJoin = new JButton("»∏ø¯∞°¿‘");
		btnJoin.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 14));
		btnJoin.setBounds(btnLogin.getX(), btnLogin.getY() + btnLogin.getHeight() + 10, 100, 30);
		btnJoin.addActionListener(this);
		btnJoin.setBackground(new Color(67, 116, 217));
		btnJoin.setUI(new StyleButtonUI());
		panelLogin.add(btnJoin);

		txtIP = new JTextField(10);
		txtIP.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 14));
		txtIP.setBounds(50, 510, 150, 30);
		txtIP.setUI(new StyleTextFieldUI());
		txtIP.setText("127.0.0.1");
		add(txtIP);
		
		btnRule = new JButton("∞‘¿” ±‘ƒ¢");
		btnRule.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 14));
		btnRule.setBounds(430, txtIP.getY(), 100, 30);
		btnRule.addActionListener(this);
		btnRule.setForeground(Color.white);
		btnRule.setBackground(new Color(70, 70, 70));
		btnRule.setUI(new StyleButtonUI());
		add(btnRule);
		
		lblMessage = new JLabel("", SwingConstants.RIGHT);
		lblMessage.setBounds(lblPW.getX(), btnJoin.getY(), lblPW.getWidth() + txtPW.getWidth() + 10, 30);
		lblMessage.setForeground(Color.red);
		lblMessage.setFont(new Font("∏º¿∫ ∞ÌµÒ", Font.BOLD, 12));
		panelLogin.add(lblMessage);
		
		imgBG = new JImageView("img/bg_window.png");
		imgBG.setBounds(0, 0, WIDTH, HEIGHT);
		add(imgBG);
		
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnJoin) {
			DBConnect.IP = txtIP.getText();
			panelJoin.setVisible(true);
		} else if(e.getSource() == btnRule) {
			new RuleFrame();
		}
	}

}
