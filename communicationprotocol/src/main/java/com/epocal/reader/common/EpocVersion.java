package com.epocal.reader.common;
import java.util.ArrayList;

/**
 * Created by dning on 6/7/2017.
 */

public class EpocVersion {

    public Integer getMajor() {
        return mMajor;
    }

    public void setMajor(Integer mMajor) {
        this.mMajor = mMajor;
    }

    public Integer getMinor() {
        return mMinor;
    }

    public void setMinor(Integer mMinor) {
        this.mMinor = mMinor;
    }

    public Integer getRevision() {
        return mRevision;
    }

    public void setRevision(Integer mRevision) {
        this.mRevision = mRevision;
    }

    public Integer getFlavor() {
        return mFlavor;
    }

    public void setFlavor(Integer mFlavor) {
        this.mFlavor = mFlavor;
    }

    private Integer mMajor;
    private Integer mMinor;
    private Integer mRevision;
    private Integer mFlavor;

    public EpocVersion() {
        mMajor = 1;
        mMinor = 0;
        mRevision = 0;
        mFlavor = 0;
    }

    public EpocVersion(Integer major) {
        mMajor = major;
        mMinor = null;
        mRevision = null;
        mFlavor = null;
    }

    public EpocVersion(Integer major, Integer minor) {
        mMajor = major;
        mMinor = minor;
        mRevision = null;
        mFlavor = null;
    }

    public EpocVersion(Integer major, Integer minor, Integer revision) {
        mMajor = major;
        mMinor = minor;
        mRevision = revision;
        mFlavor = null;
    }

    public EpocVersion(Integer major, Integer minor, Integer revision, Integer flavor) {
        mMajor = major;
        mMinor = minor;
        mRevision = revision;
        mFlavor = flavor;
    }

    public EpocVersion(EpocVersion other) {
        mMajor = other.getMajor();
        mMinor = other.getMinor();
        mRevision = other.getRevision();
        mFlavor = other.getFlavor();
    }

    public EpocVersion(String version) {
        mMajor = null;
        mMinor = null;
        mRevision = null;
        mFlavor = null;
        String[] str = version.split(".");
        if (str.length > 0) {
            try {
                mMajor = Integer.parseInt(str[0]);
            } catch (NumberFormatException e) {
            }
        }
        if (str.length > 1) {
            try {
                mMinor = Integer.parseInt(str[1]);
            } catch (NumberFormatException e) {
            }
        }
        if (str.length > 2) {
            try {
                mRevision = Integer.parseInt(str[2]);
            } catch (NumberFormatException e) {
            }
        }
        if (str.length > 3) {
            try {
                mFlavor = Integer.parseInt(str[3]);
            } catch (NumberFormatException e) {
            }
        }
    }

    public static EpocVersion FromBytes(byte[] bytes) {
        if (bytes.length < 1 || bytes.length > 4) {
            return null;
        }
        EpocVersion vers = new EpocVersion();
        if (bytes.length >= 1)
            vers.setMajor((int) bytes[0] & 0xff);
        if (bytes.length >= 2)
            vers.setMinor((int) bytes[1]& 0xff);
        if (bytes.length >= 3)
            vers.setRevision((int) bytes[2]& 0xff);
        if (bytes.length == 4)
            vers.setFlavor((int) bytes[3]& 0xff);
        return vers;
    }

    public byte[] toBytes(int fields) {
        if (fields < 1 || fields > 4) {
            return null;
        }
        ArrayList<Byte> list = new ArrayList<Byte>();
        if (fields >= 1 && mMajor != null)
            list.add(Byte.valueOf(Integer.toString(mMajor)));
        if (fields >= 2 && mMinor != null)
            list.add(Byte.valueOf(Integer.toString(mMinor)));
        if (fields >= 3 && mRevision != null)
            list.add(Byte.valueOf(Integer.toString(mRevision)));
        if (fields == 4 && mFlavor != null)
            list.add(Byte.valueOf(Integer.toString(mFlavor)));

        byte[] bytes = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            bytes[i] = list.get(i);
        }
        return bytes;
    }

    @Override
    public String toString() {
        return toString(4);
    }

    public String toString(int fields) {
        if (fields < 1 || fields > 4) {
            return null;
        }
        StringBuilder tempstr = new StringBuilder();
        if (fields >= 1 && mMajor != null)
            tempstr.append(Integer.toString(mMajor) + ".");
        if (fields >= 2 && mMinor != null)
            tempstr.append(Integer.toString(mMinor) + ".");
        if (fields >= 3 && mRevision != null)
            tempstr.append(Integer.toString(mRevision) + ".");
        if (fields == 4 && mFlavor != null)
            tempstr.append(Integer.toString(mFlavor));
        String str = tempstr.toString();
        if (str.endsWith(".")) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public boolean isEqual(EpocVersion other) {
        return mMajor == other.getMajor() && mMinor == other.getMinor()
                && mRevision == other.getRevision() && mFlavor == other.getFlavor();
    }

    // added by rzhuang at July 30 2018
    // if Epoc version >= version return true
    public static  String readerVersionWithStatistics = "2.2.2.3";

    public boolean compareTo(String version) {
        Integer nMajor = null;
        Integer nMinor = null;
        Integer nRevision = null;
        Integer nFlavor = null;

        String[] str = version.split(".");
        if (str.length > 0) {
            try {
                nMajor = Integer.parseInt(str[0]);
                if (mMajor > nMajor)
                    return true;
                else if (mMajor < nMajor)
                    return false;
            } catch (NumberFormatException e) {
            }
        }
        if (str.length > 1) {
            try {
                nMinor = Integer.parseInt(str[1]);
                if (mMinor > nMinor)
                    return true;
                else if (mMinor < nMinor)
                    return false;
            } catch (NumberFormatException e) {
            }
        }
        if (str.length > 2) {
            try {
                nRevision = Integer.parseInt(str[2]);
                if (mRevision > nRevision)
                    return true;
                else if (mRevision < nRevision)
                    return false;
            } catch (NumberFormatException e) {
            }
        }
        if (str.length > 3) {
            try {
                nFlavor = Integer.parseInt(str[3]);
                if (mFlavor > nFlavor)
                    return true;
                else if (mFlavor < nFlavor)
                    return false;
            } catch (NumberFormatException e) {
            }
        }

        return true;
    }
}
