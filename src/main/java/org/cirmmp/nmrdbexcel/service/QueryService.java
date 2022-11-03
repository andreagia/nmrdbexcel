package org.cirmmp.nmrdbexcel.service;

import com.zaxxer.hikari.util.FastList;
import org.cirmmp.nmrdbexcel.controller.ExcelController;
import org.cirmmp.nmrdbexcel.model.hibernate.*;
import org.cirmmp.nmrdbexcel.model.web.Cocktailsweb;
import org.cirmmp.nmrdbexcel.model.web.ResQueryByTarget;
import org.cirmmp.nmrdbexcel.model.web.ResQueryByZid;
import org.cirmmp.nmrdbexcel.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class QueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryService.class);
    @Autowired
    TutorialEXRepository repository;
    //@Autowired
    //FragmentLibraryEXRepository fragmentLibrary;
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

    public List<ResQueryByTarget> queryByTarget(String target){

        return new ArrayList<>();
    }
    public List<ResQueryByZid> queryByZid(String zid){
        LOGGER.info(zid);
        List<ResQueryByZid> queryByZidList = new ArrayList<>();
        List<Results> resultsList = resultsRepository.findByZid(zid);
        resultsList.stream().map(Results::getZid).forEach(System.out::println);
        resultsList.stream().map(Results::getFragment).forEach(System.out::println);
        List<Fragments> fragmentsList = new ArrayList<>();
        resultsList.forEach(a ->{
            fragmentsList.addAll(a.getFragmentsSetResult());
        });
        fragmentsList.stream().map(Fragments::getSmileformula).forEach(System.out::println);

        List<Screenings> screeningsList = new ArrayList<>();
        resultsList.forEach(a -> {
            screeningsList.add(a.getScreenings());
        });
        screeningsList.stream().map(Screenings::getName).forEach(System.out::println);
        screeningsList.stream().map(Screenings::getProteins).map(Proteins::getName).forEach(System.out::println);
        resultsList.forEach(a ->{

            List<Fragments> fragments = a.getFragmentsSetResult();
            Screenings screenings = a.getScreenings();
            String proteinname = screenings.getProteins().getName();
            String screeningname = screenings.getName();
            fragments.forEach(b->{
                ResQueryByZid resQueryByZid = new ResQueryByZid();
                resQueryByZid.setProteinname(proteinname);
                resQueryByZid.setScreeningname(screeningname);
                resQueryByZid.setAffinity(a.getAffinity());
                resQueryByZid.setMapping(a.getMapping());
                resQueryByZid.setSmileformula(b.getSmileformula());
                resQueryByZid.setResidues(a.getResidues());
                resQueryByZid.setBinding(a.getBindingstate());
                resQueryByZid.setKd(a.getKd());
                resQueryByZid.setWaterlogsy(a.getWaterlogsy());
                resQueryByZid.setMapping(a.getMapping());
                resQueryByZid.setT2(a.getT2());
                resQueryByZid.setCsp(a.getCsp());
                resQueryByZid.setStd(a.getStd());
                queryByZidList.add(resQueryByZid);
            });

        });

        queryByZidList.stream().map(ResQueryByZid::getProteinname).forEach(System.out::println);
        return queryByZidList;
    }
   // @Query("select p from Cocktails d join d.fragmentsSetCocktail e join e.cocktails p ")
   public List<Cocktailsweb> findCocktailByProteinName(String pname) {

        List<Proteins> proteinsList = proteinRepository.findProteinsByName(pname);
        List<Screenings> screeningsList = proteinsList.get(0).getScreeningsset();

        List<Cocktails> cocktailsList = new ArrayList<>();

       for(Screenings sc : screeningsList) {
        cocktailsList.addAll(sc.getCocktails());
       }

       LOGGER.info("--------- INI TEST QUERY Zid-----------");
       queryByZid("Z2064898127");
       LOGGER.info("--------- END TEST QUERY Zid-----------");
       cocktailsList.stream().map(Cocktails::getMix).forEach(System.out::println);

       cocktailsList.forEach(a-> {

           List<Fragments> fragmentsList = a.getFragmentsSetCocktail();
           //fragmentsList.forEach(System.out::println);

       });

       String tmpDirsLocation = System.getProperty("java.io.tmpdir");
       File dirtmp = new File(tmpDirsLocation+"/pippo.zip");

       return cocktailsList.stream().map(a -> {
           Cocktailsweb b = new Cocktailsweb();
           b.setMix(a.getMix());
           b.setCompaunds(
                   a.getFragmentsSetCocktail().stream().map(Fragments::getZid).collect(Collectors.toList())
           );
           b.setMapcompounds(a.getFragmentsSetCocktail().stream().collect(Collectors.toMap(Fragments::getZid,e->e.getResults().get(0).getBindingstate())));
           b.setPid(a.getId());
           b.setSd1( Base64.getEncoder().encodeToString(a.getD1()));

           try {
               Files.write(dirtmp.toPath(), a.getD1());
           }
           catch(IOException e) {
               e.printStackTrace();
           }
           return b;

       } ).collect(Collectors.toList());
    }
}
