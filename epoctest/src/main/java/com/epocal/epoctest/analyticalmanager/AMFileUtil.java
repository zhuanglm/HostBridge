package com.epocal.epoctest.analyticalmanager;

import com.epocal.common.globaldi.GloabalObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import android.util.Log;

/**
 * DEBUG utility class to write byte stream to file and read byte stream from file.
 *
 * These methods are used to write protocol buffer encoded data to a file for DEBUGGING purposes.
 * File is written to device's internal storage: Android/data/com.epocal.host4/files/
 */
public class AMFileUtil {
    private static final String AMTAG = AnalyticalManager.class.getSimpleName();

    // The name of subfolder where files are saved in the internal storage under the root folder of this app.
    private static final String  SUBFOLDER_NAME = "AM";

    // The name of files where method request (serialized request) are stored.
    private static final String REQ_FILE_CHECK_FOR_EARLY_INJECTION = "CheckForEarlyInjectionRequest.buf";
    private static final String REQ_FILE_PERFORM_REALTIME_QC = "PerformRealtimeQCRequest.buf";
    private static final String REQ_FILE_CALCULATE_BGE = "CalculateBGERequest.buf";
    private static final String REQ_FILE_COMPUTE_CALCULATED_RESULTS = "ComputeCalculatedResultsRequest.buf";
    private static final String REQ_FILE_COMPUTE_CORRECTED_RESULTS = "ComputeCorrectedResultsRequest.buf";

    // The name of files where method response (serialized request) are stored.
    private static final String RESP_FILE_CHECK_FOR_EARLY_INJECTION = "CheckForEarlyInjectionResponse.buf";
    private static final String RESP_FILE_PERFORM_REALTIME_QC = "PerformRealtimeQCResponse.buf";
    private static final String RESP_FILE_CALCULATE_BGE = "CalculateBGEResponse.buf";
    private static final String RESP_FILE_COMPUTE_CALCULATED_RESULTS = "ComputeCalculatedResultsResponse.buf";
    private static final String RESP_FILE_COMPUTE_CORRECTED_RESULTS = "ComputeCorrectedResultsResponse.buf";

    // Enum to define which method's buffer is being read from, or writen to
    public enum AMMethod {
        bgeCheckForEarlyInjection,
        bgePerformRealTimeQC,
        bgeCalculateBGE,
        bgeComputeCalculatedResults,
        bgeComputeCorrectedResults;
    }

    private static final Map<AMMethod, String> REQ_BUFFER_FILENAME;
    static {
        Map<AMMethod, String> map = new HashMap<>();

        map.put(AMMethod.bgeCheckForEarlyInjection, REQ_FILE_CHECK_FOR_EARLY_INJECTION);
        map.put(AMMethod.bgePerformRealTimeQC, REQ_FILE_PERFORM_REALTIME_QC);
        map.put(AMMethod.bgeCalculateBGE, REQ_FILE_CALCULATE_BGE);
        map.put(AMMethod.bgeComputeCalculatedResults, REQ_FILE_COMPUTE_CALCULATED_RESULTS);
        map.put(AMMethod.bgeComputeCorrectedResults, REQ_FILE_COMPUTE_CORRECTED_RESULTS);

        REQ_BUFFER_FILENAME = Collections.unmodifiableMap(map);
    }

    private static final Map<AMMethod, String> RESP_BUFFER_FILENAME;
    static {
        Map<AMMethod, String> map = new HashMap<>();

        map.put(AMMethod.bgeCheckForEarlyInjection, RESP_FILE_CHECK_FOR_EARLY_INJECTION);
        map.put(AMMethod.bgePerformRealTimeQC, RESP_FILE_PERFORM_REALTIME_QC);
        map.put(AMMethod.bgeCalculateBGE, RESP_FILE_CALCULATE_BGE);
        map.put(AMMethod.bgeComputeCalculatedResults, RESP_FILE_COMPUTE_CALCULATED_RESULTS);
        map.put(AMMethod.bgeComputeCorrectedResults, RESP_FILE_COMPUTE_CORRECTED_RESULTS);

        RESP_BUFFER_FILENAME = Collections.unmodifiableMap(map);
    }

    private static byte[] readBufferFromFile(String subFolder, String fileName) {

        File dir = GloabalObject.getApplication().getApplicationContext().getExternalFilesDir(subFolder);
        if (!dir.exists()) {
            return null;
        }

        File file = new File(dir, fileName);
        if (!file.exists()) {
            return null;
        }

        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bFile;
    }

    private static boolean writeBufferToFile(String subFolder, String fileName, byte[] bFile) {
        FileOutputStream fileOuputStream = null;

        try {
            File dir = GloabalObject.getApplication().getApplicationContext().getExternalFilesDir(subFolder);
            if (!dir.exists()) {
                return false;
            }

            File file = new File(dir, fileName);
            fileOuputStream = new FileOutputStream(file);
            fileOuputStream.write(bFile);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fileOuputStream != null) {
                try {
                    fileOuputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    public static void writeBufferToFile(AMMethod amMethod, byte[] requestBuffer) {
        String bufferFilename = REQ_BUFFER_FILENAME.get(amMethod);

        if (requestBuffer != null) {
            if (writeBufferToFile(SUBFOLDER_NAME, bufferFilename, requestBuffer)) {
                Log.i(AMTAG, "File written: " + bufferFilename);
            }
        }
    }

    public static byte[] readBufferFromFile(AMMethod amMethod) {
        byte[] requestBuffer = new byte[0];

        String bufferFilename = RESP_BUFFER_FILENAME.get(amMethod);

        byte[] tmpSerializedInputData = readBufferFromFile(SUBFOLDER_NAME, bufferFilename);
        if (tmpSerializedInputData != null) {
            requestBuffer = tmpSerializedInputData;
            Log.i(AMTAG, "File read: " + bufferFilename);
        }

        return requestBuffer;
    }
}
