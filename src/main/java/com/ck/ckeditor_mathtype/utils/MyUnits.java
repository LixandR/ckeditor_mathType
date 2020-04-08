package com.ck.ckeditor_mathtype.utils;

/**
 * @author yangk
 * @date 2020/3/23 14:30
 * @description
 */
public class MyUnits {
    public static final double PT_PER_PX = 0.75;
    public static final int IN_PER_PT = 72;
    public static final double CM_PER_PT = 28.3;
    public static final double MM_PER_PT = 2.83;
    public static final int EMU_PER_PX = 9525;

    public static final int pxToEMU(double px) {
        return DoubleUtils.mul(px, EMU_PER_PX).intValue();
    }

    public static final int emuToPx(double emu) {
        return DoubleUtils.div(emu, EMU_PER_PX).intValue();
    }

    public static final double ptToPx(double pt) {
        return DoubleUtils.div(pt, PT_PER_PX);
    }

    public static final double inToPx(double in) {
        return DoubleUtils.mul(inToPt(in), PT_PER_PX);
    }

    public static final double pxToIn(double px) {
        return ptToIn(pxToPt(px));
    }

    public static final double cmToPx(double cm) {
        return DoubleUtils.mul(cmToPt(cm), PT_PER_PX);
    }

    public static final double pxToCm(double px) {
        return ptToCm(pxToPt(px));
    }

    public static final double mmToPx(double mm) {
        return DoubleUtils.mul(mmToPt(mm), PT_PER_PX);
    }

    public static final double pxToMm(double px) {
        return ptToMm(pxToPt(px));
    }

    public static final double ptToIn(double pt) {
        return DoubleUtils.div(pt, IN_PER_PT);
    }

    public static final double ptToMm(double mm) {
        return DoubleUtils.div(mm, MM_PER_PT);
    }

    public static final double ptToCm(double in) {
        return DoubleUtils.div(in, CM_PER_PT);
    }

    public static final double pxToPt(double px) {
        return DoubleUtils.mul(px, PT_PER_PX);
    }

    public static final double inToPt(double in) {
        return DoubleUtils.mul(in, IN_PER_PT);
    }

    public static final double mmToPt(double mm) {
        return DoubleUtils.mul(mm, MM_PER_PT);
    }

    public static final double cmToPt(double cm) {
        return DoubleUtils.mul(cm, CM_PER_PT);
    }
}
