DROP TABLE IF EXISTS tg_user;

-- Create tg_user table
CREATE TABLE tg_user (chat_id INTEGER NOT NULL,
                      username VARCHAR NOT NULL,
                      firstname VARCHAR,
                      lastname VARCHAR,
                      place_of_work INTEGER,
                      experience INTEGER,
                      town VARCHAR,
                      education INTEGER);