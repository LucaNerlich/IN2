-- Legt einen Benutzer (Schema) für Tabellen usw. mit den erforderlichen 
-- Rechten an.
-- Der Benutzer erhält seinen Namen auch als Passwort.
--
-- Der Benutzer sollte mit dem Skript finalize_schema_user.sql finalisiert
-- werden, damit er nicht zu viele Rechte hat.
--
-- Globale Variablen:
-- &system_pwd: Passwort des Users SYSTEM
-- &target_sys: Zielsystem
--
-- Parameter:
-- &1: Name des Benutzers (gleichzeitig sein Passwort).

connect system/&system_pwd@&target_sys

create user &1 identified by &1;

-- Der Benutzer darf Datenbankobjekte anlegen:
grant resource to &1;

-- Der Benutzer darf sich mit der Datenbank verbinden:
grant connect to &1;

-- Der Benutzer darf interaktiv auf der Datenbank arbeiten:
grant create session to &1;

-- Der Benutzer darf Views erzeugen:
grant create view to &1;
