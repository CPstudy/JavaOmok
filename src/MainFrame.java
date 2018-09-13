import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.plaf.metal.MetalButtonUI;

class OmokFrame extends JFrame implements MouseListener, ActionListener, KeyListener, Runnable {

	public static final int WIDTH = 900;
	public static final int HEIGHT = 600;
	public static final int FRAME_MARGIN = 6;
	public static final int STONE_SIZE = 35;
	public static final int BLACK = 1;
	public static final int WHITE = 2;

	int[][] stones = new int[15][15];
	int posX = 0, posY = 0;
	int stone = 0;

	static int turnPoint = 0; // 자신의 턴인지 아닌지 표시
	String stonePoint[] = new String[3]; // 0:돌 색깔, 1:x좌표, 2:y좌표

	boolean boolColor = false;
	boolean winFlag = false;

	String userID;
	String firstPlayer;

	private InetAddress ia;
	private Socket soc;
	private PrintWriter pw;
	private BufferedReader in;
	private Thread playThread;

	JImageView imgBG;
	JLabel imgCursor;
	JImageView imgPoint;
	JImageButton btnClose;
	JImageButton btnMinimize;
	JImageButton[][] imgStone = new JImageButton[15][15];
	Login panelLogin;
	JPanel panelGame;
	JPanel panelTitle;
	JPanel panelStone;
	JPanel panelPoint;
	JPanel panelChatting;
	PlayerPanel panelPlayer1;
	PlayerPanel panelPlayer2;
	JButton btnPlayer1;
	JButton btnPlayer2;
	JButton btnSend;
	JButton btnLogin;
	JLabel lblTitle;
	JLabel lblMessage;
	JLabel lblCount;
	JScrollPane scrollPane;
	JScrollBar scrollBar;
	JScrollPane scrollUser;
	JTextArea txtChatting;
	JTextArea txtUserList;
	JTextField txtMessage;

	OmokFrame(String userID) {
		this.userID = userID;

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		int axisX = (int) (width / 2) - (WIDTH / 2);
		int axisY = (int) (height / 2) - (HEIGHT / 2);

		setTitle(userID);
		setSize(WIDTH, HEIGHT);
		setLayout(null);
		setLocation(axisX, axisY);
		setUndecorated(true);
		// getContentPane().setBackground(new Color(11, 121, 3, 0));
		setResizable(false);

		panelTitle = new JPanel();
		panelTitle.setLayout(null);
		panelTitle.setBounds(1, 1, WIDTH - 2, 35);
		panelTitle.setBackground(new Color(220, 220, 220));
		panelTitle.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				posX = e.getX();
				posY = e.getY();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				setLocation(e.getXOnScreen() - posX, e.getYOnScreen() - posY);
			}
		});
		add(panelTitle);

		lblTitle = new JLabel("오목");
		lblTitle.setBounds(FRAME_MARGIN + 12, (30 / 2) - 10, 50, 30);
		lblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		panelTitle.add(lblTitle);

		btnClose = new JImageButton("img/btn_close.png");
		btnClose.setBounds(862, 5, 25, 30);
		btnClose.setBackground(new Color(0, 0, 0, 0));
		btnClose.addMouseListener(this);
		btnClose.addActionListener(this);
		panelTitle.add(btnClose);

		btnMinimize = new JImageButton("img/btn_min.png");
		btnMinimize.setBounds(btnClose.getX() - 25, btnClose.getY(), 25, 30);
		btnMinimize.setBackground(new Color(0, 0, 0, 0));
		btnMinimize.addMouseListener(this);
		btnMinimize.addActionListener(this);
		panelTitle.add(btnMinimize);

		panelLogin = new Login();
		add(panelLogin);

		btnLogin = panelLogin.btnLogin;
		btnLogin.addActionListener(this);

		panelLogin.txtID.addKeyListener(this);
		panelLogin.txtPW.addKeyListener(this);

		panelGame = new JPanel();
		panelGame.setLayout(null);
		panelGame.setBounds(0, 0, WIDTH, HEIGHT);
		panelGame.setBackground(new Color(0, 0, 0, 0));
		panelGame.setVisible(false);
		add(panelGame);

		panelPoint = new JPanel();
		panelPoint.setLayout(null);
		panelPoint.setBounds(20, 40, 540, 540);
		panelPoint.setOpaque(false);
		panelPoint.setBackground(new Color(0, 0, 0, 0));
		panelGame.add(panelPoint);

		panelStone = new JPanel();
		panelStone.setLayout(new GridLayout(15, 15));
		panelStone.setBounds(20, 40, 540, 540);
		panelStone.setOpaque(false);
		panelStone.setBackground(new Color(0, 0, 0, 0));
		panelGame.add(panelStone);

		imgPoint = new JImageView("img/point_red.png");
		imgPoint.setBounds(0, 0, STONE_SIZE, STONE_SIZE);
		imgPoint.setVisible(false);
		panelPoint.add(imgPoint);

		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				imgStone[i][j] = new JImageButton(null);
				// imgStone[i][j].setSize(STONE_SIZE, STONE_SIZE);
				// imgStone[i][j].setBackground(new Color(0, 0, 0, 0));
				imgStone[i][j].setUI(new StyleTransparentButtonUI());
				imgStone[i][j].addMouseListener(this);
				imgStone[i][j].addActionListener(this);
				panelStone.add(imgStone[i][j]);
			}
		}

		txtUserList = new JTextArea(4, 4);
		txtUserList.setBounds(570, 200, 310, 95);
		txtUserList.setBackground(Color.white);
		txtUserList.setUI(new StyleTextAreaUI());
		txtUserList.setRows(4);
		txtUserList.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		txtUserList.setLineWrap(true);
		txtUserList.setWrapStyleWord(true);
		txtUserList.setEditable(false);
		panelGame.add(txtUserList);

		lblCount = new JLabel("헌재 접속자: 30명");
		lblCount.setBounds(txtUserList.getX(), txtUserList.getY() + txtUserList.getHeight() + 5, 310, 15);
		lblCount.setFont(new Font("맑은 고딕", Font.BOLD, 12));
		panelGame.add(lblCount);

		panelChatting = new JPanel();
		panelChatting.setLayout(null);
		panelChatting.setBackground(new Color(220, 220, 220));
		panelChatting.setBounds(lblCount.getX(), lblCount.getY() + lblCount.getHeight() + 5, 310, 250);
		panelGame.add(panelChatting);

		btnSend = new JButton("전송");
		btnSend.setBounds(250, 215, 60, 35);
		btnSend.setFont(new Font("돋움체", Font.PLAIN, 12));
		btnSend.setForeground(Color.white);
		btnSend.setBackground(new Color(67, 116, 217));
		btnSend.setUI(new StyleButtonUI());
		btnSend.addActionListener(this);
		panelChatting.add(btnSend);

		txtMessage = new JTextField();
		txtMessage.setBounds(0, btnSend.getY(), 245, 35);
		txtMessage.setFont(new Font("돋움체", Font.PLAIN, 12));
		txtMessage.setUI(new StyleTextFieldUI());
		txtMessage.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
		txtMessage.addKeyListener(this);
		panelChatting.add(txtMessage);

		txtChatting = new JTextArea(4, 4);
		txtChatting.setBackground(Color.white);
		txtChatting.setUI(new StyleTextAreaUI());
		txtChatting.setRows(4);
		txtChatting.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
		txtChatting.setLineWrap(true);
		txtChatting.setWrapStyleWord(true);
		txtChatting.setEditable(false);

		scrollPane = new JScrollPane(txtChatting, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0, 0, 310, 210);
		scrollPane.setBackground(new Color(0, 0, 0, 0));
		scrollPane.setUI(new BasicScrollPaneUI());
		panelChatting.add(scrollPane);

		scrollBar = scrollPane.getVerticalScrollBar();
		scrollBar.setUI(new StyleScrollBarUI());

		panelPlayer1 = new PlayerPanel();
		panelPlayer1.setBounds(570, 50, 150, 140);
		panelPlayer1.setStone(BLACK);
		panelGame.add(panelPlayer1);

		panelPlayer2 = new PlayerPanel();
		panelPlayer2.setBounds(730, 50, 150, 140);
		panelPlayer2.setStone(WHITE);
		panelGame.add(panelPlayer2);

		btnPlayer1 = panelPlayer1.btnStart;
		btnPlayer2 = panelPlayer2.btnStart;
		btnPlayer1.addActionListener(this);
		btnPlayer2.addActionListener(this);

		imgBG = new JImageView("img/bg_window.png");
		imgBG.setBounds(0, 0, WIDTH, HEIGHT);
		panelGame.add(imgBG);

		customCursor();

		Vector<Component> order = new Vector<Component>();
		order.add(panelLogin.txtID);
		order.add(panelLogin.txtPW);

		setFocusTraversalPolicy(new MyOwnFocusTraversalPolicy(order));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void customCursor() {
		ImageIcon icon;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		try {
			icon = new ImageIcon(getClass().getResource("img/cursor.png"));
		} catch (Exception e) {
			icon = new ImageIcon("img/cursor.png");
		}

		Image image = icon.getImage();

		Point hotspot = new Point(2, 2);
		Cursor cursor = toolkit.createCustomCursor(image, hotspot, "cursor");
		setCursor(cursor);
	}

	public void changeStones(int color, int i, int j) {
		imgPoint.setVisible(true);
		stones[i][j] = color;

		try {
			if (color == 1) {
				imgStone[i][j].setImage("img/stone_black.png");
				imgPoint.setImage("img/point_orange.png");
				imgPoint.setLocation(imgStone[i][j].getLocation().x, imgStone[i][j].getLocation().y);
				imgStone[i][j].removeActionListener(this);
				imgStone[i][j].removeMouseListener(this);
				checkStones(color, i, j);
			} else if (color == 2) {
				imgStone[i][j].setImage("img/stone_white.png");
				imgPoint.setImage("img/point_red.png");
				imgPoint.setLocation(imgStone[i][j].getLocation().x, imgStone[i][j].getLocation().y);
				imgStone[i][j].removeActionListener(this);
				imgStone[i][j].removeMouseListener(this);
				checkStones(color, i, j);
			} else if (color == 0) {
				imgStone[i][j].setImage(null);
				imgPoint.setImage("img/point_red.png");
				imgPoint.setLocation(imgStone[i][j].getLocation().x, imgStone[i][j].getLocation().y);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void winOmok(ArrayList<Stones> list) throws Exception {
		int x;
		int y;

		for (int i = 0; i < list.size(); i++) {
			x = list.get(i).x;
			y = list.get(i).y;

			if (stone == 1) {
				imgStone[x][y].setImage("img/stone_black_circle.png");
			} else if (stone == 2) {
				imgStone[x][y].setImage("img/stone_white_circle.png");
			}
		}

		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				imgStone[i][j].removeMouseListener(this);
			}
		}

		// 게임 종료
		txtChatting.append("************************************\n");
		txtChatting.append("> 알림: 게임 종료\n");
		if (stone == 1) {
			txtChatting.append("> 알림: " + panelPlayer1.lblName.getText() + "님이 승리하였습니다\n");
			scrollBar.setValue(scrollBar.getMaximum());
		} else {
			txtChatting.append("> 알림: " + panelPlayer2.lblName.getText() + "님이 승리하였습니다\n");
			scrollBar.setValue(scrollBar.getMaximum());
		}

		System.err.println("게임 승리");

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					pw.println("gg");
					pw.flush();

					gameEnd();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	public void login() {
		DBConnect.IP = panelLogin.txtIP.getText();

		if (panelLogin.txtID.getText().equals("") || panelLogin.txtPW.getText().equals("")) {
			panelLogin.lblMessage.setText("값을 입력해주세요.");
			return;
		}
		try {
			MemberDAO memDAO = new MemberDAO();
			MemberDTO memDTO = new MemberDTO();

			memDTO.setId(panelLogin.txtID.getText().toString());
			memDTO.setPw(panelLogin.txtPW.getText().toString());

			boolean result = memDAO.login(memDTO);
			if (result == true) {
				userID = memDTO.id;
				lblTitle.setText(userID);
				gamePlay();
			} else {
				panelLogin.lblMessage.setText("아이디 또는 비밀번호 오류");
			}
		} catch (HeadlessException e1) {
			panelLogin.lblMessage.setText("값을 입력해주세요.");
		}
	}

	public void gamePlay() {
		panelGame.setVisible(true);
		panelLogin.setVisible(false);

		try {
			ia = InetAddress.getByName(DBConnect.IP);
			soc = new Socket(ia, 20000);
			txtChatting.setText("> 알림: " + "서버 접속 성공!!\n");
			scrollBar.setValue(scrollBar.getMaximum());
			pw = new PrintWriter(soc.getOutputStream(), true);
			pw.println("@" + userID);

			pw.flush();

			in = new BufferedReader(new InputStreamReader(soc.getInputStream()));

			playThread = new Thread(this);
			playThread.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void gameEnd() throws Exception {

		txtChatting.append("> 알림: " + "3초 후에 판이 초기화 됩니다\n");
		scrollBar.setValue(scrollBar.getMaximum());

		Thread.sleep(1000);

		firstPlayer = null;
		panelPlayer1.setName("");
		panelPlayer1.name = "";
		panelPlayer2.setName("");
		panelPlayer2.name = "";

		turnPoint = 0;

		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				stones[i][j] = 0;
				imgStone[i][j].setImage("");
				imgStone[i][j].removeActionListener(this);
				imgStone[i][j].removeMouseListener(this);
				imgStone[i][j].addActionListener(this);
				imgStone[i][j].addMouseListener(this);
				imgPoint.setVisible(false);
			}
		}

		btnPlayer1.setEnabled(true);
		btnPlayer2.setEnabled(true);
	}

	public void sendServer() {
		pw.println(userID + ":" + txtMessage.getText());
		pw.flush();
		txtMessage.setText("");
		scrollBar.setValue(scrollBar.getMaximum());
	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (turnPoint == 1) {
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 15; j++) {
					if (e.getSource() == imgStone[i][j]) {
						if (!boolColor) {
							imgStone[i][j].setImage("img/stone_white_transparent.png");
						} else {
							imgStone[i][j].setImage("img/stone_black_transparent.png");
						}
					}
				}
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				if (e.getSource() == imgStone[i][j]) {
					imgStone[i][j].setImage(null);
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnClose) {
			if (soc != null) {
				pw.println("close " + userID);
				pw.flush();
			}
			System.exit(0);
		} else if (e.getSource() == btnMinimize) {
			setState(JFrame.ICONIFIED);
		} else if (e.getSource() == btnSend) {
			sendServer();
		} else if (e.getSource() == btnLogin) {
			login();
		} else if (e.getSource() == btnPlayer1) {
			pw.println("*btn1" + userID);
			pw.flush();
			btnPlayer2.setEnabled(false);
		} else if (e.getSource() == btnPlayer2) {
			pw.println("*btn2" + userID);
			pw.flush();
			btnPlayer1.setEnabled(false);
		} else {
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 15; j++) {
					if (e.getSource() == imgStone[i][j]) {

						if (turnPoint == 1) {
							imgPoint.setVisible(true);

							// boolColor = !boolColor;

							try {
								if (!boolColor) {
									imgStone[i][j].setImage("img/stone_white.png");

									if (!checkStones(WHITE, i, j)) {
										imgPoint.setImage("img/point_red.png");
										imgPoint.setLocation(imgStone[i][j].getLocation().x,
												imgStone[i][j].getLocation().y);

										// 클릭한 돌의 색, x좌표, y좌표 값 넣기 '#돌의 색/x좌표/y좌표' 형식으로 메시지 전송
										System.out.println(userID);
										pw.println("#2/" + i + "/" + j);
										pw.flush();
									}
								} else {
									imgStone[i][j].setImage("img/stone_black.png");

									if (!checkStones(BLACK, i, j)) {
										imgPoint.setImage("img/point_orange.png");
										imgPoint.setLocation(imgStone[i][j].getLocation().x,
												imgStone[i][j].getLocation().y);

										// 클릭한 돌의 색, x좌표, y좌표 값 넣기 '#돌의 색/x좌표/y좌표' 형식으로 메시지 전송
										System.out.println(userID);
										pw.println("#1/" + i + "/" + j);
										pw.flush();
									}
								}
								imgStone[i][j].removeActionListener(this);
								imgStone[i][j].removeMouseListener(this);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						} else {

						}
					}
				}
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (e.getSource() == txtMessage) {
				sendServer();
			} else if (e.getSource() == panelLogin.txtID || e.getSource() == panelLogin.txtPW) {
				login();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			String msg = "";
			String[] names;

			while (true) {
				msg = in.readLine();
				System.out.println("msg = " + msg);
				if (msg.split(" ")[0].equals("/user")) {
					txtUserList.setText("");
					names = msg.substring(6, msg.length()).split(" ");
					lblCount.setText("현재 접속자: " + names.length + "명");
					for (String name : names) {
						txtUserList.append(name);
						txtUserList.append("\n");
					}
				} else if (msg.charAt(0) == '*') {
					if (msg.charAt(1) == '1') {
						panelPlayer1.setName(msg.substring(2));
						panelPlayer1.setScore(MemberDAO.getRate(msg.substring(2)));
						btnPlayer1.setEnabled(false);
					} else if (msg.charAt(1) == '2') {
						panelPlayer2.setName(msg.substring(2));
						panelPlayer2.setScore(MemberDAO.getRate(msg.substring(2)));
						btnPlayer2.setEnabled(false);
					} else if (msg.charAt(1) == 'f') {
						firstPlayer = msg.substring(2);

						if (firstPlayer.equals(userID)) {
							boolColor = true;
						} else {
							boolColor = false;
						}

					}
				} else if (msg.charAt(0) == '!') { // 전체 플레이어어게 공지, 게임시작
					// txtChatting.append(msg + "\n");
					if (msg.charAt(1) == '!') { // 게임 시작 메시지 다이얼로그로 나옴
						JOptionPane.showMessageDialog(this, msg.substring(2));
					} else if (msg.charAt(1) == '1') { // 자신의 턴인지 확인
						if (msg.substring(2).equals(userID)) {
							turnPoint = 1;
						} else {
							turnPoint = 0;
						}
					} else if (msg.charAt(1) == '#' && turnPoint == 0) { 
						// 돌 위치 반영 형식: #돌 색/x좌표/y좌표
						// 본인 턴이 아닌 사람만 반영
						StringTokenizer st = new StringTokenizer(msg.substring(1), "/");
						int countTokens = st.countTokens();

						for (int i = 0; i < countTokens; i++) {
							String token = st.nextToken();
							stonePoint[i] = token;
						}

						int color = Integer.parseInt(stonePoint[0].substring(1, 2));
						int x = Integer.parseInt(stonePoint[1]);
						int y = Integer.parseInt(stonePoint[2]);

						changeStones(color, x, y);
					} else if (msg.charAt(1) == 'a') {
						String[] temp = msg.substring(2).split("/");
						for (int i = 0; i < 15; i++) {
							for (int j = 0; j < 15; j++) {
								stones[i][j] = Integer.parseInt(temp[i].substring(j, j + 1));

								if (stones[i][j] == 1) {
									imgStone[i][j].setImage("img/stone_black.png");
									imgPoint.setImage("img/point_orange.png");
									imgPoint.setLocation(imgStone[i][j].getLocation().x,
											imgStone[i][j].getLocation().y);
								} else if (stones[i][j] == 2) {
									imgStone[i][j].setImage("img/stone_white.png");
									imgPoint.setImage("img/point_red.png");
									imgPoint.setLocation(imgStone[i][j].getLocation().x,
											imgStone[i][j].getLocation().y);
								}
							}
						}
					}
				} else if (msg.charAt(0) == '$') {
					// 서버가 게임을 조작할 때
					if (msg.charAt(1) == '#') {
						StringTokenizer st = new StringTokenizer(msg.substring(1), "/");
						int countTokens = st.countTokens();

						for (int i = 0; i < countTokens; i++) {
							String token = st.nextToken();
							stonePoint[i] = token;
						}

						int color = Integer.parseInt(stonePoint[0].substring(1, 2));
						int x = Integer.parseInt(stonePoint[1]);
						int y = Integer.parseInt(stonePoint[2]);

						changeStones(color, x, y);
					} 
				} else if (msg.equals("gg")) {
					System.err.println("게임 종료");
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								gameEnd();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					thread.start();
				} else {
					txtChatting.append(msg + "\n");
					scrollBar.setValue(scrollBar.getMaximum());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printList() {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				System.out.print(stones[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public void rule(int x, int y) {
		// boolColor = !boolColor;
		stones[x][y] = 0;

		JOptionPane.showMessageDialog(null, "이곳에는 놓을 수 없습니다.", "룰 위반!", JOptionPane.WARNING_MESSAGE);
		imgStone[x][y].setIcon(null);
		imgStone[x][y].addMouseListener(this);
		imgStone[x][y].addActionListener(this);
	}

	public boolean checkStones(int color, int xx, int yy) throws Exception {
		int count = 1;
		int blankcount3 = 1;
		int blankcount4 = 1;

		int x = xx;
		int y = yy;
		ArrayList<Stones> list = new ArrayList<>();

		stones[x][y] = color;

		int count3 = 0;
		int count4 = 0;

		int between = 0;
		int space = 0;
		int space4 = 0;

		// 가로 왼쪽
		while (true) {

			x--;

			if (x >= 0) {
				if (stones[x][yy] == color) {
					count++;
					blankcount3++;
					blankcount4++;
					list.add(new Stones(x, yy));
				} else if (stones[x][yy] == 0) {
					between++;
					// 떨어진 33
					if (x >= 2 && stones[x - 1][yy] == color && stones[x - 2][yy] == 0) {
						space++;
						blankcount3++;
					} else if (x >= 3 && stones[x - 1][yy] == color && stones[x - 2][yy] == color
							&& stones[x - 3][yy] == 0) {
						space++;
						blankcount3 = blankcount3 + 2;
					}

					// 떨어진 44
					if (x >= 3 && stones[x - 1][yy] == color && stones[x - 2][yy] == color
							&& stones[x - 3][yy] == color) {
						space4++;
						blankcount4 = blankcount4 + 3;
					} else if (x >= 2 && stones[x - 1][yy] == color && stones[x - 2][yy] == color) {
						space4++;
						blankcount4 = blankcount4 + 2;
					} else if (x >= 1 && stones[x - 1][yy] == color) {
						space4++;
						blankcount4++;
					}

					break;
				} else {
					break;
				}
			} else {
				break;
			}
		}

		x = xx;

		// 가로 오른쪽
		while (true) {

			x++;

			if (x < 15) {
				if (stones[x][yy] == color) {
					count++;
					blankcount3++;
					blankcount4++;
					list.add(new Stones(x, yy));
				} else if (stones[x][yy] == 0) {
					between++;
					// 떨어진 33
					if (x < 13 && stones[x + 1][yy] == color && stones[x + 2][yy] == 0) {
						space++;
						blankcount3++;
					} else if (x < 12 && stones[x + 1][yy] == color && stones[x + 2][yy] == color
							&& stones[x + 3][yy] == 0) {
						space++;
						blankcount3 = blankcount3 + 2;
					}

					// 떨어진 44
					if (x < 12 && stones[x + 1][yy] == color && stones[x + 2][yy] == color
							&& stones[x + 3][yy] == color) {
						space4++;
						blankcount4 = blankcount4 + 3;
					} else if (x < 13 && stones[x + 1][yy] == color && stones[x + 2][yy] == color) {
						space4++;
						blankcount4 = blankcount4 + 2;
					} else if (x < 14 && stones[x + 1][yy] == color) {
						space4++;
						blankcount4++;
					}

					break;

				} else {
					break;
				}
			} else {
				break;
			}
		}

		if (count >= 5) {
			stone = color;
			list.add(new Stones(xx, yy));
			winOmok(list);
			return false;
		} else if ((count == 4 && between > 0) || (blankcount4 == 4 && space4 < 2 && space4 != 0)) {
			count4++;
		} else if ((between == 2 && count == 3) || (between == 2 && blankcount3 == 3 && space < 2)) {
			if (!(count == 3 && blankcount3 == 4)) {
				count3++;
			}
		}

		list.clear();
		count = 1;
		blankcount3 = 1;
		blankcount4 = 1;
		between = 0;
		space = 0;
		space4 = 0;

		// 세로 왼쪽
		while (true) {

			y--;

			if (y >= 0) {
				if (stones[xx][y] == color) {
					count++;
					blankcount3++;
					blankcount4++;
					list.add(new Stones(xx, y));
				} else if (stones[xx][y] == 0) {
					between++;
					if (y >= 2 && stones[xx][y - 1] == color && stones[xx][y - 2] == 0) {
						space++;
						blankcount3++;
					} else if (y >= 3 && stones[xx][y - 1] == color && stones[xx][y - 2] == color
							&& stones[xx][y - 3] == 0) {
						space++;
						blankcount3 = blankcount3 + 2;
					}

					// 떨어진 44
					if (y >= 3 && stones[xx][y - 1] == color && stones[xx][y - 2] == color
							&& stones[xx][y - 3] == color) {
						space4++;
						blankcount4 = blankcount4 + 3;
					} else if (y >= 2 && stones[xx][y - 1] == color && stones[xx][y - 2] == color) {
						space4++;
						blankcount4 = blankcount4 + 2;
					} else if (y >= 1 && stones[xx][y - 1] == color) {
						space4++;
						blankcount4++;
					}

					break;
				} else {
					break;
				}
			} else {
				break;
			}
		}

		y = yy;

		// 세로 오른쪽
		while (true) {

			y++;

			if (y < 15) {
				if (stones[xx][y] == color) {
					count++;
					blankcount3++;
					blankcount4++;
					list.add(new Stones(xx, y));
				} else if (stones[xx][y] == 0) {
					between++;
					if (y < 13 && stones[xx][y + 1] == color && stones[xx][y + 2] == 0) {
						space++;
						blankcount3++;
					} else if (y < 12 && stones[xx][y + 1] == color && stones[xx][y + 2] == color
							&& stones[xx][y + 3] == 0) {
						space++;
						blankcount3 = blankcount3 + 2;
					}

					// 떨어진 44
					if (y < 12 && stones[xx][y + 1] == color && stones[xx][y + 2] == color
							&& stones[xx][y + 3] == color) {
						space4++;
						blankcount4 = blankcount4 + 3;
					} else if (y < 13 && stones[xx][y + 1] == color && stones[xx][y + 2] == color) {
						space4++;
						blankcount4 = blankcount4 + 2;
					} else if (y < 14 && stones[xx][y + 1] == color) {
						space4++;
						blankcount4++;
					}

					break;
				} else {
					break;
				}
			} else {
				break;
			}
		}

		if (count >= 5) {
			stone = color;
			list.add(new Stones(xx, yy));
			winOmok(list);
			return false;
		} else if ((count == 4 && between > 0) || (blankcount4 == 4 && space4 < 2 && space4 != 0)) {
			count4++;
		} else if ((between == 2 && count == 3) || (between == 2 && blankcount3 == 3 && space < 2)) {
			if (!(count == 3 && blankcount3 == 4)) {
				count3++;
			}
		}

		list.clear();
		count = 1;
		blankcount3 = 1;
		blankcount4 = 1;
		between = 0;
		space = 0;
		space4 = 0;

		x = xx;
		y = yy;

		// 대각선 왼쪽 위
		while (true) {

			x--;
			y--;

			if (x >= 0 && y >= 0) {
				if (stones[x][y] == color) {
					count++;
					blankcount3++;
					blankcount4++;
					list.add(new Stones(x, y));
				} else if (stones[x][y] == 0) {
					between++;
					if (x >= 2 && y >= 2 && stones[x - 1][y - 1] == color && stones[x - 2][y - 2] == 0) {
						space++;
						blankcount3++;
					} else if (x >= 3 && y >= 3 && stones[x - 1][y - 1] == color && stones[x - 2][y - 2] == color
							&& stones[x - 3][y - 3] == 0) {
						space++;
						blankcount3 = blankcount3 + 2;
					}

					// 떨어진 44
					if (x >= 3 && y >= 3 && stones[x - 1][y - 1] == color && stones[x - 2][y - 2] == color
							&& stones[x - 3][y - 3] == color) {
						space4++;
						blankcount4 = blankcount4 + 3;
					} else if (x >= 2 && y >= 2 && stones[x - 1][y - 1] == color && stones[x - 2][y - 2] == color) {
						space4++;
						blankcount4 = blankcount4 + 2;
					} else if (x >= 1 && y >= 1 && stones[x - 1][y - 1] == color) {
						space4++;
						blankcount4++;
					}

					break;
				} else {
					break;
				}
			} else {
				break;
			}
		}

		x = xx;
		y = yy;

		// 대각선 오른쪽 아래
		while (true) {

			x++;
			y++;

			if (x < 15 && y < 15) {
				if (stones[x][y] == color) {
					count++;
					blankcount3++;
					blankcount4++;
					list.add(new Stones(x, y));
				} else if (stones[x][y] == 0) {
					between++;
					if (x < 13 && y < 13 && stones[x + 1][y + 1] == color && stones[x + 2][y + 2] == 0) {
						space++;
						blankcount3++;
					} else if (x < 12 && y < 12 && stones[x + 1][y + 1] == color && stones[x + 2][y + 2] == color
							&& stones[x + 3][y + 3] == 0) {
						space++;
						blankcount3 = blankcount3 + 2;
					}

					// 떨어진 44
					if (x < 12 && y < 12 && stones[x + 1][y + 1] == color && stones[x + 2][y + 2] == color
							&& stones[x + 3][y + 3] == color) {
						space4++;
						blankcount4 = blankcount4 + 3;
					} else if (x < 13 && y < 13 && stones[x + 1][y + 1] == color && stones[x + 2][y + 2] == color) {
						space4++;
						blankcount4 = blankcount4 + 2;
					} else if (x < 14 && y < 14 && stones[x + 1][y + 1] == color) {
						space4++;
						blankcount4++;
					}

					break;
				} else {
					break;
				}
			} else {
				break;
			}
		}

		if (count >= 5) {
			stone = color;
			list.add(new Stones(xx, yy));
			winOmok(list);
			return false;
		} else if ((count == 4 && between > 0) || (blankcount4 == 4 && space4 < 2 && space4 != 0)) {
			count4++;
		} else if ((between == 2 && count == 3) || (between == 2 && blankcount3 == 3 && space < 2)) {
			if (!(count == 3 && blankcount3 == 4)) {
				count3++;
			}
		}

		list.clear();
		count = 1;
		blankcount3 = 1;
		blankcount4 = 1;
		between = 0;
		space = 0;
		space4 = 0;

		x = xx;
		y = yy;

		// 대각선 오른쪽 위
		while (true) {

			x++;
			y--;

			if (x < 15 && y >= 0) {
				if (stones[x][y] == color) {
					count++;
					blankcount3++;
					blankcount4++;
					list.add(new Stones(x, y));
				} else if (stones[x][y] == 0) {
					between++;
					if (x < 13 && y >= 2 && stones[x + 1][y - 1] == color && stones[x + 2][y - 2] == 0) {
						space++;
						blankcount3++;
					} else if (x < 12 && y >= 3 && stones[x + 1][y - 1] == color && stones[x + 2][y - 2] == color
							&& stones[x + 3][y - 3] == 0) {
						space++;
						blankcount3 = blankcount3 + 2;
					}

					// 떨어진 44
					if (x < 12 && y >= 3 && stones[x + 1][y - 1] == color && stones[x + 2][y - 2] == color
							&& stones[x + 3][y - 3] == color) {
						space4++;
						blankcount4 = blankcount4 + 3;
					} else if (x < 13 && y >= 2 && stones[x + 1][y - 1] == color && stones[x + 2][y - 2] == color) {
						space4++;
						blankcount4 = blankcount4 + 2;
					} else if (x < 14 && y >= 1 && stones[x + 1][y - 1] == color) {
						space4++;
						blankcount4++;
					}

					break;
				} else {
					break;
				}
			} else {
				break;
			}
		}

		x = xx;
		y = yy;

		// 대각선 왼쪽 아래
		while (true) {

			x--;
			y++;

			if (x >= 0 && y < 15) {
				if (stones[x][y] == color) {
					count++;
					blankcount3++;
					blankcount4++;
					list.add(new Stones(x, y));
				} else if (stones[x][y] == 0) {
					between++;
					if (x >= 2 && y < 13 && stones[x - 1][y + 1] == color && stones[x - 2][y + 2] == 0) {
						space++;
						blankcount3++;
					} else if (x >= 3 && y < 12 && stones[x - 1][y + 1] == color && stones[x - 2][y + 2] == color
							&& stones[x - 3][y + 3] == 0) {
						space++;
						blankcount3 = blankcount3 + 2;
					}

					// 떨어진 44
					if (x >= 3 && y < 12 && stones[x - 1][y + 1] == color && stones[x - 2][y + 2] == color
							&& stones[x - 3][y + 3] == color) {
						space4++;
						blankcount4 = blankcount4 + 3;
					} else if (x >= 2 && y < 13 && stones[x - 1][y + 1] == color && stones[x - 2][y + 2] == color) {
						space4++;
						blankcount4 = blankcount4 + 2;
					} else if (x >= 1 && y < 14 && stones[x - 1][y + 1] == color) {
						space4++;
						blankcount4++;
					}

					break;
				} else {
					break;
				}
			} else {
				break;
			}
		}

		if (count >= 5) {
			stone = color;
			list.add(new Stones(xx, yy));
			winOmok(list);
			return false;
		} else if ((count == 4 && between > 0) || (blankcount4 == 4 && space4 < 2 && space4 != 0)) {
			count4++;
		} else if ((between == 2 && count == 3 && space < 2) || (between == 2 && blankcount3 == 3 && space < 2)) {
			if (!(count == 3 && blankcount3 == 4)) {
				count3++;
			}
		}

		if (count3 >= 2 || count4 >= 2) {

			if (color != WHITE) {
				rule(xx, yy);
				return true;
			}
		}

		return false;

	}
}

class Stones {
	int x;
	int y;

	Stones(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

public class MainFrame {
	public static void main(String[] args) {
		try {
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		new OmokFrame("");
	}
}
