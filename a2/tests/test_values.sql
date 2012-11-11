-- DO NOT USE.

INSERT INTO department VALUES('CSC', 'Computer Science');
INSERT INTO department VALUES('MAT', 'Mathematics');
INSERT INTO department VALUES('MGK', 'Magick');

INSERT INTO student VALUES('665', 'Wincester', 'Dean', 'M', '33', 'MGK', '2');
INSERT INTO student VALUES('666', 'Wincester', 'Sam', 'M', '27', 'MGK', '1');
INSERT INTO student VALUES('667', 'Angelica', 'Anna', 'F', '33', 'CSC', '4');
INSERT INTO student VALUES('668', 'Angelicus', 'Castiel', 'M', '33', 'CSC', '5');
INSERT INTO student VALUES('669', 'Angelicus', 'Michael', 'M', '33', 'CSC', '5');
INSERT INTO student VALUES('670', 'Angelicus', 'Lucifer', 'M', '33', 'CSC', '5');
INSERT INTO student VALUES('671', 'Demonica', 'Ruby', 'F', '33', 'CSC', '3');
INSERT INTO student VALUES('672', 'Demonica', 'Lilith', 'F', '33', 'CSC', '4');

INSERT INTO instructor VALUES(701, 'Singer', 'Bobby', 'PHD', 'CSC');
INSERT INTO instructor VALUES(702, 'Hunter', 'Rufus', 'PHD', 'CSC');
INSERT INTO instructor VALUES(703, 'Campbell', 'Samuel', 'PHD', 'MAT');
INSERT INTO instructor VALUES(704, 'Hunter', 'Ellen', 'MSC', 'MAT');
INSERT INTO instructor VALUES(705, 'Campbell', 'Deanna', 'BSC', 'MGK');
INSERT INTO instructor VALUES(706, 'Wincester', 'John', 'MSC', 'MGK');

INSERT INTO course VALUES(101, 'CSC', 'Def against d arts');
INSERT INTO course VALUES(102, 'CSC', 'Mathemagick');
INSERT INTO course VALUES(103, 'MGK', 'Transfiguration');
INSERT INTO course VALUES(104, 'CSC', 'Interdiscpln magic');
INSERT INTO course VALUES(105, 'CSC', 'Configuration');

INSERT INTO courseSection VALUES(1001, 101, 'CSC', 2006, 9, 'L0101', 701);
INSERT INTO courseSection VALUES(1002, 101, 'CSC', 2006, 1, 'L0101', 702);
INSERT INTO courseSection VALUES(1003, 102, 'CSC', 2007, 5, 'L0101', 706);
INSERT INTO courseSection VALUES(1004, 103, 'MGK', 2006, 9, 'L0101', 706);
INSERT INTO courseSection VALUES(1005, 104, 'CSC', 2005, 9, 'L0101', 702);
INSERT INTO courseSection VALUES(1006, 104, 'CSC', 2005, 5, 'L0101', 702);
INSERT INTO courseSection VALUES(1007, 105, 'CSC', 2002, 5, 'L0101', 704);
INSERT INTO courseSection VALUES(1008, 105, 'CSC', 2003, 5, 'L0101', 704);

INSERT INTO studentCourse VALUES(665, 1001, 99);
INSERT INTO studentCourse VALUES(665, 1003, 100);
INSERT INTO studentCourse VALUES(666, 1001, 100);
INSERT INTO studentCourse VALUES(666, 1003, 100);
INSERT INTO studentCourse VALUES(666, 1004, 99);
INSERT INTO studentCourse VALUES(666, 1002, 99);
INSERT INTO studentcourse VALUES(670, 1003, 44);
INSERT INTO studentCourse VALUES(668, 1001, 98);
INSERT INTO studentCourse VALUES(671, 1003, 99);
INSERT INTO studentCourse VALUES(670, 1003, 99);
INSERT INTO studentCourse VALUES(669, 1003, 99);
INSERT INTO studentCourse VALUES(668, 1003, 99);

INSERT INTO prerequisites VALUES(103, 'MGK', 102, 'MAT');
INSERT INTO prerequisites VALUES(105, 'CSC', 104, 'CSC');
INSERT INTO prerequisites VALUES(102, 'MAT', 101, 'CSC');

INSERT INTO prerequisites VALUES(103, 'MGK', 101, 'CSC');
INSERT INTO prerequisites VALUES(105, 'CSC', 101, 'CSC');
INSERT INTO prerequisites VALUES(104, 'CSC', 101, 'CSC');
