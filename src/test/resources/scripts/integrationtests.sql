CREATE TABLE assignees_to_tasks (
        task_id bigint NOT NULL,
        person_id bigint NOT NULL,
        PRIMARY KEY (task_id, person_id)
    );

CREATE TABLE person (
        id bigserial NOT NULL,
        email varchar(255) NOT NULL,
        guid varchar(255) NOT NULL,
        name varchar(255) NOT NULL,
        password varchar(255) NOT NULL,
        PRIMARY KEY (id)
    );

CREATE TABLE task (
        id bigserial NOT NULL,
        created_at timestamp(6) NOT NULL,
        deleted boolean NOT NULL,
        description TEXT,
        finished_at timestamp(6),
        guid varchar(255) NOT NULL,
        status VARCHAR(20) NOT NULL check (status in ('NOT_STARTED','DOING','COMPLETED')),
        title varchar(255) NOT NULL,
        person_id bigint NOT NULL,
        PRIMARY KEY (id)
    );

ALTER TABLE IF EXISTS person
       DROP CONSTRAINT IF EXISTS UK_fwmwi44u55bo4rvwsv0cln012;

ALTER TABLE IF EXISTS person
       ADD CONSTRAINT UK_fwmwi44u55bo4rvwsv0cln012 UNIQUE (email);

ALTER TABLE IF EXISTS person
       DROP CONSTRAINT IF EXISTS UK_qyfih6d32lgpd4nhawlqmmsy2;

ALTER TABLE IF EXISTS person
       ADD CONSTRAINT UK_qyfih6d32lgpd4nhawlqmmsy2 UNIQUE (guid);

ALTER TABLE IF EXISTS task
       DROP CONSTRAINT IF EXISTS UK_lbepcp6aox9e18r9jeum5uejd;

ALTER TABLE IF EXISTS task
       ADD CONSTRAINT UK_lbepcp6aox9e18r9jeum5uejd UNIQUE (guid);

ALTER TABLE IF EXISTS assignees_to_tasks
       ADD CONSTRAINT FKbcorpfm33ec0e4h1gu676n8o8
       FOREIGN KEY (person_id)
       REFERENCES person;

ALTER TABLE IF EXISTS assignees_to_tasks
       ADD CONSTRAINT FKaj2rtiixh6jbgpgufgt96pjjq
       FOREIGN KEY (task_id)
       REFERENCES task;

ALTER TABLE IF EXISTS task
       ADD CONSTRAINT FK1b9x9tb4qrlrhnb6v2xes2792
       FOREIGN KEY (person_id)
       REFERENCES person;

INSERT INTO person(guid, name, email, password) VALUES ('67db8b80-8e35-486c-8e9a-a7f3fc6e3202', 'Maria Helena', 'maria.helena@example.com', '$2a$10$OEU3Gt5W1K00CCDPcgejg.9hprb2yIZ4HDlTeVXWVHqp3ehOm16gy');