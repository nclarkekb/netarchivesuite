package dk.netarkivet.wayback.hadoop;

import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveReaderFactory;
import org.archive.io.ArchiveRecord;
import org.archive.io.arc.ARCRecord;
import org.archive.io.warc.WARCRecord;
import org.archive.wayback.UrlCanonicalizer;
import org.archive.wayback.core.CaptureSearchResult;
import org.archive.wayback.resourceindex.cdx.SearchResultToCDXLineAdapter;
import org.archive.wayback.resourcestore.indexer.ARCRecordToSearchResultAdapter;
import org.archive.wayback.resourcestore.indexer.WARCRecordToSearchResultAdapter;
import org.archive.wayback.util.url.IdentityUrlCanonicalizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import dk.netarkivet.common.utils.batch.WARCBatchFilter;
import dk.netarkivet.wayback.batch.UrlCanonicalizerFactory;

/**
 * Class for creating CDX indexes from archive files.
 */
public class CDXIndexer implements Indexer {
    /** The warc record searcher.*/
    protected final WARCRecordToSearchResultAdapter warcAdapter;
    protected final ARCRecordToSearchResultAdapter arcAdapter;
    /** The CDX line creator, which creates the cdx lines from the warc records.*/
    protected final SearchResultToCDXLineAdapter cdxLineCreator;
    protected final UrlCanonicalizer urlCanonicalizer;

    /** Constructor.*/
    public CDXIndexer() {
        warcAdapter = new WARCRecordToSearchResultAdapter();
        arcAdapter = new ARCRecordToSearchResultAdapter();
        cdxLineCreator = new SearchResultToCDXLineAdapter();
        urlCanonicalizer = UrlCanonicalizerFactory.getDefaultUrlCanonicalizer();
    }

    /**
     * Index the given archive file.
     * @param archiveInputStream An inputstream to the given file.
     * @param archiveName The name of the given file.
     * @return The extracted CDX lines from the file.
     * @throws IOException
     */
    public List<String> index(InputStream archiveInputStream, String archiveName) throws IOException {
        ArchiveReader archiveReader = ArchiveReaderFactory.get(archiveName, archiveInputStream, false);
        return extractCdxLines(archiveReader);
    }


    /**
     * Create the CDX indexes from an archive file.
     * @param archiveFile The archive file.
     * @return The CDX lines for the records in the archive file.
     * @throws IOException If it fails to read the archive file.
     */
    public List<String> indexFile(File archiveFile) throws IOException {
        return index(new FileInputStream(archiveFile), archiveFile.getName());
    }


    /**
     * Filter for filtering out the NON-RESPONSE records.
     *
     * @return The filter that defines what WARC records are wanted in the output CDX file.
     */
    public WARCBatchFilter getFilter() {
        return WARCBatchFilter.EXCLUDE_NON_RESPONSE_RECORDS;
    }

    /**
     * Method for extracting the cdx lines from an ArchiveReader.
     * @param reader The ArchiveReader which is actively reading an archive file (e.g WARC).
     * @return The list of CDX index lines for the records of the archive in the reader.
     */
    protected List<String> extractCdxLines(ArchiveReader reader) {
        List<String> res = new ArrayList<>();

        for (ArchiveRecord archiveRecord: reader) {
            // TODO: look at logging something here instead of the below stuff
            //recordNum++;
            //System.out.println("Processing record #" + recordNum);
           if (archiveRecord instanceof WARCRecord) {
               WARCRecord warcRecord = (WARCRecord) archiveRecord;
               if (!getFilter().accept(warcRecord)) {
                   //System.out.println("Skipping non response record #" + recordNum);
                   continue;
               }
               warcAdapter.setCanonicalizer(new IdentityUrlCanonicalizer());
               //TODO this returns null and prints stack trace on OutOfMemoryError. Bad code. //jolf & abr
               CaptureSearchResult captureSearchResult = warcAdapter.adapt(warcRecord);
               if (captureSearchResult != null) {
                   //actualLinesWritten++;
                   //System.out.println("Actual cdx lines written: " + actualLinesWritten);
                   res.add(cdxLineCreator.adapt(captureSearchResult));
               }
           } else {
               ARCRecord arcRecord = (ARCRecord) archiveRecord;
               final CaptureSearchResult captureSearchResult = arcAdapter.adapt(arcRecord);
               if (captureSearchResult != null) {
                   res.add(cdxLineCreator.adapt(captureSearchResult));
               }
           }
        }
        return res;
    }
}
