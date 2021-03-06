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

import java.io.File;

/**
 * Class handling the directories.
 */
public class DirectoryUtil {
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
}
