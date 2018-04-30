import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.Rectangle;

public class Button {

	int x, y;
	String value;
	Rectangle rect;
	
	public Button(int x, int y, String value) {
		this.x = x;
		this.y = y;
		this.value = value;
		this.rect = new Rectangle(x, y, 100, 30);
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(rect.x, rect.y, rect.width, rect.height);
		g.setColor(Color.RED);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("Hello", rect.x, rect.y);
	}
	
	public boolean hitBounds(Rectangle rect) {
		if(rect.x > this.rect.x && rect.y > this.rect.y && rect.x < this.rect.x + this.rect.width &&
			rect.y < this.rect.y + this.rect.height) {
				return true;
			} else {
				return false;
			}
	}
}