-- Setzt globale Variable.
--
-- Parameter:
-- &1: Passwort des Users SYSTEM
-- &2: Passwort des Users SYS
-- &3: IP-Adresse des Zielsystems

-- Zeige nicht Variablenersetzungen in der Ausgabe:
set verify off

-- Das Passwort des Benutzers SYSTEM:
define system_pwd = &1

-- Das Passwort des Benutzers SYS:
define sys_pwd = &2

-- Das Zielsystem
define target_sys = &3
