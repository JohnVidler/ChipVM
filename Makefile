CC=javac
CFLAGS=
#-Xlint:unchecked

DRIVER=PicVM

all:
	$(CC) $(CFLAGS)  ui/*.java hardware/*.java *.java

docs:
	javadoc -d Docs ui/*.java hardware/*.java *.java

clean:
	rm -rfv Docs
	rm -rfv  ui/*.class hardware/*.class *.class
