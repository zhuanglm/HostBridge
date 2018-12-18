package com.epocal.common.realmentities;

import com.epocal.common.types.CardFactoryType;
import com.epocal.common.types.CardType;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by bmate on 5/29/2017.
 */

public class CardLot extends RealmObject {
    @PrimaryKey
    private long id =-1;
    private String lotNumber;
    private Date manufacturedDate;
    private Date expirationDate;
    private int subLot;
    private int extraInfo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public Date getManufacturedDate() {
        return manufacturedDate;
    }

    public void setManufacturedDate(Date manufacturedDate) {
        this.manufacturedDate = manufacturedDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getSubLot() {
        return subLot;
    }

    public void setSubLot(int subLot) {
        this.subLot = subLot;
    }

    public int getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(int extraInfo) {
        this.extraInfo = extraInfo;
    }

    @Ignore
    private CardFactoryType factoryType;
    private int cardFactoryType;

    public CardFactoryType getFactoryType() {
        return CardFactoryType.fromInt(getCardFactoryType());
    }

    public void setFactoryType(CardFactoryType factoryType) {
        setCardFactoryType(factoryType.value);
    }

    private int getCardFactoryType() {
        return cardFactoryType;
    }

    private void setCardFactoryType(int cardFactoryType) {
        this.cardFactoryType = cardFactoryType;
    }

    @Ignore
    private CardType cardArt;
    private int cardType;

    public CardType getCardArt() {
        return CardType.fromInt(getCardType());
    }

    public void setCardArt(CardType cardArt) {
        setCardType(cardArt.value);
    }

    private int getCardType() {
        return cardType;
    }

    private void setCardType(int cardType) {
        this.cardType = cardType;
    }


}
