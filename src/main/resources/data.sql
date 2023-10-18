INSERT INTO currencies (rate, code)
VALUES (1.23, 'USD'),
       (1.27, 'EUR'),
       (110.5, 'JPY');

INSERT INTO players (player_role, username, birth_date, password)
VALUES ('Admin', 'admin123', '1990-01-15', E'\\x0123456789ABCDEF'),
       ('User', 'john_doe', '1985-05-20', E'\\xABCDEF0123456789'),
       ('Moderator', 'moderator1', '1988-09-10', E'\\x56789ABCDEF01234');

INSERT INTO accounts (current_balance, created_at, currency_code, player_id)
VALUES (1000.50, '2023-01-01', 'USD', 1),
       (500.75, '2023-02-15', 'EUR', 2),
       (200.30, '2023-03-20', 'JPY', 3);

INSERT INTO transactions (type, amount, created_at, currency_code, participant_account_id)
VALUES ('DEBIT', 50.25, '2023-01-02', 'USD', 1),
       ('CREDIT', 20.50, '2023-02-20', 'EUR', 2),
       ('CREDIT', 10.75, '2023-03-25', 'JPY', 3);
