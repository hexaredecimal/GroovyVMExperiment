
package vm.theme;

import java.awt.Color;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

/**
 *
 * @author hexaredecimal
 */
public class FlatMetalTheme extends DefaultMetalTheme {

	private final ColorUIResource whiteBg = new ColorUIResource(Color.WHITE);
	private final ColorUIResource blackFg = new ColorUIResource(Color.BLACK);
	private final ColorUIResource inactiveGray = new ColorUIResource(new Color(230, 230, 230));

	@Override
	public ColorUIResource getWindowTitleBackground() {
		return getWindowBackground(); 
	}

	@Override
	public ColorUIResource getWindowTitleForeground() {
		return getInactiveControlTextColor();
	}

	@Override
	public ColorUIResource getWindowTitleInactiveBackground() {
		return getWindowBackground();
	}

	@Override
	public ColorUIResource getWindowTitleInactiveForeground() {
		return getInactiveControlTextColor();
	}

	@Override
	public ColorUIResource getMenuBackground() {
		return whiteBg;
	}

	@Override
	public ColorUIResource getMenuForeground() {
		return blackFg;
	}

	@Override
	public ColorUIResource getMenuSelectedBackground() {
		return inactiveGray;
	}

	@Override
	public ColorUIResource getMenuSelectedForeground() {
		return blackFg;
	}

	@Override
	public ColorUIResource getMenuDisabledForeground() {
		return new ColorUIResource(Color.GRAY);
	}

	@Override
	public ColorUIResource getControl() {
		return whiteBg;
	}

	@Override
	public ColorUIResource getControlTextColor() {
		return blackFg;
	}
}