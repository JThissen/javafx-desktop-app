package CustomDashboard;

import javafx.scene.paint.Color;

import java.util.Random;

public class PieChartData implements Comparable<PieChartData>, Cloneable
{
    private Color color;
    private double value;
    private String name;

    public PieChartData(int i)
    {
        Random rand = new Random();
        int max = 255;
        int min = 0;
        this.color = Color.rgb(rand.nextInt((max - min) + 1) + min,
                rand.nextInt((max - min) + 1) + min,
                rand.nextInt((max - min) + 1) + min);
        this.value = rand.nextInt((max - min) + 1) + min;
        this.name = new StringBuilder().append("Data point ").append(i).toString();
    }

    public PieChartData(Color color, String name, double value)
    {
        this.color = color;
        this.name = name;
        this.value = value;
    }

    public PieChartData(PieChartData pieChartData) //used to perform a deep copy
    {
        this.color = pieChartData.color;
        this.value = pieChartData.value;
        this.name = pieChartData.name;
    }

    public Color getColor()
    {
        return this.color;
    }
    public Double getValue()
    {
        return this.value;
    }
    public String getName(){return this.name; }
    public void setColor(Color color) { this.color = color; }

    @Override
    public int compareTo(PieChartData other)
    {
        if(this.value > other.value)
            return 1;
        else if(this.value < other.value)
            return -1;
        else
            return 0;
    }
}