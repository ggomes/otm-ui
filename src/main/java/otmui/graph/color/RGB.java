package otmui.graph.color;

public class RGB {
    public double red,green,blue;

    public RGB(double [] c){
        this.red = c[0];
        this.green = c[1];
        this.blue = c[2];
    }
    public RGB(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
}
