package com.hault.codex_java.ui.util;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.core.widget.ImageViewCompat;

import java.util.ArrayList;
import java.util.List;

public class ColorPickerHelper {

    private final List<CardView> colorViews = new ArrayList<>();
    private String selectedColorHex = null;
    private CardView selectedView = null;

    public ColorPickerHelper(ViewGroup container, String initialColorHex) {
        this.selectedColorHex = initialColorHex;
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (child instanceof CardView) {
                CardView cardView = (CardView) child;
                colorViews.add(cardView);
                cardView.setOnClickListener(this::onColorSelected);

                String colorString = String.format("#%06X", (0xFFFFFF & cardView.getCardBackgroundColor().getDefaultColor()));
                if (initialColorHex != null && initialColorHex.equalsIgnoreCase(colorString)) {
                    selectView(cardView);
                }
            }
        }
    }

    private void onColorSelected(View view) {
        if (!(view instanceof CardView)) return;
        CardView cardView = (CardView) view;

        if (selectedView == cardView) { // Deselect if the same color is tapped again
            clearSelection();
        } else { // Select a new color
            if (selectedView != null) {
                getCheckMark(selectedView).setVisibility(View.GONE);
            }
            selectView(cardView);
        }
    }

    private void selectView(CardView cardView) {
        selectedView = cardView;
        ImageView checkMark = getCheckMark(selectedView);
        checkMark.setVisibility(View.VISIBLE);

        int color = cardView.getCardBackgroundColor().getDefaultColor();
        selectedColorHex = String.format("#%06X", (0xFFFFFF & color));

        // Make checkmark visible against dark colors
        if (isColorDark(color)) {
            ImageViewCompat.setImageTintList(checkMark, ColorStateList.valueOf(Color.WHITE));
        } else {
            ImageViewCompat.setImageTintList(checkMark, ColorStateList.valueOf(Color.BLACK));
        }
    }

    public void clearSelection() {
        if (selectedView != null) {
            getCheckMark(selectedView).setVisibility(View.GONE);
        }
        selectedView = null;
        selectedColorHex = null;
    }

    public String getSelectedColorHex() {
        return selectedColorHex;
    }

    private ImageView getCheckMark(CardView cardView) {
        return (ImageView) cardView.getChildAt(0);
    }

    private boolean isColorDark(int color){
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }
}