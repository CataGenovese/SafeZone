CREATE TABLE event (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    date TIMESTAMP
);

INSERT INTO event (name, description, date) VALUES
('Concierto de Rock', 'Gran concierto de bandas de rock locales.', '2026-07-15T20:00:00'),
('Festival de Jazz', 'Festival anual de jazz con artistas internacionales.', '2026-08-20T18:00:00'),
('Partido de Fútbol', 'Final del campeonato de la liga local.', '2026-09-05T16:00:00');

