package org.cirmmp.nmrdbexcel.service;

//import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.cirmmp.nmrdbexcel.helper.ExcelHelper;
import org.cirmmp.nmrdbexcel.model.web.Cocktailsweb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
public class ZipDir {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipDir.class);
    @Value("${directory.tmp}")
    private String tmpdir;

    private Path root;

    @PostConstruct
    public void init() {
        String tmpDirsLocation = System.getProperty("java.io.tmpdir");
        File dirtmp = new File(tmpDirsLocation + this.tmpdir);
        if (!dirtmp.exists()) dirtmp.mkdirs();
        this.root = dirtmp.toPath();
        //this.root = Files.createTempDirectory(this.tmpdir);
        //Files.createDirectory(root);
        LOGGER.info("Directory tmp created on init-method -> " + root.toString());
    }

    public List<Cocktailsweb> getDirAll(MultipartFile file, List<Cocktailsweb> cocktailswebList) throws IOException {


        HashMap<String, Cocktailsweb> dircock = new HashMap<>();

        cocktailswebList.forEach(a -> {
            dircock.put("/" + a.getMix() + "/1/", a);
            dircock.put("/" + a.getMix() + "/2/", a);
            dircock.put("/" + a.getMix() + "/5/", a);
            dircock.put("/" + a.getMix() + "/20/", a);
            dircock.put("/" + a.getMix() + "/21/", a);
            dircock.put("/" + a.getMix() + "/22/", a);
            dircock.put("/" + a.getMix() + "/30/", a);
            dircock.put("/" + a.getMix() + "/31/", a);
            dircock.put("/" + a.getMix() + "/32/", a);
        });

        final int BUFFER = 2048;
        //String fstringl = "/"+pro+"/"+dir+"/";


        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(file.getBytes()));

        //LOGGER.info(fstringl);
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            byte data[] = new byte[BUFFER];
            int count;
            if (!entry.isDirectory()) {
                String entryname = entry.getName();


                if (dircock.keySet().stream().anyMatch(a -> entryname.contains(a))) {
                    String fstringl = "";
                    Cocktailsweb cocktailsweb = null;
                    String fstringlkey = "";

                    for (Map.Entry<String, Cocktailsweb> set :
                            dircock.entrySet()) {
                        if (entryname.contains(set.getKey())) {
                            fstringl = set.getValue().getMix();
                            cocktailsweb = set.getValue();
                            fstringlkey = set.getKey();
                        }
                    }
                    //Cocktailsweb findcock = Optional.ofNullable(dircock.entrySet().stream().filter())
                    //  Cocktailsweb findcock = dircock.entrySet().stream().filter(a -> entryname.contains(a.getValue().getMix())).collect(singleEleme)
                    LOGGER.info(entry.getName());
                    LOGGER.info("fstring " + fstringl);
                    String entryfname = entry.getName();
                    LOGGER.info(entryfname.split(fstringl).toString());
                    LOGGER.info(fstringl + entryfname.split(fstringl)[1]);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ZipOutputStream zipfile = new ZipOutputStream(bos);
                    ZipEntry zipentry = new ZipEntry(fstringl + entryfname.split(fstringl)[1]);
                    zipfile.putNextEntry(zipentry);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        zipfile.write(data, 0, count);
                    }
                    zipfile.close();
                    bos.close();
                    String casev = fstringlkey.split("/")[2];
                    LOGGER.info("CASSEV " + casev);
                    switch (casev) {
                        case "1":
                            cocktailsweb.setD1(bos.toByteArray());
                        case "2":
                            cocktailsweb.setD2(bos.toByteArray());
                        case "5":
                            cocktailsweb.setD5(bos.toByteArray());
                        case "20":
                            cocktailsweb.setD20(bos.toByteArray());
                        case "21":
                            cocktailsweb.setD21(bos.toByteArray());
                        case "22":
                            cocktailsweb.setD22(bos.toByteArray());
                        case "30":
                            cocktailsweb.setD30(bos.toByteArray());
                        case "31":
                            cocktailsweb.setD31(bos.toByteArray());
                        case "32":
                            cocktailsweb.setD32(bos.toByteArray());
                    }

                }
            }
        }


        return cocktailswebList;
    }



    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    public void saveZip(MultipartFile filezipin, String randomdir) throws IOException {

        File roottmp = new File(this.root.resolve(Paths.get(randomdir)).toString());
        if (!roottmp.exists()) roottmp.mkdirs();

        byte[] buffer = new byte[1024];
        LOGGER.info(roottmp.toString());
        ZipInputStream archive = new ZipInputStream(new ByteArrayInputStream(filezipin.getBytes()));
        ZipEntry zipEntry = archive.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(roottmp, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = archive.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = archive.getNextEntry();
        }
        LOGGER.info("ZIP DONE");
    }


    public byte[] getDir(MultipartFile file, String pro, String dir) throws IOException {

        final int BUFFER = 2048;
        String fstringl = "/" + pro + "/" + dir + "/";

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipfile = new ZipOutputStream(bos);
        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(file.getBytes()));

        LOGGER.info(fstringl);
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            byte data[] = new byte[BUFFER];
            int count;
            if (!entry.isDirectory()) {
                if (entry.getName().contains(fstringl)) {
                    LOGGER.info(entry.getName());
                    String entryfname = entry.getName();
                    LOGGER.info(fstringl + entryfname.split(fstringl)[1]);
                    ZipEntry zipentry = new ZipEntry(fstringl + entryfname.split(fstringl)[1]);
                    zipfile.putNextEntry(zipentry);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        zipfile.write(data, 0, count);
                    }
                }
            }
        }
        zipfile.close();
        byte[] result = bos.toByteArray();
        return result;
    }
    public byte[] getDirDisk(String randomdir, String pro, String dir) throws IOException {

        File roottmp = new File(this.root.resolve(Paths.get(randomdir)).toString());
        String fstringl = "/" + pro + "/" + dir + "/";
        System.out.println("Find "+  fstringl +" in "+ roottmp);
        File filesList[] = roottmp.listFiles();

//        Files.walk(Paths.get(roottmp.toString()))
//                .filter(Files::isDirectory)
//                .forEach(System.out::println);
        List<Path> resultfind;
        try (Stream<Path> walk = Files.walk(roottmp.toPath())) {
            resultfind = walk
                    .filter(Files::isDirectory)   // is a Directory
                    .filter(p -> p.toString().contains(fstringl))
                    .collect(Collectors.toList());
        }
//        System.out.println("INI");
//        resultfind.stream().forEach(System.out::println);
//        System.out.println("END");
        String sourceFile = resultfind.get(0).toString();

        System.out.println(sourceFile);
        System.out.println(" END Find ");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zipfile = new ZipOutputStream(bos);

        File fileToZip = new File(sourceFile);

        zipFile(fileToZip, fileToZip.getName(), zipfile);

        zipfile.close();
        bos.close();


        byte[] result = bos.toByteArray();
        String tmpdir = Files.createTempDirectory("tmpDirPrefix").toFile().getAbsolutePath();
        String tmpDirsLocation = System.getProperty("java.io.tmpdir");
        String tmpfilezip = tmpDirsLocation+"/"+pro+"_"+dir+".zip";
        OutputStream outputStream = new FileOutputStream (tmpfilezip);
        bos.writeTo(outputStream);
        //Path pathz = Paths.get(tmpfilezip);
        ///byte[] result = Files.readAllBytes(pathz);
        return result;

    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
        //LOGGER.info(fileToZip.toString());
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if (fileName.endsWith("/")) {
                zipOut.putNextEntry(new ZipEntry(fileName));
                zipOut.closeEntry();
            } else {
                zipOut.putNextEntry(new ZipEntry(fileName + "/"));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            for (File childFile : children) {
                zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }
}
