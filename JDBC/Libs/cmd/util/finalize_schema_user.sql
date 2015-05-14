-- Finalisiert einen Schema-Benutzer, damit er nicht zu viele Rechte 
-- im Produktivbetrieb hat.
--
-- Globale Variablen:
-- &system_pwd: Passwort des Users SYSTEM
-- &target_sys: Zielsystem
--
-- Parameter:
-- &1: Name des Benutzers.

connect system/&system_pwd@&target_sys

-- Schema-User dürfen sich nicht anmelden:
revoke connect from &1;
revoke create session from &1;
