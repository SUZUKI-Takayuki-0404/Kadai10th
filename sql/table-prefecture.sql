DROP TABLE IF EXISTS prefectures;

CREATE TABLE prefectures(
  prefCode VARCHAR(2) PRIMARY KEY,
  prefName VARCHAR(8) NOT NULL,
  prefNameEn VARCHAR(16) NOT NULL
);

INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('01','北海道','Hokkaido');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('02','青森県','Aomori');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('03','岩手県','Iwate');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('04','宮城県','Miyagi');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('05','秋田県','Akita');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('06','山形県','Yamagata');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('07','福島県','Fukushima');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('08','茨城県','Ibaraki');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('09','栃木県','Tochigi');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('10','群馬県','Gumma');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('11','埼玉県','Saitama');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('12','千葉県','Chiba');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('13','東京都','Tokyo');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('14','神奈川県','Kanagawa');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('15','新潟県','Niigata');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('16','富山県','Toyama');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('17','石川県','Ishikawa');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('18','福井県','Fukui');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('19','山梨県','Yamanashi');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('20','長野県','Nagano');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('21','岐阜県','Gifu');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('22','静岡県','Shizuoka');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('23','愛知県','Aichi');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('24','三重県','Mie');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('25','滋賀県','Shiga');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('26','京都府','Kyoto');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('27','大阪府','Osaka');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('28','兵庫県','Hyogo');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('29','奈良県','Nara');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('30','和歌山県','Wakayama');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('31','鳥取県','Tottori');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('32','島根県','Shimane');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('33','岡山県','Okayama');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('34','広島県','Hiroshima');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('35','山口県','Yamaguchi');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('36','徳島県','Tokushima');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('37','香川県','Kagawa');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('38','愛媛県','Ehime');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('39','高知県','Kochi');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('40','福岡県','Fukuoka');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('41','佐賀県','Saga');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('42','長崎県','Nagasaki');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('43','熊本県','Kumamoto');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('44','大分県','Oita');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('45','宮崎県','Miyazaki');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('46','鹿児島県','Kagoshima');
INSERT INTO prefectures (prefCode,prefName,prefNameEn) VALUES('47','沖縄県','Okinawa');
