# swing-glassmorphism-icon

This icon custom with svg file to make java swing application ui look good.

<img src="https://github.com/DJ-Raven/swing-glassmorphism-icon/assets/58245926/64aa3317-3ef1-4da9-8839-4b1746917dd7" alt="sample light" width="350"/>
<img src="https://github.com/DJ-Raven/swing-glassmorphism-icon/assets/58245926/f2eefb7b-021f-4e8c-8293-03f9ea5d4946" alt="sample dark" width="350"/>

#### library used in this project
- common-image-3.9.4.jar
- svgSalamander-1.1.4.jar
#### Sample code

``` java
// Import
import raven.glassmorphism.GlassIcon;
import raven.glassmorphism.GlassIconConfig;
```
``` java
// Create class object
GlassIcon glassIcon=new GlassIcon();

// Create glassIconConfig
GlassIconConfig glassIconConfig=new GlassIconConfig(name, scale, glassIndex, blur, colorMap, glassShape);

// Create glassShape
GlassIconConfig.GlassShape glassShape=new GlassIconConfig.GlassShape(color, shape, rotate);
```

``` java
// Sample with value
Map<Integer, String> colorMap = new HashMap<>();
colorMap.put(0, "@background");
colorMap.put(1, "@foreground");
colorMap.put(2, "#1EBCCB");

glassIcon.setGlassIconConfig(new GlassIconConfig("/glassicon/icon.svg", 5f, 0, 5, colorMap,
        new GlassIconConfig.GlassShape(Color.decode("#e7e510"), new RoundRectangle2D.Double(2, 2, 10, 10, 5, 5), 45)
));
```
