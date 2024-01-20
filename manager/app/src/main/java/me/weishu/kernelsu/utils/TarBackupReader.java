package me.weishu.kernelsu.utils;

import android.app.backup.BackupAgent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.zip.InflaterInputStream;

public class TarBackupReader {
    private static final int TAR_HEADER_OFFSET_TYPE_CHAR = 156;
    private static final int TAR_HEADER_LENGTH_PATH = 100;
    private static final int TAR_HEADER_OFFSET_PATH = 0;
    private static final int TAR_HEADER_LENGTH_PATH_PREFIX = 155;
    private static final int TAR_HEADER_OFFSET_PATH_PREFIX = 345;
    private static final int TAR_HEADER_LENGTH_MODE = 8;
    private static final int TAR_HEADER_OFFSET_MODE = 100;
    private static final int TAR_HEADER_LENGTH_MODTIME = 12;
    private static final int TAR_HEADER_OFFSET_MODTIME = 136;
    private static final int TAR_HEADER_LENGTH_FILESIZE = 12;
    private static final int TAR_HEADER_OFFSET_FILESIZE = 124;
    private static final int TAR_HEADER_LONG_RADIX = 8;
    public static final String SHARED_BACKUP_AGENT_PACKAGE = "com.android.sharedstoragebackup";
    public static final String BACKUP_MANIFEST_FILENAME = "_manifest";
    public static final String BACKUP_METADATA_FILENAME = "_meta";
    public static final String BACKUP_FILE_HEADER_MAGIC = "ANDROID BACKUP\n";
    public static final int BACKUP_FILE_VERSION = 5;
    private InputStream mInputStream;

    public TarBackupReader(InputStream inputStream) {
        mInputStream = inputStream;

    }

    public FileMetadata readTarHeaders() throws IOException {
        byte[] block = new byte[512];
        FileMetadata info = null;

        boolean gotHeader = readTarHeader(block);
        if (gotHeader) {
            try {
                // okay, presume we're okay, and extract the various metadata
                info = new FileMetadata();
                info.size = extractRadix(block,
                        TAR_HEADER_OFFSET_FILESIZE,
                        TAR_HEADER_LENGTH_FILESIZE,
                        TAR_HEADER_LONG_RADIX);
                info.mtime = extractRadix(block,
                        TAR_HEADER_OFFSET_MODTIME,
                        TAR_HEADER_LENGTH_MODTIME,
                        TAR_HEADER_LONG_RADIX);
                info.mode = extractRadix(block,
                        TAR_HEADER_OFFSET_MODE,
                        TAR_HEADER_LENGTH_MODE,
                        TAR_HEADER_LONG_RADIX);

                info.path = extractString(block,
                        TAR_HEADER_OFFSET_PATH_PREFIX,
                        TAR_HEADER_LENGTH_PATH_PREFIX);
                String path = extractString(block,
                        TAR_HEADER_OFFSET_PATH,
                        TAR_HEADER_LENGTH_PATH);
                if (path.length() > 0) {
                    if (info.path.length() > 0) {
                        info.path += '/';
                    }
                    info.path += path;
                }

                // tar link indicator field: 1 byte at offset 156 in the header.
                int typeChar = block[TAR_HEADER_OFFSET_TYPE_CHAR];
                if (typeChar == 'x') {
                    // pax extended header, so we need to read that
                    gotHeader = readPaxExtendedHeader(info);
                    if (gotHeader) {
                        // and after a pax extended header comes another real header -- read
                        // that to find the real file type
                        gotHeader = readTarHeader(block);
                    }
                    if (!gotHeader) {
                        throw new IOException("Bad or missing pax header");
                    }

                    typeChar = block[TAR_HEADER_OFFSET_TYPE_CHAR];
                }

                switch (typeChar) {
                    case '0':
                        info.type = BackupAgent.TYPE_FILE;
                        break;
                    case '5': {
                        info.type = BackupAgent.TYPE_DIRECTORY;
                        if (info.size != 0) {

                            info.size = 0;
                        }
                        break;
                    }
                    case 0: {
                        // presume EOF

                        return null;
                    }
                    default: {

                        throw new IOException("Unknown entity type " + typeChar);
                    }
                }

                // Parse out the path
                //
                // first: apps/shared/unrecognized
                if (FullBackup.SHARED_PREFIX.regionMatches(0,
                        info.path, 0, FullBackup.SHARED_PREFIX.length())) {
                    // File in shared storage.  !!! TODO: implement this.
                    info.path = info.path.substring(FullBackup.SHARED_PREFIX.length());
                    info.packageName = SHARED_BACKUP_AGENT_PACKAGE;
                    info.domain = FullBackup.SHARED_STORAGE_TOKEN;

                } else if (FullBackup.APPS_PREFIX.regionMatches(0,
                        info.path, 0, FullBackup.APPS_PREFIX.length())) {
                    // App content!  Parse out the package name and domain

                    // strip the apps/ prefix
                    info.path = info.path.substring(FullBackup.APPS_PREFIX.length());

                    // extract the package name
                    int slash = info.path.indexOf('/');
                    if (slash < 0) {
                        throw new IOException("Illegal semantic path in " + info.path);
                    }
                    info.packageName = info.path.substring(0, slash);
                    info.path = info.path.substring(slash + 1);

                    // if it's a manifest or metadata payload we're done, otherwise parse
                    // out the domain into which the file will be restored
                    if (!info.path.equals(BACKUP_MANIFEST_FILENAME) &&
                            !info.path.equals(BACKUP_METADATA_FILENAME)) {
                        slash = info.path.indexOf('/');
                        if (slash < 0) {
                            throw new IOException("Illegal semantic path in non-manifest "
                                    + info.path);
                        }
                        info.domain = info.path.substring(0, slash);
                        info.path = info.path.substring(slash + 1);
                    }
                }
            } catch (IOException e) {
//                if (DEBUG) {
//                    Slog.e(TAG, "Parse error in header: " + e.getMessage());
//                    if (MORE_DEBUG) {
//                        hexLog(block);
//                    }
//                }
                e.printStackTrace();
            }
        }
        return info;
    }

    public static InputStream parseBackupFileHeaderAndReturnTarStream(
            InputStream rawInputStream,
            String decryptPassword)
            throws IOException {
        // First, parse out the unencrypted/uncompressed header
        boolean compressed = false;
        InputStream preCompressStream = rawInputStream;

        boolean okay = false;
        final int headerLen = BACKUP_FILE_HEADER_MAGIC.length();
        byte[] streamHeader = new byte[headerLen];
        readFullyOrThrow(rawInputStream, streamHeader);
        byte[] magicBytes = BACKUP_FILE_HEADER_MAGIC.getBytes(
                "UTF-8");
        if (Arrays.equals(magicBytes, streamHeader)) {
            // okay, header looks good.  now parse out the rest of the fields.
            String s = readHeaderLine(rawInputStream);
            final int archiveVersion = Integer.parseInt(s);
            if (archiveVersion <= BACKUP_FILE_VERSION) {
                // okay, it's a version we recognize.  if it's version 1, we may need
                // to try two different PBKDF2 regimes to compare checksums.
                final boolean pbkdf2Fallback = (archiveVersion == 1);

                s = readHeaderLine(rawInputStream);
//                compressed = true;
//                System.out.println("compressed="+compressed);
                compressed = (Integer.parseInt(s) != 0);

                s = readHeaderLine(rawInputStream);

                if (s.equals("none")) {
                    // no more header to parse; we're good to go
                    okay = true;
                } else if (decryptPassword != null && decryptPassword.length() > 0) {
//                    preCompressStream = decodeAesHeaderAndInitialize(
//                            decryptPassword, s, pbkdf2Fallback,
//                            rawInputStream);
//                    if (preCompressStream != null) {
//                        okay = true;
//                    }
                } else {
//                    Slog.w(TAG, "Archive is encrypted but no password given");
                }
            } else {
//                Slog.w(TAG, "Wrong header version: " + s);
            }
        } else {
//            Slog.w(TAG, "Didn't read the right header magic");
        }

        if (!okay) {
//            Slog.w(TAG, "Invalid restore data; aborting.");
            return null;
        }

        // okay, use the right stream layer based on compression
        return compressed ? new InflaterInputStream(preCompressStream) : preCompressStream;
    }



    private static String readHeaderLine(InputStream in) throws IOException {
        int c;
        StringBuilder buffer = new StringBuilder(80);
        while ((c = in.read()) >= 0) {
            if (c == '\n') {
                break;   // consume and discard the newlines
            }
            buffer.append((char) c);
        }
        return buffer.toString();
    }
    private static void readFullyOrThrow(InputStream in, byte[] buffer) throws IOException {
        int offset = 0;
        while (offset < buffer.length) {
            int bytesRead = in.read(buffer, offset, buffer.length - offset);
            if (bytesRead <= 0) {
                throw new IOException("Couldn't fully read data");
            }
            offset += bytesRead;
        }
    }

    private static String extractString(byte[] data, int offset, int maxChars) throws IOException {
        final int end = offset + maxChars;
        int eos = offset;
        // tar string fields terminate early with a NUL
        while (eos < end && data[eos] != 0) {
            eos++;
        }
        return new String(data, offset, eos - offset, "US-ASCII");
    }

    private boolean readPaxExtendedHeader(FileMetadata info)
            throws IOException {
        // We should never see a pax extended header larger than this
        if (info.size > 32 * 1024) {

            throw new IOException("Sanity failure: pax header size " + info.size);
        }

        // read whole blocks, not just the content size
        int numBlocks = (int) ((info.size + 511) >> 9);
        byte[] data = new byte[numBlocks * 512];
        if (readExactly(mInputStream, data, 0, data.length) < data.length) {
            throw new IOException("Unable to read full pax header");
        }
//        mBytesReadListener.onBytesRead(data.length);

        final int contentSize = (int) info.size;
        int offset = 0;
        do {
            // extract the line at 'offset'
            int eol = offset + 1;
            while (eol < contentSize && data[eol] != ' ') {
                eol++;
            }
            if (eol >= contentSize) {
                // error: we just hit EOD looking for the end of the size field
                throw new IOException("Invalid pax data");
            }
            // eol points to the space between the count and the key
            int linelen = (int) extractRadix(data, offset, eol - offset, 10);
            int key = eol + 1;  // start of key=value
            eol = offset + linelen - 1; // trailing LF
            int value;
            for (value = key + 1; data[value] != '=' && value <= eol; value++) {
                ;
            }
            if (value > eol) {
                throw new IOException("Invalid pax declaration");
            }

            // pax requires that key/value strings be in UTF-8
            String keyStr = new String(data, key, value - key, "UTF-8");
            // -1 to strip the trailing LF
            String valStr = new String(data, value + 1, eol - value - 1, "UTF-8");

            if ("path".equals(keyStr)) {
                info.path = valStr;
            } else if ("size".equals(keyStr)) {
                info.size = Long.parseLong(valStr);
            } else {
//                if (DEBUG) {
//                    Slog.i(TAG, "Unhandled pax key: " + key);
//                }
            }

            offset += linelen;
        } while (offset < contentSize);

        return true;
    }

    private long extractRadix(byte[] data, int offset, int maxChars, int radix)
            throws IOException {
        long value = 0;
        final int end = offset + maxChars;
        for (int i = offset; i < end; i++) {
            final byte b = data[i];
            // Numeric fields in tar can terminate with either NUL or SPC
            if (b == 0 || b == ' ') {
                break;
            }
            if (b < '0' || b > ('0' + radix - 1)) {
                throw new IOException("Invalid number in header: '" + (char) b
                        + "' for radix " + radix);
            }
            value = radix * value + (b - '0');
        }
        return value;
    }

    private boolean readTarHeader(byte[] block) throws IOException {
        final int got = readExactly(mInputStream, block, 0, 512);
        if (got == 0) {
            return false;     // Clean EOF
        }
        if (got < 512) {
            throw new IOException("Unable to read full block header");
        }
//        mBytesReadListener.onBytesRead(512);
        return true;
    }

    private static int readExactly(InputStream in, byte[] buffer, int offset, int size)
            throws IOException {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be > 0");
        }

        int soFar = 0;
        while (soFar < size) {
            int nRead = in.read(buffer, offset + soFar, size - soFar);
            if (nRead <= 0) {

                break;
            }
            soFar += nRead;

        }
        return soFar;
    }
}
