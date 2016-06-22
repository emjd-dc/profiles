/*
 * Copyright (c) 2015-2016, Pradeeban Kathiravelu and others. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package util;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Handle formatting.
 */
public class Formatter {
    public static String formatDateString(String value) {
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
     * Return the path to the file from the meta data
     * @param isWithHeaders will the final html pages consist of headers
     * @param baseName the basename of the meta file
     * @return the path
     */
    public static Path chooseMetaFile(boolean isWithHeaders, String baseName) {
        String fileName;
        String headerExt = "-with-headers";
        String ext = ".html";
        if (isWithHeaders) {
            fileName = baseName + headerExt + ext;
        } else {
            fileName = baseName + ext;
        }
        return Paths.get(fileName);
    }

    /**
     * Replaces space in a string with a dash
     * @param in original string
     * @return replaced out string
     */
    public static String replaceSpace(String in) {
        return in.replace(" ", "-");
    }
}
