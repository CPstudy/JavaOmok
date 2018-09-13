import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import javax.swing.plaf.basic.BasicTextFieldUI;

public class StyleScrollPaneUI extends BasicScrollPaneUI {
	
	public static ComponentUI createUI(JComponent jComponent) {
		return new BasicScrollPaneUI();
	}

	public void installUI(JComponent jComponent) {
		super.installUI(jComponent);
		jComponent.setOpaque(false);
		
		JScrollPane com = (JScrollPane) jComponent;
		com.setBackground(StaticColor.BACKGROUND);
	}

}
