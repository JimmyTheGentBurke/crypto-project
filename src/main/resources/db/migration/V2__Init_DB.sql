ALTER TABLE users
    DROP COLUMN fiat_currency_symbol;
ALTER TABLE users
    DROP COLUMN crypto_currency;
ALTER TABLE users
    DROP COLUMN crypto_currency_symbol;
ALTER TABLE users
    DROP COLUMN fiat_currency;


CREATE TABLE fiat_wallet
(
    id      bigserial primary key,
    created TIMESTAMP,
    updated TIMESTAMP,
    status  VARCHAR(10),
    amount  numeric,
    symbol  varchar(10),
    user_id BIGINT REFERENCES users (id)

);
CREATE TABLE portfolio
(
    id      bigserial primary key,
    created TIMESTAMP,
    updated TIMESTAMP,
    status  VARCHAR(10),
    name    varchar(30) UNIQUE NOT NULL,
    user_id BIGINT REFERENCES users (id)
);
CREATE TABLE crypto_wallet
(
    id           bigserial primary key,
    created      TIMESTAMP,
    updated      TIMESTAMP,
    status       VARCHAR(10),
    amount       numeric,
    symbol       varchar(10),
    price        numeric,
    portfolio_id BIGINT REFERENCES portfolio (id),
    user_id      BIGINT REFERENCES users (id)
);


