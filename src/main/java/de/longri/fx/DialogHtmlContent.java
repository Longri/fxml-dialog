package de.longri.fx;

import javafx.scene.effect.BlendMode;

public class DialogHtmlContent {

    private boolean isHtmlContent = true;
    private String htmlContent;
    private double prefWidth = 200;
    private double prefHeight = 200;
    private BlendMode blendMode;

    public DialogHtmlContent() {
    }

    public DialogHtmlContent(String content) {
        this.setHtmlContent(content);
    }

    public void setHtmlContent(String content) {
        this.htmlContent = content;
    }

    public void setIsHtmlContent(boolean isHtmlContent) {
        this.isHtmlContent = isHtmlContent;
    }

    public String getHtmlContent() {
        return this.htmlContent;
    }

    public boolean isHtmlContent() {
        return this.isHtmlContent;
    }


    /**
     * Convenience method for setting preferred width and height.
     *
     * @param prefWidth  the preferred width
     * @param prefHeight the preferred height
     */
    public void setPrefSize(double prefWidth, double prefHeight) {
        setPrefWidth(prefWidth);
        setPrefHeight(prefHeight);
    }

    public void setPrefWidth(double prefWidth) {
        this.prefWidth = prefWidth;
    }

    public double getPrefWidth() {
        return prefWidth;
    }

    public void setPrefHeight(double prefHeight) {
        this.prefHeight = prefHeight;
    }

    public double getPrefHeight() {
        return prefHeight;
    }

    public void setBlendMode(BlendMode value) {
        this.blendMode = value;
    }

    public BlendMode getBlendMode() {
        return this.blendMode;
    }
}
