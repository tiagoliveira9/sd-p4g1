
#########################
# compile, inside already of projecto03

javac -d engineHeist/serverSide -sourcepath src src/ServerSide/*.java 
javac -d engineHeist/registry -sourcepath src src/registry/*.java
javac -d engineHeist/clientSide -sourcepath src src/ClientSide/*.java 

rm -r src &> /dev/null

mkdir -p /home/sd0401/Public/projecto3/classes
mkdir -p /home/sd0401/Public/projecto3/classes/ClientSide
mkdir -p /home/sd0401/Public/projecto3/classes/Interfaces
mkdir -p /home/sd0401/Public/projecto3/classes/Auxiliary
#mkdir -p /home/sd0401/Public/projecto3/classes/clientSide/registry


cp engineHeist/clientSide/ClientSide/*.class /home/sd0401/Public/projecto3/classes/ClientSide
cp engineHeist/clientSide/Interfaces/*.class /home/sd0401/Public/projecto3/classes/Interfaces
cp engineHeist/clientSide/Auxiliary/*.class /home/sd0401/Public/projecto3/classes/Auxiliary
#cp engineHeist/clientSide/registry/*.class /home/sd0401/Public/projecto3/classes/clientSide/registry


echo "Done building"
