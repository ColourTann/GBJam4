package game.util;

import game.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Draw {
	// Non-centered stuff//

	public static void draw(Batch batch, Texture t, float x, float y) {
		drawRotatedScaled(batch, t, x, y, 1, 1, 0);
	}

	public static void drawScaled(Batch batch, Texture t, float x, float y,
			float scaleX, float scaleY) {
		drawRotatedScaled(batch, t, x, y, scaleX, scaleY, 0);
	}

	public static void drawRotatedScaled(Batch batch, Texture t, float x,
			float y, float scaleX, float scaleY, float radianRotation) {
		drawRotatedScaledFlipped(batch, t, x, y, scaleX, scaleY,
				radianRotation, false, false);
	}

	public static void drawRotatedScaledFlipped(Batch batch, Texture t,
			float x, float y, float scaleX, float scaleY, float radianRotation,
			boolean xFlip, boolean yFlip) {
		batch.draw(t, x, y, 0, 0, t.getWidth(), t.getHeight(), scaleX, scaleY,
				rad2deg(radianRotation), 0, 0, t.getWidth(), t.getHeight(),
				xFlip, yFlip);
	}

	// Centered stuff//

	public static void drawCentered(Batch batch, Texture t, float x, float y) {
		drawCenteredRotatedScaled(batch, t, x, y, 1, 1, 0);
	}

	public static void drawCenteredScaled(Batch batch, Texture t, float x,
			float y, float scaleX, float scaleY) {
		drawCenteredRotatedScaled(batch, t, x, y, scaleX, scaleY, 0);
	}

	public static void drawCenteredRotated(Batch batch, Texture t, float x,
			float y, float radianRotation) {
		drawCenteredRotatedScaled(batch, t, x, y, 1, 1, radianRotation);
	}

	public static void drawCenteredRotatedScaled(Batch batch, Texture t,
			float x, float y, float xScale, float yScale, float radianRotation) {
		drawCenteredRotatedScaledFlipped(batch, t, x, y, xScale, yScale,
				radianRotation, false, false);
	}

	public static void drawCenteredRotatedScaledFlipped(Batch batch, Texture t,
			float x, float y, float xScale, float yScale, float radianRotation,
			boolean xFlip, boolean yFlip) {
		batch.draw(t, (x - t.getWidth() / 2), (y - t.getHeight() / 2),
				t.getWidth() / 2f, t.getHeight() / 2f, t.getWidth(),
				t.getHeight(), xScale, yScale, rad2deg(radianRotation), 0, 0,
				t.getWidth(), t.getHeight(), xFlip, yFlip);
	}

	public static void draw(Batch batch, TextureRegion t, float x, float y) {
		drawRotatedScaled(batch, t, x, y, 1, 1, 0);
	}

	public static void drawScaled(Batch batch, TextureRegion t, float x,
			float y, float scaleX, float scaleY) {
		drawRotatedScaled(batch, t, x, y, scaleX, scaleY, 0);
	}


	public static void drawRotatedScaled(Batch batch, TextureRegion t, float x,
			float y, float scaleX, float scaleY, float radianRotation) {
		batch.draw(t, x, y, 0f, 0f, t.getRegionWidth(), t.getRegionHeight(),
				scaleX, scaleY, rad2deg(radianRotation));
	}



	// Centered stuff//

	public static void drawCentered(Batch batch, TextureRegion t, float x,
			float y) {
		drawCenteredRotatedScaled(batch, t, x, y, 1, 1, 0);
	}

	public static void drawCenteredScaled(Batch batch, TextureRegion t,
			float x, float y, float scaleX, float scaleY) {
		drawCenteredRotatedScaled(batch, t, x, y, scaleX, scaleY, 0);
	}


	public static void drawCenteredRotated(Batch batch, TextureRegion t,
			float x, float y, float radianRotation) {
		drawCenteredRotatedScaled(batch, t, x, y, 1, 1, radianRotation);
	}

	public static void drawCenteredRotatedScaled(Batch batch, TextureRegion t,
			float x, float y, float xScale, float yScale, float radianRotation) {
		drawCenteredRotatedScaledFlipped(batch, t, x, y, xScale, yScale,
				radianRotation);
	}

	public static void drawCenteredRotatedScaledFlipped(Batch batch,
			TextureRegion t, float x, float y, float scaleX, float scaleY,
			float radianRotation) {
		batch.draw(t, x - t.getRegionWidth() / 2f,
				y - t.getRegionHeight() / 2f, t.getRegionWidth() / 2f,
				t.getRegionHeight() / 2f, t.getRegionWidth(),
				t.getRegionHeight(), scaleX, scaleY, rad2deg(radianRotation));
	}

	public static void drawRectangle(Batch batch, float x, float y,
			float width, float height, int lineWidth) {
		drawScaled(batch, getSq(), x, y, width, lineWidth);
		drawScaled(batch, getSq(), x, y + height - lineWidth, width, lineWidth);
		drawScaled(batch, getSq(), x, y + lineWidth, lineWidth, height
				- lineWidth * 2);
		drawScaled(batch, getSq(), x + width - lineWidth, y + lineWidth,
				lineWidth, height - lineWidth * 2);
	}

	public static void fillRectangle(Batch batch, float x, float y,
			float width, float height) {
		Draw.drawScaled(batch, Draw.getSq(), x, y, width, height);
	}

	public static void drawLine(Batch batch, float x, float y, float tX,
			float tY, float width) {
		float dist = (float) Math.sqrt((tX - x) * (tX - x) + (tY - y)
				* (tY - y));
		float radians = (float) Math.atan2(tY - y, tX - x);

		x += Math.sin(radians) * width / 2f;
		y -= Math.cos(radians) * width / 2f;

		Draw.drawRotatedScaled(batch, getSq(), x, y, dist, width, radians);
	}

	// Blending Junk
	public enum BlendType {
		Normal, Additive, MaxBuggy
	}

	public static void setBlend(Batch batch, BlendType type) {
		switch (type) {
		case Additive:
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			break;
		case Normal:
			Gdx.gl20.glBlendEquation(GL20.GL_FUNC_ADD);
			batch.setBlendFunction(GL20.GL_SRC_ALPHA,
					GL20.GL_ONE_MINUS_SRC_ALPHA);
			break;
		case MaxBuggy:
			Gdx.gl20.glBlendEquation(0x8008);
			batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
			break;
		}
	}

	public static float rad2deg(float rad) {
		return (float) (rad * 180f / Math.PI);
	}

	private static AtlasRegion wSq;

	public static AtlasRegion getSq() {
		if (wSq == null) {
			wSq = Main.atlas.findRegion("pixel");
		}
		return wSq;
	}

}
