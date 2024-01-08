--liquibase formatted sql

--changeset facetnav:1
CREATE TABLE category
(
    id   BIGSERIAL primary key,
    name TEXT NOT NULL
);

--changeset facetnav:2
CREATE TABLE ad
(
    id          BIGSERIAL primary key,
    title       TEXT NOT NULL,
    description TEXT NOT NULL,
    created_at  TIMESTAMP,
    category_id   BIGSERIAL REFERENCES category (id)
);

--changeset facetnav:3
CREATE TABLE attribute
(
    id    BIGSERIAL primary key,
    name  TEXT NOT NULL,
    value INT  NOT NULL
);

--changeset facetnav:4
CREATE TABLE ad_category
(
    ad_id       BIGINT REFERENCES ad (id),
    category_id BIGINT REFERENCES category (id)
);

--changeset facetnav:5
CREATE TABLE ad_attribute
(
    ad_id        BIGINT REFERENCES ad (id),
    attribute_id BIGINT REFERENCES attribute (id)
);

--changeset facetnav:6
INSERT INTO category (name)
VALUES ('mouses');

INSERT INTO ad (title, description, created_at, category_id)
VALUES ('mouse red', 'with 3 button', CURRENT_TIMESTAMP, (SELECT ID from category where name = 'mouses')),
       ('mouse red', 'with 2 button', CURRENT_TIMESTAMP, (SELECT ID from category where name = 'mouses'));

INSERT INTO attribute (name, value)
VALUES ('button', 3),
       ('button', 2),
       ('color', 1);

INSERT INTO ad_category (ad_id, category_id)
VALUES ((SELECT id FROM ad WHERE title = 'mouse red' and description = 'with 2 button'),
        (SELECT id FROM category WHERE name = 'mouses')),
       ((SELECT id FROM ad WHERE title = 'mouse red' and description = 'with 3 button'),
        (SELECT id FROM category WHERE name = 'mouses'));


INSERT INTO ad_attribute (ad_id, attribute_id)
VALUES ((SELECT id FROM ad WHERE title = 'mouse red' and description = 'with 2 button'),
        (SELECT id FROM attribute WHERE name = 'button' and value = 2)),
       ((SELECT id FROM ad WHERE title = 'mouse red' and description = 'with 2 button'),
        (SELECT id FROM attribute WHERE name = 'color' and value = 1)),
       ((SELECT id FROM ad WHERE title = 'mouse red' and description = 'with 3 button'),
        (SELECT id FROM attribute WHERE name = 'button' and value = 3)),
       ((SELECT id FROM ad WHERE title = 'mouse red' and description = 'with 3 button'),
        (SELECT id FROM attribute WHERE name = 'color' and value = 1));



