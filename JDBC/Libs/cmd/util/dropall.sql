REM	------------------------------------------------------
REM	$Id: dropall.sql 22472 2011-06-28 07:09:10Z aberle $
REM	Funktion : Erzeugt Script, mit dem alle Tabellen des 
REM		   aktuellen DB Users gel�scht werden. 
REM	------------------------------------------------------
REM

define 	reppfad	 = ''
define	repfile	 = 'dropall.lis'

set heading off
set feedback off

ttitle off
btitle off
set newpage 1

column tname format a40

spool &reppfad&repfile

select 'drop '||object_type||' '||object_name||' CASCADE CONSTRAINTS;' from user_objects
	where object_type != 'INDEX' and object_type!='PACKAGE BODY'
	and object_type not in ('TRIGGER','SEQUENCE','FUNCTION','INDEX','PACKAGE','PACKAGE BODY','LOB')
	order by object_type,object_name;

select 'drop '||object_type||' '||object_name||'; ' from user_objects
	where object_type in ('SEQUENCE','FUNCTION','PACKAGE')
	order by object_type,object_name;

select 'exit 0;' from dual;

spool off

set heading on

set feedback on

prompt
prompt -----   DELETE Script in @&reppfad&repfile erzeugt!
prompt

exit 0
