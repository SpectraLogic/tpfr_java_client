package com.spectralogic.tpfr.client;

import com.spectralogic.tpfr.api.response.IndexStatus;

public interface Client {
    /**
     * This method will block while the index is created and will only return when either the index file has been created or for some reason it has not been possible to create the index file.
     * On HFS systems that leave stub files on disk, such as StorNext, if a request is made to create an index for a media file which has been truncated by StorNext, this call will cause the entire media file to be restored by StorNext.
     * The Web Service will support multiple concurrent calls to this command.
     * @param filePath The full path (via a mapped drive on the StorNext MDC) to the media file to be indexed.
     * @return @see {@link IndexStatus}
     * @throws Exception
     */
    IndexStatus indexFile(final String filePath) throws Exception;
}
