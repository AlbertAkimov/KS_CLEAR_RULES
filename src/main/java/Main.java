import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @Authot: Albert Akimov
 * @Date: 25.12.2020
 * @Description:
 */
public class Main {

    public static void main(String[] args) {

        String PATH_TO_XLS = "";
        String PATH_TO_TXT = "";
        String PATH_RESULT_FILE = "";

        Scanner in = new Scanner(System.in);

        System.out.println("Укажите путь до файла xls");
        PATH_TO_XLS = in.nextLine();

        System.out.println("Укажите путь до файла txt");

        PATH_TO_TXT = in.nextLine();

        System.out.println("Куда сохранить результат? Введите путь");

        PATH_RESULT_FILE = in.nextLine();

        if(PATH_RESULT_FILE.equals("") || PATH_TO_TXT.equals("") || PATH_TO_XLS.equals("")) {
            System.out.println("Не все пути для файлов указаны.Пока!");
            return;
        }

        FileInputStream file = null;
        BufferedReader bufferedReader = null;
        FileOutputStream fileOutputStream = null;
        int counter = 0;

        List<String> result = new ArrayList<>();

        try {
            file = new FileInputStream(new File(PATH_TO_XLS));
            Workbook xls = new XSSFWorkbook(file);
            Sheet sheet = xls.getSheetAt(0);

            for(Row row : sheet) {

                for (Cell cell: row) {

                    if(cell.getCellTypeEnum() == CellType.STRING) {
                        String a = cell.getRichStringCellValue().getString();
                        result.add(a.substring(5, a.length()));
                    }

                }
            }

            FileReader fileReader = new FileReader(PATH_TO_TXT);
            bufferedReader = new BufferedReader(fileReader);
            String line = "";
            String lineSeparator = System.getProperty("line.separator");

            fileOutputStream = new FileOutputStream(PATH_RESULT_FILE + "/result.bsl");
            boolean isFind = false;

            while ((line = bufferedReader.readLine()) != null) {

                for(String str: result) {

                    if(line.contains("ОписаниеПрофиля.Роли.Добавить(" + "\"" + str + "\"" + ");")) {
                        line = line.replace("ОписаниеПрофиля.Роли.Добавить(" + "\"" + str + "\"" + ");", "//");
                        isFind = true;
                        fileOutputStream.write(line.getBytes());
                        fileOutputStream.write(lineSeparator.getBytes());
                        counter++;
                        break;
                    }

                }

                if(!isFind) {
                    fileOutputStream.write(line.getBytes());
                    fileOutputStream.write(lineSeparator.getBytes());
                }

                isFind = false;

            }
            bufferedReader.close();
            fileOutputStream.close();
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                assert file != null;
                file.close();
                assert bufferedReader != null;
                bufferedReader.close();
                assert fileOutputStream != null;
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("count: " + counter);
    }
}
