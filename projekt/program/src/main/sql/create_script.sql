
drop table if exists services cascade;
create table services
(
    id serial primary key,
    type varchar not null UNIQUE,
    credits integer not null
);


drop table if exists accounts cascade;
create table accounts
(
    id serial primary key,
    username varchar not null,-- UNIQUE,
    mail varchar not null,
    password varchar not null,
    credits integer not null
);

drop table if exists transaction_history cascade;
create table transaction_history
(
    id serial primary key,
    credits integer not null,
    description varchar not null,
    service_id integer references services on delete cascade,
    account_id integer references accounts on delete cascade
);

drop table if exists abilities cascade;
create table abilities
(
    id serial primary key,
    name varchar not null,-- UNIQUE,
    power_factor float not null, --float -- numeric
    healing bool not null
);

drop table if exists classes cascade;
create table classes
(
    id serial primary key,
    name varchar not null,-- UNIQUE,
    max_health_per_level integer not null,
    defense_per_level integer not null,
    power_per_level integer not null
);

drop table if exists class_abilities cascade;
create table class_abilities
(
    ability_id integer references abilities on delete cascade,
    class_id integer references classes on delete cascade,
    primary key (ability_id, class_id)
);

drop table if exists races cascade;
create table races
(
    id serial primary key,
    name varchar not null-- UNIQUE
);

drop table if exists race_classes cascade;
create table race_classes
(
    class_id integer references classes on delete cascade,
    race_id integer references races on delete cascade,
    primary key (class_id, race_id)
);

drop table if exists characters cascade;
create table characters
(
    id serial primary key,
    name varchar not null,-- UNIQUE,
    level integer not null,
    experience integer not null,
    current_health integer not null,
    gender varchar not null, --CHECK(gender = 'F' or gender = 'M'),
    hair_type integer not null,
    body_type integer not null,
--     experience_to_next_level integer not null,
    class_id integer references classes on delete cascade,
    race_id integer references races on delete cascade,
    account_id integer references accounts on delete cascade
);

drop table if exists fights cascade;
create table fights
(
    winner_id integer references characters on delete cascade,
    loser_id integer references characters on delete cascade,
    date_of_fight timestamp not null
--     primary key (winner_id, loser_id, date_of_fight)
);

drop table if exists items cascade;
create table items
(
    id serial primary key,
    name varchar not null,
    max_health_modifier integer not null,
    defense_modifier integer not null,
    power_modifier integer not null
);

drop table if exists character_items cascade;
create table character_items
(
    character_id integer references characters on delete cascade,
    item_id integer references items on delete cascade,
    equipped bool not null
);

------------------------------------------------------
------------------------------------------------------
------------------------------------------------------
------------------------------------------------------
------------------------------------------------------
------------------------------------------------------
------------------------------------------------------