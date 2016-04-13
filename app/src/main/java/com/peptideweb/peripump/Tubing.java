package com.peptideweb.peripump;

/**
 * Created by Falendysh on 15.11.2015.
 */
public class Tubing {
    private String tubeSizeLabel;
    private Double tubeProductivity;
    public static final Tubing TUBING_16x48x16 = new Tubing("1.6x4.8x1.6",0.64);
    public static final Tubing TUBING_32x64x16 = new Tubing("3.2x6.4x1.6",2.56);
    public static final Tubing TUBING_48x74x16 = new Tubing("4.8x7.4x1.6",5.76);
    public static final Tubing TUBING_64x96x16 = new Tubing("6.4x9.6x1.6",10.24);
    public static final Tubing TUBING_79x111x16 = new Tubing("7.9x11.1x1.6",15.6025);

    private Tubing(String label, double prod){
        this.tubeProductivity = prod;
        this.tubeSizeLabel = label;
    }

    @Override
    public String toString() {
        return this.tubeSizeLabel;
    }

    public Double getTubeProductivity() {
        return this.tubeProductivity;
    }
}
