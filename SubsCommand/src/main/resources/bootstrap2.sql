-- Inserir várias linhas de dados na tabela subscriptions
INSERT INTO SUBSCRIPTIONS (SUBSCRIPTION_ID, CANCELLATION_DATE, CURRENT_DEVICES, IS_ACTIVE, IS_SUBSCRIPTION, JOKE, LAST_RENOVATION_DATE, PAYMENT_FREQUENCY, PLAN_NAME, SUBSCRIPTION_DATE, USER_ID, VERSION)
VALUES
    ('a6b7e88b865649ef8f324f92f165b9e9', NULL, 2, 1, 1, 'Some joke about Gold subscription', '2023-10-01', 'Monthly', 'Silver', '2023-10-01', 5, 1),
    ('22ac0d2d1e26465fa95c50b3123ac5f9', '2023-09-30', 1, 1, 1, 'A joke about Silver subscription', '2023-09-20', 'Annually', 'Silver', '2023-09-15', 6, 1),
    ('8e3a51bb3c8d456eba5370e638155086', NULL, 3, 1, 1, 'A Platinum subscription joke', '2023-10-05', 'Monthly', 'Free', '2023-10-05', 7, 1),
    ('d7e08162d37a493d89cf6254fc2a97fa', NULL, 1, 1, 1, 'Basic subscription humor', '2023-10-10', 'Monthly', 'Free', '2023-10-10', 8, 1);
