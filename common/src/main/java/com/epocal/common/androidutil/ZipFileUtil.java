package com.epocal.common.androidutil;

import android.content.res.AssetManager;
import android.util.Log;

import com.epocal.common.globaldi.GloabalObject;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ray zhuang on Nov 5 2018.
 */

public class ZipFileUtil{
    private String mExternalFileDirectory = "epochost";
    private long mFileLength;
    private BufferedWriter mWriter;
    private BufferedReader mReader;
    private String mFileName = "";
    private String mSubFolder = "";
    private FileLocation mFileLocation = FileLocation.Assets;

    private ZipParameters mZipParameters;
    private ZipOutputStream mOutputStream;

    public String getFileName() {
        return mFileName;
    }

    public void setFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    public String getSubFolder() {
        return mSubFolder;
    }

    public void setSubFolder(String mSubFolder) {
        this.mSubFolder = mSubFolder;
    }

    public long getFileLength() {
        return mFileLength;
    }

    public void setFileLength(long mFileLength) {
        this.mFileLength = mFileLength;
    }

    public enum FileLocation {
        NotDefined((byte) 0),
        Assets((byte) 1),
        Internal((byte) 2),
        External((byte) 3);

        public final byte value;

        FileLocation(byte value) {
            this.value = Byte.valueOf(value);
        }

        public static FileLocation convert(byte value) {
            return FileLocation.values()[value];
        }
    }

    public ZipFileUtil() {
    }

    public ZipFileUtil(String mFileName, FileLocation fileLocation) {
        mSubFolder = "";
        getFileHandler(mFileName, fileLocation);
    }

    public ZipFileUtil(String mFileName, FileLocation fileLocation, String subFolder) {
        setZipParameters(Zip4jConstants.COMP_DEFLATE,Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        mZipParameters.setSourceExternalStream(true);
        mZipParameters.setFileNameInZip(mFileName+".txt");

        mSubFolder = subFolder;
        getFileHandler(mFileName, fileLocation);
    }

    public ZipFileUtil(String mFileName, FileLocation fileLocation, String subFolder, String encryptKey) {
        setZipParameters(Zip4jConstants.COMP_DEFLATE,Zip4jConstants.DEFLATE_LEVEL_NORMAL,encryptKey);
        mZipParameters.setSourceExternalStream(true);
        mZipParameters.setFileNameInZip(mFileName+".txt");

        mSubFolder = subFolder;
        getFileHandler(mFileName, fileLocation);
    }

    private void setZipParameters(int method, int level) {
        mZipParameters = new ZipParameters();
        mZipParameters.setCompressionMethod(method);
        mZipParameters.setCompressionLevel(level);
    }

    private void setZipParameters(int method, int level, String encryptKey) {
        setZipParameters(method,level);

        mZipParameters.setEncryptFiles(true);
        mZipParameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
        mZipParameters.setPassword(encryptKey);
    }

    private void getFileHandler(String mFileName, FileLocation fileLocation)
    {
        switch (fileLocation)
        {
            case Assets:
                getFileHandlerFromAssets(mFileName);
                break;
            case Internal:
                getFileHandlerFromInternal(mFileName);
                break;
            case External:
                getFileHandlerFromExternal(mFileName);
                break;
            default:
                getFileHandlerFromAssets(mFileName);
                break;
        }
    }

    private void getFileHandlerFromAssets(String mFileName)
    {
        this.mFileName = mFileName;
        AssetManager assetManager = GloabalObject.getApplication().getApplicationContext().getAssets();
        try {
            List<String> filelist = Arrays.asList(assetManager.list(""));
            for(int i = 0; i < filelist.size() ; i++)
            {
                if(((String)filelist.get(i).toLowerCase()).equals(mFileName.toLowerCase()))
                {
                    InputStream is = assetManager.open(mFileName);
                    mReader = new BufferedReader(new InputStreamReader(is));
                    mFileLength = is.available();
                    break;
                }
            }
        }
        catch(IOException ioe){
            Log.d("Open from Assets", this.mFileName + ", " + ioe.getMessage());
        }
        catch (Exception e){
            Log.d("Open from Assets", this.mFileName + ", " + e.getMessage());
        }
    }

    private void getFileHandlerFromExternal(String mFileName) {
        this.mFileName = mFileName;
        File f = GloabalObject.getApplication().getApplicationContext().getExternalFilesDir(mExternalFileDirectory);
        File file = new File(f, mSubFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            File tfile = new File(file, this.mFileName);

            mOutputStream = new ZipOutputStream(new FileOutputStream(tfile));
            mOutputStream.putNextEntry(null, mZipParameters);
        }
        catch (Exception e) {
            Log.d("Open from External file", this.mFileName + ", " + e.getMessage());
        }
    }

    private void getFileHandlerFromInternal(String mFileName) {
        this.mFileName = mFileName;
        File f = GloabalObject.getApplication().getApplicationContext().getFilesDir();
        File file = new File(f, mSubFolder);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            File tfile = new File(file, this.mFileName);
            if(tfile.exists()) {
                mReader = new BufferedReader(new FileReader(tfile));
                mFileLength = tfile.length();
            }
                mWriter = new BufferedWriter(new FileWriter(tfile));
            }
        catch (Exception e) {
            Log.d("Open from Internal file", this.mFileName + ", " + e.getMessage());
        }
    }

    public boolean write(String data) {
        if (mOutputStream == null) return false;
        try {
            mOutputStream.write(data.getBytes());

        } catch (IOException e) {
            Log.d("Write from file", this.mFileName + ", " + e.getMessage());
            return false;
        }
        return true;
    }


    public File[] getAllFile() {
        File directory = new File(GloabalObject.getApplication().getApplicationContext().getFilesDir(), mSubFolder);
        if (!directory.exists()) {
            return null;
        }
        File[] files = directory.listFiles();
        return files;
    }

    public boolean close() {
        boolean ret = true;

        if (mOutputStream == null) {
            ret = false;
        } else {
            try {
                if (mOutputStream != null) {
                    mOutputStream.closeEntry();
                    mOutputStream.finish();
                    mOutputStream = null;
                }
            } catch (IOException e) {
                ret = false;
            } catch (ZipException e) {
                e.printStackTrace();
                ret = false;
            }
        }

        if (mWriter == null) {
            ret = false;
        }
        else {
            try {
                mWriter.close();
            } catch (IOException e) {
                ret = false;
            }
        }
        if (mReader == null) {
            ret = false;
        }
        else {
            try {
                mReader.close();
            } catch (IOException e) {
                ret = false;
            }
        }
        return ret;
    }

    static public void createAMFolder() {
        File folder = new File(String.valueOf(GloabalObject.getApplication().getApplicationContext().getExternalFilesDir("AM")));
        if (!folder.exists()) {
            folder.mkdir();
        }
    }
}
