CREATE TABLE Conference
(
    cid                 SERIAL NOT NULL PRIMARY KEY,
    name                VARCHAR (255) NOT NULL,
    description         VARCHAR (255) NOT NULL,
    location            VARCHAR (255),
    start_date          DATE,
    end_date            DATE,
    review_form         VARCHAR (255), -- Link to file
    num_reviewer        INT,  
);

-- users because user is a reserved word in psql
CREATE TABLE Users 
(
    uid                 INT NOT NULL PRIMARY KEY,
    username            VARCHAR(255) UNIQUE NOT NULL,
    email               VARCHAR(255) UNIQUE NOT NULL,
    password            VARCHAR(255) NOT NULL,
    first_name          VARCHAR(255),
    last_name           VARCHAR(255),
    institution         VARCHAR(255),
    address             VARCHAR(255),
    phone               VARCHAR(255)
);

CREATE TABLE Paper 
(
    pid                 INT NOT NULL PRIMARY KEY,
    abstract            TEXT NOT NULL,
    title               TEXT NOT NULL,
    full_version        VARCHAR(255), -- Links
    final_version       VARCHAR(255)
);

CREATE TABLE Topic 
(
    tid                 INT NOT NULL PRIMARY KEY,
    name                VARCHAR(255) NOT NULL,
    description         TEXT
);

CREATE TABLE Expertise 
(
    uid                 INT NOT NULL REFERENCES Users (uid),
    tid                 INT NOT NULL REFERENCES Topic (tid),
    PRIMARY KEY (uid, tid)
);

CREATE TABLE Bid 
(
    uid                 INT NOT NULL REFERENCES users(uid),
    pid                 INT NOT NULL REFERENCES paper(pid),
    cid                 INT NOT NULL REFERENCES conference(cid),
    PRIMARY KEY (uid, pid, cid)
);

CREATE TABLE Coi
(
    uid                 INT NOT NULL REFERENCES Users (uid),
    pid                 INT NOT NULL REFERENCES Paper (pid),
    cid                 INT NOT NULL REFERENCES Conference (cid),
    PRIMARY KEY (uid, pid, cid)
);

CREATE TABLE Review 
(
    uid                 INT NOT NULL REFERENCES users(uid),
    pid                 INT NOT NULL REFERENCES paper(pid),
    cid                 INT NOT NULL REFERENCES conference(cid),
    vote                INT CHECK (0 <= vote AND vote <= 5),
    comment             TEXT,
    PRIMARY KEY (uid, pid, cid)
);

CREATE TABLE Submission
(
    uid                 INT NOT NULL REFERENCES Users (uid),
    pid                 INT NOT NULL REFERENCES Paper (pid),
    cid                 INT NOT NULL REFERENCES Conference (cid),
    time                TIME NOT NULL,
    letter              VARCHAR (255) NOT NULL,
    PRIMARY KEY (uid, pid, cid)
);

CREATE TABLE Author
(
    uid                 INT NOT NULL REFERENCES Users (uid),
    pid                 INT NOT NULL REFERENCES Paper (pid),
    PRIMARY KEY (uid, pid)
);

CREATE TABLE PaperTopic
 (
    pid                 INT NOT NULL REFERENCES paper(pid),
    tid                 INT NOT NULL REFERENCES topic(tid),
    PRIMARY KEY (pid, tid)
);

CREATE TABLE Members 
(
    cid                 INT NOT NULL REFERENCES conference(cid),
    uid                 INT NOT NULL REFERENCES users(uid),
    PRIMARY KEY (cid, uid)
);

CREATE TABLE ConferenceTopic
(
    cid                 INT NOT NULL REFERENCES Conference (cid),
    tid                 INT NOT NULL REFERENCES Topic (tid),    
    PRIMARY KEY (cid, tid)
);