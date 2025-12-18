import java.awt.Color;

public class Runigram {

	public static void main(String[] args) {
		Color[][] tinypic = read("tinypic.ppm");
		print(tinypic);

		Color[][] image = flippedHorizontally(tinypic);
		System.out.println();
		print(image);
	}

	public static Color[][] read(String fileName) {
		In in = new In(fileName);

		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();

		Color[][] image = new Color[numRows][numCols];

		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				int r = in.readInt();
				int g = in.readInt();
				int b = in.readInt();
				image[i][j] = new Color(r, g, b);
			}
		}
		return image;
	}

	private static void print(Color c) {
		System.out.print("(");
		System.out.printf("%3s,", c.getRed());
		System.out.printf("%3s,", c.getGreen());
		System.out.printf("%3s", c.getBlue());
		System.out.print(")  ");
	}

	private static void print(Color[][] image) {
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[0].length; j++) {
				print(image[i][j]);
			}
			System.out.println();
		}
	}

	public static Color[][] flippedHorizontally(Color[][] image) {
		int h = image.length;
		int w = image[0].length;
		Color[][] out = new Color[h][w];

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				out[i][j] = image[i][w - 1 - j];
			}
		}
		return out;
	}

	public static Color[][] flippedVertically(Color[][] image) {
		int h = image.length;
		int w = image[0].length;
		Color[][] out = new Color[h][w];

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				out[i][j] = image[h - 1 - i][j];
			}
		}
		return out;
	}

	private static Color luminance(Color pixel) {
		int r = pixel.getRed();
		int g = pixel.getGreen();
		int b = pixel.getBlue();
		int lum = (int) (0.299 * r + 0.587 * g + 0.114 * b);
		return new Color(lum, lum, lum);
	}

	public static Color[][] grayScaled(Color[][] image) {
		int h = image.length;
		int w = image[0].length;
		Color[][] out = new Color[h][w];

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				out[i][j] = luminance(image[i][j]);
			}
		}
		return out;
	}

	public static Color[][] scaled(Color[][] image, int width, int height) {
		int h0 = image.length;
		int w0 = image[0].length;
		Color[][] out = new Color[height][width];

		for (int i = 0; i < height; i++) {
			int srcI = (int) ((long) i * h0 / height);
			for (int j = 0; j < width; j++) {
				int srcJ = (int) ((long) j * w0 / width);
				out[i][j] = image[srcI][srcJ];
			}
		}
		return out;
	}

	public static Color blend(Color c1, Color c2, double alpha) {
		double beta = 1.0 - alpha;

		int r = (int) Math.round(alpha * c1.getRed() + beta * c2.getRed());
		int g = (int) Math.round(alpha * c1.getGreen() + beta * c2.getGreen());
		int b = (int) Math.round(alpha * c1.getBlue() + beta * c2.getBlue());

		r = Math.max(0, Math.min(255, r));
		g = Math.max(0, Math.min(255, g));
		b = Math.max(0, Math.min(255, b));

		return new Color(r, g, b);
	}

	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		int h = image1.length;
		int w = image1[0].length;
		Color[][] out = new Color[h][w];

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				out[i][j] = blend(image1[i][j], image2[i][j], alpha);
			}
		}
		return out;
	}

	public static void morph(Color[][] source, Color[][] target, int n) {
		Color[][] t = target;

		if (source.length != target.length || source[0].length != target[0].length) {
			t = scaled(target, source[0].length, source.length);
		}

		for (int i = 0; i <= n; i++) {
			double alpha = (double) (n - i) / n;
			Color[][] frame = blend(source, t, alpha);
			display(frame);
			StdDraw.pause(500);
		}
	}

	public static void setCanvas(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(height, width);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
		StdDraw.enableDoubleBuffering();
	}

	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				StdDraw.setPenColor(
						image[i][j].getRed(),
						image[i][j].getGreen(),
						image[i][j].getBlue());
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}
