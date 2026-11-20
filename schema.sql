CREATE TABLE IF NOT EXISTS customer (
    customer_id  VARCHAR(20)  PRIMARY KEY,
    first_name   VARCHAR(50)  NOT NULL,
    last_name    VARCHAR(50)  NOT NULL,
    phone        VARCHAR(20),
    id_number    VARCHAR(30)  NOT NULL,
    email        VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS account (
    account_number    VARCHAR(20)       PRIMARY KEY,
    account_type      VARCHAR(15)       NOT NULL,
    branch            VARCHAR(50)       NOT NULL,
    date_opened       DATE              NOT NULL,
    balance           DOUBLE PRECISION  NOT NULL,
    interest_rate     DOUBLE PRECISION,
    minimum_deposit   DOUBLE PRECISION,
    employer_name     VARCHAR(100),
    employer_address  VARCHAR(100),
    overdraft_limit   DOUBLE PRECISION,
    customer_id       VARCHAR(20)       NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number  VARCHAR(20)       NOT NULL,
    txn_type        VARCHAR(15)       NOT NULL,
    amount          DOUBLE PRECISION  NOT NULL,
    txn_time        TIMESTAMP         NOT NULL,
    balance_after   DOUBLE PRECISION  NOT NULL,
    processed_by    VARCHAR(30)       NOT NULL,
    FOREIGN KEY (account_number) REFERENCES account(account_number)
);