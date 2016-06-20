CC    = gcc
FLAGS = -O3 -Wall -Wno-unused-result

SRC   = 3sat.c
EXEC  = bin/3sat

all: make_dirs $(EXEC)

clean:
	rm -f -r bin

make_dirs:
	mkdir -p bin

$(EXEC): $(SRC)
	$(CC) $(FLAGS) $^ -o $@ -lm

run:
	./$(EXEC) < input/small1.in
