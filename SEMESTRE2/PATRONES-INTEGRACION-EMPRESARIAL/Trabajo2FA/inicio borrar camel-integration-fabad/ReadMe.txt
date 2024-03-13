## TRABAJO 

Maestrante: Freddy Abad
Proyecto: camel-integration-fabad

### Descripcion del proyecto
docker pull postgres
docker run --name postgres-trabajo2 -e POSTGRES_PASSWORD=mysecretpassword -d postgres
docker exec -it 8dbbe58135cf /bin/bash
su - postgres

docker exec -it postgres-trabajo2 psql -U postgres -c ""
docker exec -it postgres-trabajo2 psql -U postgres -c "DROP TABLE persona ;"



CREATE TABLE persona (IDENTIFICADOR SERIAL PRIMARY KEY, ID VARCHAR(100), LIMIT_BAL VARCHAR(100), SEX VARCHAR(100), EDUCATION VARCHAR(100), MARRIAGE VARCHAR(100), AGE VARCHAR(100), PAY_0 VARCHAR(100), PAY_2 VARCHAR(100), PAY_3 VARCHAR(100), PAY_4 VARCHAR(100), PAY_5 VARCHAR(100), PAY_6 VARCHAR(100), BILL_1 VARCHAR(100), BILL_2 VARCHAR(100), BILL_3 VARCHAR(100), BILL_4 VARCHAR(100), BILL_5 VARCHAR(100), BILL_6 VARCHAR(100), PAY_1 VARCHAR(100), PAY_22 VARCHAR(100), PAY_23 VARCHAR(100), PAY_24 VARCHAR(100), PAY_25 VARCHAR(100), PAY_26 VARCHAR(100), default_payment_next_month VARCHAR(100));





SELECT column_name, data_type FROM information_schema.columns WHERE table_name = 'nombre_de_la_tabla';

INSERT INTO persona (ID, LIMIT_BAL, SEX, EDUCATION, MARRIAGE, AGE, PAY_0, PAY_2, PAY_3, PAY_4, PAY_5, PAY_6, BILL_1, BILL_2, BILL_3, BILL_4, BILL_5, BILL_6, PAY_1, PAY_22, PAY_23, PAY_24, PAY_25, PAY_26, default_payment_next_month)  VALUES ('1', '20000', '2', '2', '1', '24', '2', '2', '-1', '-1', '-2', '-2', '3913', '3102', '689', '0', '0', '0', '0', '689', '0', '0', '0', '0', '1');
INSERT INTO persona (ID, LIMIT_BAL, SEX, EDUCATION, MARRIAGE, AGE, PAY_0, PAY_2, PAY_3, PAY_4, PAY_5, PAY_6, BILL_1, BILL_2, BILL_3, BILL_4, BILL_5, BILL_6, PAY_1, PAY_2_2, PAY_3_2, PAY_4_2, PAY_5_2, PAY_6_2, DEFAULT_PAYMENT_NEXT_MONTH) VALUES ('393','310000','1','2','1','44','0','0','0','0','0','0','103797','91989','87720','76218','72090','71620','4400','4013','3011','3000','3001','2653','0');


SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'  AND table_type = 'BASE TABLE';

