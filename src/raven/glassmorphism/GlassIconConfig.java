package raven.glassmorphism;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Raven
 */
public class GlassIconConfig implements Serializable {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getGlassIndex() {
        return glassIndex;
    }

    public void setGlassIndex(int glassIndex) {
        this.glassIndex = glassIndex;
    }

    public int getBlur() {
        return blur;
    }

    public void setBlur(int blur) {
        this.blur = blur;
    }

    public Map<Integer, String> getColorMap() {
        return colorMap;
    }

    public void setColorMap(Map<Integer, String> colorMap) {
        this.colorMap = colorMap;
    }

    public GlassShape getGlassShape() {
        return glassShape;
    }

    public void setGlassShape(GlassShape glassShape) {
        this.glassShape = glassShape;
    }

    public GlassIconConfig(String name, float scale, int glassIndex, int blur, Map<Integer, String> colorMap, GlassShape glassShape) {
        this.name = name;
        this.scale = scale;
        this.glassIndex = glassIndex;
        this.blur = blur;
        this.colorMap = colorMap;
        this.glassShape = glassShape;
    }

    public GlassIconConfig() {
        this(null, 3f, 0, 5, null, new GlassShape(Color.decode("#0CA064"), new RoundRectangle2D.Double(2, 2, 10, 10, 5, 5), 45f));
    }

    private String name;
    private float scale = 1;
    private int glassIndex = 0;
    private int blur = 5;
    private Map<Integer, String> colorMap;
    private GlassShape glassShape;

    public static class GlassShape implements Serializable {

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public Shape getShape() {
            return shape;
        }

        public void setShape(Shape shape) {
            this.shape = shape;
        }

        public float getRotate() {
            return rotate;
        }

        public void setRotate(float rotate) {
            this.rotate = rotate;
        }

        public GlassShape(Color color, Shape shape, float rotate) {
            this.color = color;
            this.shape = shape;
            this.rotate = rotate;
        }

        public GlassShape() {
        }

        private Color color;
        private Shape shape;
        private float rotate;
    }
}
