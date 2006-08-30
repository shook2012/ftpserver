/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */  

package org.apache.ftpserver.clienttests;

import java.io.File;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileEntryParser;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.parser.FTPFileEntryParserFactory;
import org.apache.commons.net.ftp.parser.UnixFTPEntryParser;
import org.apache.ftpserver.test.TestUtil;


public class ListTest extends ClientTestTemplate {
    private static final File TEST_FILE1 = new File(ROOT_DIR, "test1.txt");
    private static final File TEST_FILE2 = new File(ROOT_DIR, "test2.txt");

    private static final File TEST_DIR1 = new File(ROOT_DIR, "dir1");
    private static final File TEST_DIR2 = new File(ROOT_DIR, "dir2");

    private static final File TEST_FILE_IN_DIR1 = new File(TEST_DIR1, "test3.txt");
    private static final File TEST_DIR_IN_DIR1 = new File(TEST_DIR1, "dir3");

    private byte[] testData;
    
    
    /* (non-Javadoc)
     * @see org.apache.ftpserver.clienttests.ClientTestTemplate#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();

        testData = "TESDATA".getBytes("UTF-8");
        
        FTPClientConfig config = new FTPClientConfig ("UNIX");
        client.configure(config);

        client.login(ADMIN_USERNAME, ADMIN_PASSWORD);
    }



    public void testListFilesInDir() throws Exception {
        
        TEST_DIR1.mkdirs();
        TEST_FILE_IN_DIR1.createNewFile();
        TEST_DIR_IN_DIR1.mkdirs();
        
        FTPFile[] files = client.listFiles(TEST_DIR1.getName());
        
        assertEquals(2, files.length);

        FTPFile file = getFile(files, TEST_FILE_IN_DIR1.getName());
        assertEquals(TEST_FILE_IN_DIR1.getName(), file.getName());
        assertEquals(0, file.getSize());
        assertEquals("group", file.getGroup());
        assertEquals("user", file.getUser());
        assertTrue(file.isFile());
        assertFalse(file.isDirectory());
        
        file = getFile(files, TEST_DIR_IN_DIR1.getName());
        assertEquals(TEST_DIR_IN_DIR1.getName(), file.getName());
        assertEquals(0, file.getSize());
        assertEquals("group", file.getGroup());
        assertEquals("user", file.getUser());
        assertFalse(file.isFile());
        assertTrue(file.isDirectory());
        
    }
    public void testListFile() throws Exception {
        
        TEST_DIR1.mkdirs();
        TEST_FILE1.createNewFile();
        TEST_FILE2.createNewFile();

        FTPFile[] files = client.listFiles(TEST_FILE1.getName());
        
        assertEquals(1, files.length);
        
        FTPFile file = getFile(files, TEST_FILE1.getName());
        assertEquals(TEST_FILE1.getName(), file.getName());
        assertEquals(0, file.getSize());
        assertEquals("group", file.getGroup());
        assertEquals("user", file.getUser());
        assertTrue(file.isFile());
        assertFalse(file.isDirectory());
    }

    public void testListFiles() throws Exception {
        TEST_FILE1.createNewFile();
        TEST_FILE2.createNewFile();
        TEST_DIR1.mkdirs();
        TEST_DIR2.mkdirs();
        
        writeDataToFile(TEST_FILE1, testData);
        
        FTPFile[] files = client.listFiles();

        assertEquals(4, files.length);
        FTPFile file = getFile(files, TEST_FILE1.getName());
        assertEquals(TEST_FILE1.getName(), file.getName());
        assertEquals(testData.length, file.getSize());
        assertEquals("group", file.getGroup());
        assertEquals("user", file.getUser());
        assertTrue(file.isFile());
        assertFalse(file.isDirectory());
        
        file = getFile(files, TEST_FILE2.getName());
        assertEquals(TEST_FILE2.getName(), file.getName());
        assertEquals(0, file.getSize());
        assertEquals("group", file.getGroup());
        assertEquals("user", file.getUser());
        assertTrue(file.isFile());
        assertFalse(file.isDirectory());

        file = getFile(files, TEST_DIR1.getName());
        assertEquals(TEST_DIR1.getName(), file.getName());
        assertEquals(0, file.getSize());
        assertEquals("group", file.getGroup());
        assertEquals("user", file.getUser());
        assertFalse(file.isFile());
        assertTrue(file.isDirectory());

        file = getFile(files, TEST_DIR2.getName());
        assertEquals(TEST_DIR2.getName(), file.getName());
        assertEquals(0, file.getSize());
        assertEquals("group", file.getGroup());
        assertEquals("user", file.getUser());
        assertFalse(file.isFile());
        assertTrue(file.isDirectory());
    }

    public void testListNames() throws Exception {
        TEST_FILE1.createNewFile();
        TEST_FILE2.createNewFile();
        TEST_DIR1.mkdirs();
        TEST_DIR2.mkdirs();
        
        String[] files = client.listNames();
        
        assertEquals(4, files.length);
        
        TestUtil.assertInArrays(TEST_FILE1.getName(), files);
        TestUtil.assertInArrays(TEST_FILE2.getName(), files);
        TestUtil.assertInArrays(TEST_DIR1.getName(), files);
        TestUtil.assertInArrays(TEST_DIR2.getName(), files);
    }

    public void testListName() throws Exception {
        TEST_FILE1.createNewFile();
        TEST_FILE2.createNewFile();
        TEST_DIR1.mkdirs();
        
        String[] files = client.listNames(TEST_FILE2.getName());
        
        assertEquals(1, files.length);
        
        TestUtil.assertInArrays(TEST_FILE2.getName(), files);
    }
    
    private FTPFile getFile(FTPFile[] files, String name) {
        for (int i = 0; i < files.length; i++) {
            FTPFile file = files[i];
            
            if(name.equals(file.getName())) {
                return file;
            }
        }
        
        return null;
    }
}