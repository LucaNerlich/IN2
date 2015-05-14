Vorgehen zum Entfernen aller Datenbankobjekte eines Benutzers:
1. In diesem Verzeichnis eine Kommandozeile öffnen.
2. Aufrufen des Skripts dropUserObjects.bat mit drei Parametern:
   a. Passwort des Benutzers (z.B. sample)
   b. Passwort des Benutzers (z.B. smaple)
   c. SID aus tnsnames.ora oder IP-Adresse, auf welcher sich die Datenbank befindet (z.B. localhost)
   Beispiel:
   dropUserObjects sample sample giorgiodb