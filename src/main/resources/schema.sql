-- Schema for MatchService

-- Status table
CREATE TABLE IF NOT EXISTS status (
    id TINYINT AUTO_INCREMENT PRIMARY KEY,
    state VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL
);

-- Leagues table
CREATE TABLE IF NOT EXISTS leagues (
    id SMALLINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    image_path VARCHAR(500),
    type VARCHAR(50),
    sub_type VARCHAR(50),
    category TINYINT
);

-- Teams table
CREATE TABLE IF NOT EXISTS teams (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    league_id SMALLINT,
    FOREIGN KEY (league_id) REFERENCES leagues(id)
);

-- Matches table
CREATE TABLE IF NOT EXISTS matches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    provider_id TINYINT,
    external_match_id BIGINT,
    league_id SMALLINT,
    status_id TINYINT,
    season SMALLINT,
    `group` SMALLINT,
    round SMALLINT,
    home_team_id INT,
    away_team_id INT,
    home_team_score TINYINT DEFAULT 0,
    away_team_score TINYINT DEFAULT 0,
    starting_at DATETIME,
    FOREIGN KEY (league_id) REFERENCES leagues(id),
    FOREIGN KEY (status_id) REFERENCES status(id),
    FOREIGN KEY (home_team_id) REFERENCES teams(id),
    FOREIGN KEY (away_team_id) REFERENCES teams(id)
);

-- Indexes for performance
CREATE INDEX idx_matches_status ON matches(status_id);
CREATE INDEX idx_matches_league ON matches(league_id);
CREATE INDEX idx_matches_starting_at ON matches(starting_at);
CREATE INDEX idx_matches_provider ON matches(provider_id);
CREATE INDEX idx_teams_name ON teams(name);
CREATE INDEX idx_leagues_name ON leagues(name);
