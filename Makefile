# define Java compiler and flag variable
JFLAGS = -g
JC = javac
# clear targers for building .class files from .java files.
.SUFFIXES: .java .class
# creating .class files from .java files 
.java.class:
	$(JC) $(JFLAGS) $*.java
# class consists of three java files
CLASSES = \
    MaxFibonacciHeap.java \
    NodeStructure.java \
    hashtagcounter.java
# default make target entry
default: classes
# make classes
classes: $(CLASSES:.java=.class)
# RM is rm -f. which is a predefined macro in make 
clean:
	$(RM) *.class
