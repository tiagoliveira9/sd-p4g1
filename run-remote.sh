
# GRI WS01

sftp sd0401@l040101-ws01.ua.pt 'bash -s' << 'ENDSFTP'
	mkdir project02
	cd project02
	put sd2017-proj2.zip
	bye
ENDSFTP


gnome-terminal -x sh -c "ssh sd0401@l040101-ws01.ua.pt 'bash -s' <<'ENDSSH'
  # commands to run on remote host
	cd project02
	if [ -d "binServer" ]; then
		# Control will enter here if $DIRECTORY exists.
		rm sd2017-proj2.zip
	else
		unzip sd2017-proj2.zip
		rm sd2017-proj2.zip	
	fi
	   
	./scripts/run-gri.sh
ENDSSH; bash"

sleep 0.5

# MUSEUM WS02

sftp sd0401@l040101-ws02.ua.pt 'bash -s' << 'ENDSFTP'
	mkdir project02
	cd project02
	put sd2017-proj2.zip
	bye
ENDSFTP


gnome-terminal -x sh -c "ssh sd0401@l040101-ws02.ua.pt 'bash -s' <<'ENDSSH'
  # commands to run on remote host
	cd project02
	if [ -d "binServer" ]; then
		# Control will enter here if $DIRECTORY exists.
		rm sd2017-proj2.zip
	else
		unzip sd2017-proj2.zip
		rm sd2017-proj2.zip	
	fi
	   
	./scripts/run-museum.sh
ENDSSH; bash"

sleep 0.5

# CONTROL WS03

sftp sd0401@l040101-ws03.ua.pt 'bash -s' << 'ENDSFTP'
	mkdir project02
	cd project02
	put sd2017-proj2.zip
	bye
ENDSFTP


gnome-terminal -x sh -c "ssh sd0401@l040101-ws03.ua.pt 'bash -s' <<'ENDSSH'
  # commands to run on remote host
	cd project02
	if [ -d "binServer" ]; then
		# Control will enter here if $DIRECTORY exists.
		rm sd2017-proj2.zip
	else
		unzip sd2017-proj2.zip
		rm sd2017-proj2.zip	
	fi
	   
	./scripts/run-control.sh
ENDSSH; bash"

sleep 0.5

# CONC WS04

sftp sd0401@l040101-ws04.ua.pt 'bash -s' << 'ENDSFTP'
	mkdir project02
	cd project02
	put sd2017-proj2.zip
	bye
ENDSFTP


gnome-terminal -x sh -c "ssh sd0401@l040101-ws04.ua.pt 'bash -s' <<'ENDSSH'
  # commands to run on remote host
	cd project02
	if [ -d "binServer" ]; then
		# Control will enter here if $DIRECTORY exists.
		rm sd2017-proj2.zip
	else
		unzip sd2017-proj2.zip
		rm sd2017-proj2.zip	
	fi
	   
	./scripts/run-conc.sh
ENDSSH; bash"

sleep 0.5

# AGR1 WS05

sftp sd0401@l040101-ws05.ua.pt 'bash -s' << 'ENDSFTP'
	mkdir project02
	cd project02
	put sd2017-proj2.zip
	bye
ENDSFTP


gnome-terminal -x sh -c "ssh sd0401@l040101-ws05.ua.pt 'bash -s' <<'ENDSSH'
  # commands to run on remote host
	cd project02
	if [ -d "binServer" ]; then
		# Control will enter here if $DIRECTORY exists.
		rm sd2017-proj2.zip
	else
		unzip sd2017-proj2.zip
		rm sd2017-proj2.zip	
	fi
	   
	./scripts/run-agr1.sh
ENDSSH; bash"

sleep 0.5

# AGR2 WS06

sftp sd0401@l040101-ws06.ua.pt 'bash -s' << 'ENDSFTP'
	mkdir project02
	cd project02
	put sd2017-proj2.zip
	bye
ENDSFTP


gnome-terminal -x sh -c "ssh sd0401@l040101-ws06.ua.pt 'bash -s' <<'ENDSSH'
  # commands to run on remote host
	cd project02
	if [ -d "binServer" ]; then
		# Control will enter here if $DIRECTORY exists.
		rm sd2017-proj2.zip
	else
		unzip sd2017-proj2.zip
		rm sd2017-proj2.zip	
	fi
	   
	./scripts/run-agr2.sh
ENDSSH; bash"

sleep 0.5

# THIEVES WS09

sftp sd0401@l040101-ws09.ua.pt 'bash -s' << 'ENDSFTP'
	mkdir project02
	cd project02
	put sd2017-proj2.zip
	bye
ENDSFTP


gnome-terminal -x sh -c "ssh sd0401@l040101-ws09.ua.pt 'bash -s' <<'ENDSSH'
  # commands to run on remote host
  
	cd project02
	if [ -d "binClient" ]; then
		# Control will enter here if $DIRECTORY exists.
		rm sd2017-proj2.zip
	else
		unzip sd2017-proj2.zip
		rm sd2017-proj2.zip	
	fi
	./scripts/run-thieves.sh
	
ENDSSH; bash"

sleep 0.5

# MASTER WS10

sftp sd0401@l040101-ws10.ua.pt 'bash -s' << 'ENDSFTP'
	mkdir project02
	cd project02
	put sd2017-proj2.zip
	bye
ENDSFTP


gnome-terminal -x sh -c "ssh sd0401@l040101-ws10.ua.pt 'bash -s' <<'ENDSSH'
  # commands to run on remote host
	cd project02
	if [ -d "binClient" ]; then
		# Control will enter here if $DIRECTORY exists.
		rm sd2017-proj2.zip
	else
		unzip sd2017-proj2.zip
		rm sd2017-proj2.zip	
	fi
	./scripts/run-master.sh
ENDSSH; bash"



