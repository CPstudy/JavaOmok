import java.net.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class OmokServer extends JFrame implements ActionListener {
	private ServerSocket ss;
	private Socket soc;
	private PrintWriter pw;
	private BufferedReader br;
	private Hashtable<String, Socket> ht;

	int stone = 0;
	static int cnt = 0;
	static int count = 0, turn = 0;
	JImageButton[][] imgStone = new JImageButton[15][15];
	int[][] stones = new int[15][15];
	String startUser[] = new String[2];
	String firstPlayer;

	String name;
	String msg;

	JPanel panelGame;
	JScrollPane scroll;
	JScrollBar bar;
	JTextArea txtConsole;
	JTextField txtMessage;
	JImageView imgBG;

	public OmokServer() throws Exception {
		ss = new ServerSocket(20000);
		ht = new Hashtable<String, Socket>();

		setTitle("오목 서버");
		setLayout(null);
		setBackground(Color.black);
		getContentPane().setBackground(Color.black);
		this.setSize(800, 400);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int xpos = (int) (screen.getWidth() / 2 - this.getWidth() / 2);
		int ypos = (int) (screen.getHeight() / 2 - this.getHeight() / 2);
		this.setLocation(xpos, ypos);
		this.setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panelGame = new JPanel();
		panelGame.setLayout(new GridLayout(15, 15, 2, 2));
		panelGame.setBounds(0, 0, 370, 370);
		panelGame.setOpaque(false);
		panelGame.setBackground(new Color(0, 0, 0, 0));
		add(panelGame);
		
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				imgStone[i][j] = new JImageButton(null);
				//imgStone[i][j].setBackground(new Color(0, 0, 0, 0));
				imgStone[i][j].setUI(new StyleTransparentButtonUI());
				imgStone[i][j].addActionListener(this);
				imgStone[i][j].setSize(22, 22);
				panelGame.add(imgStone[i][j]);
			}
		}
		
		imgBG = new JImageView("img/bg_server.png");
		imgBG.setBounds(0, 0, 370, 370);
		add(imgBG);

		txtConsole = new JTextArea();
		txtConsole.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		txtConsole.setBackground(Color.black);
		txtConsole.setForeground(Color.white);
		txtConsole.setBorder(null);
		txtConsole.setEditable(false);

		scroll = new JScrollPane(txtConsole);
		scroll.setBounds(panelGame.getWidth(), 0, 424, 341);
		scroll.setBorder(null);
		bar = scroll.getVerticalScrollBar();
		bar.setUI(new StyleConsoleScrollBarUI());
		add(scroll);
		
		JPanel panelChatting = new JPanel();
		panelChatting.setBounds(panelGame.getWidth(), 341, 600, 30);
		panelChatting.setBackground(Color.black);;
		panelChatting.setLayout(null);
		add(panelChatting);
		
		JLabel lblChat = new JLabel(">", SwingConstants.CENTER);
		lblChat.setBounds(0, 0, 30, 30);
		lblChat.setForeground(Color.white);
		lblChat.setBackground(Color.black);
		panelChatting.add(lblChat);
		
		txtMessage = new JTextField();
		txtMessage.setBounds(lblChat.getX() + lblChat.getWidth(), 0, 600 - lblChat.getWidth(), 30);
		txtMessage.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
		txtMessage.setBackground(Color.black);
		txtMessage.setForeground(Color.white);
		txtMessage.setBorder(null);
		txtMessage.setSelectedTextColor(Color.black);
		txtMessage.setSelectionColor(Color.white);
		txtMessage.setCaretColor(Color.white);
		txtMessage.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (e.getSource() == txtMessage) {
						String msg = txtMessage.getText();
						if(msg.charAt(0) == '$') {
							String cmd = msg.substring(1);
							String[] temp = cmd.split(" ");
							
							if(temp[0].equals("stone")) {
								try {
									stone = Integer.parseInt(temp[1]);
									txtConsole.append("stone = " + stone + "\n");
								} catch (Exception e1) {
									e1.printStackTrace();
									txtConsole.append("다음과 같은 형식으로 입력하세요.\n");
									txtConsole.append("$stone 숫자\n");
								}
								txtMessage.setText("");
							}
						} else if(msg.equals("gg")) {
							try {
								sendMessage("알림: 서버에서 게임을 초기화했습니다.");
								sendMessage("gg");
								txtConsole.append("게임을 초기화했습니다.\n");
								txtMessage.setText("");
								count = 0;
								turn = 0;
								startUser[0] = null;
								startUser[1] = null;
								firstPlayer = null;
								stones = new int[15][15];
								changeStone();
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						} else {
							try {
								txtConsole.append(msg + "\n");
								sendMessage(msg);
								txtMessage.setText("");
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

						bar.setValue(bar.getMaximum());
					}
				}
			}
			
		});
		panelChatting.add(txtMessage);

		this.setVisible(true);
		while (true) {
			soc = ss.accept();
			System.out.println("Start Socket");
			ChatClient cc = new ChatClient(soc);
			cc.start();
		}
	}

	public void sendMessage(Socket soc, String name, String msge) throws Exception {
		this.name = name;
		if (ht.size() != 0) {
			Enumeration<String> enu = ht.keys();
			while (enu.hasMoreElements()) {
				String n = enu.nextElement();
				Socket s = ht.get(n);
				pw = new PrintWriter(s.getOutputStream(), true);
				pw.println(":0" + msge);
				pw.flush();
			}
		}
		ht.put(msge, soc);
		sendUser();
	}

	public void sendMessage(String name, String msge) throws Exception {
		Enumeration<String> enu = ht.keys();
		while (enu.hasMoreElements()) {
			String n = enu.nextElement();
			// if (n.equals(name)) continue;
			Socket s = ht.get(n);
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));
			pw.println(":1" + name + "/" + msge);
			pw.flush();
		}
	}
	
	public void sendWhisper(String name, String msge) throws Exception {
		// 스페이스와 스페이스 사이에 아이디를 추출하기 위해
		int begin = msge.indexOf(" ") + 1;
		int end = msge.indexOf(" ", begin);

		if (end != -1) {
			String id = msge.substring(begin, end);
			String msg = msge.substring(end + 1);
			Socket s = ht.get(id);
			Socket mySocket = ht.get(name);
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));
			PrintWriter pw2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mySocket.getOutputStream())));
			
			try {
				if (pw != null) {
					pw.println(":2" + name + "/" + msg);
					pw.flush();
					pw2.println(":3" + id + "/" + msg);
					pw2.flush();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void sendUser() throws Exception {
		sendArray();
		Enumeration<String> enu = ht.keys();
		String users = "";
		while (enu.hasMoreElements()) {
			String n = enu.nextElement();

			users += n + " ";
		}
		enu = ht.keys();

		while (enu.hasMoreElements()) {
			String n = enu.nextElement();
			// if (n.equals(name)) continue;
			Socket s = ht.get(n);

			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));
			pw.println("/user " + users);
			pw.flush();

		}
	}

	public void sendJoin(int location, String name, int count) throws Exception {
		Enumeration<String> enu = ht.keys();
		while (enu.hasMoreElements()) {
			String n = enu.nextElement();
			// if (n.equals(name)) continue;
			Socket s = ht.get(n);
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));

			if (count >= 2) {
				pw.println("*1" + startUser[0]);
				pw.flush();
				pw.println("*2" + startUser[1]);
				pw.flush();
				pw.println("*f" + firstPlayer);
				pw.flush();
			} else {
				
				System.out.println("first = " + firstPlayer);
				
				if (location == 1) {
					pw.println("*1" + name);
					startUser[0] = new String(name);
					firstPlayer = name;
					pw.println("*f" + firstPlayer);
					pw.flush();
				} else if (location == 2) {
					pw.println("*2" + name);
					startUser[1] = new String(name);
				}
				pw.flush();
			}
		}
		// 위의 if문 들을 수행했을 때 사람이 두명이 모였다면..
		if (count == 1) {
			turnPass();
		}
	}

	public void sendMessage(String msge) throws Exception {
		Enumeration<String> enu = ht.keys();
		while (enu.hasMoreElements()) {
			String n = enu.nextElement();
			// if (n.equals(name)) continue;
			Socket s = ht.get(n);
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));
			pw.println(msge);
			pw.flush();
		}
	}
	
	public void sendArray() throws Exception {
		String sendMsg = "";
		
		for(int i = 0; i < 15; i++) {
			for(int j = 0; j < 15; j++) {
				sendMsg += String.valueOf(stones[i][j]);
			}
			if(i < 14) {
				sendMsg += "/";
			}
		}
		
		sendMsg = "!a" + sendMsg;
		
		Enumeration<String> enu = ht.keys();
		while (enu.hasMoreElements()) {
			String n = enu.nextElement();
			System.out.println(n);
			Socket s = ht.get(n);
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));
			pw.println(sendMsg);
			pw.flush();
		}
	}

	/*
	 * public void userQuit(String quit, String msg) throws Exception {
	 * Enumeration<String> enu = ht.keys(); while (enu.hasMoreElements()) { String n
	 * = enu.nextElement(); // if (n.equals(name)) continue; Socket s = ht.get(n);
	 * pw = new PrintWriter(new BufferedWriter(new
	 * OutputStreamWriter(s.getOutputStream()))); pw.println(msg); pw.flush(); } }
	 */

	public void turnPass() throws IOException {
		Enumeration<String> enu = ht.keys();
		while (enu.hasMoreElements()) {
			String n = enu.nextElement();
			Socket s = ht.get(n);
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())));
			pw.println("!1" + startUser[turn]);
			pw.flush();
			System.out.println("turn = " + startUser[turn]);
		}
		System.out.println("==========================================");
		turn++;
		if (turn >= 2)
			turn = 0;
	}
	
	public void changeStone() {
		for(int i = 0; i < 15; i++) {
			for(int j = 0; j < 15; j++) {
				if(stones[i][j] == 0) {
					imgStone[i][j].setImage(null);
				} else if(stones[i][j] == 1) {
					imgStone[i][j].setImage("img/server_stone_black.png");
				} else if(stones[i][j] == 2) {
					imgStone[i][j].setImage("img/server_stone_white.png");
				}
			}
		}
	}

	/**
	 * Game 스레드와 Chat 스레드를 같이 돌리다보니 Game 스레드에서 돌 좌표를 제대로 못 얻어와 하나로 통합했습니다.
	 * 
	 * @author guide
	 *
	 */
	class ChatClient extends Thread {
		Socket soc;

		ChatClient(Socket soc) {
			this.soc = soc;
		}

		public void run() {
			while (true) {
				try {
					br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
					msg = br.readLine();
					txtConsole.append(msg + "\n");
					bar.setValue(bar.getMaximum());
					System.out.println();
					System.out.println("%% 서버 메시지 " + cnt + " %%");
					System.out.println();
					System.out.println(msg);
					System.out.println("%% 서버 메시지 %%");
					System.out.println();
					cnt++;
					if (msg == null)
						break;
					if (msg.charAt(0) == '@') {
						sendMessage(soc, "@", msg.substring(1));
						System.out.println("startUser[0]= " + startUser[0] + ", startUser[1]= " + startUser[1]);
						if (startUser[0] != null) {
							System.out.println("1번 칸 이미 차있음");
							sendMessage("*1" + startUser[0]);
						}
						if (startUser[1] != null) {
							System.out.println("2번 칸 이미 차있음");
							sendMessage("*2" + startUser[1]);
						}

					} else if (msg.charAt(0) == '*') {
						if (msg.substring(1, 5).equals("btn1")) {
							sendJoin(1, msg.substring(5), count);
							count++;
						} else if (msg.substring(1, 5).equals("btn2")) {
							// System.out.println("두번째 메시지 옴");
							sendJoin(2, msg.substring(5), count);
							count++;
						}
					} else if (msg.charAt(0) == '#') {
						String str = msg.substring(1, msg.length());
						String[] temp = str.split("/");
						int c = Integer.parseInt(temp[0]);
						int i = Integer.parseInt(temp[1]);
						int j = Integer.parseInt(temp[2]);
						stones[i][j] = c;
						changeStone();
						sendMessage("!" + msg);
						turnPass();
						
						for (int ii = 0; ii < 15; ii++) {
							for (int jj = 0; jj < 15; jj++) {
								System.out.print(stones[ii][jj] + " ");
							}
							System.out.println();
						}
						System.out.println();
						
					} else if (msg.split(" ")[0].equals("close")) {
						System.err.println("close");
						sendMessage(msg.split(" ")[1] + "님이 나갔습니다");
						synchronized (ht) {
							ht.remove(msg.split(" ")[1]);
						}
						sendUser();

						int change = 1;
						for (int i = 0; i < startUser.length; i++) {
							if (msg.split(" ")[1].equals(startUser[i])) {
								sendMessage(msg.split(" ")[1] + "님이 게임중에 나갔습니다");
								System.out.println("첫번째꺼 보냄");
								sendMessage("기권으로 인해 " + startUser[i + change] + "님이 승리하셨습니다");
								System.out.println("두번째꺼 보냄");
								sendMessage("gg");
								System.out.println("세번째꺼 보냄");
								count = 0;
								turn = 0;
								startUser[0] = null;
								startUser[1] = null;
								firstPlayer = null;
								stones = new int[15][15];
								changeStone();
							}
							change *= -1;
						}
						break;
					} else if (msg.equals("gg")) {
						count = 0;
						turn = 0;
						startUser[0] = null;
						startUser[1] = null;
						firstPlayer = null;
						stones = new int[15][15];
						changeStone();
					} else if (msg.indexOf("/to") >= 0) {// 귓속말
						Scanner sc = new Scanner(msg).useDelimiter("\\s*:\\s*");
						String name = sc.next();
						String msge = sc.next();
						sendWhisper(name, msge);
						sc.close();
					} else {
						Scanner sc = new Scanner(msg).useDelimiter("\\s*:\\s*");
						String name = sc.next();
						String msge = sc.next();
						sendMessage(name, msge);
						sc.close();
					}
				} catch (Exception e) {

				}
			}

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		for(int i = 0; i < 15; i++) {
			for(int j = 0; j < 15; j++) {
				if(e.getSource() == imgStone[i][j]) {
					try {
						stones[i][j] = stone;
						changeStone();
						sendMessage("$#" + stone + "/" + i + "/" + j);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			//UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		new OmokServer();
	}
}