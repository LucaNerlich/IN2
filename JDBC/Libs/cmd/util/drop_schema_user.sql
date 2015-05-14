-- Löscht einen Benutzer (Schema).
--
-- Globale Variablen:
-- &system_pwd: Passwort des DB-Benutzers SYSTEM
-- &target_sys: Zielsystem
--
-- Parameter:
-- &1: Name des Benutzers.

connect system/&system_pwd@&target_sys

drop user &1 cascade;

