package mytools.myScrollBar;

import java.awt.Color;

import mytools.MyUtil;

public class ScrollBarUtil {
	// 背景色
	public static Color Background = Color.WHITE;
	// 按钮颜色77,97,133 98, 164, 214
	static Color Button_Pressed = new Color(77, 97, 133);
	static Color Button_Rollover = new Color(98, 164, 214);
	static Color Button_Default = new Color(187,195,201);//155, 185, 245

	static Color ControlDisabled = MyUtil.InactiveControlTextColor;
	static Color ThumbColor = Button_Default;
	public static Color[] TrackColors = new Color[] { new Color(232, 231, 233),
			new Color(254,254,251) };
}
