package raven.glassmorphism;

import com.kitfox.svg.RenderableElement;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGElement;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGUniverse;
import com.kitfox.svg.ShapeElement;
import com.kitfox.svg.animation.AnimationElement;
import com.kitfox.svg.xml.StyleAttribute;
import com.twelvemonkeys.image.ImageUtil;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;

/**
 *
 * @author Raven
 */
public class GlassIcon implements Icon, Serializable {

    private static final SVGUniverse svgUniverse = new SVGUniverse();
    private SVGDiagram diagram;
    private GlassIconConfig glassIconConfig;

    private int iconWidth;
    private int iconHeight;
    private SVGDiagram oldDiagram;
    private float oldScale;
    private int oldGlassIndex;
    private float oldBlur;
    private GlassIconConfig.GlassShape oldGlassShape;
    private Map<Integer, String> oldColorMap;
    private Component component;
    private boolean useComponentColor;
    private Color oldBackground;
    private Color oldForeground;
    private Image imageRender;

    public GlassIcon() {
        this(new GlassIconConfig());
    }

    public GlassIcon(GlassIconConfig glassIconConfig) {
        this.glassIconConfig = glassIconConfig;
        initDiagram();
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        updateImage(c);
        if (glassIconConfig != null && glassIconConfig.getScale() > 0 && imageRender != null) {
            g.drawImage(imageRender, x, y, null);
        }
    }

    @Override
    public int getIconWidth() {
        return iconWidth;
    }

    @Override
    public int getIconHeight() {
        return iconHeight;
    }

    private void updateImage(Component c) {
        if (c != component) {
            component = c;
        }
        try {
            if (glassIconConfig != null && diagram != null && (imageRender == null || glassIconConfig.getGlassIndex() != oldGlassIndex || glassIconConfig.getBlur() != oldBlur || diagram != oldDiagram || glassIconConfig.getScale() != oldScale || glassIconConfig.getColorMap() != oldColorMap || glassIconConfig.getGlassShape() != oldGlassShape || (useComponentColor && (!c.getBackground().equals(oldBackground) || !c.getForeground().equals(oldForeground))))) {
                if (glassIconConfig.getScale() > 0 && iconWidth > 0 && iconHeight > 0) {
                    BufferedImage buff = new BufferedImage(iconWidth, iconHeight, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = buff.createGraphics();
                    installRenderingHin(g2);
                    diagram.setIgnoringClipHeuristic(true);
                    renderElement(g2);
                    oldDiagram = diagram;
                    oldGlassIndex = glassIconConfig.getGlassIndex();
                    oldBlur = glassIconConfig.getBlur();
                    oldScale = glassIconConfig.getScale();
                    oldColorMap = glassIconConfig.getColorMap();
                    oldGlassShape = glassIconConfig.getGlassShape();
                    imageRender = buff;
                    oldBackground = c.getBackground();
                    oldForeground = c.getForeground();
                    g2.dispose();
                }
            }
        } catch (SVGException e) {
            System.err.println(e);
        }
    }

    private void initDiagram() {
        if (glassIconConfig.getName() != null) {
            diagram = loadSvg(svgUniverse.loadSVG(getClass().getResource(glassIconConfig.getName())));
        } else {
            diagram = null;
            imageRender = null;
        }
        initSize();
    }

    private void initSize() {
        if (diagram != null) {
            iconWidth = (int) (diagram.getWidth() * glassIconConfig.getScale());
            iconHeight = (int) (diagram.getHeight() * glassIconConfig.getScale());
        } else {
            iconWidth = 0;
            iconHeight = 0;
        }
        if (component != null) {
            component.revalidate();
        }
    }

    private void renderElement(Graphics2D g2) throws SVGException {
        List<SVGElement> list = diagram.getRoot().getChildren(null);
        useComponentColor = false;
        createGlass(g2, list, glassIconConfig.getGlassIndex());
        int i = -1;
        for (SVGElement e : list) {
            i++;
            if (e instanceof RenderableElement) {
                RenderableElement r = (RenderableElement) e;
                float scale = glassIconConfig.getScale();
                if (i != glassIconConfig.getGlassIndex()) {
                    AffineTransform oldTran = g2.getTransform();
                    g2.scale(scale, scale);
                    String mapColor = getColorMap(i);
                    if (mapColor != null) {
                        r.setAttribute("fill", AnimationElement.AT_XML, mapColor);
                    }
                    r.render(g2);
                    g2.setTransform(oldTran);
                }
            }
        }
    }

    private void createGlass(Graphics2D g2, List<SVGElement> list, int index) throws SVGException {
        int i = -1;
        for (SVGElement e : list) {
            i++;
            if (e instanceof RenderableElement) {
                float scale = glassIconConfig.getScale();
                if (i == glassIconConfig.getGlassIndex()) {
                    ShapeElement sh = (ShapeElement) e;
                    Rectangle rec = sh.getBoundingBox().getBounds();
                    int x = (int) (rec.x * scale);
                    int y = (int) (rec.y * scale);
                    AffineTransform oldTran = g2.getTransform();
                    g2.scale(scale, scale);
                    createStyle(g2);
                    g2.setTransform(oldTran);
                    String mapColor = getColorMap(index);
                    if (mapColor == null) {
                        StyleAttribute attrib = new StyleAttribute("fill");
                        e.getStyle(attrib);
                        mapColor = attrib.getStringValue();
                    }
                    g2.drawImage(createBlurImage(sh.getShape(), rec, Color.decode(mapColor)), x, y, null);
                    return;
                }
            }
        }
    }

    private Image createBlurImage(Shape shape, Rectangle rec, Color color) throws SVGException {
        float scale = glassIconConfig.getScale();
        int w = (int) (rec.width * scale);
        int h = (int) (rec.height * scale);
        BufferedImage blurImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = blurImage.createGraphics();
        installRenderingHin(g2);
        AffineTransform oldTran = g2.getTransform();
        g2.scale(scale, scale);
        g2.translate(-rec.x, -rec.y);
        g2.fill(shape);
        g2.setTransform(oldTran);
        g2.setComposite(AlphaComposite.SrcIn);
        g2.drawImage(ImageUtil.blur(createImage(rec, color), calculateBlur()), 0, 0, null);
        g2.dispose();
        return blurImage;
    }

    private BufferedImage createImage(Rectangle rec, Color color) throws SVGException {
        int b = (int) calculateBlur();
        float scale = glassIconConfig.getScale();
        Rectangle recBlur = new Rectangle(rec.x - b, rec.y - b, rec.width + b * 2, rec.height + b * 2);
        int w = (int) (rec.width * scale);
        int h = (int) (rec.height * scale);
        BufferedImage img = new BufferedImage(w + b, h + b, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        installRenderingHin(g2);
        g2.scale(scale, scale);
        g2.translate(-rec.x, -rec.y);
        g2.setColor(color);
        g2.fill(recBlur);
        createStyle(g2);
        g2.dispose();
        return img;
    }

    private void createStyle(Graphics2D g2) {
        g2.setColor(glassIconConfig.getGlassShape().getColor());
        Rectangle rec = glassIconConfig.getGlassShape().getShape().getBounds();
        int x = rec.x;
        int y = rec.y;
        g2.rotate(Math.toRadians(glassIconConfig.getGlassShape().getRotate()), x + rec.width / 2, y + rec.height / 2);
        g2.fill(glassIconConfig.getGlassShape().getShape());
    }

    private synchronized SVGDiagram loadSvg(URI uri) {
        svgUniverse.removeDocument(uri);
        SVGDiagram dg = svgUniverse.getDiagram(uri);
        return dg;
    }

    private float calculateBlur() {
        return glassIconConfig.getBlur() * glassIconConfig.getScale();
    }

    private void installRenderingHin(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    }

    private String getColorMap(int key) {
        if (glassIconConfig.getColorMap() != null) {
            if (glassIconConfig.getColorMap().containsKey(key)) {
                String v = glassIconConfig.getColorMap().get(key);
                if (v.equals("@background")) {
                    useComponentColor = true;
                    return toColorText(component.getBackground());
                } else if (v.equals("@foreground")) {
                    useComponentColor = true;
                    return toColorText(component.getForeground());
                } else {
                    return glassIconConfig.getColorMap().get(key);
                }
            }
        }
        return null;
    }

    private String toColorText(Color color) {
        String hexColor = "#" + Integer.toHexString(color.getRGB()).substring(2);
        return hexColor;
    }

    private void refresh() {
        if (component != null) {
            component.repaint();
        }
    }

    public Color getBackground() {
        if (component != null) {
            return component.getBackground();
        }
        return null;
    }

    public void setBackground(Color background) {
        if (component != null) {
            component.setBackground(background);
        }
    }

    public Color getForeground() {
        if (component != null) {
            return component.getForeground();
        }
        return null;
    }

    public void setForeground(Color foreground) {
        if (component != null) {
            component.setForeground(foreground);
        }
    }

    public void setName(String name) {
        glassIconConfig.setName(name);
        initDiagram();
        refresh();
    }

    public void setScale(float scale) {
        glassIconConfig.setScale(scale);
        initSize();
        refresh();
    }

    public void setGlassIndex(int index) {
        glassIconConfig.setGlassIndex(index);
        refresh();
    }

    public void setBlur(int blur) {
        glassIconConfig.setBlur(blur);
        refresh();
    }

    public void setColorMap(Map<Integer, String> colorMap) {
        glassIconConfig.setColorMap(colorMap);
        initDiagram();
        refresh();
    }

    public void setGlassShape(GlassIconConfig.GlassShape glassShape) {
        glassIconConfig.setGlassShape(glassShape);
        refresh();
    }

    public GlassIconConfig getGlassIconConfig() {
        return glassIconConfig;
    }

    public void setGlassIconConfig(GlassIconConfig glassIconConfig) {
        this.glassIconConfig = glassIconConfig;
        initDiagram();
        refresh();
    }
}
