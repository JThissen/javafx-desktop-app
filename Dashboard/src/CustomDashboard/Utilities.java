package CustomDashboard;

import javafx.scene.paint.Color;

public class Utilities
{
    public static String ConvertToHex(Color c)
    {
        return String.format( "#%02X%02X%02X",
                (int)( c.getRed() * 255),
                (int)( c.getGreen() * 255),
                (int)( c.getBlue() * 255));
    }

    public static double DegreeToRadians(double degrees)
    {
        return degrees * (Math.PI / 180);
    }
}

