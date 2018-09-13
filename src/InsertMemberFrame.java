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

		JLabel lblTitle = new JLabel("ȸ������", SwingConstants.CENTER);
		lblTitle.setBounds(0, 50, WIDTH, 50);
		lblTitle.setFont(new Font("�ü�ü", Font.BOLD, 30));
		lblTitle.setForeground(StaticColor.TEXT);
		add(lblTitle);

		JLabel lblID = new JLabel("���̵�:", SwingConstants.LEFT);
		lblID.setBounds(28, 200, 90, 30);
		lblID.setForeground(StaticColor.TEXT);
		add(lblID);

		txtID = new JTextField(10);
		txtID.setBounds(lblID.getX() + lblID.getWidth() + 10, lblID.getY(), 150, 30);
		txtID.setUI(new StyleTextFieldUI());
		add(txtID);

		JLabel lblPW = new JLabel("��й�ȣ:");
		lblPW.setBounds(lblID.getX(), txtID.getY() + txtID.getHeight() + 10, lblID.getWidth(), lblID.getHeight());
		lblPW.setForeground(StaticColor.TEXT);
		add(lblPW);

		txtPW = new JPasswordField(10);
		txtPW.setBounds(lblPW.getX() + lblPW.getWidth() + 10, lblPW.getY(), txtID.getWidth(), txtID.getHeight());
		txtPW.setUI(new StylePasswordFieldUI());
		add(txtPW);

		JLabel lblPW2 = new JLabel("��й�ȣ Ȯ��:");
		lblPW2.setBounds(lblPW.getX(), lblPW.getY() + lblPW.getHeight() + 10, lblPW.getWidth(), lblPW.getHeight());
		lblPW2.setForeground(StaticColor.TEXT);
		add(lblPW2);

		txtPW2 = new JPasswordField(10);
		txtPW2.setBounds(lblPW.getX() + lblPW.getWidth() + 10, lblPW2.getY(), txtPW.getWidth(), txtPW.getHeight());
		txtPW2.setUI(new StylePasswordFieldUI());
		add(txtPW2);

		btnInsert = new JButton("ȸ������");
		btnInsert.setFont(new Font("���� ���", Font.BOLD, 10));
		btnInsert.setBounds(20, 330, 80, 30);
		btnInsert.addActionListener(this);
		btnInsert.setBackground(new Color(65, 175, 57));
		btnInsert.setUI(new StyleButtonUI());
		add(btnInsert);

		btnBack = new JButton("�ڷ� ����");
		btnBack.setFont(new Font("���� ���", Font.BOLD, 10));
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
		case "ȸ������": {
			String pw = new String(txtPW.getPassword());
			String pw2 = new String(txtPW2.getPassword());
			if (pw.equals(pw2)) {
				try {
					contentSet();
					int result = memDAO.insertmember(memDTO);
					if (result == MemberDAO.SUCCESS) {
						JOptionPane.showMessageDialog(this, "ȸ�����ԵǾ����ϴ�");
						setVisible(false);
					} else if (result == MemberDAO.ERROR_ID_EXIST){
						JOptionPane.showMessageDialog(this, "�ߺ��� ���̵��Դϴ�.");
					} else {
						JOptionPane.showMessageDialog(this, "�����Ͽ����ϴ�.");
					}
				} catch (HeadlessException e1) {
					JOptionPane.showMessageDialog(this, "���� �Է��Ͻʽÿ�");
				}
				break;
			} else {
				System.out.println("��й�ȣ�� ��ġ���� �ʽ��ϴ�");
			}

			break;
		}

		case "�ڷ� ����": {
			setVisible(false);
			break;
		}

		case "����": {
			setVisible(false);
		}
		}

	}
}
