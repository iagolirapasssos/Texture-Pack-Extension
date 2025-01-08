package com.bosonshiggs.texturepack;

import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.appinventor.components.annotations.Asset;
import com.google.appinventor.components.runtime.util.MediaUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;

import java.io.IOException;

@DesignerComponent(
    version = 2,
    versionName = "2.0",
    description = "An extension to create a customizable linear progress bar with adaptive colors and textures.",
    iconName = "icon.png"
)
public class CustomProgressBar extends AndroidNonvisibleComponent {

    private final ComponentContainer container;
    private ProgressBar progressBar;
    private FrameLayout wrapper;
    private GradientDrawable progressDrawable;
    private GradientDrawable backgroundDrawable;

    private int progressColor = Color.GREEN; // Default progress color
    private int backgroundColor = Color.LTGRAY; // Default background color
    private int cornerRadius = 50; // Default corner radius

    public CustomProgressBar(ComponentContainer container) {
        super(container.$form());
        this.container = container;
    }

    @SimpleFunction(description = "Creates a progress bar with the same dimensions as the given component.")
    public void CreateLinearProgressBar(AndroidViewComponent component) {
        ViewGroup parent = (ViewGroup) component.getView().getParent();

        if (parent == null) {
            throw new IllegalStateException("The component must be inside a layout.");
        }

        // Get dimensions of the target component
        int width = component.getView().getWidth();
        int height = component.getView().getHeight();

        // Create a wrapper FrameLayout
        if (wrapper == null) {
            wrapper = new FrameLayout(container.$context());
            wrapper.setLayoutParams(new FrameLayout.LayoutParams(width, height));

            // Add wrapper above or around the target component
            parent.addView(wrapper, parent.indexOfChild(component.getView()));
        }

        // Create and style the ProgressBar
        progressBar = new ProgressBar(container.$context(), null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setIndeterminate(false);
        progressBar.setMax(100);

        // Configure default drawables
        backgroundDrawable = new GradientDrawable();
        backgroundDrawable.setColor(backgroundColor);
        backgroundDrawable.setCornerRadius(cornerRadius);

        progressDrawable = new GradientDrawable();
        progressDrawable.setColor(progressColor);
        progressDrawable.setCornerRadius(cornerRadius);

        ClipDrawable clipDrawable = new ClipDrawable(progressDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{backgroundDrawable, clipDrawable});
        layerDrawable.setId(0, android.R.id.background);
        layerDrawable.setId(1, android.R.id.progress);

        // Set the drawable to the progress bar
        progressBar.setProgressDrawable(layerDrawable);

        // Configure layout parameters
        FrameLayout.LayoutParams progressParams = new FrameLayout.LayoutParams(width, height);
        progressBar.setLayoutParams(progressParams);

        // Add progress bar to the wrapper
        wrapper.addView(progressBar);
    }

    @SimpleFunction(description = "Sets a texture for the progress bar from the assets.")
    public void  ProgressTexture(@Asset String texturePath) {
        try {
            BitmapDrawable texture = (BitmapDrawable) MediaUtil.getBitmapDrawable(container.$form(), texturePath);
            ClipDrawable clipDrawable = new ClipDrawable(texture, Gravity.LEFT, ClipDrawable.HORIZONTAL);

            if (progressBar != null) {
                LayerDrawable layerDrawable = (LayerDrawable) progressBar.getProgressDrawable();
                layerDrawable.setDrawableByLayerId(android.R.id.progress, clipDrawable);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to load texture from assets. Check the file path: " + texturePath);
        }
    }

    @SimpleFunction(description = "Sets a texture for the background of the progress bar from the assets.")
    public void  BackgroundTexture(@Asset String texturePath) {
        try {
            BitmapDrawable texture = (BitmapDrawable) MediaUtil.getBitmapDrawable(container.$form(), texturePath);

            if (progressBar != null) {
                LayerDrawable layerDrawable = (LayerDrawable) progressBar.getProgressDrawable();
                layerDrawable.setDrawableByLayerId(android.R.id.background, texture);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to load texture from assets. Check the file path: " + texturePath);
        }
    }

    @SimpleFunction(description = "Sets the progress value (0-100).")
    public void  Progress(int progress) {
        if (progressBar != null) {
            progressBar.setProgress(progress);
        } else {
            throw new IllegalStateException("Progress bar not created. Use CreateLinearProgressBar first.");
        }
    }

    @SimpleProperty(description = "Sets the color of the progress bar. Accepts integers only.")
    public void  ProgressColor(int color) {
        try {
            progressColor = color;
            if (progressDrawable != null) {
                progressDrawable.setColor(progressColor);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid color format. Use integers only.");
        }
    }

    @SimpleProperty(description = "Sets the background color of the progress bar. Accepts integers only.")
    public void  BackgroundColor(int color) {
        try {
            backgroundColor = color;
            if (backgroundDrawable != null) {
                backgroundDrawable.setColor(backgroundColor);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid color format. Use integers only.");
        }
    }

    @SimpleProperty(description = "Sets the corner radius of the progress bar.")
    public void  CornerRadius(int radius) {
        if (radius >= 0) {
            cornerRadius = radius;
            if (progressDrawable != null) {
                progressDrawable.setCornerRadius(cornerRadius);
            }
            if (backgroundDrawable != null) {
                backgroundDrawable.setCornerRadius(cornerRadius);
            }
        } else {
            throw new IllegalArgumentException("Corner radius must be non-negative.");
        }
    }

    @SimpleProperty(description = "Gets the current progress value.")
    public int GetProgress() {
        if (progressBar != null) {
            return progressBar.getProgress();
        }
        return 0;
    }
}

