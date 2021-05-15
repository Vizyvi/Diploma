INSERT INTO users VALUES (true, '12.10.2020', 'Василий', 'Vasya@mail.ru', 'password321', 'who', 'png.png');
INSERT INTO users VALUES (false, '5.12.2020', 'Теодор', 'Teo@mail.ru', 'password111', 'where', null);
INSERT INTO users VALUES (false, '18.12.2020', 'Надежда', 'Nadya@mail.ru', 'password222', null, 'jpg.jpg');

INSERT INTO posts VALUES(true, 'NEW', null, '05.01.2021', 'Nazvanie', 'sometext', 10);
INSERT INTO posts VALUES(true, 'NEW', 1, '10.01.2021', 'NazvanieDrugoe', 'sometextsometextsometext', 2);
INSERT INTO posts VALUES(true, 'NEW', 1, '1.02.2021', 'NazvanieSovsemDrugoe', 'sometextALot', 100);

INSERT INTO post_votes VALUES(2, 1, '05.01.2021', true);
INSERT INTO post_votes VALUES(1, 2, '10.01.2021', true);
INSERT INTO post_votes VALUES(3, 3, '15.01.2021', true);

INSERT INTO tags VALUES('tag1');
INSERT INTO tags VALUES('tag2');
INSERT INTO tags VALUES('science');

INSERT INTO tag2post VALUES(1, 2);
INSERT INTO tag2post VALUES(2, 3);
INSERT INTO tag2post VALUES(3, 1);

INSERT INTO post_comments VALUES (null, 2, 2, '15.01.2021', 'suchAnImportantComment');
INSERT INTO post_comments VALUES (1, 2, 1, '16.01.2021', 'moderatorAngry');
INSERT INTO post_comments VALUES (null, 2, 2, '15.01.2021', 'suchAnImportantComment');

INSERT INTO  captcha_codes VALUES ('01.12.2020', 'secretCode', 'verySecretCode');
INSERT INTO  captcha_codes VALUES ('22.11.2020', 'prostroKod', 'prostoSekretniyKod');
INSERT INTO  captcha_codes VALUES ('31.02.2021', 'Несерьезно', 'admin');

