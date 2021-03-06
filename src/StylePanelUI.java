import java.awt.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;
import javax.swing.plaf.basic.BasicTextFieldUI;

public class StylePanelUI extends BasicPanelUI {
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
	}
	
	

	@Override
	public void paint(Graphics g, JComponent c) {
		// TODO Auto-generated method stub
		super.paint(g, c);
		g.setColor(new Color(190, 190, 190));
		g.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 10, 10);
	}



	private static class RoundedBorder extends AbstractBorder {
		private static final long serialVersionUID = 1L;

		public void paintBorder(Component coponent, Graphics g, int x, int y, int width, int height) {
			g.setColor(StaticColor.PLAYERPANEL);
			g.fillRoundRect(x, y, width - 1, height - 1, ARC_WIDTH, ARC_HEIGHT);
			g.setColor(StaticColor.PLAYERPANEL_BORDER);
			g.drawRoundRect(x, y, width - 1, height - 1, ARC_WIDTH, ARC_HEIGHT);
		}

		public Insets getBorderInsets(Component c) {
			return new Insets(BORDER_TOP, BORDER_LEFT, BORDER_BOTTOM, BORDER_RIGHT);
		}

	}

}
