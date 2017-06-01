# GRI WS03

sftp sd0401@l040101-ws03.ua.pt 'bash -s' << 'ENDSFTP'
	mkdir project03
	cd project03
	put ../sd2017-proj3.zip
	bye
ENDSFTP


gnome-terminal -x sh -c "ssh sd0401@l040101-ws03.ua.pt 'bash -s' <<'ENDSSH'
  # commands to run on remote host
	cd project03
	if [ -d "engineHeist" ]; then
		# Control will enter here if $DIRECTORY(engineHeist) exists.
		rm sd2017-proj3.zip
	else
		unzip sd2017-proj3.zip
		rm sd2017-proj3.zip	
	fi
	
	./build_deploy.sh
	cd engineHeist/serverSide/
	./serverSide-gri.sh
ENDSSH; bash"

sleep 7

# MUSEUM WS04

sftp sd0401@l040101-ws04.ua.pt 'bash -s' << 'ENDSFTP'
	mkdir project03
	cd project03
	put ../sd2017-proj3.zip
	bye
ENDSFTP


gnome-terminal -x sh -c "ssh sd0401@l040101-ws04.ua.pt 'bash -s' <<'ENDSSH'
  # commands to run on remote host
	cd project03
	if [ -d "engineHeist" ]; then
		# Control will enter here if $DIRECTORY(engineHeist) exists.
		rm sd2017-proj3.zip
	else
		unzip sd2017-proj3.zip
		rm sd2017-proj3.zip	
	fi
	
	./build_deploy.sh
	cd engineHeist/serverSide/
	./serverSide-mus.sh
ENDSSH; bash"

sleep 7

# CONTROL WS05

sftp sd0401@l040101-ws05.ua.pt 'bash -s' << 'ENDSFTP'
	mkdir project03
	cd project03
	put ../sd2017-proj3.zip
	bye
ENDSFTP


gnome-terminal -x sh -c "ssh sd0401@l040101-ws05.ua.pt 'bash -s' <<'ENDSSH'
  # commands to run on remote host
	cd project03
	if [ -d "engineHeist" ]; then
		# Control will enter here if $DIRECTORY(engineHeist) exists.
		rm sd2017-proj3.zip
	else
		unzip sd2017-proj3.zip
		rm sd2017-proj3.zip	
	fi
	
	./build_deploy.sh
	cd engineHeist/serverSide/
	./serverSide-cont.sh
ENDSSH; bash"

sleep 7

# CONC WS06

sftp sd0401@l040101-ws06.ua.pt 'bash -s' << 'ENDSFTP'
	mkdir project03
	cd project03
	put ../sd2017-proj3.zip
	bye
ENDSFTP


gnome-terminal -x sh -c "ssh sd0401@l040101-ws06.ua.pt 'bash -s' <<'ENDSSH'
  # commands to run on remote host
	cd project03
	if [ -d "engineHeist" ]; then
		# Control will enter here if $DIRECTORY(engineHeist) exists.
		rm sd2017-proj3.zip
	else
		unzip sd2017-proj3.zip
		rm sd2017-proj3.zip	
	fi
	
	./build_deploy.sh
	cd engineHeist/serverSide/
	./serverSide-conc.sh
ENDSSH; bash"

sleep 7
# ASG1 WS07

sftp sd0401@l040101-ws07.ua.pt 'bash -s' << 'ENDSFTP'
	mkdir project03
	cd project03
	put ../sd2017-proj3.zip
	bye
ENDSFTP


gnome-terminal -x sh -c "ssh sd0401@l040107-ws07.ua.pt 'bash -s' <<'ENDSSH'
  # commands to run on remote host
	cd project03
	if [ -d "engineHeist" ]; then
		# Control will enter here if $DIRECTORY(engineHeist) exists.
		rm sd2017-proj3.zip
	else
		unzip sd2017-proj3.zip
		rm sd2017-proj3.zip	
	fi
	
	./build_deploy.sh
	cd engineHeist/serverSide/
	./serverSide-asg1.sh 
ENDSSH; bash"

sleep 7

# ASG2 WS08

sftp sd0401@l040101-ws08.ua.pt 'bash -s' << 'ENDSFTP'
	mkdir project03
	cd project03
	put ../sd2017-proj3.zip
	bye
ENDSFTP


gnome-terminal -x sh -c "ssh sd0401@l040107-ws08.ua.pt 'bash -s' <<'ENDSSH'
  # commands to run on remote host
	cd project03
	if [ -d "engineHeist" ]; then
		# Control will enter here if $DIRECTORY(engineHeist) exists.
		rm sd2017-proj3.zip
	else
		unzip sd2017-proj3.zip
		rm sd2017-proj3.zip	
	fi
	
	./build_deploy.sh
	cd engineHeist/serverSide/
	./serverSide-asg2.sh 
ENDSSH; bash"

sleep 7
	
# THIEVES WS09

sftp sd0401@l040101-ws09.ua.pt 'bash -s' << 'ENDSFTP'
	mkdir project03
	cd project03
	put ../sd2017-proj3.zip
	bye
ENDSFTP


gnome-terminal -x sh -c "ssh sd0401@l040101-ws09.ua.pt 'bash -s' <<'ENDSSH'
  # commands to run on remote host
  
	cd project03
	if [ -d "engineHeist" ]; then
		# Control will enter here if $DIRECTORY(engineHeist) exists.
		rm sd2017-proj3.zip
	else
		unzip sd2017-proj3.zip
		rm sd2017-proj3.zip	
	fi
	
	./build_deploy.sh
	cd engineHeist/clientSide/
	./thieves.sh 
	
ENDSSH; bash"

sleep 7

# MASTER WS10

sftp sd0401@l040101-ws10.ua.pt 'bash -s' << 'ENDSFTP'
	mkdir project03
	cd project03
	put ../sd2017-proj3.zip
	bye
ENDSFTP


gnome-terminal -x sh -c "ssh sd0401@l040101-ws10.ua.pt 'bash -s' <<'ENDSSH'
  # commands to run on remote host
	cd project03
	if [ -d "engineHeist" ]; then
		# Control will enter here if $DIRECTORY(engineHeist) exists.
		rm sd2017-proj3.zip
	else
		unzip sd2017-proj3.zip
		rm sd2017-proj3.zip	
	fi
	
	./build_deploy.sh
	cd engineHeist/clientSide/
	./master.sh 
ENDSSH; bash"



