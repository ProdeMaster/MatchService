-- Initial data for MatchService

-- Administrative States (To Be Announced, Not Started)
INSERT INTO status (id, state, name) VALUES (1, 'TBA', 'To Be Announced');
INSERT INTO status (id, state, name) VALUES (2, 'NS', 'Not Started');

-- Live Match States - First Half
INSERT INTO status (id, state, name) VALUES (3, 'INPLAY_1ST_HALF', '1st Half - In Play');

-- Half Time
INSERT INTO status (id, state, name) VALUES (4, 'HT', 'Half Time');

-- Live Match States - Second Half
INSERT INTO status (id, state, name) VALUES (5, 'INPLAY_2ND_HALF', '2nd Half - In Play');

-- Full Time (Regular Time Complete)
INSERT INTO status (id, state, name) VALUES (6, 'FT', 'Full Time');

-- Extra Time States
INSERT INTO status (id, state, name) VALUES (7, 'INPLAY_ET', 'Extra Time - In Play');
INSERT INTO status (id, state, name) VALUES (8, 'EXTRA_TIME_BREAK', 'Extra Time Break');
INSERT INTO status (id, state, name) VALUES (9, 'INPLAY_ET_2ND_HALF', 'Extra Time 2nd Half - In Play');
INSERT INTO status (id, state, name) VALUES (10, 'AET', 'After Extra Time');

-- Penalty Shootout States
INSERT INTO status (id, state, name) VALUES (11, 'INPLAY_PENALTIES', 'Penalties - In Play');
INSERT INTO status (id, state, name) VALUES (12, 'PEN_BREAK', 'Penalty Break');
INSERT INTO status (id, state, name) VALUES (13, 'FT_PEN', 'Full Time After Penalties');

-- Match Delays and Postponements
INSERT INTO status (id, state, name) VALUES (14, 'DELAYED', 'Delayed');
INSERT INTO status (id, state, name) VALUES (15, 'POSTPONED', 'Postponed');

-- Suspensions and Interruptions
INSERT INTO status (id, state, name) VALUES (16, 'SUSPENDED', 'Suspended');
INSERT INTO status (id, state, name) VALUES (17, 'INTERRUPTED', 'Interrupted');

-- Administrative Results
INSERT INTO status (id, state, name) VALUES (18, 'AWARDED', 'Awarded');
INSERT INTO status (id, state, name) VALUES (19, 'WO', 'Walk Over');

-- Terminal States (Cancelled, Abandoned, Deleted)
INSERT INTO status (id, state, name) VALUES (20, 'CANCELLED', 'Cancelled');
INSERT INTO status (id, state, name) VALUES (21, 'ABANDONED', 'Abandoned');
INSERT INTO status (id, state, name) VALUES (22, 'DELETED', 'Deleted');

-- Awaiting Updates
INSERT INTO status (id, state, name) VALUES (23, 'AWAITING_UPDATES', 'Awaiting Updates');
