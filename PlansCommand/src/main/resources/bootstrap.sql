INSERT INTO PLAN    (plan_id, plan_name, plan_description, is_active, is_promoted, monthly_fee, annual_fee, number_of_minutes, max_users, music_collections, music_suggestions, version)
VALUES              (1, 'Free', 'Free trial', true, false, 0, 0, '1000', 1, 0, 'Random', 1),
                    (2, 'Silver', 'Get more features', true, false, 5.99, 65.99, '6000', 3, 10, 'Personalized', 1);

INSERT INTO PLAN    (plan_id, plan_name, plan_description, is_active, is_promoted, monthly_fee, annual_fee, number_of_minutes, max_users, music_collections, music_suggestions, version)
VALUES              (3, 'Gold', 'Unrestricted experience!', true, false, 8.99, 98.99, 'Unlimited', 8, 100, 'Personalized', 1),
                    (100, 'Test Plan', 'This is a plan for testing purposes', false, false, 0, 0, 'Unlimited', 100, 100, 'Personalized', 1);