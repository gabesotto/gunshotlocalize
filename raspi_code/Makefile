LIBS = -lasound -lm -lpthread
FLAGS = -g -Wall -std=c99

all: main.o loc.o sampler.o
	gcc $(FLAGS) main.o loc.o sampler.o -o client $(LIBS)
main.o:
	gcc -c $(FLAGS) main.c -o main.o 
loc.o:
	gcc -c $(FLAGS) loc.c -o loc.o
sampler.o:
	gcc -c $(FLAGS) sampler.c -o sampler.o
clean:
	rm -f *.o client
