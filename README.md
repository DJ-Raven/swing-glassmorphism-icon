# Swing Glassmorphism Icon

This project showcases the customization of glassmorphism icons using Java Swing and the `javax.swing.Icon` class, along with the svgSalamander library for SVG icons and common image manipulation techniques to create blur effects.

## Overview

Glassmorphism is a modern design trend that creates a glass-like effect on user interface elements. This project aims to demonstrate how to implement glassmorphism icons in Java Swing applications, providing a visually appealing and modern user interface.

## Features

- Customization of glassmorphism icons using Java Swing and the `javax.swing.Icon` class.
- Integration with the svgSalamander library for seamless handling of SVG icons.
- Implementation of common image manipulation techniques to create blur effects for glassmorphism icons.

## Screenshot

<img src="https://github.com/DJ-Raven/swing-glassmorphism-icon/assets/58245926/200b8a58-12bb-4a8e-91c9-d861b1315215" alt="sample light" width="350"/>
<img src="https://github.com/DJ-Raven/swing-glassmorphism-icon/assets/58245926/95e1d07d-8d68-4fde-b4ee-18dad0eb8a9f" alt="sample dark" width="350"/>

## library used in this project
- common-image-3.9.4.jar
- svgSalamander-1.1.4.jar
## Sample code

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
