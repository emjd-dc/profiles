/*
 * Copyright (c) 2015 Pradeeban Kathiravelu and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

import util.DirectoryUtil;
import util.Formatter;
import util.ProfileConstants;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Read the tsv input files
 * bibtex converted with http://ref.lexique.org/
 */
public class TsvReader {
    private static String[][] studentArray;
    private static String[][] profArray;

    public static void main(String[] args) {
        try {
            studentArray = TsvReader.read("students.tsv");
            profArray = TsvReader.read("profs.tsv");
            log(ProfileConstants.isDebugEnabled);
            DirectoryUtil.createDirectories();
            writeStudents();
            writeProfessors();
            writeAboutUs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Write About Us page
     *
     * @throws IOException
     */
    public static void writeAboutUs() throws IOException {
        Path profInitPath =  Paths.get("meta-about-us-professor.html");
        Charset charset = StandardCharsets.UTF_8;

        Path outPath = Paths.get("profiles/" + "about-us.html");
        String content = "<h2>Professors</h2>\n";

        for (int i = 1; i < profArray.length; i++) {
            if (profArray[i].length > 1) {
                String profTemp = new String(Files.readAllBytes(profInitPath), charset);
                for (int j = 1; j < profArray[i].length; j++) {
                    profTemp = profTemp.replaceAll("PLACEHOLDER-" + j, profArray[i][j]);
                }
                profTemp = profTemp.replaceAll("PLACEHOLDER-0",Formatter.replaceSpace(profArray[i][1]));
                content+= profTemp + "\n\n";
            }
        }

        content += "\n<h2>Students</h2>\n";
        Path studentInitPath =  Paths.get("meta-about-us-student.html");

        for (int i = 1; i < studentArray.length; i++) {
            String studentTemp = new String(Files.readAllBytes(studentInitPath), charset);

            if (studentArray[i].length > 1) {
                for (int j = 1; j < studentArray[i].length; j++) {
                    String rep = "\n\n";


                    String value = studentArray[i][j];

                    if (j==4) {
                        if (!value.equals("") && value!=null) {
                            studentTemp = studentTemp.replaceAll("PLACEHOLDER-4", "<img src=\"PLACEHOLDER-4\" alt=\"\" height=\"200\" />");
                        } else {
                            studentTemp = studentTemp.replaceAll("PLACEHOLDER-4","");
                        }
                    }

                    if (j == 5) {
                        value = Formatter.formatDateString(value);
                    }
                    if (!value.equals("") && value!=null) {
                        if (j==3) {
                            studentTemp = studentTemp.replaceAll("Static_Text_S3", ProfileConstants.Static_Text_S3);
                        }
                        if (j==9) {
                            studentTemp = studentTemp.replaceAll("Static_Text_S9", ProfileConstants.Static_Text_S9);
                        }
                        studentTemp = studentTemp.replaceAll("PLACEHOLDER-" + j, value);
                    } else {
                        studentTemp = studentTemp.replaceAll("Static_Text_S" + j, "");
                        studentTemp = studentTemp.replaceAll("PLACEHOLDER-" + j, "");
                    }
                    studentTemp = studentTemp.replaceAll("PAGE-BREAK", rep);
                }
                if (studentArray[i].length <= 9) {
                    for (int j = studentArray[i].length; j <= 9; j++) {

                        studentTemp = studentTemp.replaceAll("PLACEHOLDER-" + j, "");
                        studentTemp = studentTemp.replaceAll("Static_Text_S" + j, "");
                    }
                }
                studentTemp = studentTemp.replaceAll("PLACEHOLDER-0",Formatter.replaceSpace(studentArray[i][1]));
                content+= studentTemp + "\n\n";
            }

        }

        Files.write(outPath, content.getBytes(charset));
    }

        /**
         * Write Student HTML files
         *
         * @throws IOException
         */
    public static void writeStudents() throws IOException {
        Path path = Formatter.chooseMetaFile(ProfileConstants.withHeaders, "meta-student");
        Charset charset = StandardCharsets.UTF_8;
        for (int i = 1; i < studentArray.length; i++) {
            String content = new String(Files.readAllBytes(path), charset);

            if (studentArray[i].length > 1) {
                // ignore the timestamp
                Path newPath = Paths.get("profiles/students/"+studentArray[i][1] + ".html");
                for (int j = 1; j < studentArray[i].length; j++) {
                    String rep;
                    if (ProfileConstants.withHeaders) {
                        rep = "<br /><br />";
                    } else {
                        rep = "\n\n";
                    }

                    String value = studentArray[i][j];

                    if (j == 5) {
                        value = Formatter.formatDateString(value);
                    }
                    if (!value.equals("") && value!=null) {
                        if (j==3) {
                            content = content.replaceAll("Static_Text_S3", ProfileConstants.Static_Text_S3);
                        }
                        if (j==9) {
                            content = content.replaceAll("Static_Text_S9", ProfileConstants.Static_Text_S9);
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

    /**
     * Write Professor HTML files.
     *
     * @throws IOException
     */
    public static void writeProfessors() throws IOException {
        Path path = Formatter.chooseMetaFile(ProfileConstants.withHeaders, "meta-professor");
        Charset charset = StandardCharsets.UTF_8;

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
