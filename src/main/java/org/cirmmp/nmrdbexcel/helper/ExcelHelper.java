package org.cirmmp.nmrdbexcel.helper;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cirmmp.nmrdbexcel.model.web.*;
import org.cirmmp.nmrdbexcel.model.Tutorial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
  private static final Logger LOGGER = LoggerFactory.getLogger(ExcelHelper.class);
  public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  static String[] HEADERs = { "Id", "Title", "Description", "Published" };
  static String SHEET = "Tutorials";

  public static boolean hasExcelFormat(MultipartFile file) {

    if (!TYPE.equals(file.getContentType())) {
      return false;
    }

    return true;
  }

  public static ByteArrayInputStream tutorialsToExcel(List<Tutorial> tutorials) {

    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
      Sheet sheet = workbook.createSheet(SHEET);

      // Header
      Row headerRow = sheet.createRow(0);

      for (int col = 0; col < HEADERs.length; col++) {
        Cell cell = headerRow.createCell(col);
        cell.setCellValue(HEADERs[col]);
      }

      int rowIdx = 1;
      for (Tutorial tutorial : tutorials) {
        Row row = sheet.createRow(rowIdx++);

        row.createCell(0).setCellValue(tutorial.getId());
        row.createCell(1).setCellValue(tutorial.getTitle());
        row.createCell(2).setCellValue(tutorial.getDescription());
        row.createCell(3).setCellValue(tutorial.isPublished());
      }

      workbook.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
    }
  }


  public static List<Cocktailsweb> excelToCocktailsweb(InputStream is) {
    try {

      Workbook workbook = new XSSFWorkbook(is);
      Sheet sheet = workbook.getSheet("cocktails");
      Iterator<Row> rows = sheet.iterator();
      List<Cocktailsweb> summaries = new ArrayList<>();

      boolean finds = false;
      //Find headers on the tables
      while (rows.hasNext()) {
        Row currentRow = rows.next();
        Iterator<Cell> cellsInRow = currentRow.iterator();
        Cocktailsweb cocktailsweb = new Cocktailsweb();
        List<String> rowex = new ArrayList<>();
        int cellIdx = 0;
        while (cellsInRow.hasNext()) {
          Cell currentCell = cellsInRow.next();
          CellType cellType = currentCell.getCellType();
          if (cellType == CellType.STRING) {
            if (currentCell.getStringCellValue().equals("mix")) break;
            if(cellIdx == 0)  {
              finds = true;
              cocktailsweb.setMix(currentCell.getStringCellValue());}
            else {
              rowex.add(currentCell.getStringCellValue());
            }
            cellIdx++;
          }
        }
        if(finds) {
          cocktailsweb.setCompaunds(rowex);
          summaries.add(cocktailsweb);
        }
      }

      return summaries;

    } catch (IOException e) {
    throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
  }

  }
  public static List<Summaryweb> excelToSummaryewb(InputStream is) {
    try {

      Workbook workbook = new XSSFWorkbook(is);

      Sheet sheet = workbook.getSheet("Summary");
      Iterator<Row> rows = sheet.iterator();
      List<Summaryweb> summaries = new ArrayList<>();
      CellAddress spectrum = null;
      CellAddress bindingstate = null;
      CellAddress waterlogsy = null;
      CellAddress zid = null;
      CellAddress t2 = null;
      CellAddress csp = null;
      CellAddress std = null;
      CellAddress mapping = null;
      CellAddress residues = null;
      CellAddress affinity = null;
      CellAddress kd = null;
      boolean finds = false;

      int rowNumber = 0;
      //Find headers on the tables
      while (rows.hasNext()) {
        Row currentRow = rows.next();
        Iterator<Cell> cellsInRow = currentRow.iterator();
        Summaryweb summary = new Summaryweb();
        //      int cellIdx = 0;
        while (cellsInRow.hasNext()) {
          Cell currentCell = cellsInRow.next();

          CellType cellType = currentCell.getCellType();
          if (cellType == CellType.STRING) {
            // LOGGER.info("Trovata Stringa");
            //LOGGER.info(currentCell.getStringCellValue());
            if (currentCell.getStringCellValue().equals("Spectrum")) {
              spectrum = currentCell.getAddress();
              LOGGER.info("Trovato Spectrum");
              LOGGER.info(Integer.toString(spectrum.getColumn()));
              LOGGER.info(Integer.toString(spectrum.getRow()));
            } else if (currentCell.getStringCellValue().equals("Bindingstate")) {
              bindingstate = currentCell.getAddress();
            } else if (currentCell.getStringCellValue().equals("Waterlogsy")) {
              waterlogsy = currentCell.getAddress();
            } else if (currentCell.getStringCellValue().equals("ID")) {
              zid = currentCell.getAddress();
            } else if (currentCell.getStringCellValue().equals("T2")) {
              t2 = currentCell.getAddress();
            } else if (currentCell.getStringCellValue().equals("CSP")) {
              csp = currentCell.getAddress();
            } else if (currentCell.getStringCellValue().equals("STD")) {
              std = currentCell.getAddress();
            } else if (currentCell.getStringCellValue().equals("Mapping")) {
              mapping = currentCell.getAddress();
            } else if (currentCell.getStringCellValue().equals("Residues")) {
              residues = currentCell.getAddress();
            } else if (currentCell.getStringCellValue().equals("Affinity")) {
              affinity = currentCell.getAddress();
            } else if (currentCell.getStringCellValue().equals("KD")) {
              kd = currentCell.getAddress();
            }
          }
          if (spectrum != null) {
            // LOGGER.info("Column "+currentCell.getColumnIndex()+" Row "+ currentCell.getRowIndex()+ " SColu "+ spectrum.getColumn()+ " Srow "+ spectrum.getRow());
            if (currentCell.getColumnIndex() == spectrum.getColumn() && currentCell.getRowIndex() > spectrum.getRow())  {
              summary.setSpectrum(currentCell.getStringCellValue());
              finds = true;
              LOGGER.info("Trovato Spectrum null "+ currentCell.getStringCellValue());
              LOGGER.info("Column "+currentCell.getColumnIndex()+" Row "+ currentCell.getRowIndex()+ " SColu "+ spectrum.getColumn()+ " Srow "+ spectrum.getRow());
            }
          }
          if (bindingstate != null) {
            if ( currentCell.getColumnIndex() == bindingstate.getColumn() && currentCell.getRowIndex() > bindingstate.getRow()) {
              summary.setBindingstate(currentCell.getStringCellValue());
            }
          }
          if (waterlogsy != null) {
            if (currentCell.getColumnIndex() == waterlogsy.getColumn()  && currentCell.getRowIndex() > waterlogsy.getRow()  ) {
              summary.setWaterlogsy(currentCell.getStringCellValue());
            }
          }
          if (zid != null) {
            if (currentCell.getColumnIndex() == zid.getColumn()  && currentCell.getRowIndex() > zid.getRow()  ) {
              summary.setZid(currentCell.getStringCellValue());
            }
          }
          if (t2 != null) {
            if (currentCell.getColumnIndex() == t2.getColumn()  && currentCell.getRowIndex() > t2.getRow()  ) {
              summary.setT2(currentCell.getStringCellValue());
            }
          }
          if (csp != null) {
            if (currentCell.getColumnIndex() == csp.getColumn()  && currentCell.getRowIndex() > csp.getRow()  ) {
              summary.setCsp(currentCell.getStringCellValue());
            }
          }
          if (std != null) {
            if (currentCell.getColumnIndex() == std.getColumn()  && currentCell.getRowIndex() > std.getRow()  ) {
              summary.setStd(currentCell.getStringCellValue());
            }
          }
          if (mapping != null) {
            if (currentCell.getColumnIndex() == mapping.getColumn()  && currentCell.getRowIndex() > mapping.getRow()  ) {
              summary.setMapping(currentCell.getStringCellValue());
            }
          }
          if (residues != null) {
            if (currentCell.getColumnIndex() == residues.getColumn()  && currentCell.getRowIndex() > residues.getRow()  ) {
              summary.setResidues(currentCell.getStringCellValue());
            }
          }
          if (affinity != null) {
            if (currentCell.getColumnIndex() == affinity.getColumn()  && currentCell.getRowIndex() > affinity.getRow()  ) {
              summary.setAffinity(currentCell.getStringCellValue());
            }
          }
          if (kd != null) {
            if (currentCell.getColumnIndex() == kd.getColumn()  && currentCell.getRowIndex() > kd.getRow()  ) {

              summary.setKd(String.valueOf(currentCell.getNumericCellValue()));
            }
          }

          //     cellIdx++;
        }
        if(spectrum != null && bindingstate != null && waterlogsy != null && finds) {
          summaries.add(summary);
        }
        finds = false;
      }

      workbook.close();

      return summaries;

    } catch (IOException e) {
      throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
    }
  }
  public static List<SummaryEx> excelToSummary(InputStream is) {
    try {

      Workbook workbook = new XSSFWorkbook(is);

      Sheet sheet = workbook.getSheet("Summary");
      Iterator<Row> rows = sheet.iterator();
      List<SummaryEx> summaries = new ArrayList<SummaryEx>();
      CellAddress spectrum = null;
      CellAddress bindingstate = null;
      CellAddress waterlogsy = null;
      CellAddress zid = null;
      CellAddress t2 = null;
      CellAddress csp = null;
      CellAddress std = null;
      CellAddress mapping = null;
      CellAddress residues = null;
      CellAddress affinity = null;
      CellAddress kd = null;
      boolean finds = false;

      int rowNumber = 0;
      //Find headers on the tables
      while (rows.hasNext()) {
        Row currentRow = rows.next();
        Iterator<Cell> cellsInRow = currentRow.iterator();
        SummaryEx summary = new SummaryEx();
  //      int cellIdx = 0;
        while (cellsInRow.hasNext()) {
          Cell currentCell = cellsInRow.next();

          CellType cellType = currentCell.getCellType();
          if (cellType == CellType.STRING) {
           // LOGGER.info("Trovata Stringa");
            //LOGGER.info(currentCell.getStringCellValue());
            if (currentCell.getStringCellValue().equals("Spectrum")) {
              spectrum = currentCell.getAddress();
              LOGGER.info("Trovato Spectrum");
              LOGGER.info(Integer.toString(spectrum.getColumn()));
              LOGGER.info(Integer.toString(spectrum.getRow()));
            } else if (currentCell.getStringCellValue().equals("Bindingstate")) {
              bindingstate = currentCell.getAddress();
            } else if (currentCell.getStringCellValue().equals("Waterlogsy")) {
              waterlogsy = currentCell.getAddress();
            } else if (currentCell.getStringCellValue().equals("ID")) {
              zid = currentCell.getAddress();
            } else if (currentCell.getStringCellValue().equals("T2")) {
              t2 = currentCell.getAddress();
            } else if (currentCell.getStringCellValue().equals("CSP")) {
              csp = currentCell.getAddress();
            } else if (currentCell.getStringCellValue().equals("STD")) {
              std = currentCell.getAddress();
            } else if (currentCell.getStringCellValue().equals("Mapping")) {
              mapping = currentCell.getAddress();
            } else if (currentCell.getStringCellValue().equals("Residues")) {
              residues = currentCell.getAddress();
            } else if (currentCell.getStringCellValue().equals("Affinity")) {
              affinity = currentCell.getAddress();
            } else if (currentCell.getStringCellValue().equals("KD")) {
              kd = currentCell.getAddress();
            }
          }
          if (spectrum != null) {
           // LOGGER.info("Column "+currentCell.getColumnIndex()+" Row "+ currentCell.getRowIndex()+ " SColu "+ spectrum.getColumn()+ " Srow "+ spectrum.getRow());
            if (currentCell.getColumnIndex() == spectrum.getColumn() && currentCell.getRowIndex() > spectrum.getRow())  {
              summary.setSpectrum(currentCell.getStringCellValue());
              finds = true;
              LOGGER.info("Trovato Spectrum null "+ currentCell.getStringCellValue());
              LOGGER.info("Column "+currentCell.getColumnIndex()+" Row "+ currentCell.getRowIndex()+ " SColu "+ spectrum.getColumn()+ " Srow "+ spectrum.getRow());
            }
          }
          if (bindingstate != null) {
            if ( currentCell.getColumnIndex() == bindingstate.getColumn() && currentCell.getRowIndex() > bindingstate.getRow()) {
              summary.setBindingstate(currentCell.getStringCellValue());
            }
          }
          if (waterlogsy != null) {
            if (currentCell.getColumnIndex() == waterlogsy.getColumn()  && currentCell.getRowIndex() > waterlogsy.getRow()  ) {
              summary.setWaterlogsy(currentCell.getStringCellValue());
            }
          }
          if (zid != null) {
            if (currentCell.getColumnIndex() == zid.getColumn()  && currentCell.getRowIndex() > zid.getRow()  ) {
              summary.setZid(currentCell.getStringCellValue());
            }
          }
          if (t2 != null) {
            if (currentCell.getColumnIndex() == t2.getColumn()  && currentCell.getRowIndex() > t2.getRow()  ) {
              summary.setT2(currentCell.getStringCellValue());
            }
          }
          if (csp != null) {
            if (currentCell.getColumnIndex() == csp.getColumn()  && currentCell.getRowIndex() > csp.getRow()  ) {
              summary.setCsp(currentCell.getStringCellValue());
            }
          }
          if (std != null) {
            if (currentCell.getColumnIndex() == std.getColumn()  && currentCell.getRowIndex() > std.getRow()  ) {
              summary.setStd(currentCell.getStringCellValue());
            }
          }
          if (mapping != null) {
            if (currentCell.getColumnIndex() == mapping.getColumn()  && currentCell.getRowIndex() > mapping.getRow()  ) {
              summary.setMapping(currentCell.getStringCellValue());
            }
          }
          if (residues != null) {
            if (currentCell.getColumnIndex() == residues.getColumn()  && currentCell.getRowIndex() > residues.getRow()  ) {
              summary.setResidues(currentCell.getStringCellValue());
            }
          }
          if (affinity != null) {
            if (currentCell.getColumnIndex() == affinity.getColumn()  && currentCell.getRowIndex() > affinity.getRow()  ) {
              summary.setAffinity(currentCell.getStringCellValue());
            }
          }
          if (kd != null) {
            if (currentCell.getColumnIndex() == kd.getColumn()  && currentCell.getRowIndex() > kd.getRow()  ) {
              summary.setKd(currentCell.getStringCellValue());
            }
          }

     //     cellIdx++;
        }
        if(spectrum != null && bindingstate != null && waterlogsy != null && finds) {
          summaries.add(summary);
        }
        finds = false;
      }

      /*while (rows.hasNext()) {
        Row currentRow = rows.next();

        // skip header
        if (rowNumber == 0) {
          rowNumber++;
          continue;
        }

        Iterator<Cell> cellsInRow = currentRow.iterator();

        Summary summary = new Summary();

        int cellIdx = 0;
        while (cellsInRow.hasNext()) {
          Cell currentCell = cellsInRow.next();

          CellAddress add =currentCell.getAddress();
          switch (cellIdx) {
            case 0:
              summary.setSpectrum( currentCell.getStringCellValue());
              break;

            case 1:
              summary.setZid( currentCell.getStringCellValue());
              break;


            default:
              break;
          }

          cellIdx++;
        }

        summaries.add(summary);
      }*/

      workbook.close();

      return summaries;

    } catch (IOException e) {
      throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
    }
  }


  public static List<Fragmentweb> excelToFragmentweb(InputStream is) {
    try {

      Workbook workbook = new XSSFWorkbook(is);

      Sheet sheet = workbook.getSheet("Fragment Library");
      Iterator<Row> rows = sheet.iterator();
      List<Fragmentweb> fragmentLibraries = new ArrayList<>();
      int rowNumber = 0;
      while (rows.hasNext()) {
        Row currentRow = rows.next();

        // skip header
        if (rowNumber == 0) {
          rowNumber++;
          continue;
        }

        Iterator<Cell> cellsInRow = currentRow.iterator();

        Fragmentweb fragmentLibrary = new Fragmentweb();

        int cellIdx = 0;
        while (cellsInRow.hasNext()) {
          Cell currentCell = cellsInRow.next();

          switch (cellIdx) {
            case 0:
              fragmentLibrary.setSpectrum( currentCell.getStringCellValue());
              break;

            case 1:
              fragmentLibrary.setZid( currentCell.getStringCellValue());

              break;

            case 2:
              fragmentLibrary.setSmileformula( currentCell.getStringCellValue());
              break;


            default:
              break;
          }

          cellIdx++;
        }

        fragmentLibraries.add(fragmentLibrary);
      }

      workbook.close();

      return fragmentLibraries;

    } catch (IOException e) {
      throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
    }
  }
  public static List<FragmentLibraryEx> excelToFragment(InputStream is) {
    try {

      Workbook workbook = new XSSFWorkbook(is);

      Sheet sheet = workbook.getSheet("Fragment Library");
      Iterator<Row> rows = sheet.iterator();
      List<FragmentLibraryEx> fragmentLibraries = new ArrayList<FragmentLibraryEx>();
      int rowNumber = 0;
      while (rows.hasNext()) {
        Row currentRow = rows.next();

        // skip header
        if (rowNumber == 0) {
          rowNumber++;
          continue;
        }

        Iterator<Cell> cellsInRow = currentRow.iterator();

        FragmentLibraryEx fragmentLibrary = new FragmentLibraryEx();

        int cellIdx = 0;
        while (cellsInRow.hasNext()) {
          Cell currentCell = cellsInRow.next();

          switch (cellIdx) {
            case 0:
              fragmentLibrary.setSpectrum( currentCell.getStringCellValue());
              break;

            case 1:
              fragmentLibrary.setZid( currentCell.getStringCellValue());

              break;

            case 2:
              fragmentLibrary.setSmileformula( currentCell.getStringCellValue());
              break;


            default:
              break;
          }

          cellIdx++;
        }

        fragmentLibraries.add(fragmentLibrary);
      }

      workbook.close();

      return fragmentLibraries;

    } catch (IOException e) {
      throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
    }
  }
  public static List<Tutorial> excelToTutorials(InputStream is) {
    try {
      Workbook workbook = new XSSFWorkbook(is);

      Sheet sheet = workbook.getSheet(SHEET);
      Iterator<Row> rows = sheet.iterator();

      List<Tutorial> tutorials = new ArrayList<Tutorial>();

      int rowNumber = 0;
      while (rows.hasNext()) {
        Row currentRow = rows.next();

        // skip header
        if (rowNumber == 0) {
          rowNumber++;
          continue;
        }

        Iterator<Cell> cellsInRow = currentRow.iterator();

        Tutorial tutorial = new Tutorial();

        int cellIdx = 0;
        while (cellsInRow.hasNext()) {
          Cell currentCell = cellsInRow.next();

          switch (cellIdx) {
          case 0:
            tutorial.setId((long) currentCell.getNumericCellValue());
            break;

          case 1:
            tutorial.setTitle(currentCell.getStringCellValue());
            break;

          case 2:
            tutorial.setDescription(currentCell.getStringCellValue());
            break;

          case 3:
            tutorial.setPublished(currentCell.getBooleanCellValue());
            break;

          default:
            break;
          }

          cellIdx++;
        }

        tutorials.add(tutorial);
      }

      workbook.close();

      return tutorials;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
    }
  }


}
