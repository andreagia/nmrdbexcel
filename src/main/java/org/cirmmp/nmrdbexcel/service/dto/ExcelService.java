package org.cirmmp.nmrdbexcel.service.dto;



import org.cirmmp.nmrdbexcel.helper.ExcelHelper;
import org.cirmmp.nmrdbexcel.model.hibernate.*;
import org.cirmmp.nmrdbexcel.model.web.*;
import org.cirmmp.nmrdbexcel.model.Tutorial;
import org.cirmmp.nmrdbexcel.repository.*;
import org.cirmmp.nmrdbexcel.service.ZipDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ExcelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelService.class);
  @Autowired
  TutorialEXRepository repository;
  @Autowired
  FragmentLibraryEXRepository fragmentLibrary;
  @Autowired
  SummaryEXRepository summaryRepository;

  @Autowired
  FragmentsRepository fragmentsRepository;
  @Autowired
  ResultsRepository resultsRepository;

  @Autowired
  CocktailsRepository cocktailsRepository;

  @Autowired
  ProteinRepository proteinRepository;
  @Autowired
  ScreeningsRepository screeningsRepository;

  @Autowired
  ZipDir zipDir;
  public void saveFragmentDTO(MultipartFile file, MultipartFile zip, String randomdir, Proteinweb protein, String sname) {

      List<Proteins> proteinsf = proteinRepository.findProteinsByName(protein.getName());
      proteinsf.stream().map(Proteins::getName).forEach(System.out::println);
      Proteins proteins = null;
      if(proteinsf.size() > 0){
         proteins = proteinsf.get(0);
      } else{
          proteins = new Proteins();
          proteins.setName(protein.getName());
          proteins.setSeq(protein.getSeq());
      }

      Screenings screenings = new Screenings();
      screenings.setTemperature("30");
      screenings.setName(sname);
      screenings.setProteins(proteins);

      List<Screenings> screeningsList = proteins.getScreeningsset();
      if(screeningsList == null) screeningsList = new ArrayList<>();
      screeningsList.add(screenings);

      proteins.setScreeningsset(screeningsList);
      proteinRepository.save(proteins);
      try {
      List<Fragmentweb> fragmentLibrariesweb = ExcelHelper.excelToFragmentweb(file.getInputStream());

      List<Fragments> fragments = fragmentLibrariesweb.stream().map(
              x-> {
                Fragments f = new Fragments();
                f.setSmileformula(x.getSmileformula());
                f.setSpectrum(x.getSpectrum());
                f.setZid(x.getZid());
                return f;
              }
      ).collect(Collectors.toList());

      //List<Screenings> screeningsListAll = screeningsRepository.findAll();
      //screeningsListAll.forEach(ff->{LOGGER.info(ff.getName());});
      fragmentsRepository.saveAll(fragments);
      //screeningsRepository.save(screenings);

      List<Summaryweb> summaryRepositoriesweb = ExcelHelper.excelToSummaryewb(file.getInputStream());

      List<Results> results = summaryRepositoriesweb.stream().map(
              x -> {
                Results f = new Results();
                //f.setFragmentsSetResult(fragmentsRepository.findBySpectrum(x.getSpectrum()));
                  f.setFragmentsSetResult(fragments.stream().filter(r->r.getSpectrum().equals(x.getSpectrum())).collect(Collectors.toList()));
                f.setFragment(x.getSpectrum());
                f.setZid(x.getZid());
                f.setBindingstate(x.getBindingstate());
                f.setWaterlogsy(x.getWaterlogsy());
                f.setT2(x.getT2());
                f.setCsp(x.getCsp());
                f.setStd(x.getStd());
                AtomicInteger count = new AtomicInteger();
                List<Screenings>  screeningsList2 = screeningsRepository.findByName(sname) ;
                screeningsList2.forEach(aa->{
                    LOGGER.info(aa.getName(), count.incrementAndGet());
                });
                f.setScreenings(screeningsList2.get(0));
                //f.setScreenings(screeningsRepository.findByName(sname).get(0));
                f.setAffinity(x.getAffinity());
                f.setKd(x.getKd());
                f.setResidues(x.getResidues());
                f.setMapping(x.getMapping());
                return f;
              }
      ).collect(Collectors.toList());

      screenings.setResults(results);
      resultsRepository.saveAll(results);

      List<Cocktailsweb> cocktailswebs = ExcelHelper.excelToCocktailsweb(file.getInputStream());

       // List<Cocktailsweb> cocktailswebList = zipDir.getDirAll(zip, cocktailswebs);

        zipDir.saveZip(zip, randomdir);

//        List<Cocktails> cocktails = cocktailswebList.stream().map(
//                x -> {
//                    Cocktails f = new Cocktails();
//                    f.setMix(x.getMix());
//                    f.setFragmentsSetCocktail(fragmentsRepository.findBySpectrumIn(x.getCompaunds()));
//                    f.setD1(x.getD1());
//                    f.setD2(x.getD2());
//                    f.setD5(x.getD5());
//                    f.setD20(x.getD20());
//                    f.setD21(x.getD21());
//                    f.setD22(x.getD22());
//                    f.setD30(x.getD30());
//                    f.setD31(x.getD31());
//                    f.setD32(x.getD32());
//                    return f;
//                }
//        ).collect(Collectors.toList());
      List<Cocktails> cocktails = cocktailswebs.stream().map(
              x -> {
                  Cocktails f = new Cocktails();
                  f.setMix(x.getMix());
                  // f.setFragmentsSetCocktail(fragmentsRepository.findBySpectrumIn(x.getCompaunds()));
                  f.setFragmentsSetCocktail(fragments.stream().filter(r->x.getCompaunds().contains(r.getSpectrum())).collect(Collectors.toList()));
                  //f.getFragmentsSetCocktail().forEach(System.out::println);
                  try {
                      System.out.println("MIX "+x.getMix());
                      f.setD1(zipDir.getDirDisk(randomdir, x.getMix(), "1"));
                      f.setD2(zipDir.getDirDisk(randomdir, x.getMix(), "2"));
                      f.setD5(zipDir.getDirDisk(randomdir, x.getMix(), "5"));
                      f.setD20(zipDir.getDirDisk(randomdir, x.getMix(), "20"));
                      f.setD21(zipDir.getDirDisk(randomdir, x.getMix(), "21"));
                      f.setD22(zipDir.getDirDisk(randomdir, x.getMix(), "22"));
                      f.setD30(zipDir.getDirDisk(randomdir, x.getMix(), "30"));
                      f.setD31(zipDir.getDirDisk(randomdir, x.getMix(), "31"));
                      f.setD32(zipDir.getDirDisk(randomdir, x.getMix(), "32"));
                  } catch (IOException e) {
                      throw new RuntimeException(e);
                  }
                  return f;
              }
      ).collect(Collectors.toList());

      screenings.setCocktails(cocktails);
      cocktailsRepository.saveAll(cocktails);

    } catch (IOException e) {
      throw new RuntimeException("fail to store excel data: " + e.getMessage());
    }
  }
  public void saveAll(MultipartFile file) {

    try {
      List<FragmentLibraryEx> fragmentLibraries = ExcelHelper.excelToFragment(file.getInputStream());
      fragmentLibrary.saveAll(fragmentLibraries);
    } catch (IOException e) {
      throw new RuntimeException("fail to store excel data: " + e.getMessage());
    }
    try {
      List<SummaryEx> summaryRepositories = ExcelHelper.excelToSummary(file.getInputStream());
      summaryRepository.saveAll(summaryRepositories);
    } catch (IOException e) {
      throw new RuntimeException("fail to store excel data: " + e.getMessage());
    }

  }
  public void save(MultipartFile file) {
    try {
      List<Tutorial> tutorials = ExcelHelper.excelToTutorials(file.getInputStream());
      repository.saveAll(tutorials);
    } catch (IOException e) {
      throw new RuntimeException("fail to store excel data: " + e.getMessage());
    }
  }

  public ByteArrayInputStream load() {
    List<Tutorial> tutorials = repository.findAll();

    ByteArrayInputStream in = ExcelHelper.tutorialsToExcel(tutorials);
    return in;
  }

  public List<Tutorial> getAllTutorials() {
    return repository.findAll();
  }
}
