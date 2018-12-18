package com.epocal.common.realmentities;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EVADCardLotDescription extends RealmObject {
    @PrimaryKey
    private long id =-1;
    private EVAD evad;
    private String CardLotDescriptor;
    private Date created;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EVAD getEvad() {
        return evad;
    }

    public void setEvad(EVAD evad) {
        this.evad = evad;
    }

    public String getCardLotDescriptor() {
        return CardLotDescriptor;
    }

    public void setCardLotDescriptor(String cardLotDescriptor) {
        CardLotDescriptor = cardLotDescriptor;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
