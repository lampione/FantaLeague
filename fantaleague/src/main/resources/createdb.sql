CREATE SEQUENCE fantaschema.sequence_id INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1;
ALTER SEQUENCE fantaschema.sequence_id OWNER TO fantauser;
GRANT ALL ON SEQUENCE fantaschema.sequence_id TO fantauser;


-- Table: fantaschema.day
-- DROP TABLE fantaschema.day;
CREATE TABLE fantaschema.day
(
    id bigint NOT NULL,
    initial_ character varying(255) COLLATE pg_catalog."default",
    final_ character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT day_pkey PRIMARY KEY (id)
        USING INDEX TABLESPACE tbs_fantaleague
)
WITH (
    OIDS = FALSE
)
TABLESPACE tbs_fantaleague;
ALTER TABLE fantaschema.day OWNER to fantauser;
GRANT ALL ON TABLE fantaschema.day TO fantauser;


-- Table: fantaschema.league
-- DROP TABLE fantaschema.league;
CREATE TABLE fantaschema.league
(
    id bigint NOT NULL,
    name character varying(255) COLLATE pg_catalog."default",
    foundation date,
    credits integer,
    duplicateplayers boolean,
    goalkeepers integer,
    defenders integer,
    midfielders integer,
    forwards integer,
    maxtimetolineup integer,
    modules character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT league_pkey PRIMARY KEY (id)
        USING INDEX TABLESPACE tbs_fantaleague
)
WITH (
    OIDS = FALSE
)
TABLESPACE tbs_fantaleague;
ALTER TABLE fantaschema.league OWNER to fantauser;
GRANT ALL ON TABLE fantaschema.league TO fantauser;


-- Table: fantaschema.match
-- DROP TABLE fantaschema.match;
CREATE TABLE fantaschema.match
(
    id bigint NOT NULL,
    day_id bigint,
    team_id bigint,
    player_id bigint,
    status character varying(1) COLLATE pg_catalog."default",
    CONSTRAINT match_pkey PRIMARY KEY (id)
        USING INDEX TABLESPACE tbs_fantaleague,
    CONSTRAINT match_day_id_fkey FOREIGN KEY (day_id)
        REFERENCES fantaschema.day (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT match_team_id_fkey FOREIGN KEY (team_id)
        REFERENCES fantaschema.team (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE tbs_fantaleague;
ALTER TABLE fantaschema.match OWNER to fantauser;
GRANT ALL ON TABLE fantaschema.match TO fantauser;


-- Table: fantaschema.players
-- DROP TABLE fantaschema.players;

CREATE TABLE fantaschema.players
(
    id bigint NOT NULL,
    role character varying(1) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    equipe character varying(255) COLLATE pg_catalog."default",
    initial_quote bigint,
    CONSTRAINT players_pkey PRIMARY KEY (id)
        USING INDEX TABLESPACE tbs_fantaleague
)
WITH (
    OIDS = FALSE
)
TABLESPACE tbs_fantaleague;
ALTER TABLE fantaschema.players OWNER to fantauser;
GRANT ALL ON TABLE fantaschema.players TO fantauser;


-- Table: fantaschema.recovery_password
-- DROP TABLE fantaschema.recovery_password;

CREATE TABLE fantaschema.recovery_password
(
    id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    user_email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    send_date date,
    CONSTRAINT recovery_password_pkey PRIMARY KEY (user_email)
        USING INDEX TABLESPACE tbs_fantaleague,
    CONSTRAINT recovery_password_user_email_fkey FOREIGN KEY (user_email)
        REFERENCES fantaschema.users (email) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE tbs_fantaleague;

ALTER TABLE fantaschema.recovery_password OWNER to fantauser;
GRANT ALL ON TABLE fantaschema.recovery_password TO fantauser;


-- Table: fantaschema.registration_check

-- DROP TABLE fantaschema.registration_check;

CREATE TABLE fantaschema.registration_check
(
    id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    user_email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    creation_date date,
    CONSTRAINT registration_check_pkey PRIMARY KEY (user_email)
        USING INDEX TABLESPACE tbs_fantaleague,
    CONSTRAINT registration_check_user_email_fkey FOREIGN KEY (user_email)
        REFERENCES fantaschema.users (email) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE tbs_fantaleague;
ALTER TABLE fantaschema.registration_check OWNER to fantauser;
GRANT ALL ON TABLE fantaschema.registration_check TO fantauser;


-- Table: fantaschema.serie_a_match
-- DROP TABLE fantaschema.serie_a_match;
CREATE TABLE fantaschema.serie_a_match
(
    id bigint NOT NULL,
    day_id bigint,
    team1 character varying(50) COLLATE pg_catalog."default",
    team2 character varying(50) COLLATE pg_catalog."default",
    "time" character varying(255) COLLATE pg_catalog."default",
    location character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT serie_a_match_pkey PRIMARY KEY (id)
        USING INDEX TABLESPACE tbs_fantaleague,
    CONSTRAINT serie_a_match_day_id_fkey FOREIGN KEY (day_id)
        REFERENCES fantaschema.day (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE tbs_fantaleague;
ALTER TABLE fantaschema.serie_a_match OWNER to fantauser;
GRANT ALL ON TABLE fantaschema.serie_a_match TO fantauser;


-- Table: fantaschema.team
-- DROP TABLE fantaschema.team;
CREATE TABLE fantaschema.team
(
    id bigint NOT NULL,
    name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT team_pkey PRIMARY KEY (id)
        USING INDEX TABLESPACE tbs_fantaleague
)
WITH (
    OIDS = FALSE
)
TABLESPACE tbs_fantaleague;
ALTER TABLE fantaschema.team OWNER to fantauser;
GRANT ALL ON TABLE fantaschema.team TO fantauser;


-- Table: fantaschema.team_players
-- DROP TABLE fantaschema.team_players;
CREATE TABLE fantaschema.team_players
(
    id bigint NOT NULL,
    team_id bigint,
    player_id bigint,
    credits bigint,
    CONSTRAINT team_players_pkey PRIMARY KEY (id)
        USING INDEX TABLESPACE tbs_fantaleague,
    CONSTRAINT team_players_player_id_fkey FOREIGN KEY (player_id)
        REFERENCES fantaschema.players (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT team_players_team_id_fkey FOREIGN KEY (team_id)
        REFERENCES fantaschema.team (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE tbs_fantaleague;
ALTER TABLE fantaschema.team_players OWNER to fantauser;
GRANT ALL ON TABLE fantaschema.team_players TO fantauser;


-- Table: fantaschema.user_team_league
-- DROP TABLE fantaschema.user_team_league;
CREATE TABLE fantaschema.user_team_league
(
    id bigint NOT NULL,
    team_id bigint,
    user_id character varying(255) COLLATE pg_catalog."default",
    league_id bigint,
    creation_date date,
    status character(1) COLLATE pg_catalog."default",
    CONSTRAINT user_team_league_pkey PRIMARY KEY (id)
        USING INDEX TABLESPACE tbs_fantaleague,
    CONSTRAINT user_team_league_league_id_fkey FOREIGN KEY (league_id)
        REFERENCES fantaschema.league (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT user_team_league_team_id_fkey FOREIGN KEY (team_id)
        REFERENCES fantaschema.team (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE CASCADE,
    CONSTRAINT user_team_league_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES fantaschema.users (email) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE tbs_fantaleague;
ALTER TABLE fantaschema.user_team_league OWNER to fantauser;
GRANT ALL ON TABLE fantaschema.user_team_league TO fantauser;


-- Table: fantaschema.users
-- DROP TABLE fantaschema.users;
CREATE TABLE fantaschema.users
(
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    first_name character varying(255) COLLATE pg_catalog."default",
    last_name character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    born date,
    CONSTRAINT users_pkey PRIMARY KEY (email)
        USING INDEX TABLESPACE tbs_fantaleague
)
WITH (
    OIDS = FALSE
)
TABLESPACE tbs_fantaleague;
ALTER TABLE fantaschema.users OWNER to fantauser;
GRANT ALL ON TABLE fantaschema.users TO fantauser;


-- Table: fantaschema.uuid
-- DROP TABLE fantaschema.uuid;
CREATE TABLE fantaschema.uuid
(
    id character varying(36) COLLATE pg_catalog."default" NOT NULL,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT uuid_pkey PRIMARY KEY (email)
        USING INDEX TABLESPACE tbs_fantaleague,
    CONSTRAINT uuid_email_fkey FOREIGN KEY (email)
        REFERENCES fantaschema.users (email) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE tbs_fantaleague;
ALTER TABLE fantaschema.uuid OWNER to fantauser;
GRANT ALL ON TABLE fantaschema.uuid TO fantauser;


-- Table: fantaschema.vote
-- DROP TABLE fantaschema.vote;
CREATE TABLE fantaschema.vote
(
    id bigint NOT NULL,
    player_id bigint,
    day_id bigint,
    vote numeric(4,2),
    red_card integer,
    yellow_card integer,
    taken integer,
    done integer,
    assist integer,
    final_vote numeric(5,2),
    CONSTRAINT vote_pkey PRIMARY KEY (id)
        USING INDEX TABLESPACE tbs_fantaleague,
    CONSTRAINT vote_day_id_fkey FOREIGN KEY (day_id)
        REFERENCES fantaschema.day (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT vote_player_id_fkey FOREIGN KEY (player_id)
        REFERENCES fantaschema.players (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE tbs_fantaleague;
ALTER TABLE fantaschema.vote OWNER to fantauser;
GRANT ALL ON TABLE fantaschema.vote TO fantauser;