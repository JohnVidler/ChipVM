CC=javac
CFLAGS=-Xlint:unchecked
#-Xlint:unchecked

DRIVER=PicVM

all:
	$(CC) $(CFLAGS)  ui/*.java hardware/*.java file/*.java *.java

docs:
	mkdir -p Docs
	javadoc -d Docs ui/*.java hardware/*.java file/*.java *.java

clean:
	rm -rfv Docs
	rm -rfv  ui/*.class hardware/*.class file/*.class *.class
