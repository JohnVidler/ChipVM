CC=javac
CFLAGS=-Xlint:unchecked -d build -cp src
#-Xlint:unchecked

DRIVER=PicVM

all:
	mkdir -p build
	$(CC) $(CFLAGS) src/ui/*.java src/hardware/*.java src/file/*.java src/*.java
	cp demo.hex build/demo.hex
	cp -rvf res build/res

docs:
	mkdir -p Docs
	javadoc -d Docs src/ui/*.java src/hardware/*.java src/file/*.java src/*.java

clean:
	rm -rfv Docs
	rm -rfv build
