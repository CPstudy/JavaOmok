import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class InsertMemberFrame extends JPanel implements ActionListener {

	public static final int WIDTH = 300;
	public static final int HEIGHT = 380;

	MemberDAO memDAO;
	MemberDTO memDTO;
	JTextField txtID;
	JPasswordField txtPW, txtPW2;
	JButton btnInsert, btnBack;

	InsertMemberFrame() {

		memDAO = new MemberDAO();
		setLayout(null);
		setBackground(StaticColor.BACKGROUND);
		setSize(WIDTH, HEIGHT);

		JLabel lblTitle = new JLabel("회원가입", SwingConstants.CENTER);
		lblTitle.setBounds(0, 50, WIDTH, 50);
		lblTitle.setFont(new Font("궁서체", Font.BOLD, 30));
		lblTitle.setForeground(StaticColor.TEXT);
		add(lblTitle);

		JLabel lblID = new JLabel("아이디:", SwingConstants.LEFT);
		lblID.setBounds(28, 200, 90, 30);
		lblID.setForeground(StaticColor.TEXT);
		add(lblID);

		txtID = new JTextField(10);
		txtID.setBounds(lblID.getX() + lblID.getWidth() + 10, lblID.getY(), 150, 30);
		txtID.setUI(new StyleTextFieldUI());
		add(txtID);

		JLabel lblPW = new JLabel("비밀번호:");
		lblPW.setBounds(lblID.getX(), txtID.getY() + txtID.getHeight() + 10, lblID.getWidth(), lblID.getHeight());
		lblPW.setForeground(StaticColor.TEXT);
		add(lblPW);

		txtPW = new JPasswordField(10);
		txtPW.setBounds(lblPW.getX() + lblPW.getWidth() + 10, lblPW.getY(), txtID.getWidth(), txtID.getHeight());
		txtPW.setUI(new StylePasswordFieldUI());
		add(txtPW);

		JLabel lblPW2 = new JLabel("비밀번호 확인:");
		lblPW2.setBounds(lblPW.getX(), lblPW.getY() + lblPW.getHeight() + 10, lblPW.getWidth(), lblPW.getHeight());
		lblPW2.setForeground(StaticColor.TEXT);
		add(lblPW2);

		txtPW2 = new JPasswordField(10);
		txtPW2.setBounds(lblPW.getX() + lblPW.getWidth() + 10, lblPW2.getY(), txtPW.getWidth(), txtPW.getHeight());
		txtPW2.setUI(new StylePasswordFieldUI());
		add(txtPW2);

		btnInsert = new JButton("회원가입");
		btnInsert.setFont(new Font("맑은 고딕", Font.BOLD, 10));
		btnInsert.setBounds(20, 330, 80, 30);
		btnInsert.addActionListener(this);
		btnInsert.setBackground(new Color(65, 175, 57));
		btnInsert.setUI(new StyleButtonUI());
		add(btnInsert);

		btnBack = new JButton("뒤로 가기");
		btnBack.setFont(new Font("맑은 고딕", Font.BOLD, 10));
		btnBack.setBounds(btnInsert.getX() + btnInsert.getWidth() + 10, btnInsert.getY(), 80, 30);
		btnBack.addActionListener(this);
		btnBack.setBackground(new Color(255, 187, 0));
		btnBack.setUI(new StyleButtonUI());
		add(btnBack);

		add(btnInsert);
		add(btnBack);
		setVisible(true);
	}

	private void contentSet() {
		// TODO Auto-generated method stub
		memDTO = new MemberDTO();
		memDTO.setId(txtID.getText().toString());
		memDTO.setPw(new String(txtPW.getPassword()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String buttonText = e.getActionCommand();
		switch (buttonText) {
		case "회원가입": {
			String pw = new String(txtPW.getPassword());
			String pw2 = new String(txtPW2.getPassword());
			if (pw.equals(pw2)) {
				try {
					contentSet();
					int result = memDAO.insertmember(memDTO);
					if (result == MemberDAO.SUCCESS) {
						JOptionPane.showMessageDialog(this, "회원가입되었습니다");
						setVisible(false);
					} else if (result == MemberDAO.ERROR_ID_EXIST){
						JOptionPane.showMessageDialog(this, "중복된 아이디입니다.");
					} else {
						JOptionPane.showMessageDialog(this, "실패하였습니다.");
					}
				} catch (HeadlessException e1) {
					JOptionPane.showMessageDialog(this, "값을 입력하십시오");
				}
				break;
			} else {
				System.out.println("비밀번호가 일치하지 않습니다");
			}

			break;
		}

		case "뒤로 가기": {
			setVisible(false);
			break;
		}

		case "종료": {
			setVisible(false);
		}
		}

	}
}
