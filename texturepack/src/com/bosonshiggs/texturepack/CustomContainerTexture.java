package com.bosonshiggs.texturepack;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.ViewGroup;
import android.graphics.Paint;
import android.graphics.RectF;

import com.google.appinventor.components.annotations.Asset;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.AndroidViewComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.util.MediaUtil;


import java.io.IOException;

@DesignerComponent(
        version = 3,
        versionName = "3.0",
        description = "An extension to apply textures, customize borders, and round corners for containers in MIT App Inventor without affecting their children components.",
        iconName = "icon.png"
)
public class CustomContainerTexture extends AndroidNonvisibleComponent {

    private final ComponentContainer container;

    public CustomContainerTexture(ComponentContainer container) {
        super(container.$form());
        this.container = container;
    }

    @SimpleFunction(description = "Applies a texture and optional rounded corners to the specified container without affecting its children components.")
	public void ApplyTexture(AndroidViewComponent containerComponent, @Asset String texturePath, float cornerRadius, float borderWidth, int borderColor) {
		if (!(containerComponent.getView() instanceof ViewGroup)) {
		    throw new IllegalArgumentException("The provided component is not a Container.");
		}

		ViewGroup containerView = (ViewGroup) containerComponent.getView();
		try {
		    Bitmap bitmap = MediaUtil.getBitmapDrawable(container.$form(), texturePath).getBitmap();
		    android.graphics.BitmapShader shader = new android.graphics.BitmapShader(
		            bitmap,
		            android.graphics.Shader.TileMode.REPEAT,
		            android.graphics.Shader.TileMode.REPEAT
		    );

		    // Configurar os cantos arredondados
		    float[] radii = new float[]{cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius, cornerRadius};
		    RectF inset = borderWidth > 0 ? new RectF(borderWidth, borderWidth, borderWidth, borderWidth) : null;

		    // Fundo com textura
		    RoundRectShape shapeWithTexture = new RoundRectShape(radii, inset, null);
		    ShapeDrawable textureDrawable = new ShapeDrawable(shapeWithTexture);
		    textureDrawable.getPaint().setShader(shader);

		    // Configurar a borda, se necessário
		    if (borderWidth > 0) {
		        RoundRectShape shapeWithBorder = new RoundRectShape(radii, null, null);
		        ShapeDrawable borderDrawable = new ShapeDrawable(shapeWithBorder);
		        borderDrawable.getPaint().setStyle(Paint.Style.STROKE);
		        borderDrawable.getPaint().setStrokeWidth(borderWidth);
		        borderDrawable.getPaint().setColor(borderColor);

		        // Combinar borda e textura
		        containerView.setBackground(new android.graphics.drawable.LayerDrawable(new Drawable[]{borderDrawable, textureDrawable}));
		    } else {
		        // Apenas textura
		        containerView.setBackground(textureDrawable);
		    }
		} catch (IOException e) {
		    throw new IllegalArgumentException("Failed to load texture from assets. Check the file path: " + texturePath);
		}
	}


    @SimpleFunction(description = "Sets the padding of the container to ensure the visibility of its children components.")
    public void SetPadding(AndroidViewComponent containerComponent, int left, int top, int right, int bottom) {
        if (!(containerComponent.getView() instanceof ViewGroup)) {
            throw new IllegalArgumentException("The provided component is not a Container.");
        }

        ViewGroup containerView = (ViewGroup) containerComponent.getView();
        containerView.setPadding(left, top, right, bottom);
    }

    @SimpleFunction(description = "Returns the number of child components inside the container.")
    public int GetChildCount(AndroidViewComponent containerComponent) {
        if (!(containerComponent.getView() instanceof ViewGroup)) {
            throw new IllegalArgumentException("The provided component is not a Container.");
        }

        ViewGroup containerView = (ViewGroup) containerComponent.getView();
        return containerView.getChildCount();
    }

    @SimpleFunction(description = "Sets the background color of the container to improve visibility.")
    public void SetBackgroundColor(AndroidViewComponent containerComponent, int color) {
        if (!(containerComponent.getView() instanceof ViewGroup)) {
            throw new IllegalArgumentException("The provided component is not a Container.");
        }

        ViewGroup containerView = (ViewGroup) containerComponent.getView();
        containerView.setBackgroundColor(color);
    }
}

