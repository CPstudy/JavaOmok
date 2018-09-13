import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class StyleTransparentButtonUI extends BasicButtonUI {
	
	Color color;
	
	@Override
    public void installUI (JComponent c) {
        super.installUI(c);
        AbstractButton button = (AbstractButton) c;
        button.setOpaque(false);
        button.setBorder(null);
        color = button.getBackground();
    }
	
    @Override
    public void paint (Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;

        if(b.isEnabled()) {
        	b.setBackground(new Color(0, 0, 0, 0));
            paintBackground(g, b, 0);
        } else {
        	b.setBackground(new Color(0, 0, 0, 0));
            paintBackground(g, b, 0);
        }
        
        super.paint(g, c);
    }

    private void paintBackground (Graphics g, JComponent c, int yOffset) {
    }
}
