LIBS = -lasound -lm
FLAGS = -g -Wall -std=c99

all: main.o
	gcc $(FLAGS) main.o -o client $(LIBS)
main.o:
	gcc -c $(FLAGS) main.c -o main.o 
clean:
	rm -f *.o client
