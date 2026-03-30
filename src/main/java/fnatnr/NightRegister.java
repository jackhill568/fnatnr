package fnatnr;

public class NightRegister {

    private static final NightData[] NIGHTS = {
        new NightData(1, "Night 1", 
            new int[]{ 3, 3, 0, 0, 0, 3 },
            new int[]{ 50, 1, 1 }
        ),
        new NightData(2, "Night 2", 
            new int[]{ 5, 3, 2, 2, 2, 2 },
            new int[]{ 50, 1, 1 }
        ),
        new NightData(3, "Night 3",
            new int[]{ 8, 6, 7, 7, 5, 5 },
            new int[]{ 50, 1, 1 }
        ),
        new NightData(4, "Night 4", 
            new int[]{ 12, 10, 11, 11, 9, 9 },
            new int[]{ 50, 1, 1 }
        ),
        new NightData(5, "Night 5",
            new int[]{ 18, 16, 17, 17, 15, 15 },
            new int[]{ 50, 1, 1 }
        ),
    };

    public static NightData get(int night) {
        if (night < 1 || night > NIGHTS.length) {
            throw new IllegalArgumentException("Invalid night: " + night);
        }
        return NIGHTS[night - 1];
    }

    public static int totalNights() {
        return NIGHTS.length;
    }
}
