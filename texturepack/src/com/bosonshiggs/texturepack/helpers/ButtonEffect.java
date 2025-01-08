package com.bosonshiggs.texturepack.helpers;

import com.google.appinventor.components.common.OptionList;
import java.util.HashMap;
import java.util.Map;

public enum ButtonEffect implements OptionList<String> {
    SCALE("Scale"),
    FADE("Fade");

    private String effectName;

    ButtonEffect(String effectName) {
        this.effectName = effectName;
    }

    @Override
    public String toUnderlyingValue() {
        return effectName;
    }

    private static final Map<String, ButtonEffect> lookup = new HashMap<>();

    static {
        for (ButtonEffect effect : ButtonEffect.values()) {
            lookup.put(effect.toUnderlyingValue(), effect);
        }
    }

    public static ButtonEffect fromUnderlyingValue(String effectName) {
        return lookup.get(effectName);
    }
}

