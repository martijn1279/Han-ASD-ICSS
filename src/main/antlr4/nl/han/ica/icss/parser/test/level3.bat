java org.antlr.v4.Tool ICSS.g4 -o files
cd files
javac ICSS*.java
java org.antlr.v4.gui.TestRig ICSS stylesheet -gui ../icss/level3.icss
cd ..