package com.epocal.common.realmentities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DMSetting extends RealmObject {
        @PrimaryKey
        private long id =-1;
        private String address;
        private String port;
        private boolean present;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String theAddress) {
            this.address = theAddress;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String thePort) {
            this.port = thePort;
        }

        public boolean getPresent() {
            return present;
        }

        public void setPresent(boolean thePresent) {
            this.present = thePresent;
        }
    }
