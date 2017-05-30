echo "RMI REGISTRY STARTED"
rmiregistry -J-Djava.rmi.server.codebase="http://l040101-ws01.ua.pt/sd0401/projecto3/classes/"\
            -J-Djava.rmi.server.useCodebaseOnly=true 22407
