package models;

public class Finger {
	private static final int m_iRectangleDimension = 100;
	private int x;
	private int y;
	private boolean fill;

	public Finger(int x, int y) {
		this.x = x;
		this.y = y;

		fill = false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isFill() {
		return fill;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setFill(boolean fill) {
		this.fill = fill;
	}
	
	public boolean isPressed(int pressX, int pressY) {
		if (pressX < (x + m_iRectangleDimension) && pressX > x)
			if (pressY < (y + m_iRectangleDimension * 3) && pressY > y)
				return true;

		return false;
	}
}
