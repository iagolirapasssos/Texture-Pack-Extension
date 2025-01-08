package com.bosonshiggs.texturepack;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.widget.Button;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation;

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
        version = 3,
        versionName = "3.0",
        description = "An extension to apply textures, animations, and customize buttons in MIT App Inventor.",
        iconName = "icon.png"
)
public class CustomButtonTexture extends AndroidNonvisibleComponent {

    private final ComponentContainer container;

    public CustomButtonTexture(ComponentContainer container) {
        super(container.$form());
        this.container = container;
    }

    @SimpleFunction(description = "Applies a texture from the assets to the specified button.")
    public void ApplyTexture(AndroidViewComponent buttonComponent, @Asset String texturePath) {
        if (!(buttonComponent.getView() instanceof Button)) {
            throw new IllegalArgumentException("The provided component is not a Button.");
        }

        Button button = (Button) buttonComponent.getView();
        try {
            BitmapDrawable texture = (BitmapDrawable) MediaUtil.getBitmapDrawable(container.$form(), texturePath);
            button.setBackground(texture);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to load texture from assets. Check the file path: " + texturePath);
        }
    }

    @SimpleFunction(description = "Applies a texture to the button text from the assets.")
    public void ApplyTextTexture(AndroidViewComponent buttonComponent, @Asset String texturePath) {
        if (!(buttonComponent.getView() instanceof Button)) {
            throw new IllegalArgumentException("The provided component is not a Button.");
        }

        Button button = (Button) buttonComponent.getView();
        try {
            BitmapDrawable texture = (BitmapDrawable) MediaUtil.getBitmapDrawable(container.$form(), texturePath);
            TextPaint paint = button.getPaint();
            paint.setShader(new android.graphics.BitmapShader(texture.getBitmap(), android.graphics.Shader.TileMode.REPEAT, android.graphics.Shader.TileMode.REPEAT));
            button.invalidate();
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to load texture from assets. Check the file path: " + texturePath);
        }
    }

    @SimpleFunction(description = "Resets the button to its default background.")
    public void ResetTexture(AndroidViewComponent buttonComponent) {
        if (!(buttonComponent.getView() instanceof Button)) {
            throw new IllegalArgumentException("The provided component is not a Button.");
        }

        Button button = (Button) buttonComponent.getView();
        button.setBackground(null);
    }

    @SimpleFunction(description = "Sets the font size of the button text.")
    public void FontSize(AndroidViewComponent buttonComponent, float fontSize) {
        if (!(buttonComponent.getView() instanceof Button)) {
            throw new IllegalArgumentException("The provided component is not a Button.");
        }

        Button button = (Button) buttonComponent.getView();
        button.setTextSize(fontSize);
    }

    @SimpleFunction(description = "Applies a touch effect to the button.")
	public void ApplyTouchEffect(AndroidViewComponent buttonComponent, @Options(ButtonEffect.class) String effect) {
		if (!(buttonComponent.getView() instanceof Button)) {
		    throw new IllegalArgumentException("The provided component is not a Button.");
		}

		Button button = (Button) buttonComponent.getView();

		try {
		    ButtonEffect selectedEffect = ButtonEffect.fromUnderlyingValue(effect);

		    switch (selectedEffect) {
		        case SCALE:
		            button.setOnTouchListener((v, event) -> {
		                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
		                    ScaleAnimation scaleDown = new ScaleAnimation(
		                            1.0f, 0.9f, 1.0f, 0.9f,
		                            Animation.RELATIVE_TO_SELF, 0.5f,
		                            Animation.RELATIVE_TO_SELF, 0.5f);
		                    scaleDown.setDuration(100);
		                    scaleDown.setFillAfter(true);
		                    button.startAnimation(scaleDown);
		                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP || event.getAction() == android.view.MotionEvent.ACTION_CANCEL) {
		                    ScaleAnimation scaleUp = new ScaleAnimation(
		                            0.9f, 1.0f, 0.9f, 1.0f,
		                            Animation.RELATIVE_TO_SELF, 0.5f,
		                            Animation.RELATIVE_TO_SELF, 0.5f);
		                    scaleUp.setDuration(100);
		                    scaleUp.setFillAfter(true);
		                    button.startAnimation(scaleUp);
		                }
		                return false;
		            });
		            break;

		        case FADE:
		            button.setOnTouchListener((v, event) -> {
		                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
		                    AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.5f);
		                    fadeOut.setDuration(100);
		                    fadeOut.setFillAfter(true);
		                    button.startAnimation(fadeOut);
		                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP || event.getAction() == android.view.MotionEvent.ACTION_CANCEL) {
		                    AlphaAnimation fadeIn = new AlphaAnimation(0.5f, 1.0f);
		                    fadeIn.setDuration(100);
		                    fadeIn.setFillAfter(true);
		                    button.startAnimation(fadeIn);
		                }
		                return false;
		            });
		            break;
		    }
		} catch (IllegalArgumentException e) {
		    throw new IllegalArgumentException("Invalid effect: " + effect + ". Valid options are: Scale, Fade.");
		}
	}


}

