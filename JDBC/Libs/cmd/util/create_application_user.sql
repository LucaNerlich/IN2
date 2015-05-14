-- Legt einen Benutzer für die Applikation mit den erforderlichen Rechten.
-- Der Benutzer erhält seinen Namen auch als Passwort.
--
--
-- Globale Variablen:
-- &system_pwd: Passwort des Users SYSTEM
-- &target_sys: Zielsystem
--
-- Parameter:
-- &1: Name des Benutzers.

connect system/&system_pwd@&target_sys

create user &1 identified by &1;

-- Der Benutzer darf sich mit der Datenbank verbinden:
grant connect to &1;

-- Der Benutzer darf interaktiv auf der Datenbank arbeiten:
-- TODO Ist das notwendig?
grant create session to &1;

