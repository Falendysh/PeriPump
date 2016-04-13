package com.peptideweb.peripump;

/**
 * Created by Falendysh on 26.10.2015.
 */
public class PumpParamSetting {

    public static final PumpParamSetting INSTANCE = new PumpParamSetting();

    private double Tubing = 0;
    private int FlowRate = 0;
    private long RotationSpeed = 0;
    private int Direction = 0; // 0 clockwise, 1 counterclockwise



    public static double TUBING_16x48x16 = Math.PI*Math.pow(0.8,2);
    public static double TUBING_32x64x16 = Math.PI*Math.pow(1.6,2);
    public static double TUBING_48x74x16 = Math.PI*Math.pow(2.4,2);
    public static double TUBING_64x96x16 = Math.PI*Math.pow(3.2,2);
    public static double TUBING_79x111x16 = Math.PI*Math.pow(3.95,2);


    private PumpParamSetting(){

    }

    public static PumpParamSetting getInstance(){
        return INSTANCE;
    }

    public void setTubing(double tubing){
        this.Tubing = tubing;
        this.RotationSpeed = Math.round(this.FlowRate / this.Tubing);
    }

    public void setFlowRate(int flowRate){
        this.FlowRate = flowRate;
        this.RotationSpeed = Math.round(this.FlowRate / this.Tubing);

    }


    public void setDirection(int direction){
        this.Direction = direction;
    }

    public double getTubing(){
        return this.Tubing;
    }

    public int getFlowRate(){
        return this.FlowRate;
    }

    public long getRotationSpeed(){

        return this.RotationSpeed;
    }







}
