package lab14;
import lab14lib.Generator;
import lab14lib.GeneratorAudioAnimator;
import lab14lib.GeneratorDrawer;
import lab14lib.GeneratorPlayer;

public class Main {
	public static void main(String[] args) {
		/** Your code here. */
		StrangeBitwiseGenerator g = new StrangeBitwiseGenerator(512);
		GeneratorDrawer gd = new GeneratorDrawer(g);
		gd.draw(4096);
	}
} 