package chessBot;


public class playColor {

	enum color { BLACK,WHITE };
	
	public static int toIntColor(color c)
	{
		if (c == color.BLACK)
			return 0;
		else
			return 1;
	}
	
	public static color inverseColor(color c)
	{
		if(c == color.BLACK)
		{
			return color.WHITE;
		}
		else
		{
			return color.BLACK;
		}
	}
	
}
