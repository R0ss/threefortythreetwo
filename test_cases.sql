
-- Output test number QueryX.Y when something's wrong: case column blank good.

SELECT num, 1.1 AS error
FROM Query1
WHERE num <> 2;

SELECT dname, 2.1 AS error
FROM Query2
WHERE dname <> 'Magick';

SELECT dept, sid, sfirstname, slastname, avggrade, 3.1 AS error
FROM query3
WHERE (dept, sid, sfirstname, slastname, avggrade) 
	<> ('MGK', '665', 'Dean', 'Wincester', 100) 
	AND (dept, sid, sfirstname, slastname, avggrade) 
	<> ('CSC', '672', 'Lilith', 'Demonica', 99)
	AND (dept, sid, sfirstname, slastname, avggrade) 
	<> ('CSC', '671', 'Ruby', 'Demonica', 99);

SELECT year, enrollment, 4.1 AS error
FROM query4
WHERE (year, enrollment) <> (2006, 4);

SELECT cname, 5.1 AS error
FROM query5
WHERE cname <> 'Configuration';
