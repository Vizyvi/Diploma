--INSERT INTO blog1.users (is_moderator, reg_time, name, email, password, code, photo) VALUES (1, '12.10.2020', 'Василий', 'Vasya@mail.ru', 'password321', 'who', 'png.png');
--INSERT INTO blog1.users (is_moderator, reg_time, name, email, password, code) VALUES (0, '5.12.2020', 'Теодор', 'Teo@mail.ru', 'password111', 'where');
--INSERT INTO blog1.users (is_moderator, reg_time, name, email, password, photo) VALUES (0, '18.12.2020', 'Надежда', 'Nadya@mail.ru', 'password222', 'jpg.jpg');

--INSERT INTO blog1.posts VALUES(true, 'NEW', null, '05.01.2021', 'Nazvanie', 'sometext', 10);
--INSERT INTO blog1.posts VALUES(true, 'NEW', 1, '10.01.2021', 'NazvanieDrugoe', 'sometextsometextsometext', 2);
--INSERT INTO blog1.posts VALUES(true, 'NEW', 1, '1.02.2021', 'NazvanieSovsemDrugoe', 'sometextALot', 100);
--
--INSERT INTO blog1.post_votes VALUES(2, 1, '05.01.2021', true);
--INSERT INTO blog1.post_votes VALUES(1, 2, '10.01.2021', true);
--INSERT INTO blog1.post_votes VALUES(3, 3, '15.01.2021', true);
--
INSERT INTO blog1.tags (name) VALUES('tag1');
INSERT INTO blog1.tags (name) VALUES('tag2');
INSERT INTO blog1.tags (name) VALUES('science');
--
--INSERT INTO blog1.tag2post VALUES(1, 2);
--INSERT INTO blog1.tag2post VALUES(2, 3);
--INSERT INTO blog1.tag2post VALUES(3, 1);
--
--INSERT INTO blog1.post_comments VALUES (null, 2, 2, '15.01.2021', 'suchAnImportantComment');
--INSERT INTO blog1.post_comments VALUES (1, 2, 1, '16.01.2021', 'moderatorAngry');
--INSERT INTO blog1.post_comments VALUES (null, 2, 2, '15.01.2021', 'suchAnImportantComment');
--
--INSERT INTO blog1.captcha_codes VALUES ('01.12.2020', 'secretCode', 'verySecretCode');
--INSERT INTO blog1.captcha_codes VALUES ('22.11.2020', 'prostroKod', 'prostoSekretniyKod');
--INSERT INTO blog1.captcha_codes VALUES ('31.02.2021', 'Несерьезно', 'admin');
--
--INSERT INTO blog1.global_settings VALUES ('MULTIUSER_MODE', 'Многопользовательский режим', 'YES');
--INSERT INTO blog1.global_settings VALUES ('POST_PREMODERATION', 'Премодерация постов', 'YES');
--INSERT INTO blog1.global_settings VALUES ('STATISTICS_IS_PUBLIC', 'Показывать всем статистику блога', 'NO');
