import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import org.archive.io.ArchiveRecord;
import org.archive.io.ArchiveRecordHeader;
import org.archive.io.warc.WARCConstants;
import org.archive.io.warc.WARCReader;
import org.archive.io.warc.WARCReaderFactory;
import org.archive.io.warc.WARCRecord;

import dk.netarkivet.common.CommonSettings;
import dk.netarkivet.common.utils.Settings;
import dk.netarkivet.common.utils.WARCUtils;
import dk.netarkivet.common.utils.cdx.CDXUtils;

import junit.framework.TestCase;

public class warcTester extends TestCase {

    private File f = new File(
    "/home/svc/netarchivesuite_trunk/tests/dk/netarkivet/harvester/" +
    "harvesting/data/crawldir/arcs/realistically-named-arcs/arcs/" +
    "42-117-20051212141241-00001-sb-test-har-001.statsbiblioteket.dk.arc");
    
    private static File f1 = new File("/home/svc/NAS-20100909163324-00000-mette.kb.dk.warc");
    
    private File f1Cdx = new File(f1.getName() + ".cdx"); 
    
    public void testWriteCDXInfo() throws IOException {
        f1Cdx.delete();
        System.out.println("Writing to cdxfile: " + f1Cdx.getCanonicalPath());
        OutputStream cdxstream = new FileOutputStream(f1Cdx);
        
        cdxstream.write("BEFORE\n".getBytes());
        CDXUtils.writeCDXInfo(f1, cdxstream);
        cdxstream.close();
    }

    //public void testGenerateCDX() {
    //    fail("Not yet implemented");
    //}

    public static void main(String[] args) throws IOException {
        /*
         *(Record type, url, offset, ContentBegin, Length): response, 
         *http://netarkivet.dk/organisation/index-da.php, 85108, 361, 9291
         */        
        long warcOffset = 85108;
        int warcContentBegin = 361;
        long warcRecordLength = 9261; // Length = WarcRecordLength - WarcContentBegin
        
        
        WARCReader ar = WARCReaderFactory.get(f1);
        
        WARCRecord record = (WARCRecord) ar.get(warcOffset);
        
        Settings.set(CommonSettings.BITARCHIVE_LIMIT_FOR_RECORD_DATATRANSFER_IN_FILE,
        "10000");
        
        byte[] bytes = WARCUtils.readWARCRecord(record); 
        
        System.out.println("#bytes: " + bytes.length);
        System.out.println(new String(bytes));
                
        
        if (false) {
        
        
        
        Iterator<ArchiveRecord> it = ar.iterator();
        long lastOffset = 0;
        while (it.hasNext()) {
            WARCRecord rec = (WARCRecord) it.next();
            ArchiveRecordHeader header = rec.getHeader();
            String type = (String) header.getHeaderValue(WARCConstants.HEADER_KEY_TYPE);
            long offset = header.getOffset();
            String mime = header.getMimetype();
            
            if (type.equals(WARCConstants.RESPONSE) && !mime.equals("text/dns")) {
                System.out.println("(Record type, url, offset, ContentBegin, Length): " + type + ", "
                        + header.getUrl() + ", " + offset + ", " + header.getContentBegin() + ", " + header.getLength());
                //System.out.println("offset: " + header.getOffset());
                //System.out.println("ContentBegin: " + header.getContentBegin());
                //System.out.println("Length: " + header.getLength());
            }
            //if (offset != lastOffset) {
            //    System.out.println("Distance to last record: " + (offset - lastOffset));
            //}
            //lastOffset = offset;
            //Object o = header.getHeaderValue(WARCConstants.HEADER_KEY_TYPE);
            //System.out.println("type of o:" + o.getClass().getName());
       
            
            
            }
        
        }
    }
    
}
