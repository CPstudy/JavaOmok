import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.plaf.basic.BasicTextPaneUI;

public class StyleTextPaneUI extends BasicTextPaneUI {
	
	private final static int ARC_WIDTH = 5;
	private final static int ARC_HEIGHT = 5;
	private final static int x = 0;
	private final static int y = 0;
	private final static int BORDER_TOP = 4;
	private final static int BORDER_BOTTOM = 4;
	private final static int BORDER_LEFT = 4;
	private final static int BORDER_RIGHT = 4;

	public static ComponentUI createUI(JComponent jComponent) {
		return new BasicTextFieldUI();
	}

	public void installUI(JComponent jComponent) {
		super.installUI(jComponent);
		jComponent.setBorder(new RoundedBorder());
		jComponent.setOpaque(false);
		
		JTextPane com = (JTextPane) jComponent;
		com.setSelectedTextColor(new Color(255, 255, 255));
		com.setSelectionColor(new Color(204, 114, 61));
	}

	protected void paintSafely(Graphics graphics) {
		JComponent c = getComponent();
		if (!c.isOpaque()) {
			graphics.setColor(StaticColor.TEXT_BACKGROUND);
			graphics.fillRoundRect(x, y, c.getWidth() - 1, c.getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
		}
		super.paintSafely(graphics);
	}

	private static class RoundedBorder extends AbstractBorder {
		private static final long serialVersionUID = 1L;

		public void paintBorder(Component coponent, Graphics g, int x, int y, int width, int height) {
			g.setColor(StaticColor.TEXT_BORDER);
			g.drawRoundRect(x, y, width - 1, height - 1, ARC_WIDTH, ARC_HEIGHT);
		}

		public Insets getBorderInsets(Component c) {
			return new Insets(BORDER_TOP, BORDER_LEFT, BORDER_BOTTOM, BORDER_RIGHT);
		}

	}

}
