package com.henley.gankio.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * 动画辅助类
 *
 * @author Henley
 * @date 2018/7/13 15:33
 */
public class AnimationHelper {

    private static final long ANIM_DURATION = 250;

    /**
     * 显示FloatingActionButton
     */
    public static void showFloatingActionButton(final View view) {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, View.SCALE_X, 0f, 1.0f, 1.25f, 1.0f, 0.85f, 1.0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f, 1.0f, 1.25f, 1.0f, 0.85f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator1, animator2);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setDuration((long) (ANIM_DURATION * 2.4));
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setVisibility(View.VISIBLE);
            }
        });
        animatorSet.start();
    }

    /**
     * 隐藏FloatingActionButton
     */
    public static void hideFloatingActionButton(final View view) {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.0f, 0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.0f, 0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator1, animator2);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.setDuration(ANIM_DURATION);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });
        animatorSet.start();
    }

}
