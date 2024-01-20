/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.weishu.kernelsu.utils;



import androidx.annotation.StringDef;

/**
 * Global constant definitions et cetera related to the full-backup-to-fd
 * binary format.  Nothing in this namespace is part of any API; it's all
 * hidden details of the current implementation gathered into one location.
 *
 * @hide
 */
public class FullBackup {
    static final String TAG = "FullBackup";
    /** Enable this log tag to get verbose information while parsing the client xml. */
    static final String TAG_XML_PARSER = "BackupXmlParserLogging";

    public static final String APK_TREE_TOKEN = "a";
    public static final String OBB_TREE_TOKEN = "obb";
    public static final String KEY_VALUE_DATA_TOKEN = "k";

    public static final String ROOT_TREE_TOKEN = "r";
    public static final String FILES_TREE_TOKEN = "f";
    public static final String NO_BACKUP_TREE_TOKEN = "nb";
    public static final String DATABASE_TREE_TOKEN = "db";
    public static final String SHAREDPREFS_TREE_TOKEN = "sp";
    public static final String CACHE_TREE_TOKEN = "c";

    public static final String DEVICE_ROOT_TREE_TOKEN = "d_r";
    public static final String DEVICE_FILES_TREE_TOKEN = "d_f";
    public static final String DEVICE_NO_BACKUP_TREE_TOKEN = "d_nb";
    public static final String DEVICE_DATABASE_TREE_TOKEN = "d_db";
    public static final String DEVICE_SHAREDPREFS_TREE_TOKEN = "d_sp";
    public static final String DEVICE_CACHE_TREE_TOKEN = "d_c";

    public static final String MANAGED_EXTERNAL_TREE_TOKEN = "ef";
    public static final String SHARED_STORAGE_TOKEN = "shared";

    public static final String APPS_PREFIX = "apps/";
    public static final String SHARED_PREFIX = SHARED_STORAGE_TOKEN + "/";

    public static final String FULL_BACKUP_INTENT_ACTION = "fullback";
    public static final String FULL_RESTORE_INTENT_ACTION = "fullrest";
    public static final String CONF_TOKEN_INTENT_EXTRA = "conftoken";

    public static final String FLAG_REQUIRED_CLIENT_SIDE_ENCRYPTION = "clientSideEncryption";
    public static final String FLAG_REQUIRED_DEVICE_TO_DEVICE_TRANSFER = "deviceToDeviceTransfer";
    public static final String FLAG_REQUIRED_FAKE_CLIENT_SIDE_ENCRYPTION =
            "fakeClientSideEncryption";
    private static final String FLAG_DISABLE_IF_NO_ENCRYPTION_CAPABILITIES
            = "disableIfNoEncryptionCapabilities";

    /**
     * When  this change is enabled, include / exclude rules specified via
     * {@code android:fullBackupContent} are ignored during D2D transfers.
     */

    private static final long IGNORE_FULL_BACKUP_CONTENT_IN_D2D = 180523564L;

    @StringDef({
        ConfigSection.CLOUD_BACKUP,
        ConfigSection.DEVICE_TRANSFER
    })
    @interface ConfigSection {
        String CLOUD_BACKUP = "cloud-backup";
        String DEVICE_TRANSFER = "device-transfer";
    }






}
