package fnatnr.NightTools;

public class NightTimer {

    private static final int TOTAL_MINUTES = 360;
    private static final int START_HOUR    = 0; 

    private int minutesElapsed = 0;
    private static final int ticksPerMinute = 2; 

		private static NightTimer instance;

    private NightTimer() {
    }

		public static NightTimer getInstance() {
			if (instance == null) {
				instance = new NightTimer();
			}
			return instance;
		}

    private int tickCount = 0;

    public void update() {
        if (isComplete()) return;
        tickCount++;
        if (tickCount >= ticksPerMinute) {
            tickCount = 0;
            minutesElapsed++;
        }
    }

    public boolean isComplete() {
        return minutesElapsed >= TOTAL_MINUTES;
    }

    public int getHour() {
        return START_HOUR + (minutesElapsed / 60);
    }

    public String getTimeString() {
        int hour = getHour();
        if (hour == 0)  return "12 AM";
        if (hour == 12) return "12 PM";
        return hour + " AM";
    }

    public float getProgress() {
        return (float) minutesElapsed / TOTAL_MINUTES;
    }

    public void reset() {
        minutesElapsed = 0;
        tickCount      = 0;
    }
}
