/*
 * Copyright (c) 2015 Pradeeban Kathiravelu and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Read the tsv input files
 * bibtex converted with http://ref.lexique.org/
 */
public class TsvReader {
    private static String[][] studentArray;
    private static String[][] profArray;

    private static String Static_Text_S3 = "Homepage: ";
    private static String Static_Text_S9 = "Selected Publications";

    private static boolean withHeaders = false;
    private static boolean isDebugEnabled = false;


    public static void main(String[] args) {
        try {
            studentArray = TsvReader.read("students.tsv");
            profArray = TsvReader.read("profs.tsv");
            log(isDebugEnabled);
            createDirectories();
            writeStudents();
            writeProfessors();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void createDirectories() {
        createDirectory("profiles");
        createDirectory("profiles" + File.separator + "students");
        createDirectory("profiles" + File.separator + "profs");
    }

    public static void createDirectory(String directoryName){
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * Write Student HTML files
     *
     * @throws IOException
     */
    public static void writeStudents() throws IOException {
        Path path;
        if (withHeaders) {
            path = Paths.get("meta-student-with-headers.html");
        } else {
            path = Paths.get("meta-student.html");
        }
        Charset charset = StandardCharsets.UTF_8;
        // ignore the header
        for (int i = 1; i < studentArray.length; i++) {
            String content = new String(Files.readAllBytes(path), charset);

            if (studentArray[i].length > 1) {
                // ignore the timestamp
                Path newPath = Paths.get("profiles/students/"+studentArray[i][1] + ".html");
                for (int j = 1; j < studentArray[i].length; j++) {
                    String rep;
                    if (withHeaders) {
                        rep = "<br /><br />";
                    } else {
                        rep = "\n\n";
                    }

                    String value = studentArray[i][j];

                    if (j == 5) {
                        value = formatDateString(value);
                    }
                    if (!value.equals("") && value!=null) {
                        if (j==3) {
                            content = content.replaceAll("Static_Text_S3", Static_Text_S3);
                        }
                        if (j==9) {
                            content = content.replaceAll("Static_Text_S9", Static_Text_S9);
                        }
                        content = content.replaceAll("PLACEHOLDER-" + j, value);
                    } else {
                        content = content.replaceAll("Static_Text_S" + j, "");
                        content = content.replaceAll("PLACEHOLDER-" + j, "");
                    }
                    content = content.replaceAll("PAGE-BREAK", rep);
                }
                if (studentArray[i].length <= 9) {
                    for (int j = studentArray[i].length; j <= 9; j++) {

                        content = content.replaceAll("PLACEHOLDER-" + j, "");
                        content = content.replaceAll("Static_Text_S" + j, "");
                    }
                }
                Files.write(newPath, content.getBytes(charset));
            }

        }
    }

    private static String formatDateString(String value) {
        Date inputDate = null;
        value = value.replaceAll("/", "-");
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        try {
            inputDate = dateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy");

        value = formatter.format(inputDate);
        return value;
    }

    /**
     * Write Professor HTML files.
     *
     * @throws IOException
     */
    public static void writeProfessors() throws IOException {
        Path path;
        if (withHeaders) {
            path = Paths.get("meta-professor-with-headers.html");
        } else {
            path = Paths.get("meta-professor.html");
        }
        Charset charset = StandardCharsets.UTF_8;

        // ignore the header
        for (int i = 1; i < profArray.length; i++) {
            String content = new String(Files.readAllBytes(path), charset);

            if (profArray[i].length > 1) {
                // ignore the timestamp
                Path newPath = Paths.get("profiles/profs/" + profArray[i][1] + ".html");
                for (int j = 1; j < profArray[i].length; j++) {
                    content = content.replaceAll("PLACEHOLDER-" + j, profArray[i][j]);
                }
                Files.write(newPath, content.getBytes(charset));
            }
        }
    }

    /**
     * Log to the console.
     */
    public static void log(boolean isDebugEnabled) {
        if (isDebugEnabled) {
            System.out.println("Students: ");
            query(studentArray);
            System.out.println("***********");
            System.out.println("Professors: ");
            query(profArray);
        }
    }

    /**
     * Read the tsv file
     *
     * @param fileName the name of the file
     * @return a 2D array
     * @throws IOException if the file does not exist.
     */
    public static String[][] read(String fileName) throws IOException {
        String[][] resultArray;

        List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);

        resultArray = new String[lines.size()][];

        for (int i = 0; i < lines.size(); i++) {
            resultArray[i] = lines.get(i).split("\t");
        }
        return resultArray;
    }

    /**
     * Just to check everything is in place by logging the array to the console.
     *
     * @param strArray the 2D array
     */
    public static void query(String[][] strArray) {
        // ignore the header
        for (int i = 1; i < strArray.length; i++) {
            // ignore the timestamp
            for (int j = 1; j < strArray[i].length; j++) {
                System.out.println("[" + i + "][" + j + "]" + strArray[i][j]);
            }
        }
    }
}
