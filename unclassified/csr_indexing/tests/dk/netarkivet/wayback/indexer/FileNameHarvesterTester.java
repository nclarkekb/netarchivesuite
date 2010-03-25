/* File:        $Id$
 * Revision:    $Revision$
 * Author:      $Author$
 * Date:        $Date$
 *
 * Copyright 2004-2009 Det Kongelige Bibliotek and Statsbiblioteket, Denmark
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package dk.netarkivet.wayback.indexer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import dk.netarkivet.archive.arcrepository.bitpreservation.FileListJob;
import dk.netarkivet.common.CommonSettings;
import dk.netarkivet.common.distribute.arcrepository.ArcRepositoryClient;
import dk.netarkivet.common.distribute.arcrepository.ArcRepositoryClientFactory;
import dk.netarkivet.common.distribute.arcrepository.BatchStatus;
import dk.netarkivet.common.distribute.arcrepository.LocalArcRepositoryClient;
import dk.netarkivet.common.utils.FileUtils;
import dk.netarkivet.common.utils.batch.FileBatchJob;
import dk.netarkivet.testutils.TestFileUtils;
import dk.netarkivet.wayback.TestInfo;

import junit.framework.TestCase;

public class FileNameHarvesterTester extends TestCase {

    private String oldClient = System.getProperty(CommonSettings.ARC_REPOSITORY_CLIENT);
    private String oldFileDir = System.getProperty("settings.common.arcrepositoryClient.fileDir");

    public void setUp() {
        FileUtils.removeRecursively(TestInfo.WORKING_DIR);
        TestFileUtils.copyDirectoryNonCVS(TestInfo.ORIGINALS_DIR, TestInfo.WORKING_DIR);
        System.setProperty(CommonSettings.ARC_REPOSITORY_CLIENT, "dk.netarkivet.common.distribute.arcrepository.LocalArcRepositoryClient");
        System.setProperty("settings.common.arcrepositoryClient.fileDir", TestInfo.FILE_DIR.getAbsolutePath());
        assertTrue(ArcRepositoryClientFactory.getPreservationInstance() instanceof LocalArcRepositoryClient);
    }


    public void tearDown() {
        FileUtils.removeRecursively(TestInfo.WORKING_DIR);
        FileUtils.remove(TestInfo.LOG_FILE);
        if (oldClient != null) {
            System.setProperty(CommonSettings.ARC_REPOSITORY_CLIENT, oldClient);
        }
        if (oldFileDir != null ) {
            System.setProperty("settings.common.arcrepositoryClient.fileDir", oldFileDir);
        }
    }

    public void testHarvest() {
        FileNameHarvester.harvest();
        ArchiveFileDAO dao = new ArchiveFileDAO();
        List<ArchiveFile> files = dao.getSession().createQuery("from ArchiveFile").list();
        assertEquals("There should be two files", 2, files.size());
        FileNameHarvester.harvest();
        assertEquals("There should still be two files", 2, files.size());      
    }

}