-- Legt einen Benutzer (Schema) für Tabellen usw. mit den erforderlichen 
-- Rechten an.
-- Der Benutzer erhält seinen Namen auch als Passwort.
--
-- Parameter:
-- &1: Name des Benutzers (gleichzeitig sein Passwort).

-- Zeige nicht Variablenersetzungen in der Ausgabe:
set verify off

create user &1 identified by &1;

-- Der Benutzer darf Datenbankobjekte anlegen:
grant resource to &1;

-- Der Benutzer darf sich mit der Datenbank verbinden:
grant connect to &1;

-- Der Benutzer darf interaktiv auf der Datenbank arbeiten:
grant create session to &1;

-- Der Benutzer darf Views erzeugen:
grant create view to &1;

-- Der Benutzer darf Stored Procedures anlegen:
grant create procedure to &1;

exit 0;
