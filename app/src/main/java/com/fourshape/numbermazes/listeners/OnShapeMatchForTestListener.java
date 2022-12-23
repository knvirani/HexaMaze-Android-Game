package com.fourshape.numbermazes.listeners;

public interface OnShapeMatchForTestListener {
    void onMatch (int shapeIndex);
    void onUnique ();
    void onIncompleteFill ();
}
