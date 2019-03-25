package jtr.jabil.jabilgit;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

import java.util.ArrayList;
import java.util.List;

public class NdefMessageParser {
    private NdefMessageParser(){

    }
    public static List<ParseNdefRecord> parse(NdefMessage message){
        return getRecords(message.getRecords());
    }
    public static List<ParseNdefRecord> getRecords(NdefRecord[] records){
        List<ParseNdefRecord> elements = new ArrayList<ParseNdefRecord>();

        for(final NdefRecord record : records){
            /*(if(UriRecord.isUri(record)){

            }*/
        }
        return elements;
    }
}
