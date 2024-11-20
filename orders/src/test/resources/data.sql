INSERT INTO orders (created_date, last_modification_date, customer_name, order_date, order_id, order_status)
VALUES
    ('2024-11-20 12:09:47.608831', '2024-11-20 15:28:00.177509', 'Bobby Drake', '2024-11-20', 'XK1203', 'PROCESSED'),
    ('2024-11-20 17:04:54.896709', '2024-11-20 17:06:00.016959', 'Hank McCoy', '2024-11-20', 'XK2304', 'PROCESSED');

INSERT INTO order_lines (created_date, last_modification_date, price, product_id, quantity, order_id)
VALUES
    ('2024-11-20 17:10:13.574797', null, 15.1, 'K100', 5, 1),
    ('2024-11-20 17:10:13.574797', null, 15.1, 'A100', 5, 1),
    ('2024-11-20 17:10:13.574797', null, 15.1, 'B100', 5, 2),
    ('2024-11-20 17:04:54.929288', '2024-11-20 17:10:13.595598', 10.1, 'O100', 20, 2),
    ('2024-11-20 17:04:54.929288', '2024-11-20 17:10:13.595598', 10.1, 'F100', 20, 2);

INSERT INTO app_users(username, role, password)
VALUES
    ('hank', 'ADMIN', '$2a$10$djimFtNZ1Lh86afIHhE/yuJCF9HEIaIh4ZvZ7G/LlVoWHguUZk2fC'),
    ('bobby', 'ADMIN', '$2a$10$djimFtNZ1Lh86afIHhE/yuJCF9HEIaIh4ZvZ7G/LlVoWHguUZk2fC');

