@echo off
REM Loescht alle Datenbankstrukturen eines Benutzers.

if "%1" == "" (
  echo DB-User fehlt.
  GOTO ENDE
)  

if "%2" == "" (
  echo Passwort des DB-Users fehlt.
  GOTO ENDE
)  

if "%3" == "" (
  echo DB-Instanz fehlt.
  GOTO ENDE
)  

REM Loesche Objekte des Benutzers:
sqlplus %1/%2@%3 @util/dropAll.sql
sqlplus %1/%2@%3 @dropall.lis

del dropall.lis
echo %0: Fertig.

:ENDE
