CREATE TABLE Conference
(
    cid                 SERIAL,
    Name                VARCHAR (255) NOT NULL,
    Description         VARCHAR (255) NOT NULL,
    Location            VARCHAR (255),
    Start_date          DATE,
    End_date            DATE,
    Review_form         VARCHAR (255), -- Link to file
    Num_reviewer        INT,
    PRIMARY KEY (cid)    
);

-- users because user is a reserved word in psql
CREATE TABLE users (
    uid             INT PRIMARY KEY,
    username        VARCHAR(255) UNIQUE NOT NULL,
    email           VARCHAR(255) UNIQUE NOT NULL,
    password        VARCHAR(255) NOT NULL,
    firstname       VARCHAR(255),
    lastname        VARCHAR(255),
    institution     VARCHAR(255),
    address         VARCHAR(255),
    phone           VARCHAR(255)
);

CREATE TABLE paper (
    pid             INT PRIMARY KEY,
    abstract        TEXT NOT NULL,
    title           TEXT NOT NULL,
    full_version    VARCHAR(255), -- Links
    final_version   VARCHAR(255)
);

CREATE TABLE topic (
    tid             INT PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    description     TEXT
);

CREATE TABLE Expertise 
(
    User_id             INT,
    Topic_id            INT,
    CONSTRAINT Expertise_User FOREIGN KEY (User_id) REFERENCES Users (uid),
    CONSTRAINT Expertise_Topic FOREIGN KEY (Topic_id) REFERENCES Topic (tid),
    CONSTRAINT pk_Expertise PRIMARY KEY (User_id, Topic_id)
);

CREATE TABLE bid (
    uid             INT REFERENCES users(uid),
    pid             INT REFERENCES paper(pid),
    cid             INT REFERENCES conference(cid),
    PRIMARY KEY (uid, pid, cid)
);

CREATE TABLE coi
(
    User_id             INT NOT NULL,
    Paper_id            INT NOT NULL,
    Conference_id       INT NOT NULL,
    CONSTRAINT COI_User FOREIGN KEY (User_id) REFERENCES users (uid),
    CONSTRAINT COI_Paper FOREIGN KEY (Paper_id) REFERENCES Paper (pid),
    CONSTRAINT COI_Conference FOREIGN KEY (Conference_id) REFERENCES Conference (cid),
    CONSTRAINT pk_COI PRIMARY KEY (User_id, Paper_id, Conference_id)
);

CREATE TABLE review (
    uid     INT REFERENCES users(uid),
    pid     INT REFERENCES paper(pid),
    cid     INT REFERENCES conference(cid),
    vote    INT CHECK (0 <= vote AND vote <= 5),
    comment TEXT,
    PRIMARY KEY (uid, pid, cid)
);

CREATE TABLE Submission
(
    User_id             INT,
    Paper_id            INT,
    Conference_id       INT,
    Time                TIME NOT NULL,
    Letter              VARCHAR (255) NOT NULL,
    CONSTRAINT Submission_User FOREIGN KEY (User_id) REFERENCES Users (uid),
    CONSTRAINT Submission_Paper FOREIGN KEY (Paper_id) REFERENCES Paper (pid),
    CONSTRAINT Submission_Conference FOREIGN KEY (Conference_id) 
    REFERENCES Conference (cid),
    CONSTRAINT pk_Submission PRIMARY KEY (User_id, Paper_id, Conference_id)
);

CREATE TABLE Author
(
    User_id             INT,
    Paper_id            INT,
    CONSTRAINT Author_User FOREIGN KEY (User_id) REFERENCES Users (uid),
    CONSTRAINT Author_Paper FOREIGN KEY (Paper_id) REFERENCES Paper (pid),
    CONSTRAINT pk_Author PRIMARY KEY (User_id, Paper_id)
);

CREATE TABLE papertopic (
    pid     INT REFERENCES paper(pid),
    tid     INT REFERENCES topic(tid),
    PRIMARY KEY (pid, tid)
);

CREATE TABLE members (
    cid     INT REFERENCES conference(cid),
    uid     INT REFERENCES users(uid),
    PRIMARY KEY (cid, uid)
);

CREATE TABLE ConferenceTopic
(
    Conference_id       INT,
    Topic_id            INT,
    CONSTRAINT Submission_Ctopic FOREIGN KEY (Conference_id)
    REFERENCES Conference (cid),
    CONSTRAINT Ctopic_Topic FOREIGN KEY (Topic_id) REFERENCES Topic (tid),
    CONSTRAINT pk_Ctopic PRIMARY KEY (Conference_id, Topic_id)
);


