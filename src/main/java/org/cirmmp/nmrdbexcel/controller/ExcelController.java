package org.cirmmp.nmrdbexcel.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.cirmmp.nmrdbexcel.helper.ExcelHelper;
import org.cirmmp.nmrdbexcel.message.ResponseMessage;
import org.cirmmp.nmrdbexcel.model.Tutorial;
import org.cirmmp.nmrdbexcel.model.hibernate.Cocktails;
import org.cirmmp.nmrdbexcel.model.hibernate.Proteins;
import org.cirmmp.nmrdbexcel.model.hibernate.Screenings;
import org.cirmmp.nmrdbexcel.model.web.Cocktailsweb;
import org.cirmmp.nmrdbexcel.model.web.Proteinweb;
import org.cirmmp.nmrdbexcel.model.web.ResQueryByZid;
import org.cirmmp.nmrdbexcel.repository.CocktailsRepository;
import org.cirmmp.nmrdbexcel.repository.ProteinRepository;
import org.cirmmp.nmrdbexcel.repository.ScreeningsRepository;
import org.cirmmp.nmrdbexcel.service.QueryService;
import org.cirmmp.nmrdbexcel.service.dto.ExcelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:3000","http://localhost:8080"})
@Controller
@RequestMapping("/api/excel")
public class ExcelController {
  private static final Logger LOGGER = LoggerFactory.getLogger(ExcelController.class);
  @Autowired
  ExcelService fileService;

  @Autowired
  QueryService queryService;

  @Autowired
  CocktailsRepository cocktailsRepository;

  @Autowired
  ProteinRepository proteinRepository;


  @GetMapping("/getproteins")
  public ResponseEntity<List<Proteinweb>> GetAllProteins() {

    //List<Proteinweb> proteinwebList = new ArrayList<>();
    List<Proteins> proteins = proteinRepository.findAll();
    List<Proteinweb> proteinwebList  =proteins.stream().map(a-> {
      Proteinweb b = new Proteinweb();
      b.setName(a.getName());
      b.setSeq(a.getSeq());
      b.setExpname(a.getScreeningsset().stream().map(Screenings::getName).collect(Collectors.toList()));
      return b;
    }).collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK).body(proteinwebList);
  }
  @PostMapping("/getZipFromDB")
  public ResponseEntity<byte[]> GetZipFromDB(@RequestParam("pid") Long pid, @RequestParam("dir") String dird) throws IOException {
    Optional<Cocktails> cocktails = cocktailsRepository.findById(pid);
    //File file = new File("/Users/andrea/tmp/A1-Mix_1.zip");
    //File file = new File("/Users/andrea/tmp/p.txt");
    //byte[] fileContent = Files.readAllBytes(file.toPath());

    byte[]  fileContent;
    switch (dird){
      default:
      case "d1":
        fileContent = cocktails.get().getD1();
       break;
      case "d2":
        fileContent = cocktails.get().getD2();
       break;
      case "d5":
        fileContent = cocktails.get().getD5();
        break;
      case "d20":
        fileContent = cocktails.get().getD20();
        break;
      case "d21":
        fileContent = cocktails.get().getD21();
        break;
      case "d22":
        fileContent = cocktails.get().getD22();
        break;
      case "d30":
        fileContent = cocktails.get().getD30();
        break;
      case "d31":
        fileContent = cocktails.get().getD31();
        break;
      case "d32":
        fileContent = cocktails.get().getD32();
        break;
    }

    LOGGER.info(pid.toString());
    // ByteArrayResource byteArrayResource = new ByteArrayResource(fileContent);
    // ByteArrayResource byteArrayResource = new ByteArrayResource(cocktails.get().getD1());

    HttpHeaders header = new HttpHeaders();

    header.setContentType(MediaType.valueOf("application/zip"));
    header.setContentLength(fileContent.length);
    header.set("Content-Disposition", "attachment; filename=file.zip");
    return new ResponseEntity<>(fileContent, header, HttpStatus.OK);
//    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"D1.zip")
//            .contentLength(byteArrayResource.contentLength()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(byteArrayResource);
  }

  @PostMapping("/findQueryByZid")
  public ResponseEntity<List<ResQueryByZid>>  findQueryByZid(@RequestParam("zid") String zid) {

    LOGGER.info("PNAME "+zid);
    List<ResQueryByZid> cocktailsList = queryService.queryByZid(zid);

    return ResponseEntity.status(HttpStatus.OK).body(cocktailsList);
  }
  @PostMapping("/findAllCockByProtname")
  public ResponseEntity<List<Cocktailsweb>>  findAllCockByProtname(@RequestParam("pname") String pname) {

    LOGGER.info("PNAME "+pname);
    List<Cocktailsweb> cocktailsList = queryService.findCocktailByProteinName(pname);

    return ResponseEntity.status(HttpStatus.OK).body(cocktailsList);
  }

  @PostMapping("/uploadfrag")
  public ResponseEntity<ResponseMessage> uploadFileFrag(@RequestParam("file") MultipartFile file, @RequestParam("zip") MultipartFile zip, @RequestParam("seq") String seq, @RequestParam("name") String pname,  @RequestParam("sname") String sname ) throws IOException {

    Proteinweb protein = new Proteinweb();
    protein.setName(pname);
    protein.setSeq(seq);
    String generatedString = RandomStringUtils.randomAlphanumeric(10);
    String message = "";
    //byte[] bytes = zipDir.getDir(zip, "F7-Mix", "1");

    if (ExcelHelper.hasExcelFormat(file)) {
      // try {
      //fileService.saveAll(file);

      fileService.saveFragmentDTO(file, zip, generatedString, protein, sname );
     // List<Cocktailsweb> cocktailsList = queryService.findCocktailByProteinName("pippo");

      message = "Uploaded the file successfully: " + file.getOriginalFilename();
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
      // } catch (Exception e) {
      //  LOGGER.info(e.toString());

      //  message = "Could not upload the file1: " + file.getOriginalFilename() + "!";
      // return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
      //}
    }

    message = "Please upload an excel file!";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
  }

  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {

    String message = "";

    if (ExcelHelper.hasExcelFormat(file)) {
     // try {
        fileService.save(file);

        message = "Uploaded the file successfully: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
     // } catch (Exception e) {
      //  LOGGER.info(e.toString());

      //  message = "Could not upload the file1: " + file.getOriginalFilename() + "!";
       // return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
      //}
    }

    message = "Please upload an excel file!";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
  }

  @GetMapping("/tutorials")
  public ResponseEntity<List<Tutorial>> getAllTutorials() {
    try {
      List<Tutorial> tutorials = fileService.getAllTutorials();

      if (tutorials.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(tutorials, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/download")
  public ResponseEntity<Resource> getFile() {
    String filename = "tutorials.xlsx";
    InputStreamResource file = new InputStreamResource(fileService.load());

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
        .body(file);
  }

}
