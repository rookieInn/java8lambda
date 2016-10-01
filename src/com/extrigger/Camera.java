package com.extrigger;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by gxy on 2016/8/24.
 */
public class Camera {

    private Function<Color, Color> filter;

    public Camera() {
        setFilter();
    }

    public Color capture(Color inputColor) {
        Color processedColor = filter.apply(inputColor);
        //... more processed of Color
        return processedColor;
    }

    public void setFilter(Function<Color, Color> ... filters) {
        filter = Arrays.asList(filters)
                        .stream()
                        .reduce((filter, next) -> filter.compose(next))
                        .orElse(color -> color);
    }

    //other functions that use the filter

    public static void main(String[] args) {
        Camera camera = new Camera();
        Consumer<String> printCaptured = (filterInfo) -> System.out.println(String.format("with %s: %s", filterInfo, camera.capture(new Color(200, 100, 200))));
        printCaptured.accept("no filter");

        camera.setFilter(Color::brighter);
        printCaptured.accept("brighter filter");

        camera.setFilter(Color::darker);
        printCaptured.accept("darker filter");

        //add multiple filters
        camera.setFilter(Color::brighter, Color::darker);
        printCaptured.accept("brighter & darker filter");
    }

}
