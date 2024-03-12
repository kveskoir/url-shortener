CREATE
extension if NOT EXISTS "uuid-ossp";

CREATE TABLE SHORT_URL
(
    ID   UUID primary key default uuid_generate_v4(),
    URL TEXT NOT NULL
);