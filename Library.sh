#!/bin/bash

# Ustawienia dla ścieżki modułów JavaFX
module_path=".\lib\openjfx-21.0.2_windows-x64_bin-sdk\javafx-sdk-21.0.2\lib"
add_modules="javafx.controls,javafx.fxml"
# Twoja klasa główna
main_class="agh.edu.pl.weedesign.library.LibraryApplication"

# Ścieżka do Javy, zastąp ją poprawnyma ścieżką
java_bin="/usr/bin/java"
java_version="11" # Wersja Java, którą chcesz użyć

$java_bin --version
if [ $? -eq 0 ]; then
    echo "Java jest zainstalowana"
else
    echo "Java nie jest zainstalowana"
fi

$java_bin --version | grep -q "$java_version"
if [ $? -eq 0 ]; then
    echo "Wersja Java jest $java_version"
else
    echo "Wersja Java nie jest $java_version"
fi

# Uruchamiamy program
$java_bin --module-path "$module_path" --add-modules "$add_modules" "$main_class"