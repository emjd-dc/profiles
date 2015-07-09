/*
 * Copyright (c) 2015 Pradeeban Kathiravelu and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

            System.out.println("Students: ");
            query(studentArray);
            System.out.println("***********");
            System.out.println("Professors: ");
            query(profArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the tsv file
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
     * @param strArray the 2D array
     */
    public static void query(String[][] strArray) {
        // ignore the header
        for (int i = 1; i < strArray.length; i ++) {
            // ignore the timestamp
            for (int j = 1; j < strArray[i].length; j++) {
                System.out.println("[" + i + "][" + j + "]" + strArray[i][j]);
            }
        }
    }
}
