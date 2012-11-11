POSTGRES='/opt/PostgreSQL/8.3/csc343/a2'
SERVER='jdbc:postgresql://localhost:5432/postgres'
USERNAME='postgres'
PASSWORD=''

all: copydb compilejava

copydb:
	sudo cp -r db/ $POSTGRES/
	
compilejava:
	javac Assignment2.java

run:
	java -cp lib/postgresql-9.2-1000.jdbc4.jar:. Assignment2

clean:
	rm -rf *~
