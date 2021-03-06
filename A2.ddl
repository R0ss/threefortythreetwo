DROP TABLE IF EXISTS student CASCADE;
DROP TABLE IF EXISTS department CASCADE;
DROP TABLE IF EXISTS instructor CASCADE;
DROP TABLE IF EXISTS course CASCADE;
DROP TABLE IF EXISTS courseSection CASCADE;
DROP TABLE IF EXISTS studentCourse CASCADE;
DROP TABLE IF EXISTS prerequisites CASCADE;

-- The department table contains the departments at the university
CREATE TABLE department (
        dcode       CHAR(3) PRIMARY KEY,
        dname       VARCHAR(20) NOT NULL);

-- The student table contains information about the students at the univerity.
-- sid is the student number.
-- sex is either 'M' or 'F'
-- yearofstudy is the student's current year at the university. One of 1 or 2 or 3 or 4 or 5. 
CREATE TABLE student (
        sid         INTEGER PRIMARY KEY,
        slastname   CHAR(20) NOT NULL,
        sfirstname  CHAR(20) NOT NULL,
        sex	        CHAR(1)  NOT NULL,
        age 	    INTEGER  NOT NULL,
		dcode       CHAR(3)  NOT NULL, 
        yearofstudy INTEGER  NOT NULL,
		FOREIGN KEY (dcode) REFERENCES department(dcode) ON DELETE RESTRICT);

-- The instructor table contains information about the instructors at the university.
-- iid is the instructor employee id.
-- idegree is the highest post-graduate degree held by the instructor. It can be one of "PHD" or "MSC" or "BSC".
-- an instructor can only be associated with one department.
CREATE TABLE instructor (
        iid         INTEGER PRIMARY KEY,
        ilastname   CHAR(20) NOT NULL,
        ifirstname  CHAR(20) NOT NULL,
        idegree	    CHAR (5) NOT NULL,
        dcode	    CHAR(3)  NOT NULL,
        FOREIGN KEY (dcode) REFERENCES department(dcode) ON DELETE RESTRICT);
	
-- The course table contains the courses offered at the university.
CREATE TABLE course (
        cid         INTEGER,
        dcode	    CHAR(3) REFERENCES DEPARTMENT(dcode) ON DELETE RESTRICT,
        cname	    CHAR(20) NOT NULL,
        PRIMARY KEY (cid, dcode));

-- The courseSection table contains the sections that are offered 
-- for courses at the university for each semester of each year.
-- Semester values are '9' fall, '1' winter, and '5' summer.
-- Section is things like "L0101"
CREATE TABLE courseSection (
        csid        INTEGER PRIMARY KEY,
        cid         INTEGER NOT NULL,
        dcode       CHAR(3) NOT NULL,
        year	    INTEGER NOT NULL,
        semester    INTEGER NOT NULL,
        section	    CHAR(5) NOT NULL,
        iid         INTEGER REFERENCES instructor(iid),
        FOREIGN KEY (cid, dcode) REFERENCES course(cid, dcode), 
        UNIQUE (cid, dcode, year, semester, section));

-- The studentCourse table contains the courses a student has enrolled in, and their grade.
-- the grade is maintained as an integer value from 0 to 100.
CREATE TABLE studentCourse (
        sid         INTEGER REFERENCES student(sid),
        csid	    INTEGER REFERENCES courseSection(csid),
        grade	    INTEGER NOT NULL DEFAULT -1,
        PRIMARY KEY (sid, csid));

-- The prerequisites table contains the prerequisites for each course.  There may be more than
-- one per course.  The course for which the prerequisite applies is identified by (cid, dcode).
-- The prerequisite for that course is identified by (pcid, pcode).
CREATE TABLE prerequisites (
        cid	        INTEGER  NOT NULL,
        dcode	    CHAR (3) NOT NULL,
        pcid	    INTEGER  NOT NULL,
        pdcode      CHAR(3)  NOT NULL,
        FOREIGN KEY (cid, dcode) REFERENCES course(cid, dcode),
        FOREIGN KEY (pcid, pdcode) REFERENCES course(cid, dcode),
        PRIMARY KEY (cid, dcode,pcid, pdcode));

