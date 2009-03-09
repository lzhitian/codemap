package ch.deif.meander.ui;

import ch.deif.meander.MapVisualization;
import processing.core.PApplet;

public class Applet {

    @SuppressWarnings("serial")
    public static class MapViz extends PApplet {

        private MapVisualization viz;

        public MapViz(MapVisualization viz) {
            this.viz = viz;
        }

        @Override
        public void setup() {
            size(viz.map.getParameters().width, viz.map.getParameters().height);
            frameRate(1);
        }

        @Override
        public void draw() {
            viz.draw(g);
        }
    }

    /**
     * Example Papplet class found at: http://abandonedart.org/?p=275
     */
    @SuppressWarnings("serial")
    public static class GlitchSticks extends PApplet {
        // Jan 2009
        // http://www.abandonedart.org
        // http://www.actionscripter.co.uk

        // ================================= global vars

        float _x = 0;
        float _y = 0;
        float _angle = 0;
        float _lasty = 250;
        float _lastx = 250;

        float _a = 5;
        float _b = 3;

        // ================================= init

        public void setup() {
            frameRate(60);
            size(500, 300);
            background(255);
            smooth();
        }

        // ================================= frame loop

        public void draw() {
            _angle += 0.5;
            if (_angle > 360) {
                _angle -= 360;
            }

            float preva = _a;
            float prevb = _b;
            _a = mouseX / 10;
            _b = mouseY / 10;

            if ((_a != preva) || (_b != prevb)) {
                strokeCap(PROJECT);
                strokeWeight((int) random(55) + 5);
                stroke(random(255));
            } else {
                strokeCap(ROUND);
                strokeWeight(1);
                stroke(255, 150);
            }

            _x = sin(_a * radians(_angle) + PI / 2) * 280;
            _y = sin(_b * radians(_angle)) * 180;
            _x += 250;
            _y += 150;

            line(_x, _y, _lastx, _lasty);

            _lastx = _x;
            _lasty = _y;
        }

        // ================================= interaction

        public void mousePressed() {
            background(255);
        }

    }

}
