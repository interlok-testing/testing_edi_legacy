package com.adaptris.edi.legacy.test;

import com.adaptris.testing.LicensedSingleAdapterFunctionalTest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Collection;

public class DefaultFunctionalTest extends LicensedSingleAdapterFunctionalTest {

    @Test
    public void test() throws Exception {
        Path xmlIn = Paths.get("xml-in");
        Path xmlOut = Paths.get("xml-out");
        Path ediIn = Paths.get("edi-in");
        Path ediOut = Paths.get("edi-out");
        ediOut.toFile().mkdir();
        try (
                InputStream ediFileIs = getClass().getClassLoader().getResourceAsStream("ordersD96a.edi");
                InputStream xmlFileIs = getClass().getClassLoader().getResourceAsStream("ordersD96a.xml");
        ) {
            assert ediFileIs != null;
            assert xmlFileIs != null;
            byte[] ediFileBa = IOUtils.toByteArray(ediFileIs);
            byte[] xmlFileBa = IOUtils.toByteArray(xmlFileIs);
            File tmpFile = Files.createTempFile(null, null).toFile();
            tmpFile.deleteOnExit();
            try (FileOutputStream xmlInOs = new FileOutputStream(tmpFile)) {
                IOUtils.write(xmlFileBa, xmlInOs);
                FileUtils.moveFileToDirectory(tmpFile, xmlIn.toFile(), true);
            }
            waitForFileEvent(ediOut, 10000L, StandardWatchEventKinds.ENTRY_CREATE);
            Collection<File> ediOutFiles = FileUtils.listFiles(ediOut.toFile(), new WildcardFileFilter.Builder().setWildcards("*").get(), null);
            Assertions.assertEquals(1, ediOutFiles.size());
            File ediOutFile = ediOutFiles.stream().findFirst().orElse(null);
            Assertions.assertNotNull(ediOutFile);

            try (FileInputStream is1 = new FileInputStream(ediOutFile); ByteArrayInputStream is2 = new ByteArrayInputStream(ediFileBa)) {

                String output = StringUtils.replaceWhitespaceCharacters(IOUtils.toString(is1, Charset.defaultCharset()), "");
                String sample = StringUtils.replaceWhitespaceCharacters(IOUtils.toString(is2, Charset.defaultCharset()), "");
                Assertions.assertEquals(sample, output);
            }

        }

    }
}
