package net.toper;

public class Overlay {

	private Texture origT, editedT;
	private int x, y;

	public Overlay(Texture t) {
		this.origT = t;
		this.editedT = t;
	}

	public void setPos(int newX, int newY) {
		x = newX;
		y = newY;
	}

	public Texture getImage() {
		return editedT;
	}

	public void render(Frame f) {
		boolean isTrans = Render.isRenderingTransparency();
		Render.shouldRenderTransparency(true);
		Render.render(f, getImage(), x, y, 0, 0, getImage().getWidth(), getImage().getHeight(), 0xff00ff, 0);
		Render.shouldRenderTransparency(isTrans);
	}

	public void setOpacityMap(Texture map, boolean stretchToFit) {
		float xScale = 1;
		float yScale = 1;
		if (stretchToFit) {
			xScale = (float) origT.getWidth() / (float) map.getWidth();
			yScale = (float) origT.getHeight() / (float) map.getHeight();
		}
		map.setScale(xScale, yScale);
		editedT = new Texture(origT.getWidth(), origT.getHeight());
		for (int y = 0; y < map.getScaledHeight(); y++) {
			for (int x = 0; x < map.getScaledWidth(); x++) {
				int mapHex = map.getData(x, y);
				int amt = (mapHex >> 24 & 0xff);
				if (origT.getAlphaAtPoint(x, y) > 0f) {
					int texHex = origT.getData(x, y) & 0x00ffffff;
					int finalHex = texHex | (amt << 24);
					editedT.setData(x + y * origT.getScaledWidth(), finalHex);
				}
			}
		}

	}
}
