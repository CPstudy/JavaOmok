import java.awt.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;

public class StyleTextFieldUI extends BasicTextFieldUI {

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
		
		JTextField com = (JTextField) jComponent;
		com.setSelectedTextColor(new Color(255, 255, 255));
		com.setSelectionColor(new Color(204, 114, 61));
	}

	protected void paintSafely(Graphics graphics) {
		JComponent c = getComponent();
		if (!c.isOpaque()) {
			graphics.setColor(c.getBackground());
			graphics.fillRoundRect(x, y, c.getWidth() - 1, c.getHeight() - 1, ARC_WIDTH, ARC_HEIGHT);
		}
		super.paintSafely(graphics);
	}

	private static class RoundedBorder extends AbstractBorder {
		private static final long serialVersionUID = 1L;

		public void paintBorder(Component coponent, Graphics g, int x, int y, int width, int height) {
			Color oldColor = g.getColor();
			g.setColor(Color.gray);
			g.drawRoundRect(x, y, width - 1, height - 1, ARC_WIDTH, ARC_HEIGHT);
			g.setColor(oldColor);
		}

		public Insets getBorderInsets(Component c) {
			return new Insets(BORDER_TOP, BORDER_LEFT, BORDER_BOTTOM, BORDER_RIGHT);
		}

	}
}
