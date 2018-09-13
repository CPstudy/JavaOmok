import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

import java.awt.*;

public class JImageButton extends JButton {

	private int xPos = 0;
	private int yPos = 0;

	/**
	 * ImageView의 크기
	 */
	private int width = 0;
	private int height = 0;

	/**
	 * 원본 이미지 크기
	 */
	private int iWidth = 0;
	private int iHeight = 0;

	private boolean autoScale = false;

	private String mPath;

	ImageIcon icon;
	Image image;

	JImageButton(String p) {
		this.mPath = p;
		
		try {
			icon = new ImageIcon(getClass().getResource(p));
		} catch (Exception e) {
			icon = new ImageIcon(p);
		}

		image = icon.getImage();
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		this.xPos = x;
		this.yPos = y;
		this.width = width;
		this.height = height;
		
		if (autoScale) {
			this.width = iWidth;
			this.height = iHeight;
		}
		
		super.setBounds(x, y, width, height);
		super.setIcon(getImage());
	}

	@Override
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		super.setSize(width, height);
		super.setIcon(getImage());
	}
	
	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
	}

	public void setAutoScale(boolean b) {
		this.autoScale = b;
		super.setIcon(getImage());
	}

	public void setImage(String p) {
		this.mPath = p;

		try {
			icon = new ImageIcon(getClass().getResource(p));
		} catch (Exception e) {
			icon = new ImageIcon(p);
		}

		image = icon.getImage();

		iWidth = image.getWidth(null);
		iHeight = image.getHeight(null);
		
		super.setIcon(getImage());
	}

	private ImageIcon getImage() {

		if (autoScale) {
			this.width = iWidth;
			this.height = iHeight;
			super.setBounds(xPos, yPos, iWidth, iHeight);
		} else {
			super.setBounds(xPos, yPos, this.width, this.height);
		}

		return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_FAST));

	}
}
