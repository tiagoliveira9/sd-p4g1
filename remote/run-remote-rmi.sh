
# RMI REGISTRY WS01
sftp sd0401@l040101-ws01.ua.pt 'bash -s' << 'ENDSFTP'
	mkdir project03
	cd project03
	put ../sd2017-proj3.zip
	bye
ENDSFTP


gnome-terminal -x sh -c "ssh sd0401@l040101-ws01.ua.pt 'bash -s' <<'ENDSSH'
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
	./set_rmi_registry.sh
ENDSSH; bash"

sleep 5

# ServerRegisterRemoteObject WS01
gnome-terminal -x sh -c "ssh sd0401@l040101-ws01.ua.pt 'bash -s' <<'ENDSSH'
  # commands to run on remote host
	
	cd project03/engineHeist/registry/
	./registry.sh
ENDSSH; bash"



