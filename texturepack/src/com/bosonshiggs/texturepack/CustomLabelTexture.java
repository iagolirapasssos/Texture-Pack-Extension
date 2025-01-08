package com.bosonshiggs.texturepack;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;
import android.widget.TextView;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.google.appinventor.components.annotations.Asset;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.Options;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.util.MediaUtil;

import com.bosonshiggs.texturepack.helpers.ButtonEffect;

import java.io.IOException;

@DesignerComponent(
        version = 1,
        versionName = "1.0",
        description = "An extension to apply textures, animations, and customize labels in MIT App Inventor.",
        iconName = "icon.png"
)
public class CustomLabelTexture extends AndroidNonvisibleComponent {

    private final ComponentContainer container;

    public CustomLabelTexture(ComponentContainer container) {
        super(container.$form());
        this.container = container;
    }

    @SimpleFunction(description = "Applies a texture from the assets to the specified label.")
    public void ApplyTexture(AndroidViewComponent labelComponent, @Asset String texturePath) {
        if (!(labelComponent.getView() instanceof TextView)) {
            throw new IllegalArgumentException("The provided component is not a Label.");
        }

        TextView label = (TextView) labelComponent.getView();
        try {
            BitmapDrawable texture = (BitmapDrawable) MediaUtil.getBitmapDrawable(container.$form(), texturePath);
            label.setBackground(texture);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to load texture from assets. Check the file path: " + texturePath);
        }
    }

    @SimpleFunction(description = "Applies a texture to the label text from the assets.")
    public void ApplyTextTexture(AndroidViewComponent labelComponent, @Asset String texturePath) {
        if (!(labelComponent.getView() instanceof TextView)) {
            throw new IllegalArgumentException("The provided component is not a Label.");
        }

        TextView label = (TextView) labelComponent.getView();
        try {
            BitmapDrawable texture = (BitmapDrawable) MediaUtil.getBitmapDrawable(container.$form(), texturePath);
            TextPaint paint = label.getPaint();
            paint.setShader(new android.graphics.BitmapShader(texture.getBitmap(), android.graphics.Shader.TileMode.REPEAT, android.graphics.Shader.TileMode.REPEAT));
            label.invalidate();
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to load texture from assets. Check the file path: " + texturePath);
        }
    }

    @SimpleFunction(description = "Sets the font size of the label text.")
    public void SetFontSize(AndroidViewComponent labelComponent, float fontSize) {
        if (!(labelComponent.getView() instanceof TextView)) {
            throw new IllegalArgumentException("The provided component is not a Label.");
        }

        TextView label = (TextView) labelComponent.getView();
        label.setTextSize(fontSize);
    }

    @SimpleFunction(description = "Centers the text within the label.")
    public void CenterText(AndroidViewComponent labelComponent) {
        if (!(labelComponent.getView() instanceof TextView)) {
            throw new IllegalArgumentException("The provided component is not a Label.");
        }

        TextView label = (TextView) labelComponent.getView();
        label.setGravity(Gravity.CENTER);
    }

    @SimpleFunction(description = "Applies an animation effect to the label text.")
    public void ApplyTextEffect(AndroidViewComponent labelComponent, @Options(ButtonEffect.class) String effect) {
        if (!(labelComponent.getView() instanceof TextView)) {
            throw new IllegalArgumentException("The provided component is not a Label.");
        }

        TextView label = (TextView) labelComponent.getView();

        try {
            ButtonEffect selectedEffect = ButtonEffect.fromUnderlyingValue(effect);

            switch (selectedEffect) {
                case SCALE:
                    ScaleAnimation scaleAnimation = new ScaleAnimation(
                            1.0f, 1.2f, 1.0f, 1.2f,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setRepeatMode(Animation.REVERSE);
                    scaleAnimation.setRepeatCount(1);
                    label.startAnimation(scaleAnimation);
                    break;

                case FADE:
                    AlphaAnimation fadeAnimation = new AlphaAnimation(1.0f, 0.5f);
                    fadeAnimation.setDuration(200);
                    fadeAnimation.setRepeatMode(Animation.REVERSE);
                    fadeAnimation.setRepeatCount(1);
                    label.startAnimation(fadeAnimation);
                    break;
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid effect: " + effect + ". Valid options are: Scale, Fade.");
        }
    }
}

