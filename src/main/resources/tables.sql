CREATE TABLE IF NOT EXISTS currencies
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    rate NUMERIC(16, 2)    NOT NULL,
    code VARCHAR(3) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS players
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    player_role VARCHAR(32)         NOT NULL,
    username    VARCHAR(128) UNIQUE NOT NULL,
    birth_date  DATE                NOT NULL,
    password    bytea               NOT NULL
);

CREATE TABLE IF NOT EXISTS accounts
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    current_balance NUMERIC(16, 2) NOT NULL,
    created_at      TIMESTAMP      NOT NULL,
    currency_code   VARCHAR(3)     NOT NULL
        REFERENCES currencies (code)
            ON UPDATE CASCADE
            ON DELETE SET NULL,
    player_id       BIGINT         NOT NULL
        REFERENCES players (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transactions
(
    id                     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type                   VARCHAR(32)    NOT NULL,
    amount                 NUMERIC(16, 2) NOT NULL,
    created_at             TIMESTAMP      NOT NULL,
    currency_code          VARCHAR(3)     NOT NULL
        REFERENCES currencies (code)
            ON UPDATE CASCADE
            ON DELETE SET NULL,
    participant_account_id BIGINT         NOT NULL
        REFERENCES accounts (id)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);
