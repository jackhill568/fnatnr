package fnatnr;

public class NightData {
	public final int night;
	public final String title;
	public final int[] enemyAgressions;
	public final int[] sink;

	public NightData(int night, String title, int[] enemyAgressions, int[] sink) {
		this.night = night;
		this.title = title;
		this.enemyAgressions = enemyAgressions;
		this.sink = sink;
	}
}
