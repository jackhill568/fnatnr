package fnatnr;

import java.util.Random;

public class GameRandom {

	private static final Random instance = new Random();

	public static Random getInstance() {
		return instance;
	}

}
