package com.epocal.util;

/**
 * Created by dning on 9/13/2017.
 */

public class Version {
    private int mMajor;
    private int mMinor;
    private int mRevision;
    private int mProduct = 0;

    private String internalVersion;

    public int getMajor() {
        return mMajor;
    }

    public void setMajor(int major) {
        mMajor = major;
    }

    public int getMinor() {
        return mMinor;
    }

    public void setMinor(int minor) {
        mMinor = minor;
    }

    public int getRevision() {
        return mRevision;
    }

    public void setRevision(int revision) {
        mRevision = revision;
    }

    public int getProduct() {
        return mProduct;
    }

    public void setProduct(int product) {
        mProduct = product;
    }

    public Version(String passedString)
    {
        String versionString = passedString;
        int numDots = 0;

        // if there is a space (like 2.5.15 (In house)) take the string to the first space..
        if (passedString.indexOf(' ') != -1)
        {
            versionString = passedString.substring(0, passedString.indexOf(' '));
        }

        // find all the dots
        for (int i = 0; i < versionString.length(); i++)
        {
            // this is really inefficient, but whatever, its a saturday and i dont want to think
            if (versionString.charAt(i) == '.')
            {
                numDots++;
            }
        }

        if (numDots == 3)
        {
            // if its a reader version.. take the first character out to be the product..
            // and the rest is the version
            mProduct = Integer.parseInt(versionString.substring(0, 1));
            versionString = versionString.substring(2);
        }
        internalVersion = versionString;
    }

    public boolean IsCompatible(Object obj)
    {
        if (obj.getClass() == Version.class)
        {
            // there is compatibility with a version if everything but the version is the same
            Version versionObject = (Version)obj;
            return ((mProduct == versionObject.getProduct()) && (mMajor == versionObject.getMajor()));
        }
        else if (obj.getClass() == String.class)
        {
            return IsCompatible(new Version((String)obj));
        }
        return false;
    }
}
