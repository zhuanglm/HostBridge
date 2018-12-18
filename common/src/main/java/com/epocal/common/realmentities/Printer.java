package com.epocal.common.realmentities;

import com.epocal.common.types.ConnectionType;
import com.epocal.common.types.PrinterType;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Printer extends RealmObject {

    //persisted

    // TODO: All these need default values

    @PrimaryKey
    private long id;
    private String PrinterName;
    private String PrinterAddress;
    private boolean IsPrintCalculatedResult = false;
    private boolean IsPrintCorrectedResult = false;
    private boolean IsPrintTestInfo = false;
    // enum mapping starts here
    private String Model;
    private String NetworkType;
    private Date LastUsed;

    @Ignore
    private PrinterType thePrinterType;
    @Ignore
    private ConnectionType theConnectionType;


    public Printer() {}

    public Printer(Printer src) {
        if (src != null) {
            id = src.getId();
            PrinterName = src.getPrinterName();
            PrinterAddress = src.getPrinterAddress();
            IsPrintCalculatedResult = src.getIsPrintCalculatedResult();
            IsPrintCorrectedResult = src.getIsPrintCorrectedResult();
            IsPrintTestInfo = src.getIsPrintTestInfo();
            Model = src.getPrinterType();
            NetworkType = src.getPrinterType();
            thePrinterType = src.getThePrinterType();
            theConnectionType = src.getTheConnectionType();
            LastUsed = src.getLastUsed();
        }
    }

    public String getPrinterType() {
        return Model;
    }
    public void setPrinterType(String model) {
        Model = model;
    }

    public PrinterType getThePrinterType() {
        String ptype = getPrinterType();
        if (ptype == null || ptype.isEmpty()) return PrinterType.Unknown;
        return PrinterType.valueOf(ptype);
    }

    public void setThePrinterType(PrinterType printerType) {
        thePrinterType = printerType;
        setPrinterType(printerType.toString());
    }

    // enum mapping ends here
    public String getConnectionType() {
        return NetworkType;
    }
    public void setConnectionType(String networkType) {
        NetworkType = networkType;
    }

    public ConnectionType getTheConnectionType() {
        String ptype = getConnectionType();
        if (ptype == null || ptype.isEmpty()) return ConnectionType.Unknown;
        return ConnectionType.valueOf(ptype);
    }

    public void setTheConnectionType(ConnectionType connectionType) {
        theConnectionType = connectionType;
        setConnectionType(connectionType.toString());
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPrinterName() {
        return PrinterName;
    }

    public void setPrinterName(String printerName) {
       this.PrinterName = printerName;
    }

    public String getPrinterAddress() {
        return PrinterAddress;
    }

    public void setPrinterAddress(String printerAddress) {
        this.PrinterAddress = printerAddress;
    }

    public boolean getIsPrintCalculatedResult() {
        return IsPrintCalculatedResult;
    }

    public void setIsPrintCalculatedResult(boolean isPrintCalculatedResult) {
        this.IsPrintCalculatedResult = isPrintCalculatedResult;
    }

    public boolean getIsPrintCorrectedResult() {
        return IsPrintCorrectedResult;
    }

    public void setIsPrintCorrectedResult(boolean isPrintCorrectedResult) {
        this.IsPrintCorrectedResult = isPrintCorrectedResult;
    }

    public boolean getIsPrintTestInfo() {
        return IsPrintTestInfo;
    }

    public void setIsPrintTestInfo(boolean isPrintTestInfo) {
        this.IsPrintTestInfo = isPrintTestInfo;
    }

    public Date getLastUsed() {
        return LastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        LastUsed = lastUsed;
    }
}
