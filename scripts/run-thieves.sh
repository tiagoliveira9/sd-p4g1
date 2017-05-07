
#########################
# compile for client side
mkdir binClient  &> /dev/null
javac -d binClient -sourcepath src src/ClientSide/ClientHeistMuseum.java 
rm -r src &> /dev/null

# start thieves
java -classpath binClient/ ClientSide.ClientHeistMuseum thief


