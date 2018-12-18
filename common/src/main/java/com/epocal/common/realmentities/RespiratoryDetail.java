package com.epocal.common.realmentities;

import com.epocal.common.types.am.AllensTest;
import com.epocal.common.types.am.DeliverySystem;
import com.epocal.common.types.am.DrawSites;
import com.epocal.common.types.am.RespiratoryMode;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bmate on 5/29/2017.
 */

public class RespiratoryDetail extends RealmObject {
    @PrimaryKey
    private long id =-1;
    private String customAllensTest = "";
    private String customDeliverySystem = "";
    private String customDrawSite = "";
    private String customMode = "";
    private String expiratoryTime = "";
    private String inspiratoryTime = "";
    private String meanAirWayPressure = "";
    private String peep = "";
    private String peakInspiratoryPressure = "";
    private String ps = "";
    private String respiratoryRate = "";
    private String totalRespiratoryRate = "";
    private String tidalVolume = "";
    private String fiO2 = "";
    private String flow = "";
    private String hertz = "";
    private String deltaP = "";
    private String noPPM = "";
    private String rq = "";
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustomAllensTest() {
        return customAllensTest;
    }

    public void setCustomAllensTest(String customAllensTest) {
        this.customAllensTest = customAllensTest;
    }

    public String getCustomDeliverySystem() {
        return customDeliverySystem;
    }

    public void setCustomDeliverySystem(String customDeliverySystem) {
        this.customDeliverySystem = customDeliverySystem;
    }

    public String getCustomDrawSite() {
        return customDrawSite;
    }

    public void setCustomDrawSite(String customDrawSite) {
        this.customDrawSite = customDrawSite;
    }

    public String getCustomMode() {
        return customMode;
    }

    public void setCustomMode(String customMode) {
        this.customMode = customMode;
    }

    public String getExpiratoryTime() {
        return expiratoryTime;
    }

    public void setExpiratoryTime(String expiratoryTime) {
        this.expiratoryTime = expiratoryTime;
    }

    public String getInspiratoryTime() {
        return inspiratoryTime;
    }

    public void setInspiratoryTime(String inspiratoryTime) {
        this.inspiratoryTime = inspiratoryTime;
    }

    public String getMeanAirWayPressure() {
        return meanAirWayPressure;
    }

    public void setMeanAirWayPressure(String meanAirWayPressure) {
        this.meanAirWayPressure = meanAirWayPressure;
    }

    public String getPeep() {
        return peep;
    }

    public void setPeep(String peep) {
        this.peep = peep;
    }

    public String getPeakInspiratoryPressure() {
        return peakInspiratoryPressure;
    }

    public void setPeakInspiratoryPressure(String peakInspiratoryPressure) {
        this.peakInspiratoryPressure = peakInspiratoryPressure;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public String getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(String respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public String getTotalRespiratoryRate() {
        return totalRespiratoryRate;
    }

    public void setTotalRespiratoryRate(String totalRespiratoryRate) {
        this.totalRespiratoryRate = totalRespiratoryRate;
    }

    public String getTidalVolume() {
        return tidalVolume;
    }

    public void setTidalVolume(String tidalVolume) {
        this.tidalVolume = tidalVolume;
    }

    public String getFiO2() {
        return fiO2;
    }

    public void setFiO2(String fiO2) {
        this.fiO2 = fiO2;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public String getHertz() {
        return hertz;
    }

    public void setHertz(String hertz) {
        this.hertz = hertz;
    }

    public String getDeltaP() {
        return deltaP;
    }

    public void setDeltaP(String deltaP) {
        this.deltaP = deltaP;
    }

    public String getNoPPM() {
        return noPPM;
    }

    public void setNoPPM(String noPPM) {
        this.noPPM = noPPM;
    }

    public String getRq() {
        return rq;
    }

    public void setRq(String rq) {
        this.rq = rq;
    }

    @Ignore
    private AllensTest respAllensType ;
    private int allensType = AllensTest.ENUM_UNINITIALIZED.value;

    public AllensTest getRespAllensType() {
        return AllensTest.fromInt(getAllensType());
    }
    public void setRespAllensType(AllensTest respAllensType) {
        setAllensType(respAllensType.value);
    }
    private int getAllensType() {
        return allensType;
    }
    private void setAllensType(int allensType) {
        this.allensType = allensType;
    }

    @Ignore
    private DeliverySystem deliverySystem;
    private int deliverySystemType = DeliverySystem.ENUM_UNINITIALIZED.value;

    public DeliverySystem getDeliverySystem() {
       return DeliverySystem.fromInt(getDeliverySystemType());
    }
    public void setDeliverySystem(DeliverySystem deliverySystem) {
        setDeliverySystemType(deliverySystem.value);
    }
    private int getDeliverySystemType() {
        return deliverySystemType;
    }
    private void setDeliverySystemType(int deliverySystemType) {
        this.deliverySystemType = deliverySystemType;
    }

    @Ignore
    private DrawSites drawSite;
    private int drawSiteType = DrawSites.ENUM_UNINITIALIZED.value;

    public DrawSites getDrawSite() {
        return DrawSites.fromInt(getDrawSiteType());
    }
    public void setDrawSite(DrawSites drawSite) {
        setDrawSiteType(drawSite.value);
    }
    private int getDrawSiteType() {
        return drawSiteType;
    }
    private void setDrawSiteType(int drawSiteType) {
        this.drawSiteType = drawSiteType;
    }

    @Ignore
    private RespiratoryMode respiratoryMode;
    private int respiratoryModeType = RespiratoryMode.ENUM_UNINITIALIZED.value;

    public RespiratoryMode getRespiratoryMode() {
        return RespiratoryMode.fromInt(getRespiratoryModeType());
    }
    public void setRespiratoryMode(RespiratoryMode respiratoryMode) {
        setRespiratoryModeType(respiratoryMode.value);
    }
    private int getRespiratoryModeType() {
        return respiratoryModeType;
    }
    private void setRespiratoryModeType(int respiratoryModeType) {
        this.respiratoryModeType = respiratoryModeType;
    }

    public void updateFrom(RespiratoryDetail input){
        if (input!=null){
        this.setCustomAllensTest(input.getCustomAllensTest());
        this.setCustomDeliverySystem(input.getCustomDeliverySystem());
        this.setCustomDrawSite(input.getCustomDrawSite());
        this.setCustomMode(input.getCustomMode());
        this.setDeliverySystem(input.getDeliverySystem());
        this.setDeltaP(input.getDeltaP());
        this.setDrawSite(input.getDrawSite());
        this.setExpiratoryTime(input.getExpiratoryTime());
        this.setFiO2(input.getFiO2());
        this.setFlow(input.getFlow());
        this.setHertz(input.getHertz());
        this.setInspiratoryTime(input.getInspiratoryTime());
        this.setMeanAirWayPressure(input.getMeanAirWayPressure());
        this.setNoPPM(input.getNoPPM());
        this.setPeakInspiratoryPressure(input.getPeakInspiratoryPressure());
        this.setPeep(input.getPeep());
        this.setPs(input.getPs());
        this.setRespAllensType(input.getRespAllensType());
        this.setRespiratoryMode(input.getRespiratoryMode());
        this.setRespiratoryRate(input.getRespiratoryRate());
        this.setTidalVolume(input.getTidalVolume());
        this.setTotalRespiratoryRate(input.getTotalRespiratoryRate());
        this.setRq(input.getRq());
        }

    }
}
