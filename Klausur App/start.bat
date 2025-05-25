@echo off
echo Starte Klasur-App...

:: Erstelle data-Verzeichnis
if not exist data mkdir data
echo Datenbank-Verzeichnis überprüft.

:: Starte Anwendung
start javaw --module-path "C:\javafx-sdk-21.0.7\lib" --add-modules javafx.controls,javafx.fxml -jar klasur_app_origin-1.0-SNAPSHOT.jar

echo Anwendung gestartet.