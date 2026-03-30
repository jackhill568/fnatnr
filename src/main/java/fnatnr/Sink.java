
package fnatnr;

public class Sink {

	private int level;
	private int threshold;
	private int increment;
	private int decrement;
	private boolean status;

	public Sink(int threshold, int increment, int decrement) {

		this.increment = increment;
		this.threshold = threshold;
		this.decrement = decrement;
		this.level = 0;
	}

	public void update() {
		if (level < threshold)
			level += increment;
		if (level >= threshold) {
			status = true;
		} else {
			status = false;
		}
	}

	public void clean() {
		if (level > 0)
			level -= decrement;
		status = false;
	}

	public boolean getStatus() {
		return status;
	}

	public float getLevel() {
		return (float)level / threshold;
	}

}
