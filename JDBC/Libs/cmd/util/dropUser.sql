-- Löscht einen Benutzer (Schema).
--
-- Parameter:
-- &1: Name des Benutzers.

-- Zeige nicht Variablenersetzungen in der Ausgabe:
set verify off

drop user &1 cascade;

exit 0;
