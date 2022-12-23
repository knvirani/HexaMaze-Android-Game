package com.fourshape.numbermazes.listeners;

public interface OnRewardedAdItemAccountListener {
    void onItemRewarded (boolean isRewarded);
    void onFailed (int errCode);
}
